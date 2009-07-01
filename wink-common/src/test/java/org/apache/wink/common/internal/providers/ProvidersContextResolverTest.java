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
package org.apache.wink.common.internal.providers;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.apache.wink.common.internal.application.ApplicationValidator;
import org.apache.wink.common.internal.factory.OFFactoryRegistry;
import org.apache.wink.common.internal.registry.ProvidersRegistry;


import junit.framework.TestCase;

public class ProvidersContextResolverTest extends TestCase {

    public static class NotAProvider {
    }

    private static final String  STRING = "String";
    private static final String  ATOM   = "Atom";
    private static final byte[]  BYTE   = new byte[0];
    private static final Integer _12345 = new Integer(12345);

    @Provider
    @Produces( { MediaType.TEXT_PLAIN, MediaType.WILDCARD })
    public static class StringContextResolver implements ContextResolver<String> {

        public String getContext(Class<?> type) {
            return STRING;
        }
    }

    @Provider
    @Produces( { MediaType.APPLICATION_ATOM_XML, MediaType.WILDCARD })
    public static class AtomContextResolver implements ContextResolver<String> {

        public String getContext(Class<?> type) {
            if (type == null) {
                return ATOM;
            }
            return null;
        }
    }

    @Provider
    @Produces( { MediaType.TEXT_PLAIN, MediaType.WILDCARD })
    public static class ByteContextResolver implements ContextResolver<byte[]> {

        public byte[] getContext(Class<?> type) {
            return BYTE;
        }
    }

    @Provider
    @Produces( { "text/decimal" })
    public static class IntegerContextResolver implements ContextResolver<Integer> {

        public Integer getContext(Class<?> type) {
            return _12345;
        }
    }

    @Provider
    public static class ListContextResolver implements ContextResolver<List<byte[]>> {

        public List<byte[]> getContext(Class<?> type) {
            return Collections.emptyList();
        }
    }

    private ProvidersRegistry createProvidersRegistryImpl() {
        ProvidersRegistry providers = new ProvidersRegistry(new OFFactoryRegistry(),
            new ApplicationValidator());;
        return providers;
    }
    
    public void testContextResolvers() {
        ProvidersRegistry providers = createProvidersRegistryImpl();
        assertTrue(providers.addProvider(new AtomContextResolver()));
        assertTrue(providers.addProvider(new StringContextResolver()));
        assertTrue(providers.addProvider(new IntegerContextResolver()));
        assertTrue(providers.addProvider(new ByteContextResolver()));
        assertTrue(providers.addProvider(new ListContextResolver()));
        assertFalse(providers.addProvider(new NotAProvider()));

        /*
         * String and text/pain, should invoke StringContextResolver
         */
        assertEquals(STRING,
            providers.getContextResolver(String.class, MediaType.TEXT_PLAIN_TYPE, null).getContext(null));

        /*
         * byte[] and text/plain, should invoke ByteContextResolver
         */
        assertEquals(BYTE,
            providers.getContextResolver(byte[].class, MediaType.TEXT_PLAIN_TYPE, null).getContext(null));

        /*
         * There is no context resolver that handlers Integer and /
         */
        assertEquals(null,
            providers.getContextResolver(Integer.class, MediaType.WILDCARD_TYPE, null));

        /*
         * AtomContextResolver comes before StringContextResolver, therefore it
         * should be invoked after
         */
        assertEquals(STRING,
            providers.getContextResolver(String.class, MediaType.WILDCARD_TYPE, null).getContext(null));

        /*
         * AtomContextResolver returnes null, if the parameter is not null,
         * therefore StringContextResolver should be invoked
         */
        assertEquals(STRING,
            providers.getContextResolver(String.class, MediaType.WILDCARD_TYPE, null).getContext(
                String.class));

        /*
         * test ContextResolver with collections
         */
        assertEquals(Collections.emptyList(), providers.getContextResolver(List.class,
            MediaType.WILDCARD_TYPE, null).getContext(null));
    }

}