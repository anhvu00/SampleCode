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

	public String getGeoServerURL() {
		return geoServerURL;
	}

	public void setGeoServerURL(String geoServerURL) {
		GeoServerCredentials.geoServerURL = geoServerURL;
	}

	public String getGeoServerUser() {
		return geoServerUser;
	}

	public void setGeoServerUser(String geoServerUser) {
		GeoServerCredentials.geoServerUser = geoServerUser;
	}

	public String getGeoServerPassword() {
		return geoServerPassword;
	}

	public void setGeoServerPassword(String geoServerPassword) {
		GeoServerCredentials.geoServerPassword = geoServerPassword;
	}

}
