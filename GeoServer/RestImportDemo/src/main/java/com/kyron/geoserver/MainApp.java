package com.kyron.geoserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.encoder.GSLayerEncoder;
import it.geosolutions.geoserver.rest.encoder.coverage.GSImageMosaicEncoder;

public class MainApp {

	private static final String BLUE_URL = "http://localhost:8600/geoserver/gwc/rest/layers/BlueMarble:BlueMarble";
	private static final String USER = "admin";
	private static final String PASSWORD = "geoserver";
	private static final String RESTURL = "http://localhost:8600/geoserver";


	public static void main(String[] args) {
		CredentialsProvider provider = getCredentials();
		/*
		GeoServerLayer layer = getLayerXML(provider);
		// edit layer value here ...
		byte newGutter = 10;
		layer.setGutter(newGutter);
		postLayerXML(provider, layer);
		// review updated data ...
		layer = getLayerXML(provider);
		*/
		System.out.println("begin test...");
		testImport();
		System.out.println("end test");
	}

	// Credentials for httpclient
	public static CredentialsProvider getCredentials() {

		CredentialsProvider provider = new BasicCredentialsProvider();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(USER, PASSWORD);
		provider.setCredentials(AuthScope.ANY, credentials);
		return provider;
	}

	// add layer
	public static void addLayer() {

	}

	// Send a GET request to GeoServer to get a layer in XML format
	public static GeoServerLayer getLayerXML(CredentialsProvider provider) {

		HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
		HttpGet getRequest = new HttpGet(BLUE_URL);
		getRequest.addHeader("accept", "application/xml");
		HttpResponse response;
		GeoServerLayer retLayer = null;
		try {
			response = client.execute(getRequest);
			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) {

				HttpEntity httpEntity = response.getEntity();
				String apiOutput = EntityUtils.toString(httpEntity);

				System.out.println(apiOutput);

				// Use jaxb to unmarshal the response content
				JAXBContext jaxbContext = JAXBContext.newInstance(GeoServerLayer.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				retLayer = (GeoServerLayer) jaxbUnmarshaller.unmarshal(new StringReader(apiOutput));

				System.out.println("==== POJO ====");
				System.out.println(retLayer.name);
				System.out.println(retLayer.metaWidthHeight.getInt());
				System.out.println(retLayer.getGutter());
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return retLayer;
	}

	// Send a POST request with new XML data/payload to update a layer in GeoServer
	public static void postLayerXML(CredentialsProvider provider, GeoServerLayer inputLayer) {

		HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
		HttpResponse response;

		// Define a postRequest request
		HttpPost postRequest = new HttpPost(BLUE_URL);
		postRequest.addHeader("content-type", "application/xml");

		// Set the request post body with data from input parameter
		StringWriter writer = new StringWriter();
		JAXBContext jaxbContext;
		try {

			// transform POJO to XML
			jaxbContext = JAXBContext.newInstance(GeoServerLayer.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.marshal(inputLayer, writer);

			StringEntity userEntity = new StringEntity(writer.getBuffer().toString());
			postRequest.setEntity(userEntity);
			postRequest.setHeader("accept", "application/json");
			postRequest.setHeader("content-type", "application/xml");

			// Send the request and get response immediately
			response = client.execute(postRequest);

			// verify the valid error code first
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				System.out.println("Layer is updated");
			} else {
				throw new RuntimeException("Failed with HTTP error code : " + statusCode);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	// Send a GET request to GeoServer to get a layer in JSON format
	public static String getLayerJson(CredentialsProvider provider) {

		HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
		HttpResponse response;
		StringBuilder retJson = new StringBuilder();
		try {
			response = client.execute(new HttpGet(BLUE_URL));
			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) {
				// read the response, default is json
				BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
				String output;
				while ((output = br.readLine()) != null) {
					retJson.append(output);
					System.out.println(retJson.toString());
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return retJson.toString();
	}

	// Send a POST request with new JSON data/payload to update a layer
	// NOTE: JSON is not recommended for managing GeoServer layers because the JSON
	// library
	// has a number of issues with multi-valued properties such as
	// “parameterFilters”
	public static void postLayerJson(CredentialsProvider provider, String inputJson) {

		HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
		HttpResponse response;

		// Define a postRequest request
		HttpPost postRequest = new HttpPost(BLUE_URL);

		// edit json values here....

		// Set the request post body
		JSONObject object = new JSONObject(inputJson);

		try {
			StringEntity userEntity = new StringEntity(object.toString());
			postRequest.setEntity(userEntity);
			postRequest.setHeader("accept", "application/json");
			postRequest.setHeader("content-type", "application/json");

			response = client.execute(postRequest);

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				throw new RuntimeException("Failed with HTTP error code : " + statusCode);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// -----------------------------------------------------------
	
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

