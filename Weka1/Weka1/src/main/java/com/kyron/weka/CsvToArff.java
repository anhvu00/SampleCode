package com.kyron.weka;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;

import weka.core.Instances;
import weka.core.converters.CSVLoader;

public class CsvToArff {


	/**
	 * takes 2 arguments:
	 * - CSV input file
	 * - ARFF output file
	 */
	public static void main(String[] args) throws Exception {
		String csv = "qryhome.csv";
		String arff = "c:/temp/out.arff";
		File outfile = new File(arff);
		if (outfile != null && outfile.exists()) {
			outfile.delete();
		}
		FileWriter fw = new FileWriter(outfile);

		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(csv);
		// load CSV
		CSVLoader loader = new CSVLoader();
		loader.setSource(is);
		Instances data = loader.getDataSet();

		// save ARFF
		BufferedWriter writer = new BufferedWriter(fw);
		writer.write(data.toString());
		writer.flush();
		writer.close();
	}

}


