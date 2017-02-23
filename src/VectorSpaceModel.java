import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

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
	double[] qVector;
	String[] listDocs;
	Map<String, Integer> docsContainQuery = new HashMap<String, Integer>();
	int queryDocNum;
	TreeMap<Double, String> cosTreeMap = new TreeMap<Double, String>();;
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
	 * 	builds a td-idf matrix with documents to terms
	 * 	get documents that contain query using inverted index
	 *	build a TF-IDF matrix with size numDocs x terms
	 *  build term frequency so that it starts off with doc:<term 0 tf.idf, term 1 tf.idf...,term 99 tf.idf>
	 * ====================	
	 */
	public void runVectorSpaceModel(){
		vectorTFIDF();
		queryTFIDF();
		cosineCalc();
		returnRankedList();
	}
	/**
	 * ====================
	 * vectorTFIDF
	 * 	 
	 * 	Description
	 * 	builds a td-idf matrix with documents to terms
	 * 	get documents that contain query using inverted index
	 *	build a TF-IDF matrix with size numDocs x terms
	 *  build term frequency so that it starts off with doc:<term 0 tf.idf, term 1 tf.idf...,term 99 tf.idf>
	 * ====================	
	 */
	private void vectorTFIDF(){
		queryDocNum = documentsContainingQuery();
		System.out.println("queryDocNum is " + queryDocNum + "number of terms" + (int) BackData.vocabAfter + "integermax " + Integer.MAX_VALUE);
		tfidfMatrix = new double[queryDocNum][(int) BackData.vocabAfter]; // okay up to 2147483647, may need to be scaled after
		Iterator<String> docContainQIterate = docsContainQuery.keySet().iterator();
		
		for (int ii =0; ii < queryDocNum; ii++){ // loop through docs
			String docName = docContainQIterate.next();
			Iterator<String> termIterator = BackData.KeysAfter.iterator();
			for (int jj =0; jj < BackData.vocabAfter; jj ++){ // loop through terms
				String term = termIterator.next();
				tfidfMatrix[ii][jj] = tf(term, docName) * idf(term);
			}	
		}
		VectorSpaceOut(0);
	}
	/**
	 * ====================
	 * queryTFIDF
	 * 	 
	 * 	Description
	 * 	This is a 1 x queryDocNum sized vector
	 *  This will be structured like tf-idf of query
	 * ====================	
	 */	
	private void queryTFIDF(){
		qVector = new double[(int) BackData.vocabAfter];
		// fill with zeros initially
		for (int ii =0; ii < BackData.vocabAfter; ii++){
			qVector[ii] = 1; // according my lecture slides 
		}
		for (int ii =0; ii < BackData.query.length; ii++){
			int index = BackData.KeysAfter.indexOf(BackData.query[ii]); // KeysAfter corresponds with qVector locations
			if (index >= 0){
				qVector[index] = idf(BackData.query[ii]);
			}
		}
	}
	/**
	 * ====================
	 * fillDocsContainQuery
	 * 	 
	 * 	Description
	 * 	This function initially fills the hash map values as 0
	 *  before counting of terms in docs has begun
	 * ====================	
	 */	
	private void fillDocsContainQuery(){
		for (int ii = 0; ii < BackData.directoryNames.length; ii++){// fill queryDocNum as all zeros initially
			docsContainQuery.put(BackData.directoryNames[ii], 0);
		}
	}
	
	/**
	 * ====================
	 * documentsContainingQuery
	 * 	 
	 * 	Description
	 * 	This function counts the number of documents containing the query terms
	 *  It also fills the docsContainQuery hashmap that contains the number of appearances of docs 
	 *  in query.
	 * ====================	
	 */	
	private int documentsContainingQuery(){
		fillDocsContainQuery();
		int queryDocNum = 0;
		for (int ii =0; ii < BackData.query.length; ii++){
			if (InvertedIndex.invertedIndex.get(BackData.query[ii]) != null){ // see if query shows up in documents
				Enumeration<String> fileNamesContainQueryN= InvertedIndex.invertedIndex.get(BackData.query[ii]).keys();
				for (int jj =0; jj < InvertedIndex.invertedIndex.get(BackData.query[ii]).size(); jj++){
					String fileName = fileNamesContainQueryN.nextElement(); // get directoryName
					int count = docsContainQuery.get(fileName); // get current count of query in docsContainQuery
					int newCount = count + InvertedIndex.invertedIndex.get(BackData.query[ii]).get(fileName);
//					System.out.println("newCount " + newCount);
					if (docsContainQuery.get(fileName)==0){
						queryDocNum++;
					}
					docsContainQuery.put(fileName, newCount);
				}	
			}
		}
//		System.out.println("queryDocNum is " + queryDocNum);
		printDocsContainQuery(1,queryDocNum);
		return queryDocNum;
	}
	
	/**
	 * ====================
	 * printDocsContainQuery
	 * 	 
	 * 	Description
	 * 	Prints the docsContainQuery hashmap
	 * ====================	
	 */	
	private void printDocsContainQuery(int printSwitch, int queryDocNum){
		if (printSwitch == 1){
			Set<String> filesNames =docsContainQuery.keySet();
			Iterator<String> iterate = filesNames.iterator();
			for (int ii =0; ii < queryDocNum; ii++){
				String file = iterate.next();
//				System.out.println(file + " " + docsContainQuery.get(file));
			}
		}
	}
		
	/**
	 * ====================
	 * tf
	 * 	 
	 * 	Description
	 * 	returns the term frequency 
	 * ====================	
	 */
	private double tf(String term, String docName){
		double fik;// number of term k in doc i

		if (InvertedIndex.invertedIndex.get(term).containsKey(docName) == false){
			return 0;
		} else{
			fik = InvertedIndex.invertedIndex.get(term).get(docName);
		}
		// number of all terms in doc i
//		System.out.println("fik" + fik);
		return fik;
	}
	
	/**
	 * ====================
	 * idf
	 * 	 	 
	 * 	Description
	 * 	returns the inverse document frequency
	 * ====================	
	 */
	private double idf(String term){
		//numDocs
		double nk;//number of documents containing term k
		if (InvertedIndex.invertedIndex.get(term) == null){
			return 0;
		} else{
			nk = InvertedIndex.invertedIndex.get(term).size();
		}
		return Math.log(queryDocNum/nk);
	}
	
	/**
	 * ====================
	 * VectorSpaceOut
	 * 	 
	 * 	Description
	 * 	outputs vectorspace model and prints
	 * ====================	
	 */
	private void VectorSpaceOut(int print){
		if (print == 1){
			System.out.println("VectorSpaceModel TD-IDF");
			for (int ii = 0; ii < tfidfMatrix.length; ii++){
				for (int jj =0; jj < tfidfMatrix[0].length; jj++){
//					System.out.print(tfidfMatrix[ii][jj] + " ");
				}
//				System.out.println("");
			}
		}
	}
	
	
	/**
	 * ====================
	 * CosineCalc
	 * 	 
	 * 	Description
	 * 	calculates the array containing all of the cosines 
	 * ====================	
	 * @return 
	 */
	private void cosineCalc(){
		Iterator<String> docNamesContainQuery = docsContainQuery.keySet().iterator();
		for (int ii = 0; ii < queryDocNum; ii++){
			String docNameQuery = docNamesContainQuery.next();  
//			System.out.println(docNameQuery);
			
			// calculate cosine value
			double cosValueNum = cosNumerator(tfidfMatrix[ii]);
			double cosValueDen = cosDenominator(tfidfMatrix[ii]);
			if (cosValueDen == 0){
				cosTreeMap.put((double) 0,docNameQuery); // put a 0 into cosTreMap rather than NaN
			} else{
				double cosValue = cosValueNum/cosValueDen;
				cosTreeMap.put(cosValue,docNameQuery);
//				System.out.println("check cast worked! " + cosValue);
			}
			//input into cosTreeMap
//			cosTreeMap.push(,docNameQuery);
		}
//		return cosTreeMap;
	}
	/**
	 * ====================
	 * cosNumerator
	 * 	 
	 * 	Description
	 * 	calculates the numerator of the cosine vector algorithm
	 * ====================	
	 */
	private double cosNumerator(double[] vector){
		int cosValue = 0;
		for(int ii =0; ii < queryDocNum; ii ++){
			cosValue+=vector[ii] * qVector[ii];
		}
		return cosValue;
	}
	
	private double cosDenominator(double[] vector){
		int cosDocValue = 0;
		int cosQueValue = 0;
		for (int ii = 0; ii < queryDocNum; ii++){
			cosDocValue += vector[ii]*vector[ii];
			cosQueValue += qVector[ii]*qVector[ii];
		}
		return Math.sqrt(cosDocValue*cosQueValue);
	}
	
	private String[] returnRankedList(){
		// rank the list
		
		NavigableSet<Double> keys = cosTreeMap.descendingKeySet();
//		System.out.println(keys);
		Iterator<Double> keyIterate = keys.iterator();
		listDocs = new String[cosTreeMap.size()];
		for (int ii =0; ii < cosTreeMap.size(); ii++){
//			System.out.println(keyIterate.next());
			listDocs[ii] = cosTreeMap.get(keyIterate.next());
			System.out.println(listDocs[ii]);
		}
		return listDocs;
	}
}
