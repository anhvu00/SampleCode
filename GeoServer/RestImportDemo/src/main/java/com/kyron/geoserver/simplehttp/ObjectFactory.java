//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.04.30 at 07:05:23 PM BOT 
//

package com.kyron.geoserver.simplehttp;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the generated package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

	/**
	 * Create a new ObjectFactory that can be used to create new instances of schema
	 * derived classes for package: generated
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link GeoServerLayer }
	 * 
	 */
	public GeoServerLayer createGeoServerLayer() {
		return new GeoServerLayer();
	}

	/**
	 * Create an instance of {@link GeoServerLayer.ParameterFilters }
	 * 
	 */
	public GeoServerLayer.ParameterFilters createGeoServerLayerParameterFilters() {
		return new GeoServerLayer.ParameterFilters();
	}

	/**
	 * Create an instance of {@link GeoServerLayer.GridSubsets }
	 * 
	 */
	public GeoServerLayer.GridSubsets createGeoServerLayerGridSubsets() {
		return new GeoServerLayer.GridSubsets();
	}

	/**
	 * Create an instance of {@link GeoServerLayer.MimeFormats }
	 * 
	 */
	public GeoServerLayer.MimeFormats createGeoServerLayerMimeFormats() {
		return new GeoServerLayer.MimeFormats();
	}

	/**
	 * Create an instance of {@link GeoServerLayer.MetaWidthHeight }
	 * 
	 */
	public GeoServerLayer.MetaWidthHeight createGeoServerLayerMetaWidthHeight() {
		return new GeoServerLayer.MetaWidthHeight();
	}

	/**
	 * Create an instance of
	 * {@link GeoServerLayer.ParameterFilters.StyleParameterFilter }
	 * 
	 */
	public GeoServerLayer.ParameterFilters.StyleParameterFilter createGeoServerLayerParameterFiltersStyleParameterFilter() {
		return new GeoServerLayer.ParameterFilters.StyleParameterFilter();
	}

	/**
	 * Create an instance of {@link GeoServerLayer.GridSubsets.GridSubset }
	 * 
	 */
	public GeoServerLayer.GridSubsets.GridSubset createGeoServerLayerGridSubsetsGridSubset() {
		return new GeoServerLayer.GridSubsets.GridSubset();
	}

}