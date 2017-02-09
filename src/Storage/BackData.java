package Storage;

import java.util.List;

// this class is used to place data that will be used by multiple classes
public class BackData {

	public BackData() {
		// TODO Auto-generated constructor stub
	}
	static public int numDocs = 0;
	static public String[] directoryNames;
	static public int[][] rankedTable;
	static public List <String> Keys;
	static public long numberOfWordsBefore;
	static public long numberOfWordsAfter;
	static public long vocabBefore;
	static public long vocabAfter;
	static public int[][] hashArrayBefore;
	static public int[][] hashArrayAfter;
	static public List<String> KeysBefore;
	static public List<String> KeysAfter;
	static public String[] query; // prev keyWords
	static public int numRetrieve = 100;
}
