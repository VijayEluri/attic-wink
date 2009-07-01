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
// Generated on: 2008.05.27 at 11:24:25 AM IDT 
//


package org.apache.wink.common.model.atom;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.wink.common.RestConstants;
import org.apache.wink.common.RestException;
import org.apache.wink.common.internal.model.NamespacePrefixMapperProvider;
import org.apache.wink.common.internal.utils.JAXBUtils;
import org.apache.wink.common.model.JAXBNamespacePrefixMapper;
import org.apache.wink.common.model.synd.SyndCategory;
import org.apache.wink.common.model.synd.SyndContent;
import org.apache.wink.common.model.synd.SyndEntry;
import org.apache.wink.common.model.synd.SyndLink;
import org.apache.wink.common.model.synd.SyndPerson;
import org.apache.wink.common.model.synd.SyndText;
import org.w3c.dom.Element;


/**
 * The "atom:entry" element Per RFC4287
 * 
 * <pre>
 * The &quot;atom:entry&quot; element represents an individual entry, acting as a
 * container for metadata and data associated with the entry.  This
 * element can appear as a child of the atom:feed element, or it can
 * appear as the document (i.e., top-level) element of a stand-alone
 * Atom Entry Document.
 * 
 * atomEntry =
 *    element atom:entry {
 *       atomCommonAttributes,
 *       (atomAuthor*
 *        &amp; atomCategory*
 *        &amp; atomContent?
 *        &amp; atomContributor*
 *        &amp; atomId
 *        &amp; atomLink*
 *        &amp; atomPublished?
 *        &amp; atomRights?
 *        &amp; atomSource?
 *        &amp; atomSummary?
 *        &amp; atomTitle
 *        &amp; atomUpdated
 *        &amp; extensionElement*)
 *    }
 * 
 * This specification assigns no significance to the order of appearance
 * of the child elements of atom:entry.
 * 
 * The following child elements are defined by this specification (note
 * that it requires the presence of some of these elements):
 * 
 * o  atom:entry elements MUST contain one or more atom:author elements,
 *    unless the atom:entry contains an atom:source element that
 *    contains an atom:author element or, in an Atom Feed Document, the
 *    atom:feed element contains an atom:author element itself.
 * o  atom:entry elements MAY contain any number of atom:category
 *    elements.
 * o  atom:entry elements MUST NOT contain more than one atom:content
 *    element.
 * o  atom:entry elements MAY contain any number of atom:contributor
 *    elements.
 * o  atom:entry elements MUST contain exactly one atom:id element.
 * o  atom:entry elements that contain no child atom:content element
 *    MUST contain at least one atom:link element with a rel attribute
 *    value of &quot;alternate&quot;.
 * o  atom:entry elements MUST NOT contain more than one atom:link
 *    element with a rel attribute value of &quot;alternate&quot; that has the
 *    same combination of type and hreflang attribute values.
 * o  atom:entry elements MAY contain additional atom:link elements
 *    beyond those described above.
 * o  atom:entry elements MUST NOT contain more than one atom:published
 *    element.
 * o  atom:entry elements MUST NOT contain more than one atom:rights
 *    element.
 * o  atom:entry elements MUST NOT contain more than one atom:source
 *    element.
 * o  atom:entry elements MUST contain an atom:summary element in either
 *    of the following cases:
 *    *  the atom:entry contains an atom:content that has a &quot;src&quot;
 *       attribute (and is thus empty).
 *    *  the atom:entry contains content that is encoded in Base64;
 *       i.e., the &quot;type&quot; attribute of atom:content is a MIME media type
 *       [MIMEREG], but is not an XML media type [RFC3023], does not
 *       begin with &quot;text/&quot;, and does not end with &quot;/xml&quot; or &quot;+xml&quot;.
 * o  atom:entry elements MUST NOT contain more than one atom:summary
 *    element.
 * o  atom:entry elements MUST contain exactly one atom:title element.
 * o  atom:entry elements MUST contain exactly one atom:updated element.
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.UNDEFINED)
@XmlType(name = "atomEntry", propOrder = {
    "id",
    "updated",
    "title",
    "summary",
    "published",
    "link",
    "author",
    "contributor",
    "category",
    "any",
    "content"
})
public class AtomEntry
    extends AtomCommonAttributes
    implements NamespacePrefixMapperProvider
{

    private static final String MATCH_ANY_PATTERN = ".*";
    
    @XmlElement(required = true)
    protected String id;
    @XmlElement(required = true)
    protected XMLGregorianCalendar updated;
    @XmlElement(required = true)
    protected AtomText title;
    protected AtomText summary;
    protected XMLGregorianCalendar published;
    protected List<AtomLink> link;
    protected List<AtomPerson> author;
    protected List<AtomPerson> contributor;
    protected List<AtomCategory> category;
    @XmlAnyElement
    protected List<Element> any;
    protected AtomContent content;

    
    @XmlTransient
    private static final JAXBContext atomContext;

    static {
        try {
            atomContext = JAXBContext.newInstance(AtomEntry.class.getPackage().getName());
        } catch (JAXBException e) {
            throw new RestException("Failed to create JAXBContext for AtomEntry", e);
        }
    }

    public static Marshaller getMarshaller() {
        return JAXBUtils.createMarshaller(atomContext);
    }
    
    public static Unmarshaller getUnmarshaller() {
        return JAXBUtils.createUnmarshaller(atomContext);
    }

    /**
     * Convenience method for creating an AtomEntry from xml
     * @param reader input reader
     * @return AtomEntry instance from the input
     */
    public static AtomEntry unmarshal(Reader reader) {
        try {
            return (AtomEntry)AtomJAXBUtils.unmarshal(AtomEntry.getUnmarshaller(), reader);
        } catch (IOException e) {
            throw new RestException(e);
        }
    }

    public static void marshal(AtomEntry entry, Writer writer) {
        try {
            JAXBElement<AtomEntry> entryElement = new ObjectFactory().createEntry(entry);
            Marshaller marshaller = AtomEntry.getMarshaller();
            AtomJAXBUtils.marshal(marshaller, entryElement, null, writer);
            writer.flush();
        } catch (IOException e) {
            throw new WebApplicationException(e);
        }
    }
    
    public JAXBNamespacePrefixMapper getNamespacePrefixMapper() {
        JAXBNamespacePrefixMapper mapper = new JAXBNamespacePrefixMapper(RestConstants.NAMESPACE_ATOM);
        mapper.omitNamespace(RestConstants.NAMESPACE_OPENSEARCH);
        return mapper;
    }
    
    public AtomEntry() {}
    
    public AtomEntry(SyndEntry value) {
        super(value);
        if (value == null) {
            return;
        }
        setId(value.getId());
        setPublished(value.getPublished());
        setSummary(value.getSummary() != null ? new AtomText(value.getSummary()) : null);
        setTitle(value.getTitle() != null ? new AtomText(value.getTitle()) : null);
        setUpdated(value.getUpdated());
        setAuthors(value.getAuthors());
        setCategories(value.getCategories());
        setLinks(value.getLinks());
        setContent(value.getContent() != null ? new AtomContent(value.getContent()) : null);
    }

    public SyndEntry toSynd(SyndEntry value) {
        if (value == null) {
            return value;
        }
        super.toSynd(value);
        value.setId(getId());
        value.setPublished(getPublished());
        value.setSummary(getSummary() != null ? getSummary().toSynd(new SyndText()) : null);
        value.setTitle(getTitle() != null ? getTitle().toSynd(new SyndText()) : null);
        value.setUpdated(getUpdated());
        value.setContent(getContent() != null ? getContent().toSynd(new SyndContent()) : null);
        value.getAuthors().addAll(getAuthorsAsSynd());
        value.getCategories().addAll(getCategoriesAsSynd());
        value.getLinks().addAll(getLinksAsSynd());
        return value;
    }
    
    private List<SyndPerson> getAuthorsAsSynd() {
        List<SyndPerson> authors = new ArrayList<SyndPerson>();
        for (AtomPerson value : getAuthors()) {
            if (value != null) {
                authors.add(value.toSynd(new SyndPerson()));
            }
        }
        return authors;
    }

    private List<SyndCategory> getCategoriesAsSynd() {
        List<SyndCategory> authors = new ArrayList<SyndCategory>();
        for (AtomCategory value : getCategories()) {
            if (value != null) {
                authors.add(value.toSynd(new SyndCategory()));
            }
        }
        return authors;
    }

    private List<SyndLink> getLinksAsSynd() {
        List<SyndLink> authors = new ArrayList<SyndLink>();
        for (AtomLink value : getLinks()) {
            if (value != null) {
                authors.add(value.toSynd(new SyndLink()));
            }
        }
        return authors;
    }

    private void setAuthors(List<SyndPerson> values) {
        author = new ArrayList<AtomPerson>();
        for (SyndPerson value : values) {
            if (value != null) {
                this.author.add(new AtomPerson(value));
            }
        }
    }
    
    private void setCategories(List<SyndCategory> values) {
        category = new ArrayList<AtomCategory>();
        for (SyndCategory value : values) {
            if (value != null) {
                this.category.add(new AtomCategory(value));
            }
        }
    }

    private void setLinks(List<SyndLink> values) {
        link = new ArrayList<AtomLink>();
        for (SyndLink value : values) {
            if (value != null) {
                this.link.add(new AtomLink(value));
            }
        }
    }

    /**
     * Gets the value of id.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of id.
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of updated as a long value
     * @return the updated value, or -1 if it is not set
     */
    public long getUpdatedAsTime() {
        return AtomJAXBUtils.xmlGregorianCalendarToTime(updated);
    }

    /**
     * Gets the value of updated as a Date object
     */
    public Date getUpdated() {
        long updatedAsTime = getUpdatedAsTime();
        if (updatedAsTime == -1) {
            return null;
        }
        return new Date(updatedAsTime);
    }

    /**
     * Sets the value of updated.
     */
    public void setUpdated(XMLGregorianCalendar value) {
        this.updated = value;
    }
    
    /**
     * Sets the value of updated.
     */
    public void setUpdated(long value) {
        setUpdated(AtomJAXBUtils.timeToXmlGregorianCalendar(value));
    }

    /**
     * Sets the value of updated.
     */
    public void setUpdated(Date value) {
        if (value == null) {
            this.updated = null;
            return;
        }
        setUpdated(value.getTime());
    }

    /**
     * Gets the value of title.
     */
    public AtomText getTitle() {
        return title;
    }

    /**
     * Sets the value of title.
     */
    public void setTitle(AtomText value) {
        this.title = value;
    }

    /**
     * Gets the value of summary.
     */
    public AtomText getSummary() {
        return summary;
    }

    /**
     * Sets the value of summary.
     */
    public void setSummary(AtomText value) {
        this.summary = value;
    }

    /**
     * Gets the value of published as a long value
     * @return the published value, or -1 if it is not set
     */
    public long getPublishedAsTime() {
        return AtomJAXBUtils.xmlGregorianCalendarToTime(published);
    }

    /**
     * Gets the value of published as a Date object
     */
    public Date getPublished() {
        long publishedAsTime = getPublishedAsTime();
        if (publishedAsTime == -1) {
            return null;
        }
        return new Date(publishedAsTime);
    }

    /**
     * Sets the value of published.
     */
    public void setPublished(XMLGregorianCalendar value) {
        this.published = value;
    }
    
    /**
     * Sets the value of published.
     */
    public void setPublished(long value) {
        setPublished(AtomJAXBUtils.timeToXmlGregorianCalendar(value));
    }

    /**
     * Sets the value of published.
     */
    public void setPublished(Date value) {
        if (value == null) {
            this.published = null;
            return;
        }
        setPublished(value.getTime());
    }

    /**
     * Gets the value of link.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the link.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLink().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AtomLink }
     * 
     * 
     */
    public List<AtomLink> getLinks() {
        if (link == null) {
            link = new ArrayList<AtomLink>();
        }
        return this.link;
    }
    
    /**
     * Get the list of links that match the relation and type regex patterns
     * @param relationPattern the regex relation pattern to match
     * @param typePattern the regex type pattern to match
     * @return the list of links matching the given regex patterns
     */
    public List<AtomLink> getLinks(String relationPattern, String typePattern) {
        if (relationPattern == null || typePattern == null) {
            throw new NullPointerException("pattern");
        }
        
        List<AtomLink> matchingLinks = new ArrayList<AtomLink>();
        List<AtomLink> links = getLinks();
        for (AtomLink link : links) {
            String rel = link.getRel();
            String type = link.getType();
            if (rel == null) {
                rel = "";
            }
            if (type == null) {
                type = "";
            }
            if (rel.matches(relationPattern) && type.matches(typePattern)) {
                matchingLinks.add(link);
            }
        }
        return matchingLinks;
    }
    
    /**
     * Get the list of links that match the relation regex pattern
     * @param relationPattern the regex relation pattern to match
     * @return the link matching the given regex pattern, or <code>null</code>
     */
    public List<AtomLink> getLinksByType(String typePattern) {
        return getLinks(MATCH_ANY_PATTERN, typePattern);
    }
    
    /**
     * Get the list of links that match the type regex pattern
     * @param typePattern the regex type pattern to match
     * @return the link matching the given regex pattern, or <code>null</code>
     */
    public List<AtomLink> getLinksByRelation(String relationPattern) {
        return getLinks(relationPattern, MATCH_ANY_PATTERN);
    }
    
    /**
     * Gets the value of author.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the author.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAuthor().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AtomPerson }
     * 
     * 
     */
    public List<AtomPerson> getAuthors() {
        if (author == null) {
            author = new ArrayList<AtomPerson>();
        }
        return this.author;
    }

    /**
     * Gets the value of contributor.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the contributor.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContributor().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AtomPerson }
     * 
     * 
     */
    public List<AtomPerson> getContributors() {
        if (contributor == null) {
            contributor = new ArrayList<AtomPerson>();
        }
        return this.contributor;
    }

    /**
     * Gets the value of category.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the category.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCategory().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AtomCategory }
     * 
     * 
     */
    public List<AtomCategory> getCategories() {
        if (category == null) {
            category = new ArrayList<AtomCategory>();
        }
        return this.category;
    }

    /**
     * Gets extension elements
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the any.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAny().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Element }
     * 
     * 
     */
    public List<Element> getAny() {
        if (any == null) {
            any = new ArrayList<Element>();
        }
        return this.any;
    }

    /**
     * Gets the content.
     */
    public AtomContent getContent() {
        return content;
    }

    /**
     * Sets the content.
     */
    public void setContent(AtomContent value) {
        this.content = value;
    }

}