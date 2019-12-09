package com.kyron;

import org.apache.commons.lang3.StringUtils;

public class MainApp {

	public static void main(String[] args) {
		String name = "Anh";
		String msg = StringUtils.join("Hello ", name, " Dependencies copied and Executable jar ran.");
		System.out.println(msg);

	}

}
