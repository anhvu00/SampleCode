package com.kyron.geoserver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.httpclient.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.kyron.geoserver.simplehttp.GeoServerLayer;
import com.kyron.geoserver.simplehttp.RestLayerDemo;

import it.geosolutions.geoserver.rest.GeoServerRESTPublisher.ParameterConfigure;

public class MainApp {

	private final static Logger LOGGER = LoggerFactory.getLogger(MainApp.class);
	private static final String RESTURL = "http://10.211.55.7:8600/geoserver";
	private static final String BLUE_URL = "http://192.168.1.211:8600/geoserver/gwc/rest/layers/BlueMarble:BlueMarble";
	private static final String USER = "admin";
	private static final String PASSWORD = "geoserver";

	public static void main(String[] args) {
		LOGGER.info("=== BEGIN DEMO ===");
//		LOGGER.info("=== IMPORT GEOTIFF TO AN EXISTING WORKSPACE " + RESTURL + " ===");
//		testImportGeoTiff();
//		testAllGetLayers();
//		testGetOneLayer("Test", "SP27GTIF");
//		LOGGER.info("=== IMPORT GEOTIFF TO A NEW WORKSPACE " + RESTURL + " ===");
//		testImportGeoTiffNewWorkspace();
//		LOGGER.info("=== DELETE STORE " + RESTURL + " ===");
//		testDeleteStore();
//		LOGGER.info("=== DELETE WORKSPACE " + RESTURL + " ===");
//		testDeleteWorkspace();
//		LOGGER.info("=== UPDATE LAYER IN DOCKER CONTAINER GEOSERVER " + BLUE_URL + " ===");		
//		testGeoServerDocker();
		LOGGER.info("=== RASTER/VECTOR LAYER-GROUP " + RESTURL + " ===");
		testSouthDakota();
		LOGGER.info("=== END DEMO ===");

	}
	
	/*
	 * A test updating an existing layer (i.e. BlueMarble:BlueMarble on GeoServer Docker container)
	 * This "BlueMarble:BlueMarble" workspace:store is in our GeoServer Docker container
	 * running on my test host 192.168.1.211:8600.
	 */
	public static void testGeoServerDocker() {
		RestLayerDemo bluemarble = new RestLayerDemo(BLUE_URL, USER, PASSWORD);
		// show original layer
		GeoServerLayer layer = bluemarble.getLayer();
		// pseudo random from 1 - 20
		int max = 20;
		int min = 1;
		Random random = new Random();
		byte newGutterValue = (byte) (random.nextInt(max - min + 1) + min);
		bluemarble.updateLayerDemo(layer, newGutterValue);
		// show update data
		layer = bluemarble.getLayer();
	}

	// -----------------------------------------------------------
	
