package com.strojny;

/**
 * Created by vreal on 18/11/16.
 *
 * Suppose a sorted array is rotated at some pivot unknown to you beforehand.

 (i.e., 0 1 2 4 5 6 7 might become 4 5 6 7 0 1 2). or 2 3 4 5 6 7 0 1 or 6 7 0 1 2 3 4 5

 You are given a target value to search. If found in the array return its index, otherwise return -1.

 You may assume no duplicate exists in the array.
 */
public class SearchinRotatedSortedArray {
    public static void main(String[] args){
        SearchinRotatedSortedArray search = new SearchinRotatedSortedArray();
        int[] nums = new int[]{4,5,6,7,0,1,2};
        System.out.printf("was: %d, should be: %d\n", search.search(nums, 100), -1);
        System.out.printf("was: %d, should be: %d\n", search.search(nums, 7), 3);
        System.out.printf("was: %d, should be: %d\n", search.search(nums, 4), 0);
        System.out.printf("was: %d, should be: %d\n", search.search(nums, 2), 6);
        System.out.printf("was: %d, should be: %d\n", search.search(nums, 0), 4);

        nums = new int[]{4};
        System.out.printf("was: %d, should be: %d\n", search.search(nums, 0), -1);
        System.out.printf("was: %d, should be: %d\n", search.search(nums, 4), 1);
        System.out.printf("was: %d, should be: %d\n", search.search(nums, 14), -1);

    }

    /*
    * Find Pivot first, then normal binary search
    *  3 2
    * */
    public int search(int[] nums, int target) {
        if(nums == null || nums.length == 0){
            return -1;
        }
        int pivot = findPivot(nums, 0, nums.length - 1);
        if(nums[0] == target){
            return 0;
        } else if(target > nums[0]){
            return binarySearch(nums, 0, pivot - 1, target);
        } else {
            return binarySearch(nums, pivot, nums.length - 1, target);
        }
    }

    private int binarySearch(int[] nums, int start, int end, int target){
        int mid = (start + end) / 2;
        if(end < start ){
            return -1;
        }
        if(nums[mid] == target){
            return mid;
        } else if(nums[mid] > target){
            return binarySearch(nums, start, mid - 1, target);
        } else {
            return binarySearch(nums, mid + 1, end, target);
        }
    }
    /* return index of first number which is smaller than previous
    * for 2 3 1 return 2
    * for 1 0 return 1
    * for 1 2 3 return 0
    */
    private int findPivot(int[] nums, int start, int end){
        int mid = (start + end) / 2;
        if(nums[start] <= nums[end]){
            return start;
        }
        if(nums[mid] >= nums[start]){
            return findPivot(nums, mid + 1, end);
        } else {
            return findPivot(nums, start, mid);
        }
    }
}




