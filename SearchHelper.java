package com.fanfish.app.util;

/**
 * Helper for seaching
 * 
 * @author wenjun 
 *  
 */
public class SearchHelper {

	// Function to find first element(index of array) that
	// is strictly greater than given target.
	public int firstGreaterValue(Integer[] arr, int target) {
		int start = 0, end = arr.length - 1;
		int ans = -1;
		while (start <= end) {
			int mid = (start + end) / 2;
			// Move to right side if target is greater.
			if (arr[mid] <= target) {
				start = mid + 1;
			}
			// Move left side.
			else {
				ans = mid;
				end = mid - 1;
			}
		}
		return ans;
	}

	// Function to find first element(index of array) that
	// is strictly smaller than given target.
	public int firstSmallerValue(Integer[] arr, int target) {
		int start = 0, end = arr.length - 1;
		int ans = -1;
		while (start <= end) {
			int mid = (start + end) / 2;
			// Move to left side if target is smaller.
			if (arr[mid] >= target) {
				end = mid - 1;
			}
			// Move right side.
			else {
				ans = mid;
				start = mid + 1;
			}
		}
		return ans;
	}

	public int binarySearch(Integer[] a, int target) {
		int low = 0;
		int high = a.length - 1;
		while (low <= high) {
			int mid = (low + high) / 2;
			if (a[mid] < target) {
				low = mid + 1;
			} else if (a[mid] > target) {
				high = mid - 1;
			} else {
				return mid;
			}
		}
		return -1; // key not found
	}

	public int median(Integer[] a, int target) {
		if (a.length == 0)
			return -1;
		return a.length / 2;
	}
}
