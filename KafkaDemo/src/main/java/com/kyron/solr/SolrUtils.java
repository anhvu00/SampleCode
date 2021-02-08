package com.kyron.solr;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;

/*
 * Intended to be a utility class with all necessary Solr functions
 * NOTE:
 * solr.url must include the collection name (i.e. "http://localhost:8983/solr/collection1")
 * Each document may contain different fields. setSearchFields() must be called to define the "key" for the search value.
 * Must pass in the type of the data object to insert/find to/from solr. 
 */
public class SolrUtils<T> {
	
	final Class<T> DATA_TYPE;
	final static Logger LOG = LogManager.getLogger(SolrUtils.class.getName());
	
	private HttpSolrClient solrClient;
	private Properties prop;
	private ArrayList<String> searchFields;
	private String solrUrl;
	
	// constructor sets the generic data type of the document/input/output data object
	public SolrUtils(Class<T> dataType, String propFileName) {
		
		// Important must have.
		this.DATA_TYPE = dataType;

		// read properties for Solr server info
		prop = getProperties(propFileName);
		this.solrUrl = prop.getProperty("solr.url");
		solrClient = createSolrClient(solrUrl);
		searchFields = new ArrayList<String>();
	}
	
	// generic add data to the Solr collection where data can be any java objects
	public <T> void insert(T object) {
		try {
			UpdateResponse response = solrClient.addBean(object); 
			solrClient.commit();
		} catch (IOException e) {
			LOG.error(e.getMessage());
		} catch (SolrServerException e) {
			LOG.error(e.getMessage());
		}
	}
	
	/*
	 *  generic search the Solr collection
	 *  Required: searchFields array must be populated (ex. "name", "occupation")
	 *  Input: query string of key:value, example "name:Tom~"
	 *  Output: list of type <T>
	 */	
	public <T> List<T> findAll(String query) {
		List<T> retList = null;
		if (isValidSearchFields()) {
			SolrQuery sq = new SolrQuery(query);
			QueryResponse response = null;
			try {
				response = solrClient.query(sq);
			} catch (SolrServerException e) {
				LOG.error(e.getMessage());
			} catch (IOException e) {
				LOG.error(e.getMessage());
			}
			// use generic data type from the constructor
			retList = (List<T>) response.getBeans(DATA_TYPE);
		} else {
			LOG.warn("Unable to search because of Undefined searchFields");
		}
		
		return retList;
	}
	
	// -------------------------
	// helper functions
	
	public Properties getProperties(String propFileName) {
		Properties retval = new Properties();
		try {
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			retval.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return retval;
	}
	
	public HttpSolrClient createSolrClient(String solrUrl) {
		
		// create a new client
		HttpSolrClient retval = new HttpSolrClient.Builder(solrUrl).build();
		try {
			XMLResponseParser xparser = new XMLResponseParser();
			retval.setParser(xparser);
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getCause());
		}

		return retval;
	} // end function
	
	/**
	 * In order to query Solr, the searchFields must be defined.
	 * It is a requirement for such query functions as findAll().
	 * @return true if searchFields is populated, false otherwise
	 */
	public boolean isValidSearchFields() {
		return (searchFields.size() > 0);
	}
	

	public HttpSolrClient getSolrClient() {
		return solrClient;
	}

	public void setSolrClient(HttpSolrClient solrClient) {
		this.solrClient = solrClient;
	}

	public Properties getProp() {
		return prop;
	}

	public void setProp(Properties prop) {
		this.prop = prop;
	}

	public ArrayList<String> getSearchFields() {
		return searchFields;
	}

	public void setSearchFields(ArrayList<String> searchFields) {
		this.searchFields = searchFields;
	}

	public String getSolrUrl() {
		return solrUrl;
	}

	public void setSolrUrl(String solrUrl) {
		this.solrUrl = solrUrl;
	}

}
