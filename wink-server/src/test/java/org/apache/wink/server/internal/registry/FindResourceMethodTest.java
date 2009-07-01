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
 

package org.apache.wink.server.internal.registry;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.wink.common.http.HttpStatus;
import org.apache.wink.server.internal.handlers.FindRootResourceHandler;
import org.apache.wink.test.mock.MockRequestConstructor;
import org.apache.wink.test.mock.MockServletInvocationTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;


public class FindResourceMethodTest extends MockServletInvocationTest {
    
    private static List<Class<?>> allResources = new LinkedList<Class<?>>();
    private static List<Class<?>> resourceClasses = new LinkedList<Class<?>>();
    private static Properties properties = new Properties();
    
    static {
        for (Class<?> cls : FindResourceMethodTest.class.getClasses()) {
            if (cls.getSimpleName().startsWith("Resource")) {
                allResources.add(cls);
            }
        }        
        resourceClasses = new LinkedList<Class<?>>(allResources);
    }
    
    @Override
    protected Class<?>[] getClasses() {
        return resourceClasses.toArray(new Class<?>[resourceClasses.size()]);
    }
    
    @Override
    protected Properties getProperties() throws IOException {
        return properties;
    }

    // /// -- Resources --

    @Path("/{fallback}")
    public static class ResourceFallback {

        @GET
        public String get() {
            return "ResourceFallback.get";
        }
    }
    
    @Path("/simpleGet")
    public static class ResourceSimpleGet {

        @GET
        public String get(@Context UriInfo uriInfo) {
            List<Object> matchedResources = uriInfo.getMatchedResources();
            assertNotNull(matchedResources);
            assertEquals(1, matchedResources.size());
            assertEquals(ResourceSimpleGet.class, matchedResources.get(0).getClass());
            
            List<String> matchedURIs = uriInfo.getMatchedURIs(false);
            assertNotNull(matchedURIs);
            assertEquals(1, matchedURIs.size());
            assertEquals("simpleGet", matchedURIs.get(0));
            
            return "ResourceSimpleGet.get";
        }
    }

    @Path("/simpleGetAndPost")
    public static class ResourceSimpleGetAndPost {

        @GET
        public String get() {
            return "ResourceSimpleGetAndPost.get";
        }

        @POST
        public String post() {
            return "ResourceSimpleGetAndPost.post";
        }
    }

    @Path("/simplePostConsumes")
    public static class ResourceSimplePostConsumes {

        @POST
        @Consumes({"application/xml"})
        public String postXml() {
            return "ResourceSimplePostConsumes.postXml";
        }

        @POST
        @Consumes({"application/atom+xml"})
        public String postAtom() {
            return "ResourceSimplePostConsumes.postAtom";
        }

        @POST
        @Consumes({"text/plain", "text/html"})
        public String postText() {
            return "ResourceSimplePostConsumes.postText";
        }

        @POST
        @Consumes({"image/*"})
        public String postImage() {
            return "ResourceSimplePostConsumes.postImage";
        }

        @POST
        @Consumes({"*/*"})
        public String postAny() {
            return "ResourceSimplePostConsumes.postAny";
        }
    }

    @Path("/simpleGetProduces")
    public static class ResourceSimpleGetProduces {

        @GET
        @Produces({"application/xml"})
        public String getXml() {
            return "ResourceSimpleGetProduces.getXml";
        }

        @GET
        @Produces({"application/atom+xml"})
        public String getAtom() {
            return "ResourceSimpleGetProduces.getAtom";
        }

        @GET
        @Produces({"text/plain", "text/html"})
        public String getText() {
            return "ResourceSimpleGetProduces.getText";
        }

        @GET
        @Produces({"image/jpeg"})
        public String getImageJpeg() {
            return "ResourceSimpleGetProduces.getImageJpeg";
        }

        @GET
        @Produces({"image/*"})
        public String getImageAny() {
            return "ResourceSimpleGetProduces.getImageAny";
        }

        @GET
        @Produces({"*/*"})
        public String getAny() {
            return "ResourceSimpleGetProduces.getAny";
        }
    }

    @Path("/simpleConsumesAndProduces")
    public static class ResourceSimpleConsumesAndProduces {

