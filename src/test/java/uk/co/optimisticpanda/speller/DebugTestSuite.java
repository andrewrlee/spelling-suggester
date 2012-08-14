package uk.co.optimisticpanda.speller;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;

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

	@Parameters()
	public static Collection<Object> values() throws IOException {
		return Files.readLines(testFile, Charsets.UTF_8, new LineProcessor<List<Object>>() {
			List<Object> result = Lists.newArrayList();

			public boolean processLine(String line) throws IOException {
				Iterable<String> split = Splitter.on(':').trimResults().omitEmptyStrings().split(line);
				String first = Iterables.getFirst(split, null);
				for (String input : Splitter.on(' ').trimResults().omitEmptyStrings().split(Iterables.getLast(split))) {
					result.add(new Object[] { input, first });
				}
				return true;
			}

			public List<Object> getResult() {
				return result;
			}
		});
	}

	@Test
	public void check() {
		String answer = speller.suggestCorrection(input);
		assertThat("input:" + input +  " gave: " +  " rather than the correct Answer:" + correctAnswer , 
				answer, is(correctAnswer));
	}

}
