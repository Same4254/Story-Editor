package Util;

import java.util.ArrayList;

public class Test {
	
	public static void main(String[] args) {
		ArrayList<Integer> totalList = new ArrayList<>();
		for(int i = 0; i < 44; i++)//Populate with 44 elements
			totalList.add(i);
		
		ArrayList<Integer> list1 = mySubList(totalList, 0,                          totalList.size() / 4);//First quarter
		ArrayList<Integer> list2 = mySubList(totalList, totalList.size() / 4,       2 * (totalList.size() / 4));//Second Quarter
		ArrayList<Integer> list3 = mySubList(totalList, 2 * (totalList.size() / 4), 3 * (totalList.size() / 4));//Third Quarter
		ArrayList<Integer> list4 = mySubList(totalList, 3 * (totalList.size() / 4), totalList.size());//Last Quarter
		
		System.out.println(totalList);
		System.out.println(list1);
		System.out.println(list2);
		System.out.println(list3);
		System.out.println(list4);
	}
	
	/**
	 * This function will take in a list and extract another list from it that has the data from the given indexes
	 * 
	 * @param list -> The list to extract information from
	 * @param startIndex -> The entrance index. This is inclusive
	 * @param endIndex -> The terminating index. This is exclusive
	 * @return -> The ArrayList that represents the given list at the given indexes
	 */
	public static ArrayList<Integer> mySubList(ArrayList<Integer> list, int startIndex, int endIndex) {
		ArrayList<Integer> toRet = new ArrayList<>();//Create a temporary list
		for(int i = startIndex; i < endIndex; i++)//Populate the list with elements
			toRet.add(list.get(i));//Add the element from the original list
		
		return toRet;
	}
}
