package com.kyron.geoserver;

/**
 * Parameters for creating layer-group
 * GeoServer Coordinate Reference System (CRS) like "EPSG:26713"
 * Bounding box x-y and lat-lon
 * TODO: how to convert lat-lon to x-y and vice versa in GeoServer?
 * @author anh
 *
 */
public class GroupParams {

	private String groupName;
	private String crs;
	private int minX, maxX, minY, maxY;
	
	// constructor 1
	public GroupParams(String name, String crs) {
		this.groupName = name;
		this.crs = crs;
	}
	
	// constructor 2
	public GroupParams(String name, String crs, int minx, int maxx, int miny, int maxy) {
		this.groupName = name;
		this.crs = crs;
		this.minX = minx;
		this.maxX = maxx;
		this.minY = miny;
		this.maxY =maxy;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getCrs() {
		return crs;
	}

	public void setCrs(String crs) {
		this.crs = crs;
	}

	public int getMinX() {
		return minX;
	}

	public void setMinX(int minX) {
		this.minX = minX;
	}

	public int getMaxX() {
		return maxX;
	}

	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}

	public int getMinY() {
		return minY;
	}

	public void setMinY(int minY) {
		this.minY = minY;
	}

	public int getMaxY() {
		return maxY;
	}

	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}

}
