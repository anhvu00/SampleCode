package com.kyron.geoserver;

import it.geosolutions.geoserver.rest.encoder.GSLayerEncoder;
import it.geosolutions.geoserver.rest.encoder.feature.GSFeatureTypeEncoder;

/**
 * This class provides:
 * - Parameters to set up a layer before CRUD operations (i.e. addVectorLayer()) 
 * - Set up GSFeatureTypeEncoder and GSLayerEncoder required by the geoserver-manager library.
 * @author anh
 * 
 * TODO:
 * - Expand supporting to more styles, other than "point"
 *
 */
public class LayerParams {
	private String workspace;
	private String store;
	private String layerName;
	private String crs;  // Coordinate Reference System
	private String defaultStyle;  // point, grass, line, etc. already defined by GeoServer
	private GSFeatureTypeEncoder fte;
	private GSLayerEncoder layerEncoder;
	
	// constructor
	public LayerParams(String workspace, String store, String layer, String crs, String style) {
		this.workspace = workspace;
		this.store = store;
		this.layerName = layer;
		this.crs = crs;
		this.defaultStyle = style;
		
		// setup supporting members
        fte = new GSFeatureTypeEncoder();
        fte.setName(layer);
        fte.setNativeName(layer);
        fte.setTitle(layer+"_TITLE");
        fte.setNativeCRS(crs); // ex: "EPSG:26713"
        fte.setDescription("demo desc");
        fte.setEnabled(true);
        
        layerEncoder = new GSLayerEncoder();
        layerEncoder.setEnabled(true);
        layerEncoder.setQueryable(true);
        layerEncoder.setDefaultStyle(style); // ex: "point"	
		
	}

	public String getWorkspace() {
		return workspace;
	}

	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getLayerName() {
		return layerName;
	}

	public void setLayerName(String layerName) {
		this.layerName = layerName;
	}

	public String getCrs() {
		return crs;
	}

	public void setCrs(String crs) {
		this.crs = crs;
	}

	public String getDefaultStyle() {
		return defaultStyle;
	}

	public void setDefaultStyle(String defaultStyle) {
		this.defaultStyle = defaultStyle;
	}

	public GSFeatureTypeEncoder getFte() {
		return fte;
	}

	public GSLayerEncoder getLayerEncoder() {
		return layerEncoder;
	}

}
