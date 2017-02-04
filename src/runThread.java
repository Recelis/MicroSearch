import java.io.IOException;
import java.util.List;


static Files nfile = new Files();
static Files vfile = new Files(); // vectorspace files
static Files common_words_file = new Files();
static String DataDirectory = "cranfieldDocs";
private static Processing PreProcessing = new Processing();
static InvertedIndex index = new InvertedIndex();
static KeyWordSear.1
ch search = new KeyWordSearch("KeyWord.txt");
static VectorSpaceModel vectorSpace;
/**
	 * ====================
	 * runThread
	 * 
	 * 	Description
	 * This class runs processing in threads
	 * ====================	
	 */
public class runThread implements Runnable{

	public runThread() {
		// TODO Auto-generated constructor stub
	}

	
	/**
	 * ====================
	 * run
	 * 
	 * 	Description
	 * Runs processing in threads
	 * ====================	
	 */
	@Override
	public void run() {
		// Vector Space
				vectorSpace = new VectorSpaceModel(numDocs);
				for(int ii = 0; ii < numDocs;ii++){ // run through each file in directory
					name = nfile.directoryNames[ii];
					nfile.ReadIn(DataDirectory + "/" + name);
					nfile.fileContents.clear(); // clear fileContents for each previous file
					while (jj < nfile.fileArray.length){ // processing of each line in file
						String line = nfile.fileArray[jj];
						
						line = PreProcessing.ProcessLine(line);
						
						index.scanForWord(line, name);
						nfile.fileContents.add(line); // add new line in each file
						jj++;
					}
					jj = 0; //reset line number for new file
					try {
						nfile.WriteOut(DataDirectory + "Out", name);				
						} catch (IOException e) {
						System.out.println("FNE");
					}
					 
					
				}
			
				search.TermAtATime(); // implements keyword search
				int[][] sortedArray = search.returnRankTable();
				List <String> Keys = search.returnKeys();
				FilesStats outStats = PreProcessing.StatsCall(); // calculates statistics
				
				
				//Vector Space Section
				List<String> Terms = PreProcessing.ReturnKeys();
				int[][] HashArray = PreProcessing.returnHashArray();
				String[] query = search.returnKeyWords();
				vectorSpace.VectorRequired(Terms, numDocs, query, HashArray);
				
				//loop through documents to build vector space model
				jj = 0;
				vfile.DirectContents(DataDirectory+"Out");
				String[] namesList = vfile.directoryNames;
				vectorSpace.VectorSpaceLookUp(namesList);
				for (int ii = 0 ; ii < numDocs; ii++){
					name = vfile.directoryNames[ii];
					vfile.ReadIn(DataDirectory+"Out" + "/" + name);
					vfile.fileContents.clear(); // clear fileContents for each previous file
					while (jj < vfile.fileArray.length){ // processing of each line in file
						String line = vfile.fileArray[jj];
						vectorSpace.ScanForFreq(line, name);
						
						jj++;
					}
					jj = 0; //reset line number for new file
				}
				String[] rankedDocs = vectorSpace.CosineCalc();
				double[][] vectorSpaceModel = vectorSpace.VectorSpaceOut(0);
				
				outStats.Assignment3Output(vectorSpaceModel, rankedDocs); // text output write 
				outStats.write();
		
	}

}
