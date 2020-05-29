package com.kyron.geoserver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.kyron.geoserver.simplehttp.GeoServerLayer;
import com.kyron.geoserver.simplehttp.RestLayerDemo;

public class MainApp {

	private final static Logger LOGGER = LoggerFactory.getLogger(MainApp.class);
	private static final String REST_URL = "http://10.211.55.7:8600/geoserver";
	private static final String BLUE_URL = "http://192.168.1.211:8600/geoserver/gwc/rest/layers/BlueMarble:BlueMarble";
	private static final String USER = "admin";
	private static final String PASSWORD = "geoserver";

	public static void main(String[] args) {
		LOGGER.info("=== BEGIN DEMO ===");
		LOGGER.info("---- DEMO DOCKER GEOSERVER " + BLUE_URL + "----");
		demoDocker(BLUE_URL);
		LOGGER.info("---- DEMO VM GEOSERVER " + REST_URL + "----");
		demoVM(REST_URL);
		LOGGER.info("=== END DEMO ===");

	}

	/*
	 * A test updating an existing layer (i.e. BlueMarble:BlueMarble on GeoServer
	 * Docker container) It only updates a single value to demonstrate the point.
	 * This "BlueMarble:BlueMarble" workspace:store is in our GeoServer Docker
	 * container running on my test host 192.168.1.211:8600. Change it to your demo
	 * host.
	 */
	private static void demoDocker(String dockerUrl) {
		LOGGER.info("=== UPDATE LAYER ===");
		RestLayerDemo bluemarble = new RestLayerDemo(dockerUrl, USER, PASSWORD);
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
		LOGGER.info("---- END DEMO DOCKER GEOSERVER ----");
	}

	/*
	 * We can test more when the GeoServer is running on a VM.
	 * 
	 */
	private static void demoVM(String restUrl) {	
		String workspace = testImportGeoTiffNewWorkspace(restUrl);
		testImportGeoTiff(restUrl, workspace);
		testLayerGroup(restUrl);		
	}

	// -----------------------------------------------------------

	private static String testImportGeoTiffNewWorkspace(String restUrl) {
		String workspace = "DEMO-WKS";
		String store = "DEMO-STORE";
		String fullPathImageName = "";
		File geotiff;
		boolean doCreateWS = true;
		
		LOGGER.info("=== IMPORT GEOTIFF TO A NEW WORKSPACE " + workspace + "===");
		try {
			geotiff = new ClassPathResource("testdata/geotiff/ChicagoSpot/UTM2GTIF.TIF").getFile();
			fullPathImageName = geotiff.getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}

		GeoServerCredentials credential = new GeoServerCredentials(restUrl, USER, PASSWORD);
		GeoServerParams param = new GeoServerParams(workspace, store, fullPathImageName);
		GeoServerUtils util = new GeoServerUtils(credential);

		// upload to a non-existing workspace
		if (util.uploadGeotiff(doCreateWS, param)) {
			LOGGER.info(fullPathImageName + " is uploaded.");
		} else {
			LOGGER.error("Upload failed.");
		}
		return workspace;
	}
	
