package edu.whu.wang.util;

import java.util.*;

public class MyMath {

	public final static int ARR1_CONTAIN_ARR2 = 1;
	public final static int ARR2_CONTAIN_ARR1 = -1;
	public final static int NOT_CONTAINED = 0;
	public final static int ARR1_EQUAL_ARR2 = 2;
	
	public static int compareDoubleArr(int root1, int root2, int[][] arr1, int[][] arr2){
		HashSet<String> set1 = new HashSet<String>();
		HashSet<String> set2 = new HashSet<String>();
		
		for(int i = 0; i < arr1.length; i++){
			if(arr1[i] == null)
				continue;
			
			if(root1 < arr1[i][0])
				set1.add(root1 + "-" + arr1[i][0]);
			else
				set1.add(arr1[i][0] + "-" + root1);
			
			for(int j = 0; j < arr1[i].length - 1; j++){
				if(arr1[i][j] < arr1[i][j+1])
					set1.add(arr1[i][j] + "-" + arr1[i][j+1]);
				else
					set1.add(arr1[i][j+1] + "-" + arr1[i][j]);
			}
		}
		
		for(int i = 0; i < arr2.length; i++){
			if(arr2[i] == null)
				continue;
			
			if(root2 < arr2[i][0])
				set2.add(root2 + "-" + arr2[i][0]);
			else
				set2.add(arr2[i][0] + "-" + root2);
			
			for(int j = 0; j < arr2[i].length - 1; j++){
				if(arr2[i][j] < arr2[i][j+1])
					set2.add(arr2[i][j] + "-" + arr2[i][j+1]);
				else
					set2.add(arr2[i][j+1] + "-" + arr2[i][j]);
			}
		}
		if(!(set1.isEmpty()) && !(set2.isEmpty())){
			if(set1.containsAll(set2) && set2.containsAll(set1))
				return ARR1_EQUAL_ARR2;
			if(set1.containsAll(set2))
				return ARR1_CONTAIN_ARR2;
			if(set2.containsAll(set1))
				return ARR2_CONTAIN_ARR1;
			return NOT_CONTAINED;
		}
		else if(set1.isEmpty() && !(set2.isEmpty())){
			for(int i = 0; i < arr2.length; i++){
				if(arr2[i] == null)
					continue;
				for(int j = 0; j < arr2[i].length; j++)
					if(arr2[i][j] == root1)
						return ARR2_CONTAIN_ARR1;
			}
			return NOT_CONTAINED;
		}
		else if(!(set1.isEmpty()) && set2.isEmpty()){
			for(int i = 0; i < arr1.length; i++){
				if(arr1[i] == null)
					continue;
				for(int j = 0; j < arr1[i].length; j++)
					if(arr1[i][j] == root2)
						return ARR1_CONTAIN_ARR2;
			}
			return NOT_CONTAINED;
		}
		
		
		return NOT_CONTAINED;
	}
	public static int compareIntArrays(int[] arr1, int[] arr2) {
		
		if (arr1.length < arr2.length) {
			int low = 0;
			for (int i = 0; i < arr1.length; i++) {
				for (int j = low; j < arr2.length; j++) {
					if (arr1[i] < arr2[j]) {
						return NOT_CONTAINED;
					}
					else if (arr1[i] == arr2[j]) {
						low = j + 1;
						break;
					}
					else {
						if (arr2.length - j <= arr1.length - i) {
							return NOT_CONTAINED;
						}
					}
				}
			}
			return ARR2_CONTAIN_ARR1;
		}
		else if (arr1.length > arr2.length) {
			int low = 0;
			for (int i = 0; i < arr2.length; i++) {
				for (int j = low; j < arr1.length; j++) {
					if (arr2[i] < arr1[j]) {
						return NOT_CONTAINED;
					}
					else if (arr2[i] == arr1[j]) {
						low = j + 1;
						break;
					}
					else {
						if (arr1.length - j <= arr2.length - i) {
							return NOT_CONTAINED;
						}
					}
				}
			}
			return ARR1_CONTAIN_ARR2;
		}
		else {
			if (Arrays.equals(arr1, arr2)) {
				return ARR2_CONTAIN_ARR1;
			}
			else {
				return NOT_CONTAINED;
			}
		}
	}
	
	public static int compareIntArrays2(int[] arr1, int[] arr2) {
		
		if (arr1.length < arr2.length) {
			int low = 0;
			for (int i = 0; i < arr1.length; i++) {
				for (int j = low; j < arr2.length; j++) {
					if (arr1[i] < arr2[j]) {
						return NOT_CONTAINED;
					}
					else if (arr1[i] == arr2[j]) {
						low = j + 1;
						break;
					}
					else {
						if (arr2.length - j <= arr1.length - i) {
							return NOT_CONTAINED;
						}
					}
				}
			}
			return ARR2_CONTAIN_ARR1;
		}
		else if (arr1.length > arr2.length) {
			int low = 0;
			for (int i = 0; i < arr2.length; i++) {
				for (int j = low; j < arr1.length; j++) {
					if (arr2[i] < arr1[j]) {
						return NOT_CONTAINED;
					}
					else if (arr2[i] == arr1[j]) {
						low = j + 1;
						break;
					}
					else {
						if (arr1.length - j <= arr2.length - i) {
							return NOT_CONTAINED;
						}
					}
				}
			}
			return ARR1_CONTAIN_ARR2;
		}
		else {
			if (Arrays.equals(arr1, arr2)) {
				return ARR1_EQUAL_ARR2;
			}
			else {
				return NOT_CONTAINED;
			}
		}
	}
	
}