        @POST
        @Consumes({"text/*"})
        @Produces({"text/plain"})
        public String postConsumesTextAny() {
            return "ResourceSimpleConsumesAndProduces.postConsumesTextAny";
        }

        @POST
        @Consumes({"text/plain"})
        @Produces({"text/*"})
        public String postProducesTextAny() {
            return "ResourceSimpleConsumesAndProduces.postProducesTextAny";
        }

        @POST
        @Consumes({"text/plain", "text/html"})
        @Produces({"text/plain"})
        public String postConsumesTextHtml() {
            return "ResourceSimpleConsumesAndProduces.postConsumesTextHtml";
        }

        @POST
        @Consumes({"text/plain"})
        @Produces({"text/plain", "text/html"})
        public String postProducesTextHtml() {
            return "ResourceSimpleConsumesAndProduces.postProducesTextHtml";
        }

        @POST
        @Consumes({"text/plain", "application/*", "image/*"})
        @Produces({"application/xml", "image/jpeg"})
        public String postConsumesManyProduceMany() {
            return "ResourceSimpleConsumesAndProduces.postConsumesManyProduceMany";
        }
    }

    @Path("/subResourceMethodSimpleGet")
    public static class ResourceWithSubResourceMethodSimpleGet {
        private boolean located = false;
        public ResourceWithSubResourceMethodSimpleGet(){}
        
        public ResourceWithSubResourceMethodSimpleGet(boolean located) {
            this.located = located;
        }

        @GET
        @Produces({"application/atom+xml"})
        public String getAtom() {
            return "ResourceSimpleGetProduces.getAtom";
        }

        @GET
        @Path("{id}")
        public String getAny(@Context UriInfo uriInfo) {
            List<Object> matchedResources = uriInfo.getMatchedResources();
            assertNotNull(matchedResources);
            List<String> matchedURIs = uriInfo.getMatchedURIs(false);
            assertNotNull(matchedURIs);
            if (located) {
                assertEquals(2, matchedResources.size());
                assertEquals(ResourceWithSubResourceMethodSimpleGet.class, matchedResources.get(0).getClass());
                assertEquals(ResourceWithSubResourceLocatorSimpleGet.class, matchedResources.get(1).getClass());
                assertEquals(3, matchedURIs.size());
                assertEquals("subResourceLocatorSimpleGet/1/2", matchedURIs.get(0));
                assertEquals("subResourceLocatorSimpleGet/1", matchedURIs.get(1));
                assertEquals("subResourceLocatorSimpleGet", matchedURIs.get(2));
            } else {
                assertEquals(1, matchedResources.size());
                assertEquals(ResourceWithSubResourceMethodSimpleGet.class, matchedResources.get(0).getClass());
                assertEquals(2, matchedURIs.size());
                assertEquals("subResourceMethodSimpleGet/1", matchedURIs.get(0));
                assertEquals("subResourceMethodSimpleGet", matchedURIs.get(1));
            }
            return "ResourceWithSubResourceMethodSimpleGet.getAny";
        }

        @GET
        @Path("{id}")
        @Produces("text/plain")
        public String getTextPlain() {
            return "ResourceWithSubResourceMethodSimpleGet.getTextPlain";
        }

        @GET
        @Path("{id}")
        @Produces("text/html")
        public String getTextHtml() {
            return "ResourceWithSubResourceMethodSimpleGet.getTextHtml";
        }

        @GET
        @Path("{id}/4")
        @Produces("text/xhtml")
        public String getSubId4() {
            return "ResourceWithSubResourceMethodSimpleGet.getSubId4";
        }

        @GET
        @Path("{id}/{sub-id}")
        @Produces("text/xhtml")
        public String getSubIdAny() {
            return "ResourceWithSubResourceMethodSimpleGet.getSubIdAny";
        }

        @POST
        @Path("{id}")
        @Consumes("text/*")
        @Produces("text/html")
        public String postTextAny() {
            return "ResourceWithSubResourceMethodSimpleGet.postTextAny";
        }

        @POST
        @Path("{id}")
        @Consumes("image/*")
        @Produces("text/html")
        public String postImageAny() {
            return "ResourceWithSubResourceMethodSimpleGet.postImageAny";
        }

    }

    @Path("/subResourceLocatorSimpleGet")
    public static class ResourceWithSubResourceLocatorSimpleGet {

