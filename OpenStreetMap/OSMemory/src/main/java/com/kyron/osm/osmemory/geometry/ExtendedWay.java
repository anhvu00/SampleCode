/**************************************************************************
 OSMemory library for OSM data processing.

 Copyright (C) 2014 Aleś Bułojčyk <alex73mail@gmail.com>

 This is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This software is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **************************************************************************/

package com.kyron.osm.osmemory.geometry;

import com.kyron.osm.osmemory.IOsmNode;
import com.kyron.osm.osmemory.IOsmObject;
import com.kyron.osm.osmemory.IOsmWay;
import com.kyron.osm.osmemory.MemoryStorage;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;

/**
 * Class for cache some extended information about way, like boundary, geometry, etc.
 */
public class ExtendedWay implements IExtendedObject {
	private final IOsmWay way;
	private final MemoryStorage storage;

	private BoundingBox boundingBox;
	private boolean allPointsDefined;
	private Coordinate[] points;
	private IOsmNode[] nodes;
	private LineString line;
	private Geometry area;

	public ExtendedWay(IOsmWay way, MemoryStorage storage) {
		this.way = way;
		this.storage = storage;
	}

	@Override
	public IOsmObject getObject() {
		return way;
	}

	@Override
	public BoundingBox getBoundingBox() {
		checkProcessed();
		return boundingBox;
	}

	public boolean isAllPointsDefined() {
		checkNodes();
		return allPointsDefined;
	}

	public boolean isClosed() {
		long[] nids = way.getNodeIds();
		return nids.length >= 3 && nids[0] == nids[nids.length - 1];
	}

	public synchronized LineString getLine() throws Exception {
		checkProcessed();
		if (!allPointsDefined) {
			return null;
		}
		if (line == null) {
			try {
				line = GeometryHelper.createLine(points);
			} catch (Exception ex) {
				throw new Exception("Impossible to create line from way #" + way.getId() + ": "
						+ ex.getMessage());
			}
			if (!line.isValid()) {
				throw new Exception("not valid line");
			}
			if (!line.isSimple()) {
				throw new Exception("self-intersected");
			}
		}
		return line;
	}

	public synchronized Geometry getArea() {
		checkProcessed();
		if (!isClosed()) {
			throw new RuntimeException("Impossible to create polygon from way #" + way.getId()
					+ ": non-closed way");
		}
		if (!allPointsDefined) {
			throw new RuntimeException("Impossible to create polygon from way #" + way.getId()
					+ ": not all points defined");
		}
		if (area == null) {
			try {
				area = GeometryHelper.createPolygon(points);
			} catch (Exception ex) {
				throw new RuntimeException("Impossible to create polygon from way #" + way.getId() + ": "
						+ ex.getMessage());
			}
			if (!area.isValid()) {
				throw new RuntimeException("Impossible to create polygon from way #" + way.getId()
						+ ": it is not valid");
			}
		}
		return area;
	}

	protected synchronized void checkProcessed() {
		if (boundingBox != null) {
			return; // already loaded
		}
		checkNodes();
		boundingBox = new BoundingBox();
		points = new Coordinate[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			IOsmNode n = nodes[i];
			if (n != null) {
				boundingBox.expandToInclude(n.getLat(), n.getLon());
				points[i] = GeometryHelper.coord(n.getLongitude(), n.getLatitude());
			}
		}
	}

	protected synchronized void checkNodes() {
		if (nodes != null) {
			return; // already filled
		}
		allPointsDefined = true;
		nodes = new IOsmNode[way.getNodeIds().length];
		for (int i = 0; i < way.getNodeIds().length; i++) {
			long nid = way.getNodeIds()[i];
			nodes[i] = storage.getNodeById(nid);
			if (nodes[i] == null) {
				allPointsDefined = false;
			}
		}
	}

	@Override
	public Boolean iterateNodes(NodesIterator iterator) {
		checkNodes();
		for (IOsmNode n : nodes) {
			if (n != null) {
				Boolean r = iterator.processNode(n);
				if (r != null) {
					return r;
				}
			}
		}
		return null;
	}
}
