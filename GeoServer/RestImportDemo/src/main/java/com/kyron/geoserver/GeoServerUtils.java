package com.kyron.geoserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.httpclient.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher.ParameterConfigure;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.decoder.RESTLayer;
import it.geosolutions.geoserver.rest.decoder.RESTLayerList;
import it.geosolutions.geoserver.rest.decoder.utils.NameLinkElem;
import it.geosolutions.geoserver.rest.encoder.GSLayerGroupEncoder;

/**
 * Provide CRUD operations with GeoServer through its REST API. Use
 * geoserver-manager from https://github.com/geosolutions-it/geoserver-manager
 * Test on GeoServer v2.16.2
 * 
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
			manager = new GeoServerRESTManager(new URL(gsCred.getGeoServerURL()), gsCred.getGeoServerUser(),
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
	 * 
	 * @param param GeoServerParams such as workspace, store, and full path image
	 *              name
	 * @return true = success. false otherwise
	 */
	public boolean uploadGeoTiff(GeoServerParams param) {
		boolean retval = false;

		try {
			retval = publisher.publishGeoTIFF(param.getWorkspace(), param.getStore(), param.getImageFile());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return retval;
	}

	/**
	 * upload a raster to GeoServer, creating new workspace in the process
	 * 
	 * @param param GeoServerParams such as workspace, store, and full path image
	 *              name
	 * @return true = success. false otherwise
	 */
	public boolean uploadGeotiff(boolean createWS, GeoServerParams param) {
		boolean retval = false;

		try {

			// add a style (optional) could be in param
			// publisher.publishStyle(new File(new
			// ClassPathResource("testdata").getFile(),"raster.sld"));

			// create new workspace
			if (createWS) {
				publisher.createWorkspace(param.getWorkspace());
			}

			retval = publisher.publishGeoTIFF(param.getWorkspace(), param.getStore(), param.getImageFile());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return retval;
	}

	public boolean uploadMosaic(boolean createWS, GeoServerParams param) {
		boolean retval = false;
		try {
			// create new workspace
			if (createWS) {
				publisher.createWorkspace(param.getWorkspace());
			}
			// create default style
			File sldFile = new ClassPathResource("testdata/style/restteststyle.sld").getFile();
			publisher.publishStyle(sldFile, "anh-raster");

			retval = publisher.publishImageMosaic(param.getWorkspace(), param.getStore(), param.getImageFile(),
					ParameterConfigure.FIRST, new NameValuePair("coverageName", "imageMosaic_test"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return retval;
	}

	// upload all shapefiles in a folder
	public boolean uploadShapefiles(boolean createWS, GeoServerParams param) {
		boolean retval = false;
		try {
			// create new workspace
			if (createWS) {
				publisher.createWorkspace(param.getWorkspace());
			}
			retval = publisher.publishShpCollection(param.getWorkspace(), param.getStore(),
					param.getImageFile().toURI());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return retval;
	}
	
	/*
	 * Upload a shapefile with a style as its default
	 */
	public boolean uploadShapefileWithStyle(boolean createWS, GeoServerParams param) {
		boolean retval = false;
		try {
			// create new workspace
			if (createWS) {
				publisher.createWorkspace(param.getWorkspace());
			}		
			// must add style before layer
			boolean pubStyleRetval = publisher.publishStyle(param.getStyleFile(), param.getStyleName());
			if (pubStyleRetval) {
		        retval = publisher.publishShp(
		        		param.getWorkspace(), 
		        		param.getStore(),
		                param.getLayerName(),
		                param.getImageFile(), 
		                param.getCoordSys(), 
		                param.getStyleName());				
			}
		} catch (FileNotFoundException e) {
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

	// get a list of all layers from GeoServer
	public ArrayList<String> getLayers() {
		ArrayList<String> retList = new ArrayList<String>();
		try {
			GeoServerRESTReader reader = new GeoServerRESTReader(gsCred.getGeoServerURL(), gsCred.getGeoServerUser(),
					gsCred.getGeoServerPassword());
			RESTLayerList list = reader.getLayers();
			for (NameLinkElem layer : list) {
				LOGGER.info(layer.getName() + " ");
				retList.add(layer.getName());
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return retList;
	}

	// get a specific layer
	public void getLayer(String workspace, String layer) {
		try {
			GeoServerRESTReader reader = new GeoServerRESTReader(gsCred.getGeoServerURL(), gsCred.getGeoServerUser(),
					gsCred.getGeoServerPassword());
			RESTLayer myLayer = reader.getLayer(workspace, layer);
			LOGGER.info("name=" + myLayer.getName() + " ");
			LOGGER.info("type=" + myLayer.getTypeString() + " ");
			LOGGER.info("title=" + myLayer.getTitle() + " ");
			LOGGER.info("abstract=" + myLayer.getAbstract() + " ");
			LOGGER.info("default style=" + myLayer.getDefaultStyle() + " ");
			// LOGGER.info("name space=" + myLayer.getNameSpace() + " ");
			LOGGER.info("resource url=" + myLayer.getResourceUrl() + " ");

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	// not work: groupWriter.setBounds(param.getCrs(), 0, 5000000, 0, 5000000);
	public boolean createLayerGroup(GroupParams param) {
		GSLayerGroupEncoder groupWriter = new GSLayerGroupEncoder();
		// minx, maxx, miny, maxy
//        groupWriter.setBounds(
//        		param.getCrs(),
//        		param.getMinX(),
//        		param.getMaxX(),
//        		param.getMinY(),
//        		param.getMaxY());
		for (String layer : param.getLayers()) {
			groupWriter.addLayer(layer);
		}

		return publisher.createLayerGroup(param.getGroupName(), groupWriter);
	}
} // end class
