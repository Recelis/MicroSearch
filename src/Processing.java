import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

/**
 * ====================
 * Constructor
 * 
 * 	Description
 * Read in the common_words.txt file and tokenises it.
 * ====================	
 */
public class Processing {
	private String newline = System.getProperty("line.separator");
	private Files CommonWords = new Files(); 
	static FilesStats outStats;
	private static Statistics Stats1 = new Statistics("Stats1");
	private static Statistics Stats2 = new Statistics("Stats2");
	int[][] HashArray2out;
	public Processing() {
		CommonWords.ReadIn("common_words.txt");
		TokenizeCommonWords();
	}
	/**
	 * ====================
	 * TokenizesCommonWords
	 * 	 
	 * 	Description
	 * 	Takes common words, reads into Class data member CommonWords.fileArray. 
	 * 	Choose bigger token if common words contain a delimiter.
	 * 	Remove returnLine character at end of line.
	 * ====================	
	 */	
	public void TokenizeCommonWords(){
		int jj = 0; 
		while (jj < CommonWords.fileArray.length){
			String line = CommonWords.fileArray[jj];
			line = RemoveSGML(line);
			line = Tokenizing(line);
			String[] splitResult = line.split("\\W"); //if delimiters == 2
			if (splitResult.length > 1){
				if (splitResult[0].length() > splitResult[1].length()) line = splitResult[0]; //if separated by comma
				else line = splitResult[1]; 
			}
			CommonWords.fileArray[jj] = line;
			jj++;
		}
	}
	/**
	 * ====================
	 * ProcessLine
	 * 	 
	 * 	Description
	 * 	Abstraction method that is used to execute all Preprocessing in one go
	 * ====================	
	 */
	public String ProcessLine(String line){
		line = RemoveSGML(line);
		line = Tokenizing(line);
		Stats1.loopLine(line); // check statistics words before StopWords are Removed
		line = StopWordsRemove(line);
		Stats2.loopLine(line); // check statistics words after StopWords are Removed
		return line;
	}
	
	/**
	 * ====================
	 * StatsCall
	 * 	 
	 * 	Description
	 * 	writes all of the output statistics processing text
	 * ====================	
	 */
	public FilesStats StatsCall(){
		long TNOW1 = Stats1.ReturnTotalNumberOfWords();
		long TNOW2 = Stats2.ReturnTotalNumberOfWords();
		long vocab1 = Stats1.returnVocab();
		long vocab2 = Stats2.returnVocab();
		int[][] HashArray = Stats1.sortStats();
		int [][] HashArray2 = Stats2.sortStats();
		HashArray2out = HashArray2;
		String statsFileName = "StatisticsProcessing";
		outStats = new FilesStats(statsFileName, TNOW1, TNOW2, vocab1, vocab2, Stats1.Keys, HashArray, Stats2.Keys, HashArray2);
		outStats.Title();
		FilesStats invertedWrite = new FilesStats("InvertedIndex", TNOW1, TNOW2, vocab1, vocab2, Stats1.Keys, HashArray, Stats2.Keys, HashArray2);
		invertedWrite.Assignment2invertedIndex();
		invertedWrite.write();
		return outStats;
	}
	
	public int[][] returnHashArray(){
		return HashArray2out;
	}
	/**
	 * ====================
	 * ReturnKeys
	 * 	 
	 * 	Description
	 * 	returns a list of the most keys from most frequent to least
	 * ====================	
	 */	
	public List<String> ReturnKeys(){
		return Stats2.Keys;
	}
	/**
	 * ====================
	 * RemoveSGML
	 * 	 
	 * 	Description
	 * 	Removes SGML tags in line, assume that tags occupy an entire line
	 * ====================	
	 */		
	public String RemoveSGML(String line){ // if detect '<' and '>' characters, then skip the line
		String returnLine = line; // local line for manipulation
		int rightArrow =-1; // location of right arrow
		int leftArrow =-1; // location of left arrow
		leftArrow = returnLine.indexOf('<');// Detect < character 
		rightArrow = returnLine.indexOf('>');// detect > character
		if (leftArrow >= 0 && rightArrow >= 1){
			returnLine = "";
		} 
		return returnLine;
	}
	/**
	 * ====================
	 * Tokenizing
	 * 	 
	 * 	Description
	 * 	Removes delimiters in line.
	 * ====================	
	 */	
	public String Tokenizing(String line){
		String locLine = line;
		String returnLine = "";
		int ii = 0;
		boolean firstTime = true;
		if(locLine == newline || locLine == "\n") return null;
		String[] tokens = locLine.split("[\\W]");
		while(ii < tokens.length){
			boolean check = tokens[ii].matches("[a-zA-Z0-9]+"); 
			if (check == true) {
				returnLine =  returnLine + " " + tokens[ii];
			}
			ii++;
		}
		returnLine = returnLine.toLowerCase(); // make tokens lower case
		return returnLine;
	}
	
	/**
	 * ====================
	 * StopWordsRemove
	 * 	 
	 * 	Description
	 * 	Removes stopwords in common_words.txt by Comparing each word in line with stopwords.
	 * ====================	
	 */	
	public String StopWordsRemove(String line) throws StringIndexOutOfBoundsException{
		if (line.equals("")) return line;
		String checkString = line; //add a space to front to make it easier to check against true instances of stop word
		String returnLine = "";
		int ii = 0;
		int wordCount = 0;
		boolean tokensEqual = false;
		String[] tokens = checkString.split("\\W");
		while(wordCount < tokens.length){ // check through each word in line
			ii = 0;
			while (ii < CommonWords.fileContents.size()){ // check through stop words in CommonWords.fileArray
				tokensEqual = tokens[wordCount].equals(CommonWords.fileArray[ii]);// check tokens against common words
				if (CommonWords.fileArray[ii].length() == 1 && tokens[wordCount].length() != 1){// do not match with single letter stop word if letter is part of word
					tokensEqual = false;
				}
				if (tokensEqual == true){
					break;
				} else{
					ii++;
				}
			}
			if (tokensEqual == true) {
				wordCount++;
				continue;
			} else{
			returnLine = returnLine + " " + tokens[wordCount];
			wordCount++;
			}
		}
		return returnLine; 
	}
	
}
