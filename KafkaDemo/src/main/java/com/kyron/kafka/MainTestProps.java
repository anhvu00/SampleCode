package com.kyron.kafka;

import java.util.Properties;

import com.kyron.util.MyFileUtils;

/*
 * Test read resource properties file
 */
public class MainTestProps {

	public static void main(String[] args) {
		MyFileUtils util = new MyFileUtils();
		// read resource properties
		Properties props = util.readResourceProps("demo.properties");
		util.printProps(props);
	}

}