	public static void testImportGeoTiff() {
		// workspace "Test" must exists on GeoServer
		String workspace = "Test";
		String store = "SP27GTIF";
		String fullPathImageName = "";
		File geotiff;
		try {
			geotiff = new ClassPathResource("testdata/geotiff/ChicagoSpot/SP27GTIF.TIF").getFile();
			fullPathImageName = geotiff.getCanonicalPath();
			//System.out.println("canonical path=" + fullPathImageName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		GeoServerCredentials credential = new GeoServerCredentials(RESTURL,USER,PASSWORD);
        GeoServerParams param = new GeoServerParams(workspace,store,fullPathImageName);
        GeoServerUtils util = new GeoServerUtils(credential);
        
        // upload to an existing workspace
        if (util.uploadGeoTiff(param)) {
        	LOGGER.info(fullPathImageName + " is uploaded.");
        } else {
        	LOGGER.error("Upload failed.");
        }
	}
	
	public static void testImportGeoTiffNewWorkspace() {
		String workspace = "AUTOWS";
		String store = "AUTOSTORE";
		String fullPathImageName = "";
		File geotiff;
		boolean doCreateWS = true;
		try {
			geotiff = new ClassPathResource("testdata/geotiff/ChicagoSpot/UTM2GTIF.TIF").getFile();
			fullPathImageName = geotiff.getCanonicalPath();
			//System.out.println("canonical path=" + fullPathImageName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		GeoServerCredentials credential = new GeoServerCredentials(RESTURL,USER,PASSWORD);
        GeoServerParams param = new GeoServerParams(workspace,store,fullPathImageName);
        GeoServerUtils util = new GeoServerUtils(credential);

        // upload to a non-existing workspace
        if (util.uploadGeotiff(doCreateWS, param)) {
        	LOGGER.info(fullPathImageName + " is uploaded.");
        } else {
        	LOGGER.error("Upload failed.");
        }
	}
	
	public static void testDeleteStore() {
		String workspace = "Test";
		String store = "SP27GTIF";

		GeoServerCredentials credential = new GeoServerCredentials(RESTURL,USER,PASSWORD);
        GeoServerUtils util = new GeoServerUtils(credential);
        if (util.deleteRasterStore(workspace, store)) {
        	LOGGER.info(store + " is deleted.");
        } else {
        	LOGGER.error("Delete store failed.");
        }
	}
	
	public static void testDeleteWorkspace() {
		String workspace = "AUTOWS";

		GeoServerCredentials credential = new GeoServerCredentials(RESTURL,USER,PASSWORD);
        GeoServerUtils util = new GeoServerUtils(credential);
        if (util.deleteWorkspace(workspace)) {
        	LOGGER.info(workspace + " is deleted.");
        } else {
        	LOGGER.error("Delete workspace failed.");
        }
	}
	
	public static void testAllGetLayers() {
		GeoServerCredentials credential = new GeoServerCredentials(RESTURL,USER,PASSWORD);
        GeoServerUtils util = new GeoServerUtils(credential);
        ArrayList<String> list = util.getLayers();
        String[] ary;
        for(String item : list) {
        	ary = item.split(":");
        	System.out.println(ary[0] + ":" + ary[1]);
        	util.getLayer(ary[0], ary[1]);
        }
	}
	
	public static void testGetOneLayer(String workspace, String layer) {
		GeoServerCredentials credential = new GeoServerCredentials(RESTURL,USER,PASSWORD);
        GeoServerUtils util = new GeoServerUtils(credential);
        util.getLayer(workspace, layer);
	}
	
	// SLD test. code sample from geoserver-manager GeoserverRESTImageMosaicTest.java
	public static void testMosaic() {
		
		String workspace = "AUTOWS";
		boolean doCreateWS = true;
		String store = "AUTOMOSAIC";
		String fullPathImageName = "";
		File imageMosaicFile;
		try {
			imageMosaicFile = new ClassPathResource("testdata/mosaic_geotiff.zip").getFile();
			fullPathImageName = imageMosaicFile.getCanonicalPath();
			//System.out.println("canonical path=" + fullPathImageName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		GeoServerCredentials credential = new GeoServerCredentials(RESTURL,USER,PASSWORD);
        GeoServerParams param = new GeoServerParams(workspace,store,fullPathImageName);
        GeoServerUtils util = new GeoServerUtils(credential);

        // upload to a non-existing workspace
        if (util.uploadMosaic(doCreateWS, param)) {
        	LOGGER.info(fullPathImageName + " is uploaded.");
        } else {
        	LOGGER.error("Upload failed.");
        }		
	}
	
	/*
	 * Ultimate test. Baseline = GeoTiff. Layer-group = shape files
	 * - Import a GeoTiff store for the base image
	 * - Import a Vector store pointing to the shapefile folder containing streams, roads, restrict, and bugsites.
	 * - Create a new layer-group.
	 */
	public static void testSouthDakota() {
		String workspace = "ANH-DAKOTA";
		String store = "SF-GEOTIFF";
		String shapeStore = "SF-SHAPE";
		String fullPathImageName = "";
		File geotiff;
		File shapeFile;
		boolean doCreateWS;
		
		// 1. IMPORT BASE IMAGE
		try {
			geotiff = new ClassPathResource("testdata/shapefile/sfdem.tif").getFile();
			fullPathImageName = geotiff.getCanonicalPath();
			//System.out.println("canonical path=" + fullPathImageName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		GeoServerCredentials credential = new GeoServerCredentials(RESTURL,USER,PASSWORD);
        GeoServerParams param = new GeoServerParams(workspace,store,fullPathImageName);
        GeoServerUtils util = new GeoServerUtils(credential);

        // upload to a non-existing workspace
        doCreateWS = true;
        if (util.uploadGeotiff(doCreateWS, param)) {
        	LOGGER.info(fullPathImageName + " is uploaded.");
        } else {
        	LOGGER.error("Upload failed.");
        }
        
        // 2. IMPORT SHAPEFILES
		try {
			shapeFile = new ClassPathResource("testdata/shapefile/sf-shapes.zip").getFile();
			param.setImageFile(shapeFile); // different input folder
	        param.setStore(shapeStore); // different store!
	        doCreateWS = false; // workspace already created
	        if (util.uploadShapefiles(doCreateWS, param)) {
	        	LOGGER.info(param.getImage() + " is uploaded.");
	        } else {
	        	LOGGER.error("Upload failed.");
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 3. CREATE LAYER-GROUP
		//GroupParams gparam = new GroupParams("ANH-LG", "EPSG:26713", 589980, 609000, 4913700, 4928010);
		GroupParams gparam = new GroupParams("ANH-LG", "EPSG:26713");
		util.createLayerGroup(gparam);
	}
	
} // end main class