        @Path("{id}")
        public ResourceWithSubResourceMethodSimpleGet getAny(@PathParam("id") String id, @Context UriInfo uriInfo) {
            List<Object> matchedResources = uriInfo.getMatchedResources();
            assertNotNull(matchedResources);
            assertEquals(1, matchedResources.size());
            assertEquals(ResourceWithSubResourceLocatorSimpleGet.class, matchedResources.get(0).getClass());
            
            List<String> matchedURIs = uriInfo.getMatchedURIs(false);
            assertNotNull(matchedURIs);
            assertEquals(2, matchedURIs.size());
            assertEquals("subResourceLocatorSimpleGet/"+id, matchedURIs.get(0));
            assertEquals("subResourceLocatorSimpleGet", matchedURIs.get(1));
            
            return new ResourceWithSubResourceMethodSimpleGet(true);
        }

        @Path("{id}/4")
        public ResourceWithSubResourceMethodSimpleGet getSubId4(@PathParam("id") String id) {
            assertEquals("1", id);
            return new ResourceWithSubResourceMethodSimpleGet();
        }
        
        @Path("ignore-consumes")
        @Consumes("text/kuku")
        public ResourceWithSubResourceMethodSimpleGet ignoreConsumes() {
            return new ResourceWithSubResourceMethodSimpleGet();
        }

        @Path("ignore-produces")
        @Produces("text/kuku")
        public ResourceWithSubResourceMethodSimpleGet ignoreProduces() {
            return new ResourceWithSubResourceMethodSimpleGet();
        }
        
        // locators cannot have an entity param
        @Path("bad-locator")
        public void badLocator(String entity) {
            fail("locator method should not have been invoked");
        }
        
    }

    @Path("/mixed/{id}")
    public static class ResourceMixed {

        // methods
        @GET
        @Produces("application/xml")
        public String getXml() {
            return "ResourceMixed.getXml";
        }

        @POST
        @Consumes("application/xml")
        public String postXml() {
            return "ResourceMixed.postXml";
        }

        @POST
        @Consumes("image/gif")
        @Produces("image/jpeg")
        public String postImage() {
            return "ResourceMixed.postImage";
        }

        // sub-resource methods
        @GET
        @Path("{sub-id}")
        @Produces("text/plain")
        public String getTextPlain() {
            return "ResourceMixed.getTextPlain";
        }

        @Path("locate")
        public ResourceMixed locateTextPlainSpecific() {
            return new ResourceMixed();
        }

        @POST
        @Path("{sub-id}")
        @Consumes("text/*")
        @Produces("text/html")
        public String postTextAny() {
            return "ResourceMixed.postTextAny";
        }

        // sub-resource locators
        @Path("{sub-id}")
        public ResourceMixed locateTextPlain() {
            return new ResourceMixed();
        }

        @Path("locateNull")
        public ResourceMixed locateNull() {
            return null;
        }
    }
    
    // == resources for continued search policy testing 

    @Path("/{continued}")
    public static class ContinuedSearchResource {
        @PUT
        public String put(@Context UriInfo uriInfo) {
            MultivaluedMap<String,String> variables = uriInfo.getPathParameters();
            assertEquals("simpleGet", variables.getFirst("continued"));
            return "ContinuedSearchResource.put";
        }
        
        @PUT
        @Path("{subPutId}")
        public String subPut(@Context UriInfo uriInfo) {
            MultivaluedMap<String,String> variables = uriInfo.getPathParameters();
            assertEquals("subResourceMethodSimpleGet", variables.getFirst("continued"));
            assertEquals("1", variables.getFirst("subPutId"));
            return "ContinuedSearchResource.subPut";
        }
        
        @Path("{subLocatorId}")
        public LocatedContinuedSearchResource subLocator() {
            return new LocatedContinuedSearchResource();
        }
    }
    
    @Path("/continuedSearchResourceLocatorBad")
    public static class ContinuedSearchResourceLocatorBad {
        @Path("{badSubLocatorId}")
        public ResourceWithSubResourceMethodSimpleGet subLocator() {
            return new ResourceWithSubResourceMethodSimpleGet();
        }
    }

