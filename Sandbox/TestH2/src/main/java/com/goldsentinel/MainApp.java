package com.goldsentinel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainApp {

	public static void main(String[] args) {
		testNewDB();

	}
	public static void testNewDB() {

		Connection conn = null;

		try {
			conn = DriverManager.getConnection("jdbc:h2:~/anh/train/TestH2", "sa", "");
			System.out.println("h2 database TestH2 created...");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// add application code here
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
