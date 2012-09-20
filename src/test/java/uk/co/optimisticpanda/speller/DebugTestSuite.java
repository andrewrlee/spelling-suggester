package uk.co.optimisticpanda.speller;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class DebugTestSuite {

	private static Speller speller;
	private static File testFile = new File("src/test/resources/test1.txt");
	private final String correctAnswer;
	private final String input;

	@BeforeClass
	public static void setup() throws IOException {
		speller = new Speller("src/main/resources/big.txt");
	}

	public DebugTestSuite(String input, String correctAnswer) {
		this.correctAnswer = correctAnswer;
		this.input = input;
	}

	@Parameters
	public static Collection<Object> values() throws IOException {
		return Utils.readTestDataIntoParams(testFile);
	}

	@Test
	public void check() {
		String answer = speller.suggestCorrection(input);
		assertThat("input:" + input + " gave: " + answer
				+ " rather than the correct Answer:" + correctAnswer, //
				answer, is(correctAnswer));
	}

}