    public static class LocatedContinuedSearchResource {
        @PUT
        @Path("{locatedSubPutId}")
        public String subPut(@Context UriInfo uriInfo) {
            MultivaluedMap<String,String> variables = uriInfo.getPathParameters();
            assertEquals("continuedSearchResourceLocatorBad", variables.getFirst("continued"));
            assertEquals("1", variables.getFirst("subLocatorId"));
            assertEquals("2", variables.getFirst("locatedSubPutId"));
            assertNull(variables.getFirst("badSubLocatorId"));
            assertNull(variables.getFirst("id"));
            return "LocatedContinuedSearchResource.subPut";
        }        
    }
    
    // /// -- Tests --

    public void testFindResourceSimple() throws Exception {
        MockHttpServletRequest request = null;
        MockHttpServletResponse response = null;
        
        request = MockRequestConstructor.constructMockRequest("GET", "/simpleGet", "text/plain");
        response = invoke(request);
        assertMethodFound(response, ResourceSimpleGet.class, "get");
 
        //
        request = MockRequestConstructor.constructMockRequest("POST", "/simpleGetAndPost", "*/*");
        response = invoke(request);
        assertMethodFound(response, ResourceSimpleGetAndPost.class, "post");

        //
        request = MockRequestConstructor.constructMockRequest("POST", "/simplePostConsumes", "*/*", "application/xml", null);
        response = invoke(request);
        assertMethodFound(response, ResourceSimplePostConsumes.class, "postXml");

        request = MockRequestConstructor.constructMockRequest("POST", "/simplePostConsumes", "text/plain", "application/xml", null);
        response = invoke(request);
        assertMethodFound(response, ResourceSimplePostConsumes.class, "postXml");

        request = MockRequestConstructor.constructMockRequest("POST", "/simplePostConsumes", "*/*", "application/atom+xml", null);
        response = invoke(request);
        assertMethodFound(response, ResourceSimplePostConsumes.class, "postAtom");

        request = MockRequestConstructor.constructMockRequest("POST", "/simplePostConsumes", "*/*", "text/plain", null);
        response = invoke(request);
        assertMethodFound(response, ResourceSimplePostConsumes.class, "postText");

        request = MockRequestConstructor.constructMockRequest("POST", "/simplePostConsumes", "*/*", "text/html", null);
        response = invoke(request);
        assertMethodFound(response, ResourceSimplePostConsumes.class, "postText");

        request = MockRequestConstructor.constructMockRequest("POST", "/simplePostConsumes", "*/*", "image/jpeg", null);
        response = invoke(request);
        assertMethodFound(response, ResourceSimplePostConsumes.class, "postImage");

        request = MockRequestConstructor.constructMockRequest("POST", "/simplePostConsumes", "*/*", "image/gif",  null);
        response = invoke(request);
        assertMethodFound(response, ResourceSimplePostConsumes.class, "postImage");

        request = MockRequestConstructor.constructMockRequest("POST", "/simplePostConsumes", "*/*", "mytype/mysubtype", null);
        response = invoke(request);
        assertMethodFound(response, ResourceSimplePostConsumes.class, "postAny");

        //
        request = MockRequestConstructor.constructMockRequest("GET", "/simpleGetProduces", "application/xml");
        response = invoke(request);
        assertMethodFound(response, ResourceSimpleGetProduces.class, "getXml");

        request = MockRequestConstructor.constructMockRequest("GET", "/simpleGetProduces", "application/atom+xml");
        response = invoke(request);
        assertMethodFound(response, ResourceSimpleGetProduces.class, "getAtom");

        request = MockRequestConstructor.constructMockRequest("GET", "/simpleGetProduces", "text/plain");
        response = invoke(request);
        assertMethodFound(response, ResourceSimpleGetProduces.class, "getText");

        request = MockRequestConstructor.constructMockRequest("GET", "/simpleGetProduces", "text/html");
        response = invoke(request);
        assertMethodFound(response, ResourceSimpleGetProduces.class, "getText");

        request = MockRequestConstructor.constructMockRequest("GET", "/simpleGetProduces", "image/jpeg");
        response = invoke(request);
        assertMethodFound(response, ResourceSimpleGetProduces.class, "getImageJpeg");

        request = MockRequestConstructor.constructMockRequest("GET", "/simpleGetProduces", "image/gif");
        response = invoke(request);
        assertMethodFound(response, ResourceSimpleGetProduces.class, "getImageAny");

        request = MockRequestConstructor.constructMockRequest("GET", "/simpleGetProduces", "image/gif;q=0.6,image/jpeg;q=0.5");
        response = invoke(request);
        assertMethodFound(response, ResourceSimpleGetProduces.class, "getImageAny");

        request = MockRequestConstructor.constructMockRequest("GET", "/simpleGetProduces", "text/plain;q=0.4,image/jpeg;q=0.5");
        response = invoke(request);
        assertMethodFound(response, ResourceSimpleGetProduces.class, "getImageJpeg");

        request = MockRequestConstructor.constructMockRequest("GET", "/simpleGetProduces", "mytype/mysubtype");
        response = invoke(request);
        assertMethodFound(response, ResourceSimpleGetProduces.class, "getAny");

        //
        request = MockRequestConstructor.constructMockRequest("POST", "/simpleConsumesAndProduces", "text/plain", "text/plain", null);
        response = invoke(request);
        assertMethodFound(response, ResourceSimpleConsumesAndProduces.class, "postConsumesTextHtml");

        request = MockRequestConstructor.constructMockRequest("POST", "/simpleConsumesAndProduces", "text/plain", "text/xhtml", null);
        response = invoke(request);
        assertMethodFound(response, ResourceSimpleConsumesAndProduces.class, "postConsumesTextAny");

        request = MockRequestConstructor.constructMockRequest("POST", "/simpleConsumesAndProduces", "text/xhtml", "text/plain", null);
        response = invoke(request);
        assertMethodFound(response, ResourceSimpleConsumesAndProduces.class, "postProducesTextAny");

        request = MockRequestConstructor.constructMockRequest("POST", "/simpleConsumesAndProduces", "text/plain", "text/html", null);
        response = invoke(request);
        assertMethodFound(response, ResourceSimpleConsumesAndProduces.class, "postConsumesTextHtml");

        request = MockRequestConstructor.constructMockRequest("POST", "/simpleConsumesAndProduces", "application/xml", "text/plain", null);
        response = invoke(request);
        assertMethodFound(response, ResourceSimpleConsumesAndProduces.class, "postConsumesManyProduceMany");

        request = MockRequestConstructor.constructMockRequest("POST", "/simpleConsumesAndProduces", "application/xml", "application/atom+xml", null);
        response = invoke(request);
        assertMethodFound(response, ResourceSimpleConsumesAndProduces.class, "postConsumesManyProduceMany");

        request = MockRequestConstructor.constructMockRequest("POST", "/simpleConsumesAndProduces", "application/xml", "image/gif", null);
        response = invoke(request);
        assertMethodFound(response, ResourceSimpleConsumesAndProduces.class, "postConsumesManyProduceMany");

        request = MockRequestConstructor.constructMockRequest("POST", "/simpleConsumesAndProduces", "image/jpeg", "text/plain", null);
        response = invoke(request);
        assertMethodFound(response, ResourceSimpleConsumesAndProduces.class, "postConsumesManyProduceMany");

        request = MockRequestConstructor.constructMockRequest("POST", "/simpleConsumesAndProduces", "image/jpeg", "application/atom+xml", null);
        response = invoke(request);
        assertMethodFound(response, ResourceSimpleConsumesAndProduces.class, "postConsumesManyProduceMany");

        request = MockRequestConstructor.constructMockRequest("POST", "/simpleConsumesAndProduces", "image/jpeg", "image/gif", null);
        response = invoke(request);
        assertMethodFound(response, ResourceSimpleConsumesAndProduces.class, "postConsumesManyProduceMany");

        request = MockRequestConstructor.constructMockRequest("POST", "/simpleConsumesAndProduces", "image/*,text/html", "text/plain", null);
        response = invoke(request);
        assertMethodFound(response, ResourceSimpleConsumesAndProduces.class, "postProducesTextHtml");

        request = MockRequestConstructor.constructMockRequest("POST", "/simpleConsumesAndProduces", 
                "text/plain;q=0.405,image/*;q=0.406", "text/plain", null);
        response = invoke(request);
        assertMethodFound(response, ResourceSimpleConsumesAndProduces.class, "postConsumesManyProduceMany");
    }

