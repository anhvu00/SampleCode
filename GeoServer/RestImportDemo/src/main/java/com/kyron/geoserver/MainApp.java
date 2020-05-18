package com.kyron.geoserver;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.kyron.geoserver.simplehttp.RestLayerDemo;

public class MainApp {

	private final static Logger LOGGER = LoggerFactory.getLogger(MainApp.class);
	private static final String RESTURL = "http://10.211.55.7:8600/geoserver";
	private static final String USER = "admin";
	private static final String PASSWORD = "geoserver";

	public static void main(String[] args) {
		LOGGER.info("=== BEGIN DEMO ===");
		LOGGER.info("\n=== IMPORT GEOTIFF TO AN EXISTING WORKSPACE ===");
		testImportGeoTiff();
		LOGGER.info("\n=== IMPORT GEOTIFF TO A NEW WORKSPACE ===");
		testImportGeoTiffNewWorkspace();
		LOGGER.info("=== END DEMO ===");
	}
	
	// A test for GeoServer Docker container runs on localhost:8600
	public static void testGeoServerDocker() {
		//change this value to some other number as you test
		byte newGutter = 10;  
		RestLayerDemo.demoUpdateLayer(newGutter);
		
		// show update data
		RestLayerDemo.demoGetLayer();
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
	
} // end main class

