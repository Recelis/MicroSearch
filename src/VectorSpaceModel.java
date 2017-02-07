import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
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
	int[][] tfidfMatrix; // used for idf number of docs per term
	String[] listDocs;
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
		tfidfMatrix = new int[BackData.query.length][BackData.numDocs];
		for (int ii =0; ii < BackData.query.length; ii++){
			for (int jj =0; jj < BackData.numDocs; jj ++){
				double d = tf(BackData.query[ii]) * idf(BackData.query[ii]);
				
			}
		}
		//get documents that contain query using inverted index
		//build a TF-IDF matrix with size numDocs x terms
		
		// build term frequency so that it starts off with doc:<term 0 tf.idf, term 1 tf.idf...,term 99 tf.idf>
		
		
	}
	
	public void documentsContainQuery(){
		
	}
	
	public double tf(String query){
		// number of term k in doc i
		// number of all terms in doc i
		double a = 1;
		return a;
	}
	
	public double idf(String query){
		//numDocs
		InvertedIndex.invertedIndex.get(query).size();//number of documents containing term k
		double a = 1;
		return a;
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
			
		}
	}
	/**
	 * ====================
	 * DocsPerTerm
	 * 	 
	 * 	Description
	 * 	calculates the number of documents 
	 *  that each term appears in 
	 * ====================	
	 */
//	private void DocsPerTerm(){
//		for (int ii =0; ii < mostFrequentTerms.length; ii++){//each term
//			for (int jj = 0; jj < N; jj++){ // each document
//				if (TermFreq[ii][jj] > 0) TermDocFreq[ii]++;
//				if (TermFreqQ[ii][jj] > 0) TermDocFreqQ[ii]++;
//			}System.out.println(TermDocFreqQ[ii]);
//		}
//	}
	/**
	 * ====================
	 * PropagateVectorSpace
	 * 	 
	 * 	Description
	 * 	fill VectorSpace with tf.idf 
	 * ====================	
	 */
//	private void PropagateVectorSpace(){
//		DocsPerTerm(); // build freqDocs
//		for (int jj = 0; jj<N; jj++){ // per doc
//			double SIGMAfij = 0; 
//			for(int ii= 0; ii < mostFrequentTerms.length; ii++){
//				SIGMAfij = SIGMAfij + TermFreq[ii][jj];
//			}
//			for(int ii= 0; ii < mostFrequentTerms.length; ii++){
//				double tf;
//				double idf;
//				if (SIGMAfij == 0) tf = 0;
//				else tf = (double)(TermFreq[ii][jj])/SIGMAfij;
//				if (TermDocFreq[ii] == 0) idf = 0;
//				else idf = Math.log10(N/TermDocFreq[ii]);
//				VectorSpace[ii][jj] = tf*idf;
//			}
//		}
//	}
	/**
	 * ====================
	 * PropagateVectorSpaceQ
	 * 	 
	 * 	Description
	 * 	fill VectorSpace with tf.idf 
	 * ====================	
	 */
//	private void PropagateVectorSpaceQ(){
//		DocsPerTerm(); // build freqDocs
//			double SIGMAfijq = 0; 
//			for(int ii= 0; ii < BackData.query.length; ii++){
//				SIGMAfijq = SIGMAfijq + TermDocFreqQ[ii];
//			}
////			System.out.println(SIGMAfijq);
//			for(int ii= 0; ii < mostFrequentTerms.length; ii++){
//				double idf = 0;
//				if (TermDocFreqQ[ii] == 0) idf = 1;
//				else{
//					idf = Math.log10(N/TermDocFreqQ[ii]);
//					System.out.println("df"+idf);
//					System.out.println("N"+N);
//					System.out.println(TermDocFreqQ[ii]);
//				}
//				VectorSpaceQ[ii] = idf;
////				System.out.println(idf);
////				System.out.println(N);
//				
//			}
//	}
//	
//	public String[] CosineCalc(){
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
//	}
}
