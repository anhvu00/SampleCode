package com.kyron.dictionary;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import com.kyron.dictionary.spi.Dictionary;

public class DictionaryService {
	
    private static DictionaryService service;
    private ServiceLoader<Dictionary> loader;
    
    private static final int DONE = 0;
    private static final int ERROR = 1;
    private static final int IN_PROGRESS = 2;
    private static final int NO_SERVICE = 3;
    
    private static HashMap<String,Dictionary> enginesMap;

    private DictionaryService() {
        loader = ServiceLoader.load(Dictionary.class);
        // initialize enginesMap. Must be refreshed on a schedule.
        enginesMap = getEnginesMap();
    }

    public static synchronized DictionaryService getInstance() {
        if (service == null) {
            service = new DictionaryService();
        }
        return service;
    }

    // processs...
    public String getDefinition(String word) {
        String definition = null;

        try {
        	// loop through a list of Dictionary to get the definition
            Iterator<Dictionary> dictionaries = loader.iterator();
            while (definition == null && dictionaries.hasNext()) {
                Dictionary d = dictionaries.next();
                definition = d.getDefinition(word);
            }
        } catch (ServiceConfigurationError serviceError) {
            definition = null;
            serviceError.printStackTrace();

        }
        return definition;
    }
    
    // new function to get the list 
    public List<String> getAll() {
    	ArrayList<String> retval = new ArrayList<String>();
        try {
        	// loop through a list of Dictionary to get the definition
            Iterator<Dictionary> dictionaries = loader.iterator();
            while (dictionaries.hasNext()) {
                Dictionary d = dictionaries.next();
                retval.add(d.getType());
            }
        } catch (ServiceConfigurationError serviceError) {
            serviceError.printStackTrace();

        }   	
    	return retval;
    }
    
    // new function to get a map of <name>, <object>. Should be called on a schedule to refresh class variable
    public HashMap<String,Dictionary> getEnginesMap() {
    	HashMap<String,Dictionary> retval = new HashMap<String,Dictionary>();
        try {
        	// loop through a list of Dictionary to get the definition
            Iterator<Dictionary> dictionaries = loader.iterator();
            while (dictionaries.hasNext()) {
                Dictionary d = dictionaries.next();
                retval.put(d.getType(), d);
            }
        } catch (ServiceConfigurationError serviceError) {
            serviceError.printStackTrace();

        }   	
    	return retval;
    }
    
    // new function to send request to a particular engine
    public int sendRequest(String type, String data) {
    	int status = DONE;
    	if (enginesMap.isEmpty()) {
    		status = NO_SERVICE;
    	} else {
    		if (enginesMap.containsKey(type)) {
    			Dictionary d = enginesMap.get(type);
    			String result = d.getDefinition(data);
    			System.out.println("TYPE="+type + ", output=" + result);
    		}
    	}
    	return status;
    }
}