import java.io.*;
import java.util.*;

import Storage.BackData;


public class Files {
	private static Processing PreProcessing = new Processing();
	static InvertedIndex index = new InvertedIndex();
	public ArrayList<String> fileContents = new ArrayList<String>();
	public String[] fileArray;
	public String[] newFileArray;
	public BufferedReader bufferReader;
	private static String line = null;
	private Integer lengthOfFile;
	public static String newline = System.getProperty("line.separator");
	/**
	 * ====================
	 * Constructor
	 * 
	 * 	Description
	 * Called when File Object is initialised
	 * ====================	
	 */
	public Files(){
		
	}
	/**
	 * ====================
	 * DirectContents
	 * 	 
	 * 	Description
	 * 	Finds all of the files in the directory.
	 * ====================	
	 */
	public void DirectContents(String direct){ // Read Directory contents
		File Folder = new File(direct);
		BackData.directoryNames = Folder.list();	
	}
	/**
	 * ====================
	 * printDirectoryNames
	 * 	 
	 * 	Description
	 * 	prints all of the files in directory
	 * ====================	
	 */
	public void printDirectoryNames(){
		for (int ii =0; ii < BackData.numDocs; ii ++){
			System.out.println(BackData.directoryNames[ii]);
		}
	}
	/**
	 * ====================
	 * ReadIn
	 * 	 
	 * 	Description
	 * 	Reads file contents per line into String Array. 
	 * ====================	
	 */
	public void ReadIn(String fileName){
		try {
			FileReader fileReader = new FileReader(fileName);
			bufferReader = new BufferedReader(fileReader);
			fileContents.clear();
			while((line = bufferReader.readLine()) != null){
				if (line.compareTo("") == 0){
					//System.out.println("blank");
					continue;
				}
				fileContents.add(line + "\n");
				//System.out.println(line);
			}	
			bufferReader.close();
			fileContents.trimToSize();
			lengthOfFile = fileContents.size();
			fileArray = new String[lengthOfFile];
			fileContents.toArray(fileArray);
		}
		catch(IOException io){
			System.out.println("IO");
		}
		catch(NullPointerException npe){
			System.out.println("npe");
		}
	}
	/**
	 * ====================
	 * CreateNewDirectory
	 * 	 
	 * 	Description
	 * 	Creates a New Output Directory if doesn't exist
	 * ====================	
	 */
	public void CreateNewDirectory(String direct){
		File OutFolder = new File(direct);
		OutFolder.mkdir();
	}
	/**
	 * ====================
	 * CreateOutputFile
	 * 	 
	 * 	Description
	 * 	Creates a New Output File if doesn't exist
	 * ====================	
	 */
	public void CreateOutputFile(String direct, String fileName){
		File newFile = new File(direct+"/"+fileName+".txt");
		try {
			newFile.createNewFile();
		} catch (IOException e) {
			System.out.print("New File has not been created");
		}
	}
	/**
	 * ====================
	 * WriteOut
	 * 	 
	 * 	Description
	 * 	Writes to File
	 * ====================	
	 */
	public void WriteOut(String direct, String fileName) throws IOException{		
		Integer lengthOfFile = fileContents.size();
		newFileArray = new String[lengthOfFile];
		fileContents.toArray(newFileArray); // save filecontents to fileArray
		CreateNewDirectory(direct);
		CreateOutputFile(direct, fileName);		
		FileOutputStream output = new FileOutputStream(direct+"/"+fileName+".txt");
		try {
			int lineNum = 0;
			while(lineNum < lengthOfFile){
				byte data[] = newFileArray[lineNum].getBytes();
				output.write(data,0,newFileArray[lineNum].length());
				lineNum++;
			}

		} catch (IOException e) {
			System.out.println("Currently null");
		}
		output.close();
	}
	
	/**
	 * ====================
	 * processLine
	 * 	 
	 * 	Description
	 * 	processes all each line
	 * 	removing stopwords, tags and punctuation
	 *  inputs each line's elements into inverted index.
	 * ====================	
	 */
	public void processLine(String name) {
		int jj = 0;
		while (jj < fileArray.length){ // loop over each line in file
			String line = fileArray[jj];
			line = PreProcessing.ProcessLine(line);
			index.scanForWord(line, name); //build inverted index
			fileContents.add(line); // add processed line in each file
			jj++;
		}
		jj = 0; //reset line number for new file
	}
	/**
	 * ====================
	 * WriteOut
	 * 	 
	 * 	Description
	 * 	allows for MicroSearch to call StatsCall 
	 * 	after all processing is done
	 * ====================	
	 */
	public void ProcessStatsCall(){
		PreProcessing.StatsCall();
	}
}
