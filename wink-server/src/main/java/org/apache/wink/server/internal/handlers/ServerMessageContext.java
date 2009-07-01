/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *  
 *   http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *  
 *******************************************************************************/
package org.apache.wink.server.internal.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Providers;

import org.apache.wink.common.http.HttpHeadersEx;
import org.apache.wink.common.internal.contexts.ProvidersImpl;
import org.apache.wink.common.internal.registry.ProvidersRegistry;
import org.apache.wink.common.internal.runtime.AbstractRuntimeContext;
import org.apache.wink.server.handlers.MessageContext;
import org.apache.wink.server.internal.DeploymentConfiguration;
import org.apache.wink.server.internal.MediaTypeMapper;
import org.apache.wink.server.internal.contexts.HttpHeadersImpl;
import org.apache.wink.server.internal.contexts.RequestImpl;
import org.apache.wink.server.internal.contexts.SecurityContextImpl;
import org.apache.wink.server.internal.contexts.UriInfoImpl;
import org.apache.wink.server.internal.registry.ResourceRegistry;
import org.apache.wink.server.internal.utils.LinkBuildersImpl;
import org.apache.wink.server.utils.LinkBuilders;


public class ServerMessageContext extends AbstractRuntimeContext implements MessageContext {

    private int responseStatusCode;
    private Object responseEntity;
    private MediaType responseMediaType;

    private String httpMethod;

    public ServerMessageContext(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
            DeploymentConfiguration configuration) {
        this.httpMethod = buildHttpMethod(servletRequest);
        this.responseStatusCode = -1;

        // save stuff on attributes
        setAttribute(HttpServletRequest.class, servletRequest);
        setAttribute(HttpServletResponse.class, new WrappedResponse(servletRequest, servletResponse, configuration
                .getMediaTypeMapper()));
        setAttribute(ServletContext.class, configuration.getServletContext());
        setAttribute(ServletConfig.class, configuration.getServletConfig());
        setAttribute(DeploymentConfiguration.class, configuration);
        setAttribute(ResourceRegistry.class, configuration.getResourceRegistry());
        setAttribute(ProvidersRegistry.class, configuration.getProvidersRegistry());

        initContexts();
    }

    private void initContexts() {
        setAttribute(Providers.class, new ProvidersImpl(getDeploymentConfiguration().getProvidersRegistry(), this));
        setAttribute(HttpHeaders.class, new HttpHeadersImpl(this));
        setAttribute(UriInfo.class, new UriInfoImpl(this));
        setAttribute(SecurityContext.class, new SecurityContextImpl(this));
        setAttribute(Request.class, new RequestImpl(this));
        setAttribute(LinkBuilders.class, new LinkBuildersImpl(this));
    }

    private String buildHttpMethod(HttpServletRequest servletRequest) {
        String xHttpMethodOverride = servletRequest.getHeader(HttpHeadersEx.X_HTTP_METHOD_OVERRIDE);
        if (xHttpMethodOverride != null) {
            return xHttpMethodOverride.trim();
        }

        String xMethodOverride = servletRequest.getHeader(HttpHeadersEx.X_METHOD_OVERRIDE);
        if (xMethodOverride != null) {
            return xMethodOverride.trim();
        }

        try {
            return servletRequest.getMethod();
        } catch (IllegalArgumentException e) {
            return null; // return null for unknown methods
        }
    }

    // MessageContext methods
    public Properties getProperties() {
        return getDeploymentConfiguration().getProperties();
    }

    public void setResponseStatusCode(int responseStatusCode) {
        this.responseStatusCode = responseStatusCode;
    }

    public int getResponseStatusCode() {
        return responseStatusCode;
    }

    public void setResponseEntity(Object entity) {
        this.responseEntity = entity;
    }

    public Object getResponseEntity() {
        return responseEntity;
    }

    public void setResponseMediaType(MediaType responseMediaType) {
        this.responseMediaType = responseMediaType;
    }

    public MediaType getResponseMediaType() {
        return responseMediaType;
    }

    public void setHttpMethod(String method) {
        this.httpMethod = method;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    @Override
    public String toString() {
        return String.format("Method: %s, Path: %s, MediaType: %s", String.valueOf(getHttpMethod()), String
                .valueOf(getUriInfo().getPath(false)), String.valueOf(getHttpHeaders().getMediaType()));
    }

    // Contexts methods

    public LinkBuilders getLinkBuilders() {
        return getAttribute(LinkBuilders.class);
    }

    public InputStream getInputStream() throws IOException {
        return getAttribute(HttpServletRequest.class).getInputStream();
    }

    public OutputStream getOutputStream() throws IOException {
        return getAttribute(HttpServletResponse.class).getOutputStream();
    }

    private DeploymentConfiguration getDeploymentConfiguration() {
        return getAttribute(DeploymentConfiguration.class);
    }

    // Wrapped response for changing the output media type according to the media type mapper
    private class WrappedResponse extends HttpServletResponseWrapper {

        private final HttpServletRequest servletRequest;
        private final MediaTypeMapper mediaTypeMapper;
        private String userContentType = null;

        WrappedResponse(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
                MediaTypeMapper mediaTypeMapper) {
            super(servletResponse);
            this.servletRequest = servletRequest;
            this.mediaTypeMapper = mediaTypeMapper;
        }

        @Override
        public void setHeader(String name, String value) {
            if (name.equals(HttpHeaders.CONTENT_TYPE)) {
                setContentType(value);
            } else {
                super.setHeader(name, value);
            }
        }

        @Override
        public void addHeader(String name, String value) {
            if (name.equals(HttpHeaders.CONTENT_TYPE)) {
                setContentType(value);
            } else {
                super.addHeader(name, value);
            }
        }

        @Override
        public void setContentType(String type) {
            userContentType = type;
            MediaType realResponseMimeType = getRealResponseMimeType(type);
            super.setContentType(realResponseMimeType.toString());
        }

        @Override
        public String getContentType() {
            if (userContentType != null) {
                return userContentType;
            }
            return super.getContentType();
        }

        private MediaType getRealResponseMimeType(String responseMimeType) {
            String userAgent = servletRequest.getHeader(HttpHeaders.USER_AGENT);
            return mediaTypeMapper.mapOutputMediaType(MediaType.valueOf(responseMimeType), userAgent);
        }
    }

}