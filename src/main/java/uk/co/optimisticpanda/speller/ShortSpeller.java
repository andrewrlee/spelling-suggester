package uk.co.optimisticpanda.speller;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.isEmpty;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import com.google.common.base.Charsets;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import static com.google.common.collect.Iterables.*;

public class ShortSpeller {

	private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
	private final HashMultiset<String> words;

	public ShortSpeller(String trainingText) throws IOException {
		String txt = Files.toString(new File(trainingText), Charsets.UTF_8).toLowerCase();
		Iterable<String> words = Splitter.onPattern("[^a-z]+").omitEmptyStrings().trimResults().split(txt);
		this.words = HashMultiset.create(words);
	}
	
	Collection<String> editsWithErrorDistanceOf1(String word) {
		Set<String> edits = Sets.newHashSet();
		//deletions
		for (int i = 0; i < word.length(); i++) {
			edits.add(word.substring(0, i) + word.substring(i + 1, word.length()));
		}
		//transpositions
		for (int i = 0, j = 1; j <= word.length() - 1; i++, j++) {
			edits.add(word.substring(0, i) + word.charAt(j) + word.charAt(i) + word.substring(j + 1));
		}
		if (!word.trim().isEmpty()) {
			//insertions
			for (int i = 0; i < word.length() + 1; i++) {
				for (int j = 0; j < ALPHABET.length(); j++) {
					edits.add(word.substring(0, i) + ALPHABET.charAt(j) + word.substring(i));
				}
			}
			//replacements
			for (int i = 0; i < word.length(); i++) {
				for (int j = 0; j < ALPHABET.length(); j++) {
					edits.add(word.substring(0, i) + ALPHABET.charAt(j) + word.substring(i + 1, word.length()));
				}
			}
		}
		return edits;
	}

	Iterable<String> editsWithErrorDistanceOf2(String word) {
		Collection<String> editsWithErrorDistance2 = Sets.newHashSet();//
		for (String edit : editsWithErrorDistanceOf1(word)) {
			addAll(editsWithErrorDistance2, editsWithErrorDistanceOf1(edit));
		}
		return editsWithErrorDistance2;
	}

	public String suggestCorrection(String word) {
		Iterable<String> edits = Collections.emptySet();

		if (words.contains(word)) {
			// Assume the word is correct
			edits = Collections.singleton(word);
		}

		if (isEmpty(edits)) {
			edits = known(editsWithErrorDistanceOf1(word));
		}
		if (isEmpty(edits)) {
			edits = known(editsWithErrorDistanceOf2(word));
		}

		HashMap<Integer, String> map = Maps.newHashMap();
		for (String edit : edits) {
			// We don't care if more than one result has the same frequency
			map.put(words.count(edit), edit);
		}
		return map.isEmpty() ? "" : map.get(Collections.max(map.keySet()));
	}
	
	Iterable<String> known(Iterable<String> edits) {
		return filter(edits, new Predicate<String>() {
			public boolean apply(String input) {
				return words.contains(input);
			}
		});
	}
}
