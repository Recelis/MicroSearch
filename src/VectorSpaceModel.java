import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import Storage.BackData;

import java.lang.Math;


/**
 * ====================
 * VectorSpace Model
 * 	 
 * 	Description
 * 	This class takes in all of the documents, calculates the score for them and then
 *  ranks them. 
 * ====================	
 */
public class VectorSpaceModel {
	double[][] tfidfMatrix; // used for idf number of docs per term
	double[][] qVector;
	String[] listDocs;
	Map<String, Integer> docsContainQuery = new HashMap<String, Integer>();
	/**
	 * ====================
	 * Constructor
	 * 	 
	 * 	Description
	 * 	creates vectorspace model data structure
	 * ====================	
	 */
	public VectorSpaceModel() {
	}
	/**
	 * ====================
	 * vectorTFIDF
	 * 	 
	 * 	Description
	 * 	builds a td-idf matrix with documents that contain the query
	 * ====================	
	 */
	public void vectorTFIDF(){	
		tfidfMatrix = new double[BackData.query.length][BackData.numDocs];
//		for (int ii =0; ii < BackData.query.length; ii++){
//			double idfOut = idf(BackData.query[ii]);
//			int tfidfSwitch = 0;
//			if (InvertedIndex.invertedIndex.get(BackData.query[ii]) == null){ //makes tfidf more efficient
//				tfidfSwitch = 1;
//			}
//			for (int jj =0; jj < BackData.numDocs; jj ++){
//				if (tfidfSwitch == 1){
//					tfidfMatrix[ii][jj] = 0; // fills tfidfMatrix, no extra calcs
//				} else{	
//					String docName = BackData.directoryNames[jj]; 
//					tfidfMatrix[ii][jj] = tf(BackData.query[ii], docName) * idfOut;
//				}	
//			}
//		}
		VectorSpaceOut(1);
		//get documents that contain query using inverted index
		//build a TF-IDF matrix with size numDocs x terms
		// build term frequency so that it starts off with doc:<term 0 tf.idf, term 1 tf.idf...,term 99 tf.idf>
	}
	
	public void queryTFIDF(){
		
	}
	
	private void fillDocsContainQuery(){
		for (int ii = 0; ii < BackData.directoryNames.length; ii++){// fill queryDocNum as all zeros initially
			docsContainQuery.put(BackData.directoryNames[ii], 0);
		}
	}
	public int documentsContainingQuery(){
		fillDocsContainQuery();
		int queryDocNum = 0;
		for (int ii =0; ii < BackData.query.length; ii++){
			if (InvertedIndex.invertedIndex.get(BackData.query[ii]) != null){ // see if query shows up in documents
				Enumeration<String> fileNamesContainQueryN= InvertedIndex.invertedIndex.get(BackData.query[ii]).keys();
				for (int jj =0; jj < InvertedIndex.invertedIndex.get(BackData.query[ii]).size(); jj++){
					String fileName = fileNamesContainQueryN.nextElement(); // get directoryName
					int count = docsContainQuery.get(fileName); // get current count of query in docsContainQuery
					int newCount = count + InvertedIndex.invertedIndex.get(BackData.query[ii]).get(fileName);
					System.out.println("newCount " + newCount);
					if (docsContainQuery.get(fileName)==0){
						queryDocNum++;
					}
					docsContainQuery.put(fileName, newCount);
				}	
			}
		}
		System.out.println("queryDocNum is " + queryDocNum);
		printDocsContainQuery(queryDocNum);
		return queryDocNum;
	}
	
	private void printDocsContainQuery(int queryDocNum){
		Set<String> filesNames =docsContainQuery.keySet();
		Iterator<String> iterate = filesNames.iterator();
		for (int ii =0; ii < queryDocNum; ii++){
			String file = iterate.next();
			System.out.println(file + " " + docsContainQuery.get(file));
		}
	}
		
	
	public void settingQVector(){
		// set qVector to queryDocNum
	}
	public double tf(String query, String docName){
		double fik;// number of term k in doc i

		if (InvertedIndex.invertedIndex.get(query).containsKey(docName) == false){
			return 0;
		} else{
			fik = InvertedIndex.invertedIndex.get(query).get(docName);
		}
		// number of all terms in doc i
//		System.out.println("fik" + fik);
		return fik;
	}
	
	public double idf(String query){
		//numDocs
		double nk;//number of documents containing term k
		if (InvertedIndex.invertedIndex.get(query) == null){
			return 0;
		} else{
			nk = InvertedIndex.invertedIndex.get(query).size();
		}
		return Math.log(BackData.numDocs/nk);
	}
	
	/**
	 * ====================
	 * VectorSpaceOut
	 * 	 
	 * 	Description
	 * 	outputs vectorspace model and prints
	 * ====================	
	 */
	public void VectorSpaceOut(int print){
		if (print == 1){
			System.out.println("VectorSpaceModel TD-IDF");
			for (int ii = 0; ii < BackData.query.length; ii++){
				for (int jj =0; jj < BackData.numDocs; jj++){
					System.out.print(tfidfMatrix[ii][jj] + " ");
				}
				System.out.println("");
			}
		}
	}
	
	public String[] CosineCalc(){
		
		return listDocs;
		
//		PropagateVectorSpace();
//		PropagateVectorSpaceQ();
//		double[] rankedList = new double[N];
//		Hashtable<Double, Integer> rankTable = new Hashtable<Double, Integer>(); 
//		double dotProduct = 0;
//		for (int ii =0; ii < N; ii++){ // docs
//			double Dmag = 0;
//			double Qmag = 0;
//			for (int jj = 0; jj < mostFrequentTerms.length; jj++){
//				Dmag = Dmag + Math.pow(VectorSpace[jj][ii], 2);
//				Qmag = Qmag + Math.pow(VectorSpaceQ[jj], 2);
//			}
//			double denominator = Math.sqrt(Dmag*Qmag);
//			//dotProduct
//			for (int jj = 0; jj < mostFrequentTerms.length; jj++){
//				dotProduct += VectorSpace[jj][ii]*VectorSpaceQ[jj];
////				System.out.println(VectorSpace[jj]);
////				System.out.println(VectorSpaceQ[jj]);
//			}
//			if (dotProduct == 0 || denominator == 0){
//				dotProduct = 0;
//				denominator = 0;
//			} else rankedList[ii] = dotProduct/denominator;
////			System.out.println(dotProduct);
////			System.out.println(denominator);
//			rankTable.put(rankedList[ii], ii);
//			//reset variables
//			dotProduct = 0;
//			
//		}
//		//sort list
//		Arrays.sort(rankedList);
//		// put into descending order
//		double[] tempRanked = rankedList.clone();
//		for (int ii =0; ii < rankedList.length; ii++){
//			tempRanked[ii] = rankedList[rankedList.length-ii-1];
//			//System.out.println(tempRanked[ii]);
//		}
//		rankedList = tempRanked.clone();
//		rankedList = Arrays.copyOfRange(rankedList, 0, 10);
//		String[] rankedDocs = new String[rankedList.length];
//		for(int ii =0; ii < rankedList.length; ii++){
//			Integer index = rankTable.get(rankedList[ii]);
//			rankedDocs[ii] = listDocs[index];
//			System.out.print(rankedDocs[ii]+" ");
//			System.out.print(rankedList[ii]);
//			System.out.println(" index: "+ rankTable.get(rankedList[ii]));
//		}
//		return rankedDocs;
	}
}
