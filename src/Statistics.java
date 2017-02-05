
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import HashArray.SortingHashArray;

public class Statistics {
	Hashtable<String, Integer> WordFreq = new Hashtable<String, Integer>(); // Hash table contains key and value
	public long TotalNumberWords = 0;
	public int VocabSize = 0;
	private String id;
	public List<String> Keys;
	public static String newline = System.getProperty("line.separator");
	/**
	 * ====================
	 * Constructor
	 * 
	 * 	Description
	 * saves id of object
	 * ====================	
	 */
	public Statistics(String id) {
		// builds a data structure which counts number of instances word has come up
		this.id = id;
		
	}
	/**
	 * ====================
	 * returnID
	 * 	 
	 * 	Description
	 * 	returns the ID of the object
	 * ====================	
	 */	
	public String returnID(){
		return this.id;
	}
	/**
	 * ====================
	 * loopLine
	 * 	 
	 * 	Description
	 * 	Loops through each word in line, adding frequency to Hash Table
	 * 	Also adds up total number of words.
	 * ====================	
	 */	
	public void loopLine(String line){
		String checkString = line;
		String[] tokens = checkString.split("\\s");
		int wordCount = 0;
		int Freq = 0;
		while (wordCount < tokens.length){
			if (tokens[wordCount].equals("") || tokens[wordCount].equals("\n")){
				wordCount++;
				continue;
			}
			if (WordFreq.containsKey(tokens[wordCount])) Freq = RetrieveFreq(tokens[wordCount]);
			WordFreq.put(tokens[wordCount], Freq + 1);
			wordCount++;
			Freq = 0;
			TotalNumberWords++;
		}
	}
	/**
	 * ====================
	 * RetrieveFreq
	 * 	 
	 * 	Description
	 * 	Gets frequency from Hash Table, returns 0 if not found
	 * ====================	
	 */
	private Integer RetrieveFreq (String token){
		Integer n = WordFreq.get(token);
		if (WordFreq != null){
			return n;
		}else{
		n = 0;
		}
		return n;
	}
	/**
	 * ====================
	 * printHash
	 * 	 
	 * 	Description
	 * 	Prints Hash Table out
	 * ====================	
	 */
	public void printHash(){
		Enumeration<String> e = WordFreq.keys();
		Enumeration<Integer> v = WordFreq.elements();
		System.out.println(returnID() + "-------------------------------------------------------------------------------");
		while (e.hasMoreElements()){
			System.out.print(e.nextElement() +" ");
			System.out.println(v.nextElement());
		}
	}
	/**
	 * ====================
	 * ReturnTotalNumberOfWords
	 * 	 
	 * 	Description
	 * 	Returns the TotalNumberOfWords thus seen
	 * ====================	
	 */
	public long ReturnTotalNumberOfWords(){
		System.out.println("Total Number of Words is " + returnID() + ": " + TotalNumberWords);
		return TotalNumberWords;
	}
	/**
	 * ====================
	 * returnVocab
	 * 	 
	 * 	Description
	 * 	Returns the vocabulary size.
	 * ====================	
	 */	
	public long returnVocab(){
		VocabSize = 0;
		Enumeration<String> e = WordFreq.keys();
			while (e.hasMoreElements()){
				e.nextElement();
				VocabSize++;
			}
		VocabSize--; //remove extra space
		System.out.println("Vocabulary Size is: " + VocabSize);	
		return VocabSize;
	}
	/**
	 * ====================
	 * sortsStats
	 * 	 
	 * 	Description
	 * 	sorts the hashtable containing word frequencies.
	 * ====================	
	 */	
	public int[][] sortStats(){
		SortingHashArray sortTheHash = new SortingHashArray(WordFreq, VocabSize);
		Keys = sortTheHash.sortHash();
		return sortTheHash.printArray(0);
	}

}
