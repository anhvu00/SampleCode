package com.kyron.dictionary;

import com.kyron.dictionary.spi.Dictionary;

import java.util.SortedMap;
import java.util.TreeMap;

public class ExtendedDictionary implements Dictionary {

	private SortedMap<String, String> map;

	public ExtendedDictionary() {
		map = new TreeMap<String, String>();
		map.put("xml", "a document standard often used in web services, among other " + "things");
		map.put("REST",
				"an architecture style for creating, reading, updating, "
						+ "and deleting data that attempts to use the common "
						+ "vocabulary of the HTTP protocol; Representational State " + "Transfer");
	}

	@Override
	public String getDefinition(String word) {
		return map.get(word);
	}

	@Override
	public String getType() {
		return ("EXTENDED-DICTIONARY");
	}

}
