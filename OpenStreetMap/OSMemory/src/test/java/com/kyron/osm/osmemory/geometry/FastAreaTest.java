package com.kyron.osm.osmemory.geometry;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.kyron.osm.osmemory.MemoryStorage;
import com.kyron.osm.osmemory.OsmNode;
import com.vividsolutions.jts.geom.Geometry;

public class FastAreaTest {
	@Test
	public void testInside() {
		Geometry box = GeometryHelper.createBoxPolygon(1, 2, 1, 2);
		FastArea fastArea = new FastArea(box, new MemoryStorage());

		assertFalse(fastArea.coversNode(point(0, 0)));
		assertTrue(fastArea.coversNode(point(1, 1)));
		assertTrue(fastArea.coversNode(point(1, 2)));
		assertTrue(fastArea.coversNode(point(1.5, 1.5)));
		assertTrue(fastArea.coversNode(point(1, 1.5)));
	}

	OsmNode point(double x, double y) {
		return new OsmNode(0, 0, (int) (y / OsmNode.DIVIDER), (int) (x / OsmNode.DIVIDER), (short) 0);
	}
}
