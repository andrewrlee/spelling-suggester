package uk.co.optimisticpanda.speller;

import static com.google.common.collect.Iterables.size;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

public class SpellerTest {

	private static Speller speller;

	@BeforeClass
	public static void setup() throws IOException {
		speller = new Speller("src/main/resources/big.txt");
	}

	@Test
	public void getDeletions() {
		Iterable<String> deletions = speller.getDeletions("test");
		assertThat("Should be n items for a word of n length", size(deletions), is("test".length()));
		assertThat(deletions, hasItems("tes", "tet", "tst", "est"));

		deletions = speller.getDeletions("");
		assertThat("Should be empty", size(deletions), is(0));
	}

	@Test
	public void getTranspostions() {
		Iterable<String> transpositions = speller.getTranspositions("test");
		assertThat("Should be n-1 items for a word of n length", size(transpositions), is("test".length() - 1));
		assertThat(transpositions, hasItems("tets", "tset", "etst"));

		transpositions = speller.getTranspositions("");
		assertThat("Should be empty", size(transpositions), is(0));
	}

	@Test
	public void getAlterations() {
		Iterable<String> alterations = speller.getAlterations("cat");
		assertThat("Should be 26(n) items for a word of n length", size(alterations), is("cat".length() * 26));
		assertThat(
				alterations,
				hasItems("aat", "bat", "cat", "dat", "eat", "fat", "gat", "hat", "iat", "jat", "kat", "lat", "mat", "nat", "oat", "pat", "qat", "rat", "sat", "tat", "uat", "vat",
						"wat", "xat", "yat", "zat", "cat", "cbt", "cct", "cdt", "cet", "cft", "cgt", "cht", "cit", "cjt", "ckt", "clt", "cmt", "cnt", "cot", "cpt", "cqt", "crt",
						"cst", "ctt", "cut", "cvt", "cwt", "cxt", "cyt", "czt", "caa", "cab", "cac", "cad", "cae", "caf", "cag", "cah", "cai", "caj", "cak", "cal", "cam", "can",
						"cao", "cap", "caq", "car", "cas", "cat", "cau", "cav", "caw", "cax", "cay", "caz"));

		alterations = speller.getAlterations("");
		assertThat("Should be empty", size(alterations), is(0));
	}

	@Test
	public void getInsertions() {
		Iterable<String> insertions = speller.getInsertions("cat");
		assertThat("Should be 26(n+1) items for a word of n length", size(insertions), is(("cat".length() + 1) * 26));
		assertThat(
				insertions,
				hasItems("acat", "bcat", "ccat", "dcat", "ecat", "fcat", "gcat", "hcat", "icat", "jcat", "kcat", "lcat", "mcat", "ncat", "ocat", "pcat", "qcat", "rcat", "scat",
						"tcat", "ucat", "vcat", "wcat", "xcat", "ycat", "zcat", "caat", "cbat", "ccat", "cdat", "ceat", "cfat", "cgat", "chat", "ciat", "cjat", "ckat", "clat",
						"cmat", "cnat", "coat", "cpat", "cqat", "crat", "csat", "ctat", "cuat", "cvat", "cwat", "cxat", "cyat", "czat", "caat", "cabt", "cact", "cadt", "caet",
						"caft", "cagt", "caht", "cait", "cajt", "cakt", "calt", "camt", "cant", "caot", "capt", "caqt", "cart", "cast", "catt", "caut", "cavt", "cawt", "caxt",
						"cayt", "cazt", "cata", "catb", "catc", "catd", "cate", "catf", "catg", "cath", "cati", "catj", "catk", "catl", "catm", "catn", "cato", "catp", "catq",
						"catr", "cats", "catt", "catu", "catv", "catw", "catx", "caty", "catz"));

		insertions = speller.getInsertions("");
		assertThat("Should be empty", size(insertions), is(0));
	}

	@Test
	public void getEdits() {
		assertThat(speller.editsWithErrorDistanceOf1("something").size(), is((54 * "something".length()) + 25));
	}

	@Test
	public void getAllEditsWithADistanceOf2() {
		assertThat(speller.editsWithErrorDistanceOf2("something").size(), is(114324));
	}
	
	@Test
	public void getKnownEdits() {
		Iterable<String> knownEdits = speller.known(speller.editsWithErrorDistanceOf2("something"));
		assertThat(size(knownEdits), is(4));
		assertThat(knownEdits, hasItems(
				"soothing","smoothing","seething","something"));
	}

	@Test
	public void getSuggestion() {
		assertThat(speller.suggestCorrection("something"), is("something"));
		assertThat(speller.suggestCorrection("somethi"), is("something"));
		assertThat(speller.suggestCorrection("omethin"), is("something"));
		assertThat(speller.suggestCorrection("somthin"), is("something"));
		assertThat(speller.suggestCorrection("sumthin"), is("sustain"));
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println(new Speller("src/main/resources/big.txt").suggestCorrection("trajedy"));
	}


}
