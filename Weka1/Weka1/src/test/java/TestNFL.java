import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import org.junit.Test;

import weka.classifiers.Evaluation;
import weka.classifiers.rules.OneR;
import weka.core.Instances;


public class TestNFL {

	private InputStream trainFile;
	private Instances trainIns;

	private void init() throws IOException {
		trainFile = this.getClass().getResourceAsStream("testData_RA_SU.arff");
		InputStreamReader fr = new InputStreamReader(trainFile);
		BufferedReader br = new BufferedReader(fr);

		trainIns = new Instances(br);
		// last attribute is the class/type
		trainIns.setClassIndex(trainIns.numAttributes() - 1);
		br.close();
	}

	private void classify() throws Exception {
		// define training data + engine
		OneR oneR = new OneR();
		oneR.buildClassifier(trainIns);

		// Classify with cross-validation 10 folds as recommended
		Evaluation eval = new Evaluation(trainIns);
		eval.crossValidateModel(oneR, trainIns, 10, new Random(1));
		print(eval.toSummaryString("Results\n", true));
		int classIndex = 1;
		print(eval.fMeasure(classIndex) + "," + eval.precision(1) + "," + eval.recall(classIndex));

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

	@Test
	public void test() {
		try {
			init();
			classify();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Done.");
		assertTrue(true);
	}

}
