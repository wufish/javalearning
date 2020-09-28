package com.wufish.javalearning.sort;

/**
 * The type Sort algorithm test.
 *
 * @Author wzj @58ganji.com
 * @Create time : 2020/7/16 0016
 * @Description: 八种排序算法的思想 、实现以及复杂度。
 */
public class SortAlgorithmTest {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        int[] nums = new int[]{8, 4, 2, 3, 1, 6, 5, 7, 10, 0};
        //bubbleSort(nums);
        //quickSort(nums, 0, nums.length - 1);
        insertSort(nums);
        System.out.println();
    }

    private static void bubbleSort(int[] nums) {
        if (nums == null || nums.length == 0) return;
        int length = nums.length;
        for (int i = 0; i < length; i++) {
            boolean change = false;
            for (int j = length - 1; j > i; j--) {
                if (nums[j] < nums[j - 1]) {
                    int tmp = nums[j];
                    nums[j] = nums[j - 1];
                    nums[j - 1] = tmp;
                    change = true;
                }
            }
            if (!change) {
                break;
            }
        }
    }

    private static void quickSort(int[] nums, int left, int right) {
        if (nums == null || nums.length == 0) return;
        if (left < right) {
            int mid = partition(nums, left, right);
            quickSort(nums, left, mid - 1);
            quickSort(nums, mid + 1, right);
        }
    }

    private static int partition(int[] nums, int left, int right) {
        int base = nums[left];
        while (left < right) {
            while (left < right && nums[right] >= base) {
                right--;
            }
            nums[left] = nums[right];
            while (left < right && nums[left] <= base) {
                left++;
            }
            nums[right] = nums[left];
        }
        nums[left] = base;
        return left;
    }

    private static void insertSort(int[] nums) {
        if (nums == null || nums.length == 0) return;
        int length = nums.length;
        for (int i = 1; i < length; i++) {
            int base = nums[i];
            int j = i - 1;
            for (; j >= 0 && base < nums[j]; j--) {
                nums[j + 1] = nums[j];
            }
            nums[j + 1] = base;
        }
    }

    private static void shellSort(int[] nums) {

    }
}
