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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The <code>link</code> XML element per the WebDAV specification [RFC 4918]
 * 
 * <pre>
 *    Name:       link
 *    Namespace:  DAV:
 *    Purpose:    Identifies the property as a link and contains the source
 *    and destination of that link.
 *    Description: The link XML element is used to provide the sources and
 *    destinations of a link.  The name of the property containing the link
 *    XML element provides the type of the link.  Link is a multi-valued
 *    element, so multiple links may be used together to indicate multiple
 *    links with the same type.  The values in the href XML elements inside
 *    the src and dst XML elements of the link XML element MUST NOT be
 *    rejected if they point to resources which do not exist.
 * 
 *    &lt;!ELEMENT link (src+, dst+) &gt;
 * 
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "src",
    "dst"
})
@XmlRootElement(name = "link")
public class Link {

    @XmlElement(required = true)
    protected List<String> src;
    @XmlElement(required = true)
    protected List<String> dst;

    /**
     * Gets the value of the src property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the src property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSrc().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getSrc() {
        if (src == null) {
            src = new ArrayList<String>();
        }
        return this.src;
    }

    /**
     * Gets the value of the dst property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dst property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDst().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getDst() {
        if (dst == null) {
            dst = new ArrayList<String>();
        }
        return this.dst;
    }

}