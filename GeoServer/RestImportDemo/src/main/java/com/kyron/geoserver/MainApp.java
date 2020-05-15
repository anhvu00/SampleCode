package com.kyron.geoserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.encoder.GSLayerEncoder;
import it.geosolutions.geoserver.rest.encoder.coverage.GSImageMosaicEncoder;

import com.kyron.geoserver.simplehttp.RestLayerDemo;

public class MainApp {

	private final static Logger LOGGER = LoggerFactory.getLogger(MainApp.class);
	private static final String RESTURL = "http://10.211.55.7:8600/geoserver";
	private static final String USER = "admin";
	private static final String PASSWORD = "geoserver";

	public static void main(String[] args) {
		LOGGER.info("=== BEGIN DEMO ===");	
		// uncomment this to test with GeoServer Docker container
		//testGeoServerDocker();
		testImport_1();
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
	
	public static void testImport_1() {
		GeoServerCredentials credential = new GeoServerCredentials(RESTURL,USER,PASSWORD);
        GeoServerParams param = new GeoServerParams("Test","SP27GTIF","C:/TestImage/Chicago/SP27GTIF.TIF");
        GeoServerUtils util = new GeoServerUtils(credential);
        if (util.uploadGeoTiff(param)) {
        	LOGGER.info("C:/TestImage/Chicago/SP27GTIF.TIF is uploaded.");
        } else {
        	LOGGER.error("Upload failed.");
        }
	}
	
	public static void testImport() {
		GeoServerRESTManager manager;
		GeoServerRESTReader reader;
		GeoServerRESTPublisher publisher;

		try {
            manager = new GeoServerRESTManager(new URL(RESTURL), USER, PASSWORD);
            reader = manager.getReader();
            publisher = manager.getPublisher();
			//GeoServerRESTReader reader = new GeoServerRESTReader(RESTURL, USER, PASSWORD);
			//GeoServerRESTPublisher publisher = new GeoServerRESTPublisher(RESTURL, USER, PASSWORD);
			// create a new workspace
			//boolean workspace = publisher.createWorkspace("AnhCodeWorkspace");
			testGeotiff(publisher, "Test");
			System.out.println("done create mosaic");

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void testGeotiff(GeoServerRESTPublisher gsPublisher, String workspace) throws FileNotFoundException, IllegalArgumentException {

		final String STORE = "SP27GTIF";
		//private static final File MOSAIC = new File("/Users/anh/Downloads/Chicago/UTM2GTIF.TIF");
		final File LOCAL_FILE = new File("C:/TestImage/Chicago/SP27GTIF.TIF");
		//final File REMOTE_FILE = new File("/opt/geoserver/data_dir/GeoTiff/Sample/Chicago-spot/UTM2GTIF.tif");
		
		URI uri = LOCAL_FILE.toURI();

		// layer encoder
		final GSLayerEncoder layerEnc = new GSLayerEncoder();
		layerEnc.setDefaultStyle("raster");

		// coverage encoder
		final GSImageMosaicEncoder coverageEnc = new GSImageMosaicEncoder();
		coverageEnc.setName(STORE);
		coverageEnc.setTitle(STORE);

		// ... many other options are supported

		// create a new ImageMosaic layer...
		//final boolean published = gsPublisher.publishExternalMosaic(workspace, STORE, MOSAIC, coverageEnc, layerEnc);
		final boolean published = gsPublisher.publishGeoTIFF(workspace, STORE, LOCAL_FILE);
		//gsPublisher.publishG
		// check the results
		if (!published) {
			final String msg = "Error creating the new store ";
			System.out.println(msg);
		}
	}
} // end main class

