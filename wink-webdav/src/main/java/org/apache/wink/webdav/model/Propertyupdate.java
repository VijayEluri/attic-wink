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
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1.1-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.12.04 at 02:20:17 PM IST 
//

package org.apache.wink.webdav.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;




/**
 * The <code>propertyupdate</code> XML element per the WebDAV specification [RFC 4918]
 * 
 * <pre>
 *    Name:       propertyupdate
 *    Namespace:  DAV:
 *    Purpose:    Contains a request to alter the properties on a
 *    resource.
 *    Description: This XML element is a container for the information
 *    required to modify the properties on the resource.  This XML element
 *    is multi-valued.
 * 
 *    &lt;!ELEMENT propertyupdate (remove | set)+ &gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"removeOrSet"})
@XmlRootElement(name = "propertyupdate")
public class Propertyupdate {

    @XmlElements({@XmlElement(name = "set", type = Set.class), @XmlElement(name = "remove", type = Remove.class)})
    protected List<Object> removeOrSet;

    /**
     * Gets the value of the removeOrSet property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is
     * why there is not a <CODE>set</CODE> method for the removeOrSet property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getRemoveOrSet().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Set } {@link Remove }
     * 
     * 
     */
    public List<Object> getRemoveOrSet() {
        if (removeOrSet == null) {
            removeOrSet = new ArrayList<Object>();
        }
        return this.removeOrSet;
    }
    
    /**
     * Get a list of Prop objects to be set
     * @return a list of Prop objects to set
     */
    public List<Prop> getPropsToSet() {
        List<Prop> list = new ArrayList<Prop>();
        for (Object object : getRemoveOrSet()) {
            if (object instanceof Set) {
                list.add(((Set)object).getProp());
            }
        }
        return list;
    }

    /**
     * Get a list of Prop objects to remove
     * @return a list of Prop objects to remove
     */
    public List<Prop> getPropsToRemove() {
        List<Prop> list = new ArrayList<Prop>();
        for (Object object : getRemoveOrSet()) {
            if (object instanceof Remove) {
                list.add(((Remove)object).getProp());
            }
        }
        return list;
    }
    
    /**
     * Unmarshal a Propertyupdate object from the provided input stream
     * @param is the input stream
     * @return an instance of a Propertyupdate object
     * @throws IOException
     */
    public static Propertyupdate unmarshal(InputStream is) throws IOException {
        return unmarshal(new InputStreamReader(is));
    }

    /**
     * Marshal a Propertyupdate object to the provided output stream
     * @param instance the Propertyupdate instance to marshal
     * @param outputStreamWriter the output stream
     * @throws IOException
     */
    public static void marshal(Propertyupdate instance, OutputStream os) throws IOException {
        marshal(instance, new OutputStreamWriter(os));
    }

    /**
     * Unmarshal a Propertyupdate object from the provided reader
     * @param reader the input reader
     * @return an instance of a Propertyupdate object
     * @throws IOException
     */
    public static Propertyupdate unmarshal(Reader reader) throws IOException {
        Unmarshaller unmarshaller = WebDAVModelHelper.createUnmarshaller();
        Propertyupdate instance = WebDAVModelHelper.unmarshal(unmarshaller, reader, Propertyupdate.class, "propertyupdate");
        return instance;
    }
    
    /**
     * Marshal a Propfind object to the provided writer
     * @param instance the Profind instance to marshal
     * @param writer the output writer
     * @throws IOException
     */
    public static void marshal(Propertyupdate instance, Writer writer) throws IOException {
        Marshaller marshaller = WebDAVModelHelper.createMarshaller();
        WebDAVModelHelper.marshal(marshaller, instance, writer, "propertyupdate");
    }

}