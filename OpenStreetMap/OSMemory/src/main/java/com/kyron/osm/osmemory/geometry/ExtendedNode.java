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
import com.kyron.osm.osmemory.MemoryStorage;

/**
 * Class for cache some extended information about node.
 */
public class ExtendedNode implements IExtendedObject {
	private final IOsmNode node;
	private final MemoryStorage storage;

	private BoundingBox boundingBox;

	public ExtendedNode(IOsmNode node, MemoryStorage storage) {
		this.node = node;
		this.storage = storage;
	}

	@Override
	public IOsmObject getObject() {
		return node;
	}

	@Override
	public BoundingBox getBoundingBox() {
		checkProcessed();
		return boundingBox;
	}

	protected synchronized void checkProcessed() {
		if (boundingBox != null) {
			return; // already loaded
		}
		boundingBox = new BoundingBox();
		boundingBox.expandToInclude(node.getLat(), node.getLon());
	}

	@Override
	public Boolean iterateNodes(NodesIterator iterator) {
		return iterator.processNode(node);
	}
}
