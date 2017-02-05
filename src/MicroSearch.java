import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Storage.BackData;


public class MicroSearch {
	/*
	 * Read files in 
	 * then preprocess each line of file. 
	 * 
	 */
	
	// objects
	static Files nfile = new Files();
	static Files vfile = new Files(); // vectorspace files
	static Files common_words_file = new Files();
	static String DataDirectory = "cranfieldDocs";
	static KeyWordSearch search = new KeyWordSearch("KeyWord.txt");
	static VectorSpaceModel vectorSpace;
	// data members
	
	
	/**
	 * ====================
	 * Constructor
	 * 
	 * 	Description
	 * 
	 * ====================	
	 */
	public MicroSearch() {
		
	}
	/**
	 * ====================
	 * run
	 * 
	 * 	Description
	 * Runs Program
	 * ====================	
	 */
	public static void run(){
		readInDocs();
		processAndIndex();
		keywordSearchOp();
		nfile.ProcessStatsCall(); // moves stats info from above functions to BackData 
		
//		//Vector Space Section
//		List<String> Terms = PreProcessing.ReturnKeys();
//		int[][] HashArray = PreProcessing.returnHashArray();
//		String[] query = search.returnKeyWords();
//		vectorSpace.VectorRequired(Terms, BackData.numDocs, query, HashArray);
//		
//		//loop through documents to build vector space model
//		int jj = 0;
//		String name;
//		vfile.DirectContents(DataDirectory+"Out");
//		String[] namesList = vfile.directoryNames;
//		vectorSpace.VectorSpaceLookUp(namesList);
//		for (int ii = 0 ; ii < BackData.numDocs; ii++){
//			name = vfile.directoryNames[ii];
//			vfile.ReadIn(DataDirectory+"Out" + "/" + name);
//			vfile.fileContents.clear(); // clear fileContents for each previous file
//			while (jj < vfile.fileArray.length){ // processing of each line in file
//				String line = vfile.fileArray[jj];
//				vectorSpace.ScanForFreq(line, name);
//				
//				jj++;
//			}
//			jj = 0; //reset line number for new file
//		}
//		String[] rankedDocs = vectorSpace.CosineCalc();
//		double[][] vectorSpaceModel = vectorSpace.VectorSpaceOut(0);
//		
//		outStats.Assignment3Output(vectorSpaceModel, rankedDocs); // text output write 
//		
		//
//		
		writeOut();

	}	
	
	/**
	 * ====================
	 * readInDocs
	 * 
	 * 	Description
	 * reads document names from specified directory into Files object
	 * ====================	
	 */
	public static void readInDocs(){
		try{ 
			nfile.DirectContents(DataDirectory); // get list of directory contents
			BackData.numDocs = nfile.directoryNames.length;
		}
		catch (NullPointerException npe) {
				 System.out.println("IOE");
		}
	}
	
	/**
	 * ====================
	 * readInDocs
	 * 
	 * 	Description
	 * reads and processes each line of document into Files object fileContests 
	 * ====================	
	 */
	public static void processAndIndex(){
		String name;
		int jj = 0; // value for line number in file
		// Vector Space
		vectorSpace = new VectorSpaceModel(BackData.numDocs);
		for(int ii = 0; ii < BackData.numDocs;ii++){ // loop through each document in directory
			name = Files.directoryNames[ii];
			nfile.ReadIn(DataDirectory + "/" + name);
			nfile.fileContents.clear(); // clear fileContents for each previous file
			nfile.processLine(name);
			try { // Write out processed files
				nfile.WriteOut(DataDirectory + "Out", name);				
				} catch (IOException e) {
				System.out.println("FNE");
			}
		}
	}
	/**
	 * ====================
	 * keywordSearchOp
	 * 
	 * 	Description
	 * performs a keyword search
	 * ====================	
	 */
	public static void keywordSearchOp(){
		search.TermAtATime(); // implements keyword search
		search.returnRankTable();
		search.returnKeys();
	}
	/**
	 * ====================
	 * writeOut
	 * 
	 * 	Description
	 * function that performs all of the 
	 * FilesStat info inputs and writes
	 * ====================	
	 */
	public static void writeOut(){
		// may need to change FilesStats so that argument inputs are easier
		FilesStats outStats = new FilesStats("StatisticsProcessing", BackData.numberOfWordsBefore, BackData.numberOfWordsAfter, BackData.vocabBefore, BackData.vocabAfter, BackData.KeysBefore, BackData.hashArrayBefore, BackData.KeysAfter, BackData.hashArrayAfter);
		outStats.Title();
		outStats.Assignment1Output();
		outStats.Assignment2Output(BackData.rankedTable, BackData.Keys);
		outStats.write();
		FilesStats invertedWrite = new FilesStats("InvertedIndex", BackData.numberOfWordsBefore, BackData.numberOfWordsAfter, BackData.vocabBefore, BackData.vocabAfter, BackData.KeysBefore, BackData.hashArrayBefore, BackData.KeysAfter, BackData.hashArrayAfter);
		invertedWrite.Assignment2invertedIndex();
		invertedWrite.write();
	}
	
	
	/**
	 * ====================
	 * main
	 * 
	 * 	Description
	 * calls run
	 * ====================	
	 */
	public static void main(String args[]) {
		long elaspedTime;
		long startTime = System.currentTimeMillis();
		System.out.println("\nMicroSearch - a new search engine using old techniques");
		run();
		long stopTime = System.currentTimeMillis();
		elaspedTime = stopTime - startTime; // calculates execution time
		System.out.println("top ten results in " + elaspedTime+"milliseconds");
				
	}

}
