package com.kyron.geoserver;

import java.io.File;
import java.io.IOException;

/**
 * POJO holding specific data for http request parameters
 * TODO:
 * - Add more params
 * - Make more generic
 * @author anh
 *
 */
public class GeoServerParams {
	private String workspace;
	private String store;
	private String layerName;
	private String image;
	private File imageFile;
	private File styleFile;
	private String styleName;
	private String coordSys;  // Coordinate Reference System

	// constructor
	// workspace = GeoServer Workspace name
	// store = GeoServer Store name, usually the same as the image file name
	// fullPathImageName = Full path image (i.e. "C:/data/myRasterImage.tiff")
	public GeoServerParams(String workspace, String store, String fullPathImageName) {
		this.workspace = workspace;
		this.store = store;
		this.image = fullPathImageName;
		this.imageFile = new File(fullPathImageName);
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
		// also change the imageFile
		this.imageFile = new File(image);
	}

	public File getImageFile() {
		return imageFile;
	}

	public void setImageFile(File imageFile) throws IOException {
		this.imageFile = imageFile;
		// also change the full path image name
		this.image = imageFile.getCanonicalPath();
	}

	public File getStyleFile() {
		return styleFile;
	}

	public void setStyleFile(File styleFile) {
		this.styleFile = styleFile;
	}

	public String getStyleName() {
		return styleName;
	}

	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}

	public String getLayerName() {
		return layerName;
	}

	public void setLayerName(String layerName) {
		this.layerName = layerName;
	}

	public String getCoordSys() {
		return coordSys;
	}

	public void setCoordSys(String coordSys) {
		this.coordSys = coordSys;
	}
	
}
