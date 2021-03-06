/*
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
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.wink.itest.standard;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.wink.test.integration.ServerEnvironmentInfo;

public class JAXRSBytesArrayTest extends TestCase {

    public String getBaseURI() {
        if (ServerEnvironmentInfo.isRestFilterUsed()) {
            return ServerEnvironmentInfo.getBaseURI();
        }
        return ServerEnvironmentInfo.getBaseURI() + "/standard";
    }

    /**
     * Tests posting a byte array.
     * 
     * @throws HttpException
     * @throws IOException
     */
    public void testPostByteArray() throws HttpException, IOException {
        HttpClient client = new HttpClient();

        PostMethod postMethod = new PostMethod(getBaseURI() + "/providers/standard/bytesarray");
        byte[] barr = new byte[1000];
        Random r = new Random();
        r.nextBytes(barr);
        postMethod.setRequestEntity(new InputStreamRequestEntity(new ByteArrayInputStream(barr),
                                                                 "text/plain"));
        postMethod.addRequestHeader("Accept", "text/plain");
        try {
            client.executeMethod(postMethod);

            assertEquals(200, postMethod.getStatusCode());
            InputStream is = postMethod.getResponseBodyAsStream();

            byte[] receivedBArr = new byte[1000];
            DataInputStream dis = new DataInputStream(is);
            dis.readFully(receivedBArr);

            int checkEOF = dis.read();
            assertEquals(-1, checkEOF);
            for (int c = 0; c < barr.length; ++c) {
                assertEquals(barr[c], receivedBArr[c]);
            }
            assertEquals("text/plain", postMethod.getResponseHeader("Content-Type").getValue());
            assertEquals(barr.length, Integer.valueOf(postMethod
                .getResponseHeader("Content-Length").getValue()).intValue());
        } finally {
            postMethod.releaseConnection();
        }
    }

    /**
     * Tests putting and then getting a byte array.
     * 
     * @throws HttpException
     * @throws IOException
     */
    public void testPutByteArray() throws HttpException, IOException {
        HttpClient client = new HttpClient();

        PutMethod putMethod = new PutMethod(getBaseURI() + "/providers/standard/bytesarray");
        byte[] barr = new byte[1000];
        Random r = new Random();
        r.nextBytes(barr);
        putMethod.setRequestEntity(new InputStreamRequestEntity(new ByteArrayInputStream(barr),
                                                                "bytes/array"));
        try {
            client.executeMethod(putMethod);
            assertEquals(204, putMethod.getStatusCode());
        } finally {
            putMethod.releaseConnection();
        }

        GetMethod getMethod = new GetMethod(getBaseURI() + "/providers/standard/bytesarray");
        try {
            client.executeMethod(getMethod);
            assertEquals(200, getMethod.getStatusCode());
            InputStream is = getMethod.getResponseBodyAsStream();

            byte[] receivedBArr = new byte[1000];
            DataInputStream dis = new DataInputStream(is);
            dis.readFully(receivedBArr);

            int checkEOF = dis.read();
            assertEquals(-1, checkEOF);
            for (int c = 0; c < barr.length; ++c) {
                assertEquals(barr[c], receivedBArr[c]);
            }
            String contentType =
                (getMethod.getResponseHeader("Content-Type") == null) ? null : getMethod
                    .getResponseHeader("Content-Type").getValue();
            assertNotNull(contentType, contentType);
            assertEquals(barr.length, Integer.valueOf(getMethod.getResponseHeader("Content-Length")
                .getValue()).intValue());
        } finally {
            getMethod.releaseConnection();
        }
    }

    /**
     * Tests receiving an empty byte array.
     * 
     * @throws HttpException
     * @throws IOException
     */
    public void testWithRequestAcceptHeaderWillReturnRequestedContentType() throws HttpException,
        IOException {
        HttpClient client = new HttpClient();

        PutMethod putMethod = new PutMethod(getBaseURI() + "/providers/standard/bytesarray");
        byte[] barr = new byte[1000];
        Random r = new Random();
        r.nextBytes(barr);
        putMethod.setRequestEntity(new InputStreamRequestEntity(new ByteArrayInputStream(barr),
                                                                "any/type"));
        try {
            client.executeMethod(putMethod);
            assertEquals(204, putMethod.getStatusCode());
        } finally {
            putMethod.releaseConnection();
        }

        GetMethod getMethod = new GetMethod(getBaseURI() + "/providers/standard/bytesarray");
        getMethod.addRequestHeader("Accept", "mytype/subtype");
        try {
            client.executeMethod(getMethod);
            assertEquals(200, getMethod.getStatusCode());
            InputStream is = getMethod.getResponseBodyAsStream();

            byte[] receivedBArr = new byte[1000];
            DataInputStream dis = new DataInputStream(is);
            dis.readFully(receivedBArr);

            int checkEOF = dis.read();
            assertEquals(-1, checkEOF);
            for (int c = 0; c < barr.length; ++c) {
                assertEquals(barr[c], receivedBArr[c]);
            }
            assertEquals("mytype/subtype", getMethod.getResponseHeader("Content-Type").getValue());
            assertEquals(barr.length, Integer.valueOf(getMethod.getResponseHeader("Content-Length")
                .getValue()).intValue());
        } finally {
            getMethod.releaseConnection();
        }
    }

    /**
     * Tests posting an request parameter to a byte[] entity parameter.
     * 
     * @throws HttpException
     * @throws IOException
     */
    public void testSendingNoRequestEntityByteArray() throws HttpException, IOException {
        HttpClient client = new HttpClient();

        PostMethod postMethod =
            new PostMethod(getBaseURI() + "/providers/standard/bytesarray/empty");
        try {
            client.executeMethod(postMethod);
            assertEquals(200, postMethod.getStatusCode());
            assertEquals("expected", postMethod.getResponseBodyAsString());
        } finally {
            postMethod.releaseConnection();
        }
    }
}
