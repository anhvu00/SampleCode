package com.kyron.geoserver.simplehttp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

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

/**
 * The goal is to demonstrate interaction with GeoServer REST API with just
 * simple REST calls. This class gets and updates an existing layer on GeoServer
 * running locally in a Docker container. This GeoServer should already had the
 * test workspace:store (i.e. BlueMarble:BlueMarble) 
 * Demo steps: 
 * - Run GeoServernDocker container on localhost:8600 
 * - Call RestLayerDemo.demoGetLayer() to see the existing gutter value (i.e. 10) 
 * - Call RestLayerDemo.demoUpdateLayer(15) to change the gutter value to 15. 
 * - Call RestLayerDemo.demoGetLayer() again to see the new gutter value. 
 * TODO: 
 * - Update other attributes besides gutter.
 * - Add new attributes to the layers (ex. parameterFilters) 
 * - Add more CRUD operations (ex. delete) 
 * - Refactor the class to be a utility class (i.e. remove static keyword). 
 * NOTE: 
 * - It takes some effort to create XML mapping and cover a combination of test cases 
 * - There is a library geoserver-manager on GitHub that might already had all the 
 * functions we need.
 * 
 * @author anh
 *
 */
public class RestLayerDemo {
	private static final String BLUE_URL = "http://localhost:8600/geoserver/gwc/rest/layers/BlueMarble:BlueMarble";
	private static final String USER = "admin";
	private static final String PASSWORD = "geoserver";

	// TESTS....
	public static void demoGetLayer() {
		CredentialsProvider provider = getCredentials();
		GeoServerLayer layer = getLayerXML(provider);
	}

	// change gutter value to 10
	public static void demoUpdateLayer(byte newGutter) {
		CredentialsProvider provider = getCredentials();
		GeoServerLayer layer = getLayerXML(provider);
		// edit layer value
		layer.setGutter(newGutter);
		postLayerXML(provider, layer);
	}

	// Credentials for httpclient
	public static CredentialsProvider getCredentials() {
		CredentialsProvider provider = new BasicCredentialsProvider();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(USER, PASSWORD);
		provider.setCredentials(AuthScope.ANY, credentials);
		return provider;
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

				// Use jaxb to unmarshal the response content
				JAXBContext jaxbContext = JAXBContext.newInstance(GeoServerLayer.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				retLayer = (GeoServerLayer) jaxbUnmarshaller.unmarshal(new StringReader(apiOutput));

				// System.out.println("==== API OUTPUT ====");
				// System.out.println(apiOutput);
				System.out.println("==== GetLayerXML ====");
				System.out.println("Layer Name = " + retLayer.name);
				System.out.println("Gutter value = " + retLayer.getGutter());
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
	// library has a number of issues with multi-valued properties such as
	// "parameterFilters"
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

} // end class