    public void testFindResourceWithSubResourceMethodSimpleGet() throws Exception {
        MockHttpServletRequest request = null;
        MockHttpServletResponse response = null;
        
        request = MockRequestConstructor.constructMockRequest("GET", "/subResourceMethodSimpleGet/1", "text/plain");
        response = invoke(request);
        assertMethodFound(response, ResourceWithSubResourceMethodSimpleGet.class, "getTextPlain");
        
        request = MockRequestConstructor.constructMockRequest("GET", "/subResourceMethodSimpleGet/1", "text/html");
        response = invoke(request);
        assertMethodFound(response, ResourceWithSubResourceMethodSimpleGet.class, "getTextHtml");

        request = MockRequestConstructor.constructMockRequest("POST", "/subResourceMethodSimpleGet/1", "text/html", "text/xhtml", null);
        response = invoke(request);
        assertMethodFound(response, ResourceWithSubResourceMethodSimpleGet.class, "postTextAny");

        request = MockRequestConstructor.constructMockRequest("POST", "/subResourceMethodSimpleGet/1", "text/html", "image/jpeg", null);
        response = invoke(request);
        assertMethodFound(response, ResourceWithSubResourceMethodSimpleGet.class, "postImageAny");

        request = MockRequestConstructor.constructMockRequest("GET", "/subResourceMethodSimpleGet/1", "application/xml", "image/jpeg", null);
        response = invoke(request);
        assertMethodFound(response, ResourceWithSubResourceMethodSimpleGet.class, "getAny");

        request = MockRequestConstructor.constructMockRequest("GET", "/subResourceMethodSimpleGet/1/4", "text/xhtml");
        response = invoke(request);
        assertMethodFound(response, ResourceWithSubResourceMethodSimpleGet.class, "getSubId4");

        request = MockRequestConstructor.constructMockRequest("GET", "/subResourceMethodSimpleGet/1/5", "text/xhtml");
        response = invoke(request);
        assertMethodFound(response, ResourceWithSubResourceMethodSimpleGet.class, "getSubIdAny");
    }

