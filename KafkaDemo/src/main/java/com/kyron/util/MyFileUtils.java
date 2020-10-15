package com.kyron.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MyFileUtils {
	// get a file from the resources folder
	// works everywhere, IDEA, unit test and JAR file.
	public InputStream getFileFromResourceAsStream(String fileName) {

		// The class loader that loaded the class
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream(fileName);

		// the stream holding the file content
		if (inputStream == null) {
			throw new IllegalArgumentException("File not found! " + fileName);
		} else {
			return inputStream;
		}
	}
	
	public Properties readResourceProps(String fileName) {
		Properties prop = new Properties();
		InputStream is = getFileFromResourceAsStream(fileName);
		try {
			prop.load(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}
	
	public void printProps(Properties prop) {
		for (Object key: prop.keySet()) {
			System.out.println(key + ": " + prop.getProperty(key.toString()));
		}
	}
}
