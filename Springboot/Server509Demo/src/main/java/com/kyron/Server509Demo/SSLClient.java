package com.kyron.Server509Demo;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;

public class SSLClient {

	// hard code to look like app.properties. fix later...
     static
       {
    	 String basePath = "/Users/anh/Github/SampleCode/Springboot/Server509Demo/src/main/resources/stores/";
         System.setProperty("javax.net.ssl.trustStore", basePath + "trust/truststore.jks");
         System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
         System.setProperty("javax.net.ssl.keyStore", basePath + "key/keystore.jks");
         System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
      }

    public static void main(String[] args) throws HttpException, IOException {

        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod();
        method.setURI(new URI("https://localhost:8443/hello", false));
        client.executeMethod(method);

        System.out.println(method.getResponseBodyAsString());

    }

}
