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
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-558 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.24 at 01:47:17 PM IDT 
//

package org.apache.wink.common.internal.model.admin;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;method&quot; maxOccurs=&quot;unbounded&quot;&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name=&quot;name&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *                   &lt;element ref=&quot;{http://apache.org/wink/common/internal/model/admin}accept-media-types&quot; minOccurs=&quot;0&quot;/&gt;
 *                   &lt;element ref=&quot;{http://apache.org/wink/common/internal/model/admin}produced-media-types&quot; minOccurs=&quot;0&quot;/&gt;
 *                   &lt;element ref=&quot;{http://apache.org/wink/common/internal/model/admin}query-parameters&quot; minOccurs=&quot;0&quot;/&gt;
 *                   &lt;element ref=&quot;{http://apache.org/wink/common/internal/model/admin}matrix-parameters&quot; minOccurs=&quot;0&quot;/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"method"})
@XmlRootElement(name = "methods")
public class Methods {

    @XmlElement(required = true)
    protected List<Methods.Method> method;

    /**
     * Gets the value of the method property.
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the method property.
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getMethod().add(newItem);
     * </pre>
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Methods.Method }
     */
    public List<Methods.Method> getMethod() {
        if (method == null) {
            method = new ArrayList<Methods.Method>();
        }
        return this.method;
    }

    /**
     * <p>
     * Java class for anonymous complex type.
     * <p>
     * The following schema fragment specifies the expected content contained
     * within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
     *       &lt;sequence&gt;
     *         &lt;element name=&quot;name&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
     *         &lt;element ref=&quot;{http://apache.org/wink/common/internal/model/admin}accept-media-types&quot; minOccurs=&quot;0&quot;/&gt;
     *         &lt;element ref=&quot;{http://apache.org/wink/common/internal/model/admin}produced-media-types&quot; minOccurs=&quot;0&quot;/&gt;
     *         &lt;element ref=&quot;{http://apache.org/wink/common/internal/model/admin}query-parameters&quot; minOccurs=&quot;0&quot;/&gt;
     *         &lt;element ref=&quot;{http://apache.org/wink/common/internal/model/admin}matrix-parameters&quot; minOccurs=&quot;0&quot;/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {"name", "acceptMediaTypes", "producedMediaTypes",
        "queryParameters", "matrixParameters"})
    public static class Method {

        protected String             name;
        @XmlElement(name = "accept-media-types")
        protected AcceptMediaTypes   acceptMediaTypes;
        @XmlElement(name = "produced-media-types")
        protected ProducedMediaTypes producedMediaTypes;
        @XmlElement(name = "query-parameters")
        protected QueryParameters    queryParameters;
        @XmlElement(name = "matrix-parameters")
        protected MatrixParameters   matrixParameters;

        /**
         * Gets the value of the name property.
         * 
         * @return possible object is {@link String }
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the value of the name property.
         * 
         * @param value allowed object is {@link String }
         */
        public void setName(String value) {
            this.name = value;
        }

        /**
         * Gets the value of the acceptMediaTypes property.
         * 
         * @return possible object is {@link AcceptMediaTypes }
         */
        public AcceptMediaTypes getAcceptMediaTypes() {
            return acceptMediaTypes;
        }

        /**
         * Sets the value of the acceptMediaTypes property.
         * 
         * @param value allowed object is {@link AcceptMediaTypes }
         */
        public void setAcceptMediaTypes(AcceptMediaTypes value) {
            this.acceptMediaTypes = value;
        }

        /**
         * Gets the value of the producedMediaTypes property.
         * 
         * @return possible object is {@link ProducedMediaTypes }
         */
        public ProducedMediaTypes getProducedMediaTypes() {
            return producedMediaTypes;
        }

        /**
         * Sets the value of the producedMediaTypes property.
         * 
         * @param value allowed object is {@link ProducedMediaTypes }
         */
        public void setProducedMediaTypes(ProducedMediaTypes value) {
            this.producedMediaTypes = value;
        }

        /**
         * Gets the value of the queryParameters property.
         * 
         * @return possible object is {@link QueryParameters }
         */
        public QueryParameters getQueryParameters() {
            return queryParameters;
        }

        /**
         * Sets the value of the queryParameters property.
         * 
         * @param value allowed object is {@link QueryParameters }
         */
        public void setQueryParameters(QueryParameters value) {
            this.queryParameters = value;
        }

        /**
         * Gets the value of the matrixParameters property.
         * 
         * @return possible object is {@link MatrixParameters }
         */
        public MatrixParameters getMatrixParameters() {
            return matrixParameters;
        }

        /**
         * Sets the value of the matrixParameters property.
         * 
         * @param value allowed object is {@link MatrixParameters }
         */
        public void setMatrixParameters(MatrixParameters value) {
            this.matrixParameters = value;
        }

    }

}
