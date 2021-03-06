

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import HashArray.SortingHashArray;
import Storage.BackData;

public class KeyWordSearch {
	private Files keyWordIn = new Files();
	public String filename;
	public static Hashtable<String, Integer> RankingTable = new Hashtable<String, Integer>();
	int[][] finalRankingTable;
	SortingHashArray rankDocuments;
	/**
	 * ====================
	 * Constructor
	 * 	 
	 * 	Description
	 * 	reads the Keywords in Keyword.txt
	 * turns keywords to lowercase
	 * ====================	
	 */
	public KeyWordSearch(String filename) {
		keyWordIn.ReadIn(filename);
		BackData.query = keyWordIn.fileArray[0].split("[\\W]");
		System.out.println("You are searching for today... ");
		for (int ii = 0; ii <BackData.query.length; ii++){
			BackData.query[ii] = BackData.query[ii].toLowerCase(); // convert Keywords to Lower Case
			System.out.print(BackData.query[ii] + " ");
		}
	}
	/**
	 * ====================
	 * returnKeywords()
	 * 	 
	 * 	Description
	 * 	implements TermAtATime
	 * ====================	
	 */
	public String[] returnKeyWords(){
		return BackData.query;
	}
	/**
	 * ====================
	 * TermAtATime()
	 * 	 
	 * 	Description
	 * 	implements TermAtATime
	 * ====================	
	 */
	public void TermAtATime(){
		for (String word:BackData.query){ //word:(name:freq) (name:freq) ...etc
			if (!InvertedIndex.invertedIndex.containsKey(word)) { // if InvertedIndex does not have query word, skip
				continue;
			}
			else{
				Enumeration<String> docList = InvertedIndex.invertedIndex.get(word).keys();
				if (RankingTable.isEmpty()){
					RankingTable = InvertedIndex.invertedIndex.get(word); // start building term at a time table
				} else{	
					while (docList.hasMoreElements()){ //(name:freq)
						String docName = docList.nextElement();
						Integer invertValue = InvertedIndex.invertedIndex.get(word).get(docName); // found value
						Integer searchedValue = RankingTable.get(docName);
						if (RankingTable.containsKey(docName)){ //compare and add freq together
							RankingTable.put(docName, invertValue+searchedValue);
						} else{
							RankingTable.put(docName, invertValue); // create new key:value pair
						}
					}	
				}
			}	
		}
		RankDocs();
	}
	/**
	 * ====================
	 * RankDocs()
	 * 	 
	 * 	Description
	 * 	uses SortingHashArray to sort the ranking table so that most relevant values are first
	 * ====================	
	 */
	public void RankDocs(){
		rankDocuments = new SortingHashArray(RankingTable, RankingTable.size()); // sort the rank table
		rankDocuments.sortHash();
		finalRankingTable = rankDocuments.printArray(0); 
	}
	/**
	 * ====================
	 * returnRankTable()
	 * 	 
	 * 	Description
	 * 	returns finalRankingTable
	 * ====================	
	 */
	public void returnRankTable(){
		BackData.rankedTable = finalRankingTable;
	}
	/**
	 * ====================
	 * returnKeys()
	 * 	 
	 * 	Description
	 * 	returns the Strings names for each corresponding value in finalRankingTable
	 * ====================	
	 */
	public void returnKeys(){
		BackData.Keys = rankDocuments.Keys;
	}
}
