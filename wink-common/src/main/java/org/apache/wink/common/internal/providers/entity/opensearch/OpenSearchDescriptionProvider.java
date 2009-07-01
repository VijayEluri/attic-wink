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
 
package org.apache.wink.common.internal.providers.entity.opensearch;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;

import org.apache.wink.common.internal.utils.JAXBUtils;
import org.apache.wink.common.internal.utils.MediaTypeUtils;
import org.apache.wink.common.model.XmlFormattingOptions;
import org.apache.wink.common.model.atom.AtomJAXBUtils;
import org.apache.wink.common.model.opensearch.ObjectFactory;
import org.apache.wink.common.model.opensearch.OpenSearchDescription;
import org.apache.wink.common.utils.ProviderUtils;



@Provider
@Produces(MediaTypeUtils.OPENSEARCH)
public class OpenSearchDescriptionProvider implements MessageBodyWriter<OpenSearchDescription> {

    private final ObjectFactory of = new ObjectFactory();

    public long getSize(OpenSearchDescription t, Class<?> type, Type genericType,
        Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations,
        MediaType mediaType) {
        return (type == OpenSearchDescription.class);
    }

    public void writeTo(OpenSearchDescription openSearchDescriptor, Class<?> type,
        Type genericType, Annotation[] annotations, MediaType mediaType,
        MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException,
        WebApplicationException {
        JAXBElement<OpenSearchDescription> openSearchDocumentWrapper = of.createOpenSearchDescription(openSearchDescriptor);
        Marshaller marshaller = OpenSearchDescription.getMarshaller();
        JAXBUtils.setXmlFormattingOptions(marshaller,
            XmlFormattingOptions.getDefaultXmlFormattingOptions());

        Writer writer = ProviderUtils.createWriter(entityStream, mediaType);
        AtomJAXBUtils.marshal(marshaller, openSearchDocumentWrapper, null, writer);
        writer.flush();
    }

}