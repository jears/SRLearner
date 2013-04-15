package tfidf;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * Class that represents a text document
 * Keeps track of the number of times a word appears in the document, it's term frequency, and 
 * eventually, its inverse document frequency (if used with TfIdf) for finding
 * important keywords in the document
 * 
 * 
 * @author Barkin Aygun
 *
 */
public class Document {
	/**
	 * double[0]:n_ij	double[1]:tf_ij	double[2]:tf_idf
	 */
	public TreeMap<String, Double[]> words; // n_ij, n_ij, tf_idf
	/**
	 * total count of tokens of all words
	 */
	int sumof_n_kj;
	/**
	 * the length of the vector space model using tf-idf
	 */
	double vectorlength;
	
	/**
	 * Constructor for document class that is used by the parent TfIdf class
	 * @param br Reader that loaded the text file already, used to read lines from
	 * large documents
	 * @param parent the TfIdf class calling this ctor
	 */
	public Document(BufferedReader br, TfIdf parent) {
		String line;
		String word;
		StringTokenizer tokens;
		sumof_n_kj = 0;
		vectorlength = 0;
		Double[] tempdata;
		words = new TreeMap<String, Double[]>();
		
		//count words one by one, cal the value of n_ij
		try {
			line = br.readLine();
			while (line != null) {
				tokens = new StringTokenizer(line, ":; \"\',.[]{}()!?-/");
				while(tokens.hasMoreTokens()) {
					word = tokens.nextToken().toLowerCase().trim();
					if (word.length() < 1) continue;
					if (words.get(word) == null) {
						tempdata = new Double[]{1.0,0.0,0.0};
						words.put(word, tempdata);
					}
					else {
						tempdata = words.get(word);
						tempdata[0]++;
						words.put(word,tempdata);
					}
					sumof_n_kj++;
				}
				line = br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Iterate through the words to fill their tf's
		for (Iterator<String> it = words.keySet().iterator(); it.hasNext(); ) {
			word = it.next();
			tempdata = words.get(word);
			tempdata[1] = tempdata[0] / (float) sumof_n_kj;
			words.put(word,tempdata);
			
			//update the info of tfidf.allwords
			parent.addWordOccurence(word);
		}		
	}
	
	/**
	 * Calculates the tfidf of the words after called by the parent TfIdf class
	 * @param parent the TfIdf class
	 */
	public void calculateTfIdf(TfIdf parent) {
		String word;
		Double[] corpusdata;
		Double[] worddata;
		double tfidf;
		for (Iterator<String> it = words.keySet().iterator(); it.hasNext(); ) {
			word = it.next();
			corpusdata= parent.allwords.get(word);
			worddata = words.get(word);
			tfidf = worddata[1] * corpusdata[1];
			worddata[2] = tfidf;
			vectorlength += tfidf * tfidf;
			words.put(word, worddata);
		}
		vectorlength = Math.sqrt(vectorlength);
	}
	
	/**
	 * Prints every word in the document with their information:
	 * <ul> 
	 * <li>number of times word appears
	 * <li>Word's term frequency (number of times it appears / total word count)
	 * <li>Word's inverse document frequency (specifically how important it is in this document)
	 * </ul> 
	 */
	public void printData() {
		String word;
		Double[] td;
		for (Iterator<String> it = words.keySet().iterator(); it.hasNext(); ) {
			word = it.next();
			td = words.get(word);
			System.out.println(word + "\t" + td[0] + "\t" + td[1] + "\t" + td[2]);
		}
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String word;
		Double[] td;
		for (Iterator<String> it = words.keySet().iterator(); it.hasNext(); ) {
			word = it.next();
			td = words.get(word);
			sb.append(word + "\t" + td[0] + "\t" + td[1] + "\t" + td[2] + "\n");
		}
		return sb.toString();
	}
	
	/**
	 * Gives the most important numWords words
	 * @param numWords Number of words to return
	 * @return String array of words
	 */
	public String[] bestWordList(int numWords) {
		SortedMap<String, Double[]> sortedWords = new TreeMap<String, Double[]>(new Document.ValueComparer(words));
		
		System.out.println(words.keySet());
		sortedWords.putAll(words);
		System.out.println(sortedWords.keySet());
		int counter = 0;
		String[] bestwords = new String[numWords];
		for (Iterator<String> it = sortedWords.keySet().iterator(); it.hasNext() && (counter < numWords); counter++) {
			bestwords[counter] = it.next();
		}
		return bestwords;
	}
	
	/**
	 * Gives the most important numWords words
	 * @param numWords Number of words to return
	 * @return String array of words
	 */
	public double[] bestWordListScore(int numWords) {
		SortedMap<String, Double[]> sortedWords = new TreeMap<String, Double[]>(new Document.ValueComparer(words));
		System.out.println(words.keySet());
		sortedWords.putAll(words);
		System.out.println(sortedWords.keySet());
		int counter = 0;
		double[] bestwords = new double[numWords];
		for (Iterator<String> it = sortedWords.keySet().iterator(); it.hasNext() && (counter < numWords); counter++) {
			bestwords[counter] = words.get(it.next())[2];
		}
		return bestwords;
	}
	
	/**
	 * Override for bestWordList with default number of words of 10
	 * @return String array of best words
	 */
	public String[] bestWordList() {
		return bestWordList(10);
	}
	
	/** inner class to do sorting of the map **/
	private static class ValueComparer implements Comparator<String> {
		private TreeMap<String, Double[]>  _data = null;
		public ValueComparer (TreeMap<String, Double[]> data){
			super();
			_data = data;
		}

         public int compare(String o1, String o2) {
        	 double e1 = ((Double[]) _data.get(o1))[2];
             double e2 = ((Double[]) _data.get(o2))[2];
             if (e1 > e2) return -1;
             if (e1 < e2) return 1;
             if (e1 == e2) {
            	 return o1.compareTo(o2);
             }
             return 0;
         }
	}
	
	public int getKeySetSize() {
		return words.keySet().size();
	}
}
