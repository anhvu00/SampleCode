package com.goldsentinel;

import java.util.ArrayList;
import java.util.List;

// POJO/DTO for nodes on the Board
public class BoardNode {

	private String nodeType;
	private boolean selected;
	private String id;
	private int x;
	private int y;
	private String notes;
	private String data;

	// empty constructor for Jackson serialize/deserialize
	public BoardNode() {
		// do nothing
	}

	// constructor
	public BoardNode(String type, String id, int x, int y, String data, boolean selected, String notes) {
		this.nodeType = type;
		this.id = id;
		this.x = x;
		this.y = y;
		this.selected = selected;
		this.notes = notes;
		this.data = data;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		String retval = "nodeType=" + this.nodeType + ",id=" + this.id + ",x=" + this.x + ",y=" + this.y + ",selected="
				+ this.selected + ",notes=" + this.notes + ",data=" + this.data;
		return retval;

	}
}
