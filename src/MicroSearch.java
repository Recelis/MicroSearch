import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
	private static Processing PreProcessing = new Processing();
	static InvertedIndex index = new InvertedIndex();
	static KeyWordSearch search = new KeyWordSearch("KeyWord.txt");
	static VectorSpaceModel vectorSpace;
	
	// data members
	private static int numDocs = 0;
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
		FilesStats outStats = PreProcessing.StatsCall(); // calculates statistics and prepares for write
		
		
//		//Vector Space Section
//		List<String> Terms = PreProcessing.ReturnKeys();
//		int[][] HashArray = PreProcessing.returnHashArray();
//		String[] query = search.returnKeyWords();
//		vectorSpace.VectorRequired(Terms, numDocs, query, HashArray);
//		
//		//loop through documents to build vector space model
//		jj = 0;
//		vfile.DirectContents(DataDirectory+"Out");
//		String[] namesList = vfile.directoryNames;
//		vectorSpace.VectorSpaceLookUp(namesList);
//		for (int ii = 0 ; ii < numDocs; ii++){
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
		
//		outStats.Assignment3Output(vectorSpaceModel, rankedDocs); // text output write 
		outStats.Assignment1Output();
		outStats.write();
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
			numDocs = nfile.directoryNames.length;
		}
		catch (NullPointerException npe) {
				 System.out.println("IOE");
		}
		search.numberOfDocs = numDocs;
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
		vectorSpace = new VectorSpaceModel(numDocs);
		for(int ii = 0; ii < numDocs;ii++){ // loop through each document in directory
			name = nfile.directoryNames[ii];
			nfile.ReadIn(DataDirectory + "/" + name);
			nfile.fileContents.clear(); // clear fileContents for each previous file
			while (jj < nfile.fileArray.length){ // loop over each line in file
				String line = nfile.fileArray[jj];
				line = PreProcessing.ProcessLine(line);
				index.scanForWord(line, name); //build inverted index
				nfile.fileContents.add(line); // add processed line in each file
				jj++;
			}
			jj = 0; //reset line number for new file
			try {
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
		int[][] sortedArray = search.returnRankTable();
		List <String> Keys = search.returnKeys();
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
