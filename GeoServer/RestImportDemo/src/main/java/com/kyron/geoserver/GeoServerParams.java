package com.kyron.geoserver;

import java.io.File;

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

	public static String getWorkspace() {
		return workspace;
	}

	public static void setWorkspace(String workspace) {
		GeoServerParams.workspace = workspace;
	}

	public static String getStore() {
		return store;
	}

	public static void setStore(String store) {
		GeoServerParams.store = store;
	}

	public static String getImage() {
		return image;
	}

	public static void setImage(String image) {
		GeoServerParams.image = image;
	}

	public static File getImageFile() {
		return imageFile;
	}

	public static void setImageFile(File imageFile) {
		GeoServerParams.imageFile = imageFile;
	}
	
}
