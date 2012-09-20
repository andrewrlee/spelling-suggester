package uk.co.optimisticpanda.speller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;

public enum Utils {
;	

public static List<Object> readTestDataIntoParams(File testFile) throws IOException {
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

}
