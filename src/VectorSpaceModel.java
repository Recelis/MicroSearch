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
	Map<String, Integer> docsContainQueryPrior = new HashMap<String, Integer>();
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
		System.out.println("queryDocNum is " + queryDocNum + ". number of terms " + (int) BackData.vocabAfter + ". integermax " + Integer.MAX_VALUE);
		tfidfMatrix = new double[queryDocNum][(int) BackData.vocabAfter]; // okay up to 2147483647, may need to be scaled after
		Iterator<String> docContainQIterate = docsContainQuery.keySet().iterator();
		
		for (int ii =0; ii < queryDocNum; ii++){ // loop through docs
			String docName = docContainQIterate.next();
			Iterator<String> termIterator = BackData.KeysAfter.iterator();
			for (int jj =0; jj < BackData.vocabAfter; jj ++){ // loop through terms
				String term = termIterator.next();
//				if (jj < 30) System.out.println(term);
				tfidfMatrix[ii][jj] = tf(term, docName) * idf(term);
//				System.out.println(idf(term));
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
//			System.out.println("index is: " + index + " query is " + BackData.query[ii]);
			if (index >= 0){
				qVector[index] = idf(BackData.query[ii]);
			}
		}
//		for (int ii =0; ii< qVector.length; ii++){
//		System.out.print(qVector[ii] +" ");
//		}
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
			docsContainQueryPrior.put(BackData.directoryNames[ii], 0);
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
				for (int jj =0; jj < InvertedIndex.invertedIndex.get(BackData.query[ii]).size(); jj++){ // see how many documents contain specific query term
					String fileName = fileNamesContainQueryN.nextElement(); // get fileName
					int count = docsContainQueryPrior.get(fileName); // get current count of query terms in docsContainQuery
					int newCount = count + InvertedIndex.invertedIndex.get(BackData.query[ii]).get(fileName);
//					System.out.println("newCount " + newCount);
					if (docsContainQueryPrior.get(fileName)==0){ // new file with detected!
						queryDocNum++;
					}
					docsContainQueryPrior.put(fileName, newCount);
				}	
			}
		}
//		System.out.println("queryDocNum is " + queryDocNum);
		removeZeroDocsContainQuery(1,queryDocNum);
		return queryDocNum;
	}
	
	/**
	 * ====================
	 * removeZeroDocsContainQuery
	 * 	 
	 * 	Description
	 * 	Prints the docsContainQuery hashmap
	 *  removes all 0s from docsContainQueryPrior
	 * ====================	
	 */	
	private void removeZeroDocsContainQuery(int printSwitch, int queryDocNum){
		if (printSwitch == 1){
			Set<String> filesNames =docsContainQueryPrior.keySet();
			Iterator<String> iterate = filesNames.iterator();
			for (int ii =0; ii < BackData.numDocs; ii++){
				String file = iterate.next();
				if (docsContainQueryPrior.get(file) > 0){
					docsContainQuery.put(file, docsContainQueryPrior.get(file)); 
//					System.out.println(file + " " + docsContainQuery.get(file));
				} 
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
		}else if(BackData.wordFreqDoc.get(docName) == 0){
			return 0;
		}
		else{
			fik = InvertedIndex.invertedIndex.get(term).get(docName);
		}
		// number of all terms in doc i
		
//		System.out.println("fik " + fik/BackData.wordFreqDoc.get(docName));
		return fik/BackData.wordFreqDoc.get(docName);
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
		double nk = 0;//number of documents containing term k		
		if (InvertedIndex.invertedIndex.get(term) == null){
			return 0;
		} else{
			Enumeration<String> keys = InvertedIndex.invertedIndex.get(term).keys();
			while (keys.hasMoreElements()){
				if (docsContainQuery.get(keys.nextElement()) !=null) nk++;
			}
//			nk = InvertedIndex.invertedIndex.get(term).size();
//			System.out.println("Print nk: " + nk + "term is: " + term);
		}
		if (nk == 0) return 1;
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
					System.out.print(tfidfMatrix[ii][jj] + " ");
				}
				System.out.println("");
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
//			System.out.println(cosValueNum +" " + cosValueDen);
			if (cosValueDen == 0.0){
				cosTreeMap.put(0.0,docNameQuery); // put a 0 into cosTreeMap rather than NaN
			} else{
				double cosValue = cosValueNum/cosValueDen;
//				System.out.println(cosValue);
				cosTreeMap.put(cosValue,docNameQuery);
//				System.out.println(cosValue + " " + docNameQuery);
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
		double cosValue = 0;
		for(int ii =0; ii < BackData.vocabAfter; ii ++){
			cosValue= cosValue + vector[ii] * qVector[ii];
		}
		return cosValue;
	}
	
	private double cosDenominator(double[] vector){
		double cosDocValue = 0;
		double cosQueValue = 0;
		for (int ii = 0; ii < BackData.vocabAfter; ii++){
//			System.out.println(cosDocValue +" "+ qVector[ii]);
			cosDocValue = cosDocValue+ vector[ii]*vector[ii];
			cosQueValue = cosQueValue+ qVector[ii]*qVector[ii];
		}
//		System.out.println(Math.sqrt(cosDocValue*cosQueValue));
		return Math.sqrt(cosDocValue*cosQueValue);
	}
	
	private String[] returnRankedList(){
		// rank the list
		NavigableSet<Double> keys = cosTreeMap.descendingKeySet();
		System.out.println("The size of cosTreeMap is: " + cosTreeMap.size());
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