	private static void testImportGeoTiff(String restUrl, String workspace) {
		String store = "SP27GTIF";
		String fullPathImageName = "";
		File geotiff;
		
		LOGGER.info("=== IMPORT GEOTIFF TO AN EXISTING WORKSPACE " + workspace + " ===");
		try {
			geotiff = new ClassPathResource("testdata/geotiff/ChicagoSpot/SP27GTIF.TIF").getFile();
			fullPathImageName = geotiff.getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		GeoServerCredentials credential = new GeoServerCredentials(restUrl, USER, PASSWORD);
		GeoServerParams param = new GeoServerParams(workspace, store, fullPathImageName);
		GeoServerUtils util = new GeoServerUtils(credential);

		// upload to an existing workspace
		if (util.uploadGeoTiff(param)) {
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
	private static void testLayerGroup(String restUrl) {
		String workspace = "DEMO-LAYER-GROUP";
		String store = "SF-GEOTIFF";
		String shapeStore = "SF-SHAPE";
		String layerGroup = "DEMO-GROUP";
		String crs = "EPSG:26713";
		String fullPathImageName = "";
		File geotiff;
		File shapeFile;
		boolean doCreateWS;
		
		LOGGER.info("=== RASTER/VECTOR LAYER-GROUP " + restUrl + " ===");

		// 1. IMPORT BASE IMAGE
		try {
			geotiff = new ClassPathResource("testdata/shapefile/sfdem.tif").getFile();
			fullPathImageName = geotiff.getCanonicalPath();
			// System.out.println("canonical path=" + fullPathImageName);
		} catch (IOException e) {
			e.printStackTrace();
		}

		GeoServerCredentials credential = new GeoServerCredentials(restUrl, USER, PASSWORD);
		GeoServerParams param = new GeoServerParams(workspace, store, fullPathImageName);
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
		// hard code for now. Later, should query GeoServer for the layers
		ArrayList<String> layers = new ArrayList<String>();
        layers.add("DEMO-LAYER-GROUP:SF-GEOTIFF");
        layers.add("DEMO-LAYER-GROUP:bugsites");
		// GroupParams gparam = new GroupParams(layerGroup, crs, 589980, 609000, 4913700, 4928010);
		GroupParams gparam = new GroupParams(layerGroup, crs, layers);
		util.createLayerGroup(gparam);
	}
	
	// -----------------------------------------------------------	
	
	public static void testGetLayers(String restUrl) {
		GeoServerCredentials credential = new GeoServerCredentials(restUrl, USER, PASSWORD);
		GeoServerUtils util = new GeoServerUtils(credential);
		ArrayList<String> list = util.getLayers();
		String[] ary;
		for (String item : list) {
			ary = item.split(":");
			System.out.println(ary[0] + ":" + ary[1]);
			util.getLayer(ary[0], ary[1]);
		}
	}
	
	/* Get one layer.
	 * Usage: testGetOneLayer(REST_URL, "DEMO-WKS", "SP27GTIF");
	 */
	public static void testGetLayer(String restUrl, String workspace, String layer) {
		GeoServerCredentials credential = new GeoServerCredentials(restUrl, USER, PASSWORD);
		GeoServerUtils util = new GeoServerUtils(credential);
		util.getLayer(workspace, layer);
	}
	
	public static void testDeleteWorkspace() {
		String workspace = "AUTOWS";

		GeoServerCredentials credential = new GeoServerCredentials(REST_URL, USER, PASSWORD);
		GeoServerUtils util = new GeoServerUtils(credential);
		if (util.deleteWorkspace(workspace)) {
			LOGGER.info(workspace + " is deleted.");
		} else {
			LOGGER.error("Delete workspace failed.");
		}
	}
	
	public static void testDeleteStore() {
		String workspace = "Test";
		String store = "SP27GTIF";

		GeoServerCredentials credential = new GeoServerCredentials(REST_URL, USER, PASSWORD);
		GeoServerUtils util = new GeoServerUtils(credential);
		if (util.deleteRasterStore(workspace, store)) {
			LOGGER.info(store + " is deleted.");
		} else {
			LOGGER.error("Delete store failed.");
		}
	}
	// SLD test. code sample from geoserver-manager
	// GeoserverRESTImageMosaicTest.java
	public static void testMosaic() {

		String workspace = "AUTOWS";
		boolean doCreateWS = true;
		String store = "AUTOMOSAIC";
		String fullPathImageName = "";
		File imageMosaicFile;
		try {
			imageMosaicFile = new ClassPathResource("testdata/mosaic_geotiff.zip").getFile();
			fullPathImageName = imageMosaicFile.getCanonicalPath();
			// System.out.println("canonical path=" + fullPathImageName);
		} catch (IOException e) {
			e.printStackTrace();
		}

		GeoServerCredentials credential = new GeoServerCredentials(REST_URL, USER, PASSWORD);
		GeoServerParams param = new GeoServerParams(workspace, store, fullPathImageName);
		GeoServerUtils util = new GeoServerUtils(credential);

		// upload to a non-existing workspace
		if (util.uploadMosaic(doCreateWS, param)) {
			LOGGER.info(fullPathImageName + " is uploaded.");
		} else {
			LOGGER.error("Upload failed.");
		}
	}

} // end main class
