
import java.util.Hashtable;

import Storage.BackData;

public class InvertedIndex {
	public static Hashtable<String, Hashtable<String, Integer>> invertedIndex;
	/**
	 * ====================
	 * InvertedIndex
	 * 
	 * 	Description
	 * Creates an InvertedIndex
	 * ====================	
	 */
	public InvertedIndex() {
		try{ 
		invertedIndex = new Hashtable<String, Hashtable<String, Integer>>();// creates hash table
		} catch(IllegalArgumentException iae){
			System.out.println("Failed to initialise Inverted Index");
		}
	}
	/**
	 * ====================
	 * scanForWord
	 * 
	 * 	Description
	 * Loops through each word in line 
	 * ====================	
	 */
	public void scanForWord(String newLine, String name){
		String[] tokens = newLine.split("\\s");
		int wordCount = 0;
		int currentValue = 0;
		while(wordCount < tokens.length){ // check through each word in line
			
			if (!tokens[wordCount].matches("[a-zA-Z0-9]+")) {
				wordCount++;
				continue;
			} else{
				checkForWord(tokens[wordCount], name);
				wordCount++;
				currentValue = BackData.wordFreqDoc.get(name); // build HashMap of doc word frequency
				BackData.wordFreqDoc.put(name, currentValue++);
			}
		}
		//System.out.println(invertedIndex.toString());
	}
	/**
	 * ====================
	 * checkForWord
	 * 
	 * 	Description
	 *  adds word to invertedIndex or makes new addition
	 * ====================	
	 */
	private void checkForWord(String token, String name){
		if (token.isEmpty()) return;
		if (invertedIndex.containsKey(token)){ // if word exists get frequency of word and add one 
			Hashtable<String, Integer> hashObjects = new Hashtable<String, Integer>();
			Integer value = 0;
			hashObjects = invertedIndex.get(token);
			if (!hashObjects.containsKey(name)) value = 0;
			else{
				value = hashObjects.get(name);
			}
			value++;
			hashObjects.put(name, value);
			invertedIndex.put(token, hashObjects);
		} else{ // if word doesn't exist, make new key
			
			Hashtable<String, Integer> hashObjects = new Hashtable<String, Integer>();
			hashObjects.put(name, 1);
			invertedIndex.put(token, hashObjects);
		}
	}
	
	
}
