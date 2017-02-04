import java.io.IOException;
import java.util.List;

public class FilesStats extends Files{
	long TNOW1out;
	long TNOW2out; 
	long vocab1out;
	long vocab2out; 
	List<String> Keys1out;
	int[][] HashArray1out;
	List<String> Keys2out;
	int[][] HashArray2out;
	String outDirect;
	String fileNameout;
	/**
	 * ====================
	 * Constructor
	 * 
	 * 	Description
	 * Called when File Object is initialised, gets all relevant data into object
	 * ====================	
	 */
	public FilesStats(String fileName, long TNOW1, long TNOW2,long vocab1, long vocab2, List<String> Keys1, int[][] HashArray1, List<String> Keys2, int[][] HashArray2){ // write to stats file
		fileNameout = fileName;
		TNOW1out = TNOW1;
		TNOW2out = TNOW2; 
		vocab1out = vocab1;
		vocab2out = vocab2; 
		Keys1out = Keys1;
		HashArray1out = HashArray1;
		Keys2out = Keys2;
		HashArray2out = HashArray2;
		outDirect = "Data";		
	}
	/**
	 * ====================
	 * Title
	 * 
	 * 	Description
	 * Outputs Title 
	 * ====================	
	 */
	public void Title(){
		fileContents.add("Welcome to MicroSearch, a new search engine using old techniques." + newline);
		fileContents.add("JACKY LUI PROCESSING S4452988" + newline);
	}
	/**
	 * ====================
	 * Assignment1Output
	 * 
	 * 	Description
	 * Outputs for Assignment 1
	 * ====================	
	 */
	public void Assignment1Output(){
		fileContents.add("Statistics 1:" +newline + newline);
		fileContents.add("The Total Number of Words is " + (int) TNOW1out + newline + newline);
		fileContents.add("The Vocabulary is: " + vocab1out + newline + newline);
		fileContents.add("Top 50 words before StopWordRemoval: " + newline);
		int ii =0;
		while(ii < 50){
			fileContents.add(Keys1out.get(HashArray1out[ii][0]) + " " + HashArray1out[ii][1]+ newline);
			ii++;
		}
		fileContents.add("------------------------------------------------------------------------------------------------------------------------------------" + newline);
		fileContents.add("Statistics 2:" + newline + newline+newline);
		fileContents.add("The Total Number of Words is " + (int) TNOW2out + newline + newline);
		fileContents.add("The Vocabulary is: " + vocab2out + newline+newline);
		fileContents.add("Top 50 words after StopWordRemoval: "+ newline);
		ii = 0;
		while(ii < 50){
			System.out.print(Keys2out.get(HashArray2out[ii][0])+" ");
			System.out.println(HashArray2out[ii][1]);
			fileContents.add(Keys2out.get(HashArray2out[ii][0]) + " " + HashArray2out[ii][1]+ newline);
			ii++;
		}
	}
	/**
	 * ====================
	 * Assignment2Output
	 * 
	 * 	Description
	 * Outputs for Assignment 2
	 * ====================	
	 */
	public void Assignment2Output(int[][] rankTable, List<String> Keys){
		fileContents.add("------------------------------------------------------------------------------------------------------------------------------------" + newline);
		fileContents.add("The top 10 or less most relevent files are"+newline);
		int n = Math.min(rankTable.length, 10);
		for(int ii =0; ii < n; ii++){
			String sortedRankLine = "file: " + Keys.get(rankTable[ii][0]) + " frequency: " + rankTable[ii][1];
			System.out.println(sortedRankLine);
			fileContents.add(sortedRankLine + newline);
		}
	}
	/**
	 * ====================
	 * Assignment2invertedIndex
	 * 
	 * 	Description
	 * Outputs a completed Inverted Index
	 * ====================	
	 */
	public void Assignment2invertedIndex(){
		//System.out.println(InvertedIndex.invertedIndex.toString());
		fileContents.add(InvertedIndex.invertedIndex.toString());
	}
	
	
	/**
	 * ====================
	 * Assignment3Output
	 * 
	 * 	Description
	 * Outputs for Assignment 3
	 * ====================	
	 */
	public void Assignment3Output(double[][] vectorSpaceModel, String[] rankedDocs){
		fileContents.add("VectorSpaceModel");
		Assignment3VSM(vectorSpaceModel);
	}
	
	public void Assignment3VSM(double[][] vectorSpaceModel){
		String vectorSpaceString = "";
		for (int ii =0; ii < vectorSpaceModel.length; ii++){
			for (int jj =0; jj < 1000; jj++){
				vectorSpaceString += " "+ vectorSpaceModel[ii][jj];
			}
			fileContents.add(vectorSpaceString);
			vectorSpaceString = "";
		}
	}
	
	
	/**
	 * ====================
	 * write
	 * 
	 * 	Description
	 * Writes FileContents to output text file.
	 * ====================	
	 */
	public void write(){
		try {
			WriteOut(outDirect , fileNameout);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