    public void testFindResourceWithSubResourceLocatorSimpleGet() throws Exception {
        MockHttpServletRequest request = null;
        MockHttpServletResponse response = null;

        request = MockRequestConstructor.constructMockRequest("GET", "/subResourceLocatorSimpleGet/1/2", "text/plain");
        response = invoke(request);
        assertMethodFound(response, ResourceWithSubResourceMethodSimpleGet.class, "getTextPlain");
        
        request = MockRequestConstructor.constructMockRequest("GET", "/subResourceLocatorSimpleGet/1/2", "text/html");
        response = invoke(request);
        assertMethodFound(response, ResourceWithSubResourceMethodSimpleGet.class, "getTextHtml");

        request = MockRequestConstructor.constructMockRequest("POST", "/subResourceLocatorSimpleGet/1/2", "text/html", "text/xhtml", null);
        response = invoke(request);
        assertMethodFound(response, ResourceWithSubResourceMethodSimpleGet.class, "postTextAny");

        request = MockRequestConstructor.constructMockRequest("POST", "/subResourceLocatorSimpleGet/1/2", "text/html", "image/jpeg", null);
        response = invoke(request);
        assertMethodFound(response, ResourceWithSubResourceMethodSimpleGet.class, "postImageAny");

        request = MockRequestConstructor.constructMockRequest("GET", "/subResourceLocatorSimpleGet/1/2", "application/xml", "image/jpeg", null);
        response = invoke(request);
        assertMethodFound(response, ResourceWithSubResourceMethodSimpleGet.class, "getAny");

        request = MockRequestConstructor.constructMockRequest("GET", "/subResourceLocatorSimpleGet/1", "application/atom+xml");
        response = invoke(request);
        assertMethodFound(response, ResourceSimpleGetProduces.class, "getAtom");

        request = MockRequestConstructor.constructMockRequest("GET", "/subResourceLocatorSimpleGet/1/4/2/4", "text/xhtml");
        response = invoke(request);
        assertMethodFound(response, ResourceWithSubResourceMethodSimpleGet.class, "getSubId4");

        request = MockRequestConstructor.constructMockRequest("GET", "/subResourceLocatorSimpleGet/1/5/5", "text/xhtml");
        response = invoke(request);
        assertMethodFound(response, ResourceWithSubResourceMethodSimpleGet.class, "getSubIdAny");

        request = MockRequestConstructor.constructMockRequest("GET", "/subResourceLocatorSimpleGet/ignore-consumes/2", "text/plain");
        response = invoke(request);
        assertMethodFound(response, ResourceWithSubResourceMethodSimpleGet.class, "getTextPlain");
        
        request = MockRequestConstructor.constructMockRequest("GET", "/subResourceLocatorSimpleGet/ignore-produces/2", "text/plain");
        response = invoke(request);
        assertMethodFound(response, ResourceWithSubResourceMethodSimpleGet.class, "getTextPlain");
    
        request = MockRequestConstructor.constructMockRequest("GET", "/subResourceLocatorSimpleGet/bad-locator/2", "text/plain");
        response = invoke(request);
        assertMethodFound(response, ResourceWithSubResourceMethodSimpleGet.class, "getTextPlain");

    }

