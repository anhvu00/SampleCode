package com.kyron.weka;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;

public class StartWeka {
	public static final String iris = "C:/Program Files/Weka-3-6/data/iris.arff";
	public static final String glass = "C:/Program Files/Weka-3-6/data/glass.arff";
	public static final String weather_numeric = "C:/Program Files/Weka-3-6/data/weather.numeric.arff";
	public static final String tues = "D:/Anh/workspace_sandbox/weka/train/tuesday.arff";

	public static void main(String[] args) throws Exception {
		FileReader fr = new FileReader(iris);
		BufferedReader br = new BufferedReader(fr);

		Instances trainIns = new Instances(br);
		// last attribute is the class/type
		trainIns.setClassIndex(trainIns.numAttributes() - 1);
		br.close();

		// define training data + engine
		NaiveBayes nB = new NaiveBayes();
		nB.buildClassifier(trainIns);

		// Classify with cross-validation 10 folds as recommended
		Evaluation eval = new Evaluation(trainIns);
		eval.crossValidateModel(nB, trainIns, 10, new Random(1));
		print(eval.toSummaryString("Results\n", true));
		int classIndex = 1;
		print(eval.fMeasure(classIndex) + "," + eval.precision(1) + ","
				+ eval.recall(classIndex));

		print("confusion matrix");
		double[][] ary = eval.confusionMatrix();
		for (int row = 0; row < ary.length; row++) {
			for (int col = 0; col < ary[row].length; col++) {
				System.out.print(ary[row][col] + " , ");
			}
			System.out.println();
		}

	}

	public static void print(String s) {
		System.out.println(s);
	}

}
