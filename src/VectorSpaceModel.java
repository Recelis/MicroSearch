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
	double[][] VectorSpace;
	double[] VectorSpaceQ = new double[1000];
	int[][] TermFreq;
	int[] TermDocFreq = new int[1000]; // used for idf number of docs per term
	int[] TermDocFreqQ = new int[1000]; // used for idf number of docs per term
	int[][] TermFreqQ; // used for idf number of docs per term
	Hashtable<String, Integer> termDex = new Hashtable<String, Integer>();
	Hashtable<String, Integer> termQDex = new Hashtable<String, Integer>();
	Hashtable<String, Integer> docuDex = new Hashtable<String, Integer>(); 
	String[] mostFrequentTerms = new String[1000];
	int[] MostFrequentFreq = new int[1000];
	String[] Q;
	String[] listDocs;
	int N;
	/**
	 * ====================
	 * Constructor
	 * 	 
	 * 	Description
	 * 	creates vectorspace model data structure
	 * ====================	
	 */
	public VectorSpaceModel(int docNum) {
		VectorSpace = new double[1000][docNum]; // assume already 0
		TermFreq = new int[1000][docNum]; // assume already 0
		TermFreqQ = new int[1000][docNum]; // assume already 0
		N = docNum;
	}
	/**
	 * ====================
	 * VectorRequired
	 * 	 
	 * 	Description
	 * 	gets top 1000 words as list
	 * ====================	
	 */
	public void VectorRequired(){
		//sort this to descending order
		int ii = 0;
		int arrayMin = Math.min(BackData.hashArrayAfter.length, 1000);
		System.out.println("array min is " + arrayMin);
		while(ii < arrayMin){
			mostFrequentTerms[ii] = (BackData.KeysAfter.get(BackData.hashArrayAfter[ii][0]));
			MostFrequentFreq[ii] = (BackData.hashArrayAfter[ii][1]);
			System.out.println(mostFrequentTerms[ii] + " " + MostFrequentFreq[ii]);
			ii++;
		}
		Q = BackData.query; //break into words
	}
	
	/**
	 * ====================
	 * VectorSpaceLookUp
	 * 	 
	 * 	Description
	 * 	makes hashtables for easy lookup of doc and terms tf on vectorspace.
	 * ====================	
	 */ 
	public void VectorSpaceLookUp(String[] docList){
		listDocs = docList;
		// build term frequency so that it starts off with doc:<term, 0>
		for (int ii = 0; ii < mostFrequentTerms.length; ii++){
//			termDex.put(MostFrequent[ii][0], MostFrequent[ii][1]);
		}
		
		for (int ii = 0; ii < BackData.query.length; ii++){
			termQDex.put(BackData.query[ii],ii);
//			System.out.println(BackData.query[ii] + " " +ii);
		}
		for (int ii = 0; ii < docList.length; ii++){ // for entire processed data set, may delete later
			docuDex.put(docList[ii], ii);
//			System.out.println(docList[ii] + " " +ii);
		}
	}
	
	/**
	 * ====================
	 * ScanForFreq
	 * 	 
	 * 	Description
	 * 	takes line from document and fills TF and TFQ array
	 * ====================	
	 */
	public void ScanForFreq(String line, String docName) {
		//System.out.println(line);
		String[] tokens = line.split("\\W"); //break into words
		for (int ii = 0; ii < tokens.length; ii++){// loop through tokens/words
			for (int jj = 0; jj < mostFrequentTerms.length; jj++){ // see if query is in line
				if (Objects.equals(mostFrequentTerms[jj], tokens[ii])) { // if word is within top 1000
					Integer termIndex = termDex.get(tokens[ii]);
					Integer docuIndex = docuDex.get(docName);
//					System.out.println(termIndex);
					if (TermFreq[termIndex][docuIndex] == 0) TermFreq[termIndex][docuIndex] = 1;
					else TermFreq[termIndex][docuIndex]++; // add one to TermFreq 
//					System.out.println(TermFreq[termIndex][docuIndex]);
				}
			}
			for (int jj = 0; jj < BackData.query.length; jj++){ // see if query is in line
//				System.out.println(tokens[ii]);
//				System.out.println(Q[jj]);
				if (Objects.equals((String)BackData.query[jj], (String)tokens[ii])){//
					Integer termIndexQ = termQDex.get(BackData.query[jj]);
					Integer docuIndexQ = docuDex.get(docName);
					if (TermFreqQ[termIndexQ][docuIndexQ] == 0) TermFreqQ[termIndexQ][docuIndexQ] = 1;
					else TermFreqQ[termIndexQ][docuIndexQ]++; // add one to TermFreqQ
				}
			} 
		}
		
//			for (int ii1 =0; ii1 < MostFrequent.length; ii1++){
//		System.out.print(" "+ TermFreqQ[ii1] + " ");
//		}
//	System.out.println("");

	}

	/**
	 * ====================
	 * VectorSpaceOut
	 * 	 
	 * 	Description
	 * 	outputs vectorspace model and prints
	 * ====================	
	 */
	public double[][] VectorSpaceOut(int print){
		if (print == 1){
			System.out.println("VectorSpaceModel");
			for (int jj = 0; jj < mostFrequentTerms.length; jj++){
				for (int ii =0; ii < N; ii++){
					System.out.print(VectorSpace[jj][ii] + " ");
				}
				System.out.println("");
			}
		}
		return VectorSpace;
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
	private void DocsPerTerm(){
		for (int ii =0; ii < mostFrequentTerms.length; ii++){//each term
			for (int jj = 0; jj < N; jj++){ // each document
				if (TermFreq[ii][jj] > 0) TermDocFreq[ii]++;
				if (TermFreqQ[ii][jj] > 0) TermDocFreqQ[ii]++;
			}System.out.println(TermDocFreqQ[ii]);
		}
	}
	/**
	 * ====================
	 * PropagateVectorSpace
	 * 	 
	 * 	Description
	 * 	fill VectorSpace with tf.idf 
	 * ====================	
	 */
	private void PropagateVectorSpace(){
		DocsPerTerm(); // build freqDocs
		for (int jj = 0; jj<N; jj++){ // per doc
			double SIGMAfij = 0; 
			for(int ii= 0; ii < mostFrequentTerms.length; ii++){
				SIGMAfij = SIGMAfij + TermFreq[ii][jj];
			}
			for(int ii= 0; ii < mostFrequentTerms.length; ii++){
				double tf;
				double idf;
				if (SIGMAfij == 0) tf = 0;
				else tf = (double)(TermFreq[ii][jj])/SIGMAfij;
				if (TermDocFreq[ii] == 0) idf = 0;
				else idf = Math.log10(N/TermDocFreq[ii]);
				VectorSpace[ii][jj] = tf*idf;
			}
		}
	}
	/**
	 * ====================
	 * PropagateVectorSpaceQ
	 * 	 
	 * 	Description
	 * 	fill VectorSpace with tf.idf 
	 * ====================	
	 */
	private void PropagateVectorSpaceQ(){
		DocsPerTerm(); // build freqDocs
			double SIGMAfijq = 0; 
			for(int ii= 0; ii < BackData.query.length; ii++){
				SIGMAfijq = SIGMAfijq + TermDocFreqQ[ii];
			}
//			System.out.println(SIGMAfijq);
			for(int ii= 0; ii < mostFrequentTerms.length; ii++){
				double idf = 0;
				if (TermDocFreqQ[ii] == 0) idf = 1;
				else{
					idf = Math.log10(N/TermDocFreqQ[ii]);
					System.out.println("df"+idf);
					System.out.println("N"+N);
					System.out.println(TermDocFreqQ[ii]);
				}
				VectorSpaceQ[ii] = idf;
//				System.out.println(idf);
//				System.out.println(N);
				
			}
	}
//	
	public String[] CosineCalc(){
		PropagateVectorSpace();
		PropagateVectorSpaceQ();
		double[] rankedList = new double[N];
		Hashtable<Double, Integer> rankTable = new Hashtable<Double, Integer>(); 
		double dotProduct = 0;
		for (int ii =0; ii < N; ii++){ // docs
			double Dmag = 0;
			double Qmag = 0;
			for (int jj = 0; jj < mostFrequentTerms.length; jj++){
				Dmag = Dmag + Math.pow(VectorSpace[jj][ii], 2);
				Qmag = Qmag + Math.pow(VectorSpaceQ[jj], 2);
			}
			double denominator = Math.sqrt(Dmag*Qmag);
			//dotProduct
			for (int jj = 0; jj < mostFrequentTerms.length; jj++){
				dotProduct += VectorSpace[jj][ii]*VectorSpaceQ[jj];
//				System.out.println(VectorSpace[jj]);
//				System.out.println(VectorSpaceQ[jj]);
			}
			if (dotProduct == 0 || denominator == 0){
				dotProduct = 0;
				denominator = 0;
			} else rankedList[ii] = dotProduct/denominator;
//			System.out.println(dotProduct);
//			System.out.println(denominator);
			rankTable.put(rankedList[ii], ii);
			//reset variables
			dotProduct = 0;
			
		}
		//sort list
		Arrays.sort(rankedList);
		// put into descending order
		double[] tempRanked = rankedList.clone();
		for (int ii =0; ii < rankedList.length; ii++){
			tempRanked[ii] = rankedList[rankedList.length-ii-1];
			//System.out.println(tempRanked[ii]);
		}
		rankedList = tempRanked.clone();
		rankedList = Arrays.copyOfRange(rankedList, 0, 10);
		String[] rankedDocs = new String[rankedList.length];
		for(int ii =0; ii < rankedList.length; ii++){
			Integer index = rankTable.get(rankedList[ii]);
			rankedDocs[ii] = listDocs[index];
			System.out.print(rankedDocs[ii]+" ");
			System.out.print(rankedList[ii]);
			System.out.println(" index: "+ rankTable.get(rankedList[ii]));
		}
		return rankedDocs;
	}
}
