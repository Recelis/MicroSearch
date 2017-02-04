import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class SortingHashArray {
	private int[][] sortedArray; // for sorting
	public int numFields;
	public List<String> Keys;
	private Hashtable<String, Integer> fields; // Hash table contains key and value
	/**
	 * ====================
	 * Constructor
	 * 	 
	 * 	Description
	 * 	enters in important local values
	 * ====================	
	 */
	public SortingHashArray(Hashtable<String, Integer> hashIn, int hashNumIn) {
		fields = hashIn;
		numFields = hashNumIn;
	}

	/**
	 * ====================
	 * HashToArray
	 * 	 
	 * 	Description
	 * 	As Hash Tables cannot be sorted, moves Hash Table to int Array: sortedArray for sorting
	 * ====================	
	 */
	private void HashToArray(){
		// cannot sort sortedArray so move into an Array
		Enumeration<String> e = fields.keys();
		Enumeration<Integer> v = fields.elements();
		int Row = 0;
		sortedArray = new int[(int) numFields][2];
		while(e.hasMoreElements()){
			e.nextElement();
			if (Row == numFields) break; // break when Row has reached max values (last element is a space)
			sortedArray[Row][0] = Row;
			sortedArray[Row][1] = (int) v.nextElement();
			//System.out.println("fields :"  + sortedArray[Row][0] + "value: " + sortedArray[Row][1]);
			Row++;
		}
	}
	/**
	 * ====================
	 * quickSort
	 * 	 
	 * 	Description
	 * 	Performs quickSort frequency column of sortedArray
	 * ====================	
	 */	
	private void quickSort(int lo, int hi){
		if (lo <hi){
			int p = partition(lo,hi);
			quickSort(lo,p-1);
			quickSort(p+1, hi);
		}
	}
	/**
	 * ====================
	 * partition
	 * 	 
	 * 	Description
	 * 	function required by quickSort algorithm that actually sorts rows.
	 * ====================	
	 */
	private int partition(int lo, int hi){
		int pivot = sortedArray[hi][1];
		int temp0 = 0;
		int temp1 = 0;
		int ii = lo;
		for (int jj = lo; jj < hi; jj++){
			if (sortedArray[jj][1] <= pivot) {
				temp0 = sortedArray[ii][0];
				temp1 = sortedArray[ii][1];
				sortedArray[ii][0] = sortedArray[jj][0];
				sortedArray[ii][1] = sortedArray[jj][1];
				sortedArray[jj][0] = temp0;
				sortedArray[jj][1] = temp1;
				ii++;
			}
		}
		temp0 = sortedArray[ii][0];
		temp1 = sortedArray[ii][1];
		sortedArray[ii][0] = sortedArray[hi][0];
		sortedArray[ii][1] = sortedArray[hi][1];
		sortedArray[hi][0] = temp0;
		sortedArray[hi][1] = temp1;
		return ii;
	}
	/**
	 * ====================
	 * flipArray
	 * 	 
	 * 	Description
	 * 	Flips quickSorted sortedArray from ascending order to descending
	 * ====================	
	 */	
	private void flipArray(){
		int temp0 = 0;
		int temp1 = 0;
		for (int ii =0; ii < Math.floor(numFields/2); ii++){
			temp0 = sortedArray[ii][0];
			temp1 = sortedArray[ii][1];
			sortedArray[ii][0] = sortedArray[numFields - ii-1][0];
			sortedArray[ii][1] = sortedArray[numFields - ii-1][1];
			sortedArray[numFields - ii-1][0] = temp0;
			sortedArray[numFields - ii-1][1] = temp1;
		}
	}
	/**
	 * ====================
	 * printArray
	 * 	 
	 * 	Description
	 * 	Prints out sortedArray
	 * ====================	
	 */	
	public int[][] printArray(int lock){
		int ii = 0;
		if (lock == 1){
			System.out.println("sortedArray ----------------------------------------------------------------------");
			while(ii < numFields){
				System.out.println(Keys.get(sortedArray[ii][0]) + " " + sortedArray[ii][1]);
				ii++;
			}
		}
		return sortedArray;
	}
	/**
	 * ====================
	 * sortHash
	 * 	 
	 * 	Description
	 * 	public function called to sort Hash Array generated from loopLine.
	 * ====================	
	 */
	public List<String> sortHash(){
		System.out.println("----------------------------------------------------------------------");
		HashToArray();//
		Keys = Collections.list(fields.keys());// Map Hash Table fields to first row of sortedArray
		quickSort(0, numFields-1); // sort array by second column
		flipArray();
		return Keys;
	}
}
