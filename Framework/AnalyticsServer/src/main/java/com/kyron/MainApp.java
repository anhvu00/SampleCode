package com.kyron;

import com.kyron.dictionary.DictionaryService;

public class MainApp {

	public static void main(String[] args) {

		System.out.println("Server started.");
		
		// get a service
		DictionaryService dictionary = DictionaryService.getInstance();
		
		System.out.println("*** Look up dictionary ***");
		System.out.println(lookup(dictionary, "book"));
		System.out.println(lookup(dictionary, "editor"));
		System.out.println(lookup(dictionary, "xml"));
		System.out.println(lookup(dictionary, "REST"));
		
		// another function call
		System.out.println("*** Available engines ***");
		dictionary.getAll().forEach(name -> System.out.println(name));
		
		// send to a specific dictionary
		System.out.println("*** sendRequest(GENERAL-DICTIONARY, book) ***");
		dictionary.sendRequest("GENERAL-DICTIONARY", "book");
	}

	// use that service
	public static String lookup(DictionaryService dictionary, String word) {
		String outputString = word + ": ";
		String definition = dictionary.getDefinition(word);
		if (definition == null) {
			return outputString + "Cannot find definition for this word.";
		} else {
			return outputString + definition;
		}
	}

}
