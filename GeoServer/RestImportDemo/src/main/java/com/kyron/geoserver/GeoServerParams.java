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
	private static String workspace;
	private static String store;
	private static String image;
	private static File imageFile;

	// constructor
	// workspace = GeoServer Workspace name
	// store = GeoServer Store name, usually the same as the image file name
	// fullPathImageName = Full path image (i.e. "C:/data/myRasterImage.tiff")
	public GeoServerParams(String workspace, String store, String fullPathImageName) {
		GeoServerParams.workspace = workspace;
		GeoServerParams.store = store;
		GeoServerParams.image = fullPathImageName;
		GeoServerParams.imageFile = new File(fullPathImageName);
	}

	public String getWorkspace() {
		return workspace;
	}

	public void setWorkspace(String workspace) {
		GeoServerParams.workspace = workspace;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		GeoServerParams.store = store;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		GeoServerParams.image = image;
		// also change the imageFile
		GeoServerParams.imageFile = new File(image);
	}

	public File getImageFile() {
		return imageFile;
	}

	public void setImageFile(File imageFile) throws IOException {
		GeoServerParams.imageFile = imageFile;
		// also change the full path image name
		GeoServerParams.image = imageFile.getCanonicalPath();
	}
	
}