    public void testMixedResource() throws Exception {
        MockHttpServletRequest request = null;
        MockHttpServletResponse response = null;

        request = MockRequestConstructor.constructMockRequest("GET", "/mixed/1", "application/xml");
        response = invoke(request);
        assertMethodFound(response, ResourceMixed.class, "getXml");

        request = MockRequestConstructor.constructMockRequest("POST", "/mixed/1", "*/*", "application/xml", null);
        response = invoke(request);
        assertMethodFound(response, ResourceMixed.class, "postXml");

        request = MockRequestConstructor.constructMockRequest("GET", "/mixed/1/2", "text/plain");
        response = invoke(request);
        assertMethodFound(response, ResourceMixed.class, "getTextPlain");

        request = MockRequestConstructor.constructMockRequest("POST", "/mixed/1/2", "text/html", "text/plain", null);
        response = invoke(request);
        assertMethodFound(response, ResourceMixed.class, "postTextAny");

        request = MockRequestConstructor.constructMockRequest("POST", "/mixed/1/2", "text/html", "text/xhtml", null);
        response = invoke(request);
        assertMethodFound(response, ResourceMixed.class, "postTextAny");

        request = MockRequestConstructor.constructMockRequest("GET", "/mixed/1/2/3", "text/plain");
        response = invoke(request);
        assertMethodFound(response, ResourceMixed.class, "getTextPlain");

        request = MockRequestConstructor.constructMockRequest("GET", "/mixed/locate", "application/xml");
        response = invoke(request);
        assertMethodFound(response, ResourceMixed.class, "getXml");
    }

    public void testErrorStates() throws Exception {
        MockHttpServletRequest request = null;
        MockHttpServletResponse response = null;

        request = MockRequestConstructor.constructMockRequest("GET", "/not/exist/path", "application/xml");
        response = invoke(request);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());

