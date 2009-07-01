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
 

package org.apache.wink.server.internal.providers.entity;

import java.io.StringReader;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.wink.common.model.atom.AtomEntry;
import org.apache.wink.common.model.synd.SyndEntry;
import org.apache.wink.test.mock.MockRequestConstructor;
import org.apache.wink.test.mock.MockServletInvocationTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;


public class AtomEntryProviderTest extends MockServletInvocationTest {
    
    @Override
    protected Class<?>[] getClasses() {
        return new Class<?>[]{TestResource.class};
    }

    @Path("test")
    public static class TestResource {
        
        @GET
        @Path("atomentry")
        @Produces("application/atom+xml")
        public AtomEntry getAtomEntry() {
            AtomEntry entry = AtomEntry.unmarshal(new StringReader(ENTRY));
            return entry;
        }
        
        @GET
        @Path("atomentryelement")
        @Produces("application/atom+xml")
        public JAXBElement<AtomEntry> getAtomEntryElement() {
            AtomEntry entry = AtomEntry.unmarshal(new StringReader(ENTRY));
            org.apache.wink.common.model.atom.ObjectFactory of = new org.apache.wink.common.model.atom.ObjectFactory();
            return of.createEntry(entry);
        }

        @GET
        @Path("atomsyndentry")
        @Produces("application/atom+xml")
        public SyndEntry getSyndEntry() {
            AtomEntry entry = AtomEntry.unmarshal(new StringReader(ENTRY));
            return entry.toSynd(new SyndEntry());
        }
        
        @POST
        @Path("atomentry")
        @Produces("application/atom+xml")
        @Consumes("application/atom+xml")
        public AtomEntry postAtomEntry(AtomEntry entry) {
            return entry;
        }

        @POST
        @Path("atomentryelement")
        @Produces("application/atom+xml")
        @Consumes("application/atom+xml")
        public JAXBElement<AtomEntry> postAtomEntryElement(JAXBElement<AtomEntry> entry) {
            return entry;
        }

        @POST
        @Path("atomsyndentry")
        @Produces("application/atom+xml")
        @Consumes("application/atom+xml")
        public SyndEntry postAtomSyndEntry(SyndEntry entry) {
            return entry;
        }

    }

    public void testGetAtomEntry() throws Exception {
        MockHttpServletRequest request = MockRequestConstructor.constructMockRequest("GET", "/test/atomentry", "application/atom+xml");
        MockHttpServletResponse response = invoke(request);
        assertEquals(200, response.getStatus());
        assertEquals(ENTRY, response.getContentAsString());
    }
    
    public void testGetAtomEntryElement() throws Exception {
        MockHttpServletRequest request = MockRequestConstructor.constructMockRequest("GET", "/test/atomentryelement", "application/atom+xml");
        MockHttpServletResponse response = invoke(request);
        assertEquals(200, response.getStatus());
        assertEquals(ENTRY, response.getContentAsString());
    }

    public void testGetAtomSyndEntry() throws Exception {
        MockHttpServletRequest request = MockRequestConstructor.constructMockRequest("GET", "/test/atomsyndentry", "application/atom+xml");
        MockHttpServletResponse response = invoke(request);
        assertEquals(200, response.getStatus());
        assertEquals(ENTRY, response.getContentAsString());
    }
    
    public void testPostAtomEntry() throws Exception {
        MockHttpServletRequest request = MockRequestConstructor.constructMockRequest("POST", "/test/atomentry", "application/atom+xml");
        request.setContentType("application/atom+xml");
        request.setContent(ENTRY.getBytes());
        MockHttpServletResponse response = invoke(request);
        assertEquals(200, response.getStatus());
        assertEquals(ENTRY, response.getContentAsString());
    }

    public void testPostAtomEntryElement() throws Exception {
        MockHttpServletRequest request = MockRequestConstructor.constructMockRequest("POST", "/test/atomentryelement", "application/atom+xml");
        request.setContentType("application/atom+xml");
        request.setContent(ENTRY.getBytes());
        MockHttpServletResponse response = invoke(request);
        assertEquals(200, response.getStatus());
        assertEquals(ENTRY, response.getContentAsString());
    }

    public void testPostAtomSyndEntry() throws Exception {
        MockHttpServletRequest request = MockRequestConstructor.constructMockRequest("POST", "/test/atomsyndentry", "application/atom+xml");
        request.setContentType("application/atom+xml");
        request.setContent(ENTRY.getBytes());
        MockHttpServletResponse response = invoke(request);
        assertEquals(200, response.getStatus());
        assertEquals(ENTRY, response.getContentAsString());
    }

    private static final String ENTRY_STR =
    		"<entry xml:base=\"http://b216:8080/reporting/reports\" xmlns=\"http://www.w3.org/2005/Atom\">\n" + 
    		"    <id>toptenvalidators</id>\n" + 
    		"    <updated>@TIME@</updated>\n" + 
    		"    <title type=\"text\" xml:lang=\"en\">top ten validators</title>\n" + 
    		"    <published>@TIME@</published>\n" + 
    		"    <link href=\"http://b216:8080/reporting/reports/toptenvalidators?alt=application/json\" type=\"application/json\" rel=\"alternate\"/>\n" + 
            "    <link href=\"http://b216:8080/reporting/reports/toptenvalidators?alt=text/plain\" type=\"text/plain\" rel=\"alternate\"/>\n" + 
    		"    <link href=\"http://b216:8080/reporting/reports/toptenvalidators\" rel=\"self\"/>\n" + 
    		"    <link href=\"http://b216:8080/reporting/reports/toptenvalidators?alt=text/xml\" type=\"text/xml\" rel=\"alternate\"/>\n" + 
    		"    <link href=\"http://b216:8080/reporting/reports/toptenvalidators/documents/\" type=\"application/atom+xml\" rel=\"execute\"/>\n" + 
    		"    <link href=\"http://b216:8080/reporting/reports/toptenvalidators\" type=\"application/atom+xml\" rel=\"edit\"/>\n" + 
    		"    <link href=\"http://b216:8080/reporting/reports/toptenvalidators?alt=application/xml\" type=\"application/xml\" rel=\"alternate\"/>\n" + 
    		"    <link href=\"http://b216:8080/reporting/reports/toptenvalidators\" type=\"application/xml\" rel=\"edit-media\"/>\n" + 
    		"    <author>\n" + 
    		"        <name>admin</name>\n" + 
    		"    </author>\n" + 
    		"    <category label=\"report definition\" scheme=\"urn:com:systinet:reporting:kind\" term=\"urn:com:systinet:reporting:kind:definition\"/>\n" + 
    		"    <category label=\"Policy Manager - Homepage Report\" scheme=\"urn:com:systinet:policymgr:report:type\" term=\"aoi\"/>\n" + 
    		"</entry>\n";
    
    private static final String ENTRY;
    
    static {
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTimeInMillis((new Date()).getTime());
            XMLGregorianCalendar xmlGregCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
            ENTRY = ENTRY_STR.replace("@TIME@", xmlGregCal.toXMLFormat());
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}