package com.kyron.solr;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple insert java objects to solr and query them.
 * Quick start:
 * 1. Start solr on local host with "solr.cmd start -e cloud"
 * default is 2 nodes, port 8983 and 7574. Use "solr status"
 * or http://localhost:8983 to see if solr is up/running
 * 2. Create the "demo" collection in solr if not existed 
 * using the solr management web console (localhost:8983)
 * 3. Verify resources/server.properties
 * 4. Build and run this app
 * 5. Stop solr with "solr.cmd stop -all"
 * @author anh
 *
 */
public class MainApp {

	public static void main(String[] args) {
		
		// create some test data
		SolrData data = new SolrData("001", "Tom Cruise", new Long(5), "actor");
		SolrData data_1 = new SolrData("002", "John Doe", new Long(25), "tomato farmer");
		SolrData data_2 = new SolrData("003", "Tom Jones", new Long(77), "singer");
		SolrData data_3 = new SolrData("004", "Tom Hank", new Long(60), "actor");
		SolrData data_4 = new SolrData("005", "Bill Gates", new Long(65), "ceo");

		// Define a solr util that works with SolrData class above
		SolrUtils su = new SolrUtils(SolrData.class);
		
		// create a list of Solr fields to search, specifically for SolrData
		ArrayList<String> searchFields = new ArrayList<>();
		searchFields.add("name");
		searchFields.add("occupation");
		su.setSearchFields(searchFields);
		
		// insert a few sample data here
		su.insert(data);
		
		// search and print out
		List<SolrData> list = su.findAll("name:Tom~");
		if (list != null) {
			for(SolrData d : list) {
				System.out.println(d.toString());
			}			
		}
		
		System.out.println("done.");

	}

}
