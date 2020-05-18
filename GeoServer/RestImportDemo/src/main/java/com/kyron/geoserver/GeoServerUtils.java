package com.kyron.geoserver;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;

/**
 * Provide CRUD operations with GeoServer through its REST API.
 * Use geoserver-manager from https://github.com/geosolutions-it/geoserver-manager
 * @author anh
 *
 */
public class GeoServerUtils {
	private final static Logger LOGGER = LoggerFactory.getLogger(GeoServerUtils.class);
	private static GeoServerCredentials gsCred;

	// constructor
	public GeoServerUtils(GeoServerCredentials credential) {
		LOGGER.info("Created " + this.getClass().getSimpleName());
		gsCred = credential;
	}
	
	/** 
	 * upload a raster to GeoServer
	 * @param param GeoServerParams such as workspace, store, and full path image name
	 * @return true = success. false otherwise
	 */
	public boolean uploadGeoTiff(GeoServerParams param) {
		boolean retval = false;
		GeoServerRESTManager manager;
		GeoServerRESTPublisher publisher;

		try {
            manager = new GeoServerRESTManager(
            		new URL(gsCred.getGeoServerURL()), 
            		gsCred.getGeoServerUser(), 
            		gsCred.getGeoServerPassword());
            publisher = manager.getPublisher();

			retval = publisher.publishGeoTIFF(
					param.getWorkspace(),
					param.getStore(),
					param.getImageFile());

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return retval;
	}
	
	/** 
	 * upload a raster to GeoServer, creating new workspace in the process
	 * @param param GeoServerParams such as workspace, store, and full path image name
	 * @return true = success. false otherwise
	 */
    public boolean uploadGeotiff(boolean createWS, GeoServerParams param) {
		boolean retval = false;
		GeoServerRESTManager manager;
		GeoServerRESTPublisher publisher;

		try {
            manager = new GeoServerRESTManager(
            		new URL(gsCred.getGeoServerURL()), 
            		gsCred.getGeoServerUser(), 
            		gsCred.getGeoServerPassword());
            publisher = manager.getPublisher();
            // add a style (optional) could be in param
            //publisher.publishStyle(new File(new ClassPathResource("testdata").getFile(),"raster.sld"));
            
            // create new workspace
            if (createWS) {
             publisher.createWorkspace(param.getWorkspace());
            }
            
			retval = publisher.publishGeoTIFF(
					param.getWorkspace(),
					param.getStore(),
					param.getImageFile());

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return retval;       
    }
}
