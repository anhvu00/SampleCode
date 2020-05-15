package com.kyron.geoserver;

/**
 * POJO class holding the GeoServer URL, user and password
 * TODO:
 * - Add more security
 * - Add httpclient CredentialProvider
 * @author anh
 *
 */
public class GeoServerCredentials {
	private static String geoServerURL;
	private static String geoServerUser;
	private static String geoServerPassword;
	
	// constructor
	public GeoServerCredentials(String url, String user, String password) {
		GeoServerCredentials.geoServerURL = url;
		GeoServerCredentials.geoServerUser = user;
		GeoServerCredentials.geoServerPassword = password;
	}

	public static String getGeoServerURL() {
		return geoServerURL;
	}

	public static void setGeoServerURL(String geoServerURL) {
		GeoServerCredentials.geoServerURL = geoServerURL;
	}

	public static String getGeoServerUser() {
		return geoServerUser;
	}

	public static void setGeoServerUser(String geoServerUser) {
		GeoServerCredentials.geoServerUser = geoServerUser;
	}

	public static String getGeoServerPassword() {
		return geoServerPassword;
	}

	public static void setGeoServerPassword(String geoServerPassword) {
		GeoServerCredentials.geoServerPassword = geoServerPassword;
	}
	

}
