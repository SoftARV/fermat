//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.26 at 02:09:24 PM VET 
//


package com.bitdubai.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice minOccurs="0">
 *         &lt;element ref="{}addons"/>
 *         &lt;element ref="{}androids"/>
 *         &lt;element ref="{}libraries"/>
 *         &lt;element ref="{}plugins"/>
 *       &lt;/choice>
 *       &lt;attribute name="language" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="super_layer" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "addons",
    "androids",
    "libraries",
    "plugins"
})
@XmlRootElement(name = "layer")
public class Layer {

    protected Addons addons;
    protected Androids androids;
    protected Libraries libraries;
    protected Plugins plugins;
    @XmlAttribute(name = "language")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String language;
    @XmlAttribute(name = "name")
    @XmlSchemaType(name = "anySimpleType")
    protected String name;
    @XmlAttribute(name = "super_layer")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String superLayer;

    /**
     * Gets the value of the addons property.
     * 
     * @return
     *     possible object is
     *     {@link Addons }
     *     
     */
    public Addons getAddons() {
        return addons;
    }

    /**
     * Sets the value of the addons property.
     * 
     * @param value
     *     allowed object is
     *     {@link Addons }
     *     
     */
    public void setAddons(Addons value) {
        this.addons = value;
    }

    /**
     * Gets the value of the androids property.
     * 
     * @return
     *     possible object is
     *     {@link Androids }
     *     
     */
    public Androids getAndroids() {
        return androids;
    }

    /**
     * Sets the value of the androids property.
     * 
     * @param value
     *     allowed object is
     *     {@link Androids }
     *     
     */
    public void setAndroids(Androids value) {
        this.androids = value;
    }

    /**
     * Gets the value of the libraries property.
     * 
     * @return
     *     possible object is
     *     {@link Libraries }
     *     
     */
    public Libraries getLibraries() {
        return libraries;
    }

    /**
     * Sets the value of the libraries property.
     * 
     * @param value
     *     allowed object is
     *     {@link Libraries }
     *     
     */
    public void setLibraries(Libraries value) {
        this.libraries = value;
    }

    /**
     * Gets the value of the plugins property.
     * 
     * @return
     *     possible object is
     *     {@link Plugins }
     *     
     */
    public Plugins getPlugins() {
        return plugins;
    }

    /**
     * Sets the value of the plugins property.
     * 
     * @param value
     *     allowed object is
     *     {@link Plugins }
     *     
     */
    public void setPlugins(Plugins value) {
        this.plugins = value;
    }

    /**
     * Gets the value of the language property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the value of the language property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLanguage(String value) {
        this.language = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the superLayer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSuperLayer() {
        return superLayer;
    }

    /**
     * Sets the value of the superLayer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSuperLayer(String value) {
        this.superLayer = value;
    }

}