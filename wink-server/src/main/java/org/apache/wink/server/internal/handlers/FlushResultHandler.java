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
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Providers;
import javax.ws.rs.ext.RuntimeDelegate;
import javax.ws.rs.ext.RuntimeDelegate.HeaderDelegate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.wink.common.internal.MultivaluedMapImpl;
import org.apache.wink.server.handlers.AbstractHandler;
import org.apache.wink.server.handlers.MessageContext;


public class FlushResultHandler extends AbstractHandler {

    private static final Logger logger = LoggerFactory.getLogger(FlushResultHandler.class);
    private static final RuntimeDelegate runtimeDelegate = RuntimeDelegate.getInstance();

    @SuppressWarnings("unchecked")
    public void handleResponse(MessageContext context) throws Throwable {

        // assert status code is valid
        int statusCode = context.getResponseStatusCode();
        if (statusCode < 0) {
            if (logger.isDebugEnabled()) {
                logger.debug("Status code was not set. Nothing to do.");
            }
            return;
        }

        // assert response is not committed
        final HttpServletResponse httpResponse = context.getAttribute(HttpServletResponse.class);
        if (httpResponse.isCommitted()) {
            if (logger.isDebugEnabled()) {
                logger.debug("The response is already committed. Nothing to do.");
            }
            return;
        }

        // set the status code
        httpResponse.setStatus(statusCode);

        // get the entity
        Object entity = context.getResponseEntity();

        // extract the entity and headers from the response (if it is a response)
        MultivaluedMap<String,Object> httpHeaders = null;
        if (entity instanceof Response) {
            Response response = (Response)entity;
            entity = response.getEntity();
            httpHeaders = response.getMetadata();
        }

        // prepare the entity to write, its class and generic type
        Type genericType = null;
        Annotation[] declaredAnnotations = null;
        SearchResult searchResult = context.getAttribute(SearchResult.class);
        if (searchResult != null && searchResult.isFound()) {
            Method reflectionMethod = searchResult.getMethod().getMetadata().getReflectionMethod();
            genericType = reflectionMethod.getGenericReturnType();
            declaredAnnotations = reflectionMethod.getDeclaredAnnotations();
        }

        Class<?> rawType = null;

        if (entity instanceof GenericEntity) {
            GenericEntity<?> genericEntity = (GenericEntity<?>)entity;
            entity = genericEntity.getEntity();
            rawType = genericEntity.getRawType();
            genericType = genericEntity.getType();
        } else {
            rawType = (entity != null ? entity.getClass() : null);
            genericType = (genericType != null ? genericType : rawType);
        }

        if (httpHeaders == null) {
            httpHeaders = new MultivaluedMapImpl<String,Object>();
        }

        // we're done if the actual entity is null
        if (entity == null) {
            flushHeaders(httpResponse, httpHeaders);
            return;
        }

        // we have an entity, set the response media type
        MediaType responseMediaType = context.getResponseMediaType();
        if (responseMediaType != null) {
            httpResponse.setContentType(responseMediaType.toString());
        }

        // get the provider to write the entity
        Providers providers = context.getProviders();
        MessageBodyWriter<Object> messageBodyWriter = (MessageBodyWriter<Object>)providers.getMessageBodyWriter(
                rawType, genericType, declaredAnnotations, responseMediaType);

        // TODO: try to find a data handler using JavaBeans Activation Framework,
        // if found use DataSourceProvider

        // use the provider to write the entity
        if (messageBodyWriter != null) {
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("Serialization using provider %s", messageBodyWriter.getClass().getName()));
            }

            final MultivaluedMap<String,Object> headers = httpHeaders;

            long size = messageBodyWriter.getSize(entity, rawType, genericType, declaredAnnotations, responseMediaType);
            if (size >= 0) {
                headers.putSingle(HttpHeaders.CONTENT_LENGTH, String.valueOf(size));
            }

            messageBodyWriter.writeTo(entity, rawType, genericType, declaredAnnotations, responseMediaType,
                    httpHeaders, new FlushHeadersOutputStream(httpResponse, headers));

        } else {
            logger.error(String.format("Could not find a writer for %s and %s", entity.getClass().getName(),
                    responseMediaType));
            throw new WebApplicationException(500);
        }
    }

    @SuppressWarnings("unchecked")
    private static void flushHeaders(final HttpServletResponse httpResponse, final MultivaluedMap<String,Object> headers) {
        for (Entry<String,List<Object>> entry : headers.entrySet()) {
            String key = entry.getKey();
            List<Object> values = entry.getValue();
            for (Object val : values) {
                if (val != null) {
                    HeaderDelegate<Object> headerDelegate = (HeaderDelegate<Object>)runtimeDelegate
                            .createHeaderDelegate(val.getClass());
                    String header = headerDelegate != null ? headerDelegate.toString(val) : val.toString();
                    httpResponse.addHeader(key, header);
                }
            }
        }
    }

    private static class FlushHeadersOutputStream extends OutputStream {

        // The Proxy over OutputStream is provided here in order to write the
        // headers that a provider MAY have added to the response, before it
        // actually started writing to stream.

        private boolean writeStarted;
        private HttpServletResponse httpResponse;
        private ServletOutputStream outputStream;
        private MultivaluedMap<String,Object> headers;

        public FlushHeadersOutputStream(HttpServletResponse httpResponse, MultivaluedMap<String,Object> headers)
                throws IOException {
            this.writeStarted = false;
            this.httpResponse = httpResponse;
            this.outputStream = httpResponse.getOutputStream();
            this.headers = headers;
        }

        @Override
        public void write(int b) throws IOException {
            flushHeaders();
            outputStream.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            flushHeaders();
            outputStream.write(b, off, len);
        }

        @Override
        public void write(byte[] b) throws IOException {
            flushHeaders();
            outputStream.write(b);
        }

        private void flushHeaders() {
            if (!writeStarted) {
                FlushResultHandler.flushHeaders(httpResponse, headers);
                writeStarted = true;
            }
        }
    }

    public void init(Properties props) {
    }
}