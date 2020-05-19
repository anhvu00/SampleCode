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
	private static GeoServerRESTManager manager;
	private static GeoServerRESTPublisher publisher;

	// constructor
	public GeoServerUtils(GeoServerCredentials credential) {
		LOGGER.info("Created " + this.getClass().getSimpleName());
		gsCred = credential;
        try {
			manager = new GeoServerRESTManager(
					new URL(gsCred.getGeoServerURL()), 
					gsCred.getGeoServerUser(), 
					gsCred.getGeoServerPassword());
	        publisher = manager.getPublisher();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	/** 
	 * upload a raster to GeoServer
	 * @param param GeoServerParams such as workspace, store, and full path image name
	 * @return true = success. false otherwise
	 */
	public boolean uploadGeoTiff(GeoServerParams param) {
		boolean retval = false;

		try {
			retval = publisher.publishGeoTIFF(
					param.getWorkspace(),
					param.getStore(),
					param.getImageFile());
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

		try {

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

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return retval;       
    }
    
    // delete workspace and all the stores in it
    public boolean deleteWorkspace(String workspace) {
    	boolean retval = false;
    	boolean recursive = true;
    	
    	try {
			LOGGER.warn("Delete workspace " + workspace);
	    	retval = publisher.removeWorkspace(workspace, recursive);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
    	return retval;
    }
    
    public boolean deleteRasterStore(String workspace, String store) {
    	boolean retval = false;
    	boolean recursive = true;
    	
    	try {
			LOGGER.warn("Delete workspace:store " + workspace + ":" + store);
	    	retval = publisher.removeCoverageStore(workspace, store, recursive);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
    	return retval;
    }
}