        request = MockRequestConstructor.constructMockRequest("GET", "/simpleGet/1", "application/xml");
        response = invoke(request);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());

        request = MockRequestConstructor.constructMockRequest("GET", "/subResourceMethodSimpleGet/not-exist/1/2", "text/plain");
        response = invoke(request);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());

        request = MockRequestConstructor.constructMockRequest("DELETE", "/mixed/1", "application/xml");
        response = invoke(request);
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED.getCode(), response.getStatus());

        request = MockRequestConstructor.constructMockRequest("POST", "/mixed/1", "*/*", "application/atom+xml", null);
        response = invoke(request);
        assertEquals(Response.Status.UNSUPPORTED_MEDIA_TYPE.getStatusCode(), response.getStatus());

        request = MockRequestConstructor.constructMockRequest("POST", "/mixed/1", "image/bmp,image/tiff", "image/gif", null);
        response = invoke(request);
        assertEquals(Response.Status.NOT_ACCEPTABLE.getStatusCode(), response.getStatus());

        request = MockRequestConstructor.constructMockRequest("GET", "/mixed/1/locateNull", "application/xml");
        response = invoke(request);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
    
    public void testContinuedSearchSetup() {
        // set up the environment for the continued search test thats that are coming next
        resourceClasses.clear();
        resourceClasses.add(ContinuedSearchResource.class);
        resourceClasses.add(ResourceSimpleGet.class);
        resourceClasses.add(ResourceWithSubResourceMethodSimpleGet.class);
        resourceClasses.add(ContinuedSearchResourceLocatorBad.class);
        properties.setProperty(FindRootResourceHandler.SEARCH_POLICY_CONTINUED_SEARCH_KEY, "false");
    }
    
    public void testContinuedSearch_1_1() throws Exception {
        MockHttpServletRequest request = null;
        MockHttpServletResponse response = null;
        
        // 1. test resource method
        // 1.1. negative test - make sure that ContinuedSearchResource is not reachable when continued search policy is off
        request = MockRequestConstructor.constructMockRequest("PUT", "/simpleGet", "text/plain", "text/plain", null);        
        response = invoke(request);
        assertEquals(405, response.getStatus());
        
        // set the continuedSearch flag for the continued search test that is coming next
        properties.setProperty(FindRootResourceHandler.SEARCH_POLICY_CONTINUED_SEARCH_KEY, "true");
    }
    
    public void testContinuedSearch_1_2() throws Exception {
        MockHttpServletRequest request = null;
        MockHttpServletResponse response = null;

        // 1.2. make sure that ContinuedSearchResource is reachable when continued search policy is activated 
        request = MockRequestConstructor.constructMockRequest("PUT", "/simpleGet", "text/plain", "text/plain", null);        
        response = invoke(request);
        assertMethodFound(response, ContinuedSearchResource.class, "put");
        
        // set the continuedSearch flag for the continued search test that is coming next
        properties.setProperty(FindRootResourceHandler.SEARCH_POLICY_CONTINUED_SEARCH_KEY, "false");
    }
    
    public void testContinuedSearch_2_1() throws Exception {
        MockHttpServletRequest request = null;
        MockHttpServletResponse response = null;

        // 2. test sub-resource method
        // 2.1. negative test - make sure that ContinuedSearchResource is not reachable when continued search policy is off
        request = MockRequestConstructor.constructMockRequest("PUT", "/subResourceMethodSimpleGet/1", "text/plain", "text/plain", null);        
        response = invoke(request);
        assertEquals(405, response.getStatus());

        // set the continuedSearch flag for the continued search test that is coming next
        properties.setProperty(FindRootResourceHandler.SEARCH_POLICY_CONTINUED_SEARCH_KEY, "true");
    }

    public void testContinuedSearch_2_2() throws Exception {
        MockHttpServletRequest request = null;
        MockHttpServletResponse response = null;
        
        // 2.2. make sure that ContinuedSearchResource is reachable when continued search policy is activated 
        request = MockRequestConstructor.constructMockRequest("PUT", "/subResourceMethodSimpleGet/1", "text/plain", "text/plain", null);        
        response = invoke(request);
        assertMethodFound(response, ContinuedSearchResource.class, "subPut");
        
        // set the continuedSearch flag for the continued search test that is coming next
        properties.setProperty(FindRootResourceHandler.SEARCH_POLICY_CONTINUED_SEARCH_KEY, "false");
    }
    
    public void testContinuedSearch_3_1() throws Exception {
        MockHttpServletRequest request = null;
        MockHttpServletResponse response = null;
        
        // 3. test sub-resource locator
        // 3.1. negative test - make sure that ContinuedSearchResource is not reachable when continued search policy is off
        request = MockRequestConstructor.constructMockRequest("PUT", "/continuedSearchResourceLocatorBad/1/2", "text/plain", "text/plain", null);        
        response = invoke(request);
        assertEquals(405, response.getStatus());
        
        // set the continuedSearch flag for the continued search test that is coming next
        properties.setProperty(FindRootResourceHandler.SEARCH_POLICY_CONTINUED_SEARCH_KEY, "true");
    }

    public void testContinuedSearch_3_2() throws Exception {
        MockHttpServletRequest request = null;
        MockHttpServletResponse response = null;

        // 3.2. make sure that ContinuedSearchResource is reachable when continued search policy is activated 
        request = MockRequestConstructor.constructMockRequest("PUT", "/continuedSearchResourceLocatorBad/1/2", "text/plain", "text/plain", null);        
        response = invoke(request);
        assertMethodFound(response, LocatedContinuedSearchResource.class, "subPut");
    }

    // // -- Helpers --

    private void assertMethodFound(MockHttpServletResponse response, Class<?> expectedResource, String expectedMethod) throws UnsupportedEncodingException {
        assertEquals(200, response.getStatus());
        String expected = expectedResource.getSimpleName() + "." + expectedMethod;
        assertEquals(expected, response.getContentAsString());
    }

}