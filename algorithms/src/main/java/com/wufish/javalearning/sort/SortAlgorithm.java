package com.wufish.javalearning.sort;

import java.util.Arrays;

/**
 * @Author wufish
 * @Create time: 2020/7/16 0016
 * @Description: 八种排序算法的思想、实现以及复杂度。
 */
public class SortAlgorithm {
    public static void main(String[] args) {
        int[] nums = new int[]{8, 4, 2, 3, 1, 6, 5, 7, 10, 0};
        //bubbleSort(nums);
        //quickSort(nums, 0, nums.length - 1);
        //insertSort(nums);
        shellSort(nums);
        //selectSort(nums);
        heapSort(nums);
        mergeSort(nums);
        ListNode l1 = new ListNode(4);
        ListNode l2 = new ListNode(2);
        ListNode l3 = new ListNode(1);
        ListNode l4 = new ListNode(3);
        l1.next = l2;
        l2.next = l3;
        l3.next = l4;
        ListNode listNode = sortList(l1);
        System.out.println();
    }

    /**
     * 1. 冒泡排序: 是一种交换排序
     * 交换排序: 两两比较待排序的关键字，并交换不满足次序要求的那对数，直到整个表都满足次序要求为止。
     * <p>
     * 算法分析
     * 时间复杂度:平均O(n^2)，最坏O(n^2)，最好O(n)
     * 空间复杂度: O(1)
     * 稳定性: 稳定(相同元素的前后顺序并没有改变)
     * 复杂度: 简单
     * 特点：当数据越接近正序时，冒泡排序性能越好。
     *
     * @param nums
     */
    public static void bubbleSort(int[] nums) {
        int n = nums.length;
        // 是否有数据改变
        boolean exchange;
        // 要遍历的次数
        for (int i = 0; i < n - 1; i++) {
            exchange = false;
            // 从后向前依次的比较相邻两个数的大小，遍历一次后，把数组中第i小的数放在第i个位置上
            for (int j = n - 1; j > i; j--) {
                // 比较相邻的元素，如果前面的数大于后面的数，则交换
                if (nums[j - 1] > nums[j]) {
                    int tmp = nums[j];
                    nums[j] = nums[j - 1];
                    nums[j - 1] = tmp;
                    exchange = true;
                }
            }
            if (!exchange) {
                break;
            }
        }
    }

    /**
     * 2.快速排序: 一个数，一次遍历，左右反复横跳，然后一分为二继续
     * 交换排序，采用分治算法
     * 1. 通过一趟排序将要排序的数据分割成独立的两部分：分割点左边都是比它小的数，右边都是比它大的数。
     * 2. 然后再按此方法对这两部分数据分别进行快速排序，整个排序过程可以递归进行，以此达到整个数据变成有序序列。
     * https://mmbiz.qpic.cn/mmbiz_png/eQPyBffYbueqS8KN47wL526oSB2bXribWlSo45dZhcDbRg5YoGjuAaQB9xjpbmyuxpqIWwvSJWKXepgPhsKEUbw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1
     * <p>
     * 步骤：双指针，left，right，初始base=left，从右往左遍历，小于base和left交换，然后left++，
     * 然后从左往右，找到比base大的，和right交换。重复，直至left=right，把base赋值。然后以base为分割点，递归左右
     * <p>
     * <p>
     * 算法分析：
     * 时间复杂度：O(n*logn), 最坏O(n^2), 最好O(n*logn)。最好是顺序，最坏 逆序
     * 平均情况，T(n)=T(n/2)+O(n)，平均复杂度O(nlogn)。
     * 空间复杂度：O(logn)，可以看成二叉树的节点
     * 稳定性：不稳定，相等元素可能会因为分区而交换顺序，所以它是不稳定的算法。
     * 复杂度：复杂
     * 特点：数据越随机分布时，快速排序性能越好；数据越接近有序，快速排序性能越差。
     *
     * @param nums
     */
    public static void quickSort(int[] nums, int left, int right) {
        // 左下标一定小于右下标，否则就越界了
        if (left < right) {
            // 对数组进行分割，取出下次分割的基准标号
            int base = division(nums, left, right);
            // 对“基准标号“左侧的一组数值进行递归的切割，以至于将这些数值完整的排序
            quickSort(nums, left, base - 1);
            // 对“基准标号“右侧的一组数值进行递归的切割，以至于将这些数值完整的排序
            quickSort(nums, base + 1, right);
        }
    }

    /**
     * 一次数组拆分
     *
     * @param nums
     * @param left
     * @param right
     * @return
     */
    private static int division(int[] nums, int left, int right) {
        // 以最左边的数(left)为基准
        int base = nums[left];
        while (left < right) {
            // 从序列右端开始，向左遍历，直到找到小于base的数
            while (left < right && nums[right] >= base) {
                right--;
            }
            // 找到了比base小的元素，将这个元素放到最左边的位置
            nums[left] = nums[right];
            // 从序列左端开始，向右遍历，直到找到大于base的数
            while (left < right && nums[left] <= base) {
                left++;
            }
            // 找到了比base大的元素，将这个元素放到最右边的位置
            nums[right] = nums[left];
        }
        // 最后将base放到left位置。此时，left位置的左侧数值应该都比left小；
        // 而left位置的右侧数值应该都比left大。
        nums[left] = base;
        return left;
    }

    /**
     * 3. 直接插入排序：一种最简单的插入排序。会打牌吗？
     * 插入排序：每一趟将一个待排序的记录，按照其关键字的大小插入到有序队列的合适位置里，知道全部插入完成。
     * <p>
     * 思考：类似摸牌，每摸一张，根据当前已排序，从右往左比较数据 。将ni，插入前i已经排好序的数据中
     * <p>
     * 算法分析
     * 时间复杂度：平均O(n^2), 最好O(n), 最坏O(n^2)
     * 空间复杂度：O(1)
     * 稳定性：稳定
     * 复杂度：简单
     * 特点：
     * 当数据正序时，执行效率最好，每次插入都不用移动前面的元素，时间复杂度为 O(N)。
     * 当数据反序时，执行效率最差，每次插入都要前面的元素后移，时间复杂度为 O(N2)。
     * 所以，数据越接近正序，直接插入排序的算法性能越好。
     *
     * @param nums
     */
    public static void insertSort(int[] nums) {
        // 第1个数肯定是有序的，从第2个数开始遍历，依次插入有序序列
        for (int i = 1; i < nums.length; i++) {
            int tmp = nums[i];
            // 因为前i-1个数都是从小到大的有序序列，所以只要当前比较的数(num[j])比temp大，就把这个数后移一位
            int j;
            for (j = i - 1; j >= 0 && tmp < nums[j]; j--) {
                // 前面的数据后移
                nums[j + 1] = nums[j];
            }
            nums[j + 1] = tmp;
        }
    }

    /**
     * 4. 希尔(Shell)排序又称为缩小增量排序，它是一种插入排序。它是直接插入排序算法的一种威力加强版。
     * 平分多个组，一组一组地第一个元素打牌插入。
     * 步骤：
     * 1. 把记录按步长 gap 分组，对每组记录采用直接插入排序方法进行排序。
     * 2. 随着步长逐渐减小，所分成的组包含的记录越来越多，当步长的值减小到 1 时，整个数据合成为一组，构成一组有序记录，则完成排序。
     * <p>
     * 算法最开始以一定的步长进行排序。然后会继续以一定步长进行排序，最终算法以步长为 1 进行排序。当步长为 1 时，算法变为插入排序，这就保证了数据一定会被排序。
     *
     * <p>
     * 算法分析：
     * 时间复杂度：平均O(n*logn)
     * 空间复杂度：O(1)
     * 稳定性：希尔排序中相等数据可能会交换位置，所以希尔排序是不稳定的算法。
     * 复杂度：较复杂
     * 特点：
     * 1. 直接插入排序是稳定的；而希尔排序是不稳定的。
     * 2. 直接插入排序更适合于原始记录基本有序的集合。
     * 3. 希尔排序的比较次数和移动次数都要比直接插入排序少，当 N 越大时，效果越明显。
     * 4. 在希尔排序中，增量序列 gap 的取法必须满足：最后一个步长必须是 1
     * 5. 直接插入排序也适用于链式存储结构；希尔排序不适用于链式结构。
     *
     * @param nums
     */
    public static void shellSort(int[] nums) {
        int gap = nums.length / 2;
        while (gap >= 1) {
            // 把距离为 gap 的元素编为一个组，扫描所有组
            for (int i = gap; i < nums.length; i++) {
                int tmp = nums[i];
                int j;
                for (j = i - gap; j >= 0 && tmp < nums[j]; j = j - gap) {
                    nums[j + gap] = nums[j];
                }
                nums[j + gap] = tmp;
            }
            // 减小增量
            gap = gap / 2;
        }
    }

    /**
     * 5. 简单选择排序：和冒泡差不多，但是冒泡是两两比较，选择排序是选择一个最小的，然后和第一个交换。
     * <p>
     * 选择排序：每趟从待排序的记录中选出关键字最小的记录，顺序放在已排序的记录序列末尾，直到全部排序结束为止。
     * 步骤：
     * 1. 从待排序序列中，找到关键字最小的元素；
     * 2. 如果最小元素不是待排序序列的第一个元素，将其和第一个元素互换；
     * 3. 从余下的 N - 1 个元素中，找出关键字最小的元素，重复 1、2 步，直到排序结束。
     * 算法分析：
     * 时间复杂度：O(n^2)
     * 空间复杂度：O(1)
     * 稳定性：不稳定
     * 复杂度：简单
     * 特点：主要根据插入排序在有序的情况下，效率最高
     *
     * @param nums
     */
    public static void selectSort(int[] nums) {
        for (int i = 0; i < nums.length - 1; i++) {
            int minIndex = i;
            // 寻找第i个小的数值
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[j] < nums[minIndex]) {
                    minIndex = j;
                }
            }
            // 将找到的第i个小的数值放在第i个位置上
            int temp = nums[minIndex];
            nums[minIndex] = nums[i];
            nums[i] = temp;
        }
    }

    /**
     * 6. 堆排序（Heapsort）:利用堆这种数据结构所设计的一种排序算法。
     * 堆积是一个近似完全二叉树的结构，并同时满足堆积的性质：
     * 即子结点的键值或索引总是小于（或者大于）它的父节点。堆排序可以说是一种利用堆的概念来排序的选择排序。分为两种方法：
     * 大顶堆：每个节点的值都大于或等于其子节点的值，在堆排序算法中用于升序排列；
     * 小顶堆：每个节点的值都小于或等于其子节点的值，在堆排序算法中用于降序排列；
     *
     * @param nums
     */
    /*public static void heapSort(int[] nums) {
        // 循环建立初始堆
        for (int i = nums.length / 2 - 1; i >= 0; i--) {
            //从第一个非叶子结点从下至上，从右至左调整结构, 叶子节点不用调整
            adjustHeap(nums, i, nums.length);
        }

        // 进行n-1次循环，完成排序
        for (int i = nums.length - 1; i > 0; i--) {
            // 最后一个元素和第一元素进行交换
            int temp = nums[i];
            nums[i] = nums[0];
            nums[0] = temp;

            // 筛选 R[0] 结点，得到i-1个结点的堆
            adjustHeap(nums, 0, i);
        }
    }

    private static void adjustHeap(int[] nums, int i, int length) {
        int temp = nums[i];//先取出当前元素i，从左至右，从上至下调整
        for (int k = i * 2 + 1; k < length; k = k * 2 + 1) {//从i结点的左子结点开始，也就是2i+1处开始
            if (k + 1 < length && nums[k] < nums[k + 1]) {//如果左子结点小于右子结点，k指向右子结点
                k++;
            }
            if (nums[k] > temp) {//如果子节点大于父节点，将子节点值赋给父节点（不用进行交换）
                nums[i] = nums[k];
                i = k;
            } else {
                break;
            }
        }
        nums[i] = temp;//将temp值放到最终的位置
    }*/
    public static void heapSort(int[] nums) {
        //1. 数组从第一个非叶子节点构建最大堆，从下至上，从右至左
        // 2. 堆顶元素和最后一个元素交换，然后重新调整堆；如此反复直到只剩一个元素
        for (int i = nums.length / 2 - 1; i >= 0; i--) {
            adjustHeap(nums, i, nums.length);
        }
        for (int i = nums.length - 1; i > 0; i--) {
            int tmp = nums[0];
            nums[0] = nums[i];
            nums[i] = tmp;
            adjustHeap(nums, 0, i);
        }
    }

    private static void adjustHeap(int[] nums, int i, int length) {
        int tmp = nums[i];
        // 一层一层遍历，找到下面子节点最大值
        for (int k = 2 * i + 1; k < length; k = 2 * k + 1) {
            // 找左右子节点中最大的元素
            if (k + 1 < length && nums[k+1] > nums[k]) {
                k++;
            }
            if (nums[k] > tmp) {
                nums[i] = nums[k];
                i = k;
            } else {
                break;
            }
        }
        nums[i] = tmp;
    }

    /**
     * 7. 归并排序：
     * 是建立在归并操作上的一种有效的排序算法，该算法是采用分治法（Divide and Conquer）的一个非常典型的应用。
     * 将已有序的子序列合并，得到完全有序的序列；即先使每个子序列有序，再使子序列段间有序。
     * 若将两个有序表合并成一个有序表，称为二路归并。
     * <p>
     * 步骤：
     * 将待排序序列 R[0…n-1] 看成是 n 个长度为 1 的有序序列，
     * 将相邻的有序表成对归并，得到 n/2 个长度为 2 的有序表；
     * 将这些有序序列再次归并，得到 n/4 个长度为 4 的有序序列；
     * 如此反复进行下去，最后得到一个长度为 n 的有序序列。
     * 两件事：分解，合并
     * <p>
     * 算法分析：
     * 时间复杂度：O(nlogn),归并排序的形式就是一棵二叉树，它需要遍历的次数就是二叉树的深度，而根据完全二叉树的可以得出它的时间复杂度是 O(n*log2n)。
     * 空间复杂度：O(n)
     * 稳定性：稳定, 在归并排序中，相等的元素的顺序不会改变，所以它是稳定的算法。
     * 复杂度：较复杂
     * 特点：
     * 归并排序和堆排序、快速排序的比较
     * 1. 若从空间复杂度来考虑：首选堆排序，其次是快速排序，最后是归并排序。
     * 2. 若从稳定性来考虑，应选取归并排序，因为堆排序和快速排序都是不稳定的。
     * 3. 若从平均情况下的排序速度考虑，应该选择快速排序。
     *
     * @param nums
     */
    public static void mergeSort(int[] nums) {
        int n = nums.length;
        for (int gap = 1; gap < n; gap = 2 * gap) {
            // 归并gap长度的两个相邻子表
            int i;
            for (i = 0; i + 2 * gap - 1 < n; i = i + 2 * gap) {
                merge(nums, i, i + gap - 1, i + 2 * gap - 1);
            }
            // 余下两个子表，后者长度小于gap
            if (i + gap < n) {
                merge(nums, i, i + gap - 1, n - 1);
            }
        }
    }

    private static void merge(int[] nums, int low, int mid, int high) {
        // i是第一段序列的下标
        int i = low;
        // j是第二段序列的下标
        int j = mid + 1;
        // k是临时存放合并序列的下标
        int k = 0;
        // array2是临时合并序列
        int[] nums2 = new int[high - low + 1];

        // 扫描第一段和第二段序列，直到有一个扫描结束
        while (i <= mid && j <= high) {
            // 判断第一段和第二段取出的数哪个更小，将其存入合并序列，并继续向下扫描
            if (nums[i] <= nums[j]) {
                nums2[k++] = nums[i++];
            } else {
                nums2[k++] = nums[j++];
            }
        }

        // 若第一段序列还没扫描完，将其全部复制到合并序列
        while (i <= mid) {
            nums2[k++] = nums[i++];
        }

        // 若第二段序列还没扫描完，将其全部复制到合并序列
        while (j <= high) {
            nums2[k++] = nums[j++];
        }

        // 将合并序列复制到原始序列中
        for (k = 0, i = low; i <= high; i++, k++) {
            nums[i] = nums2[k];
        }
    }

    /**
     * 8. 计数排序：计数排序不是比较排序，排序的速度快于任何比较排序算法。
     * 通俗地理解，例如有 10 个年龄不同的人，统计出有 8 个人的年龄比 A 小，
     * 那 A 的年龄就排在第 9 位,用这个方法可以得到其他每个人的位置,也就排好了序。
     * 当然，年龄有重复时需要特殊处理（保证稳定性），这就是为什么最后要反向填充目标数组，以及将每个数字的统计减去 1 的原因。
     * <p>
     * 算法的步骤如下：
     * <p>
     * （1）找出待排序的数组中最大和最小的元素
     * （2）统计数组中每个值为i的元素出现的次数，存入数组C的第i项
     * （3）对所有的计数累加（从C中的第一个元素开始，每一项和前一项相加）
     * （4）反向填充目标数组：将每个元素i放在新数组的第C(i)项，每放一个元素就将C(i)减去1
     *
     * @param nums
     */
    public static void countingSort(int[] nums) {
        // 找到数组中最大值
        int maxValue = nums[0];
        for (int value : nums) {
            if (value > maxValue) {
                maxValue = value;
            }
        }
        // 根据最大值创建桶，桶存放索引出现的次数
        int bucketLen = maxValue + 1;
        int[] bucket = new int[bucketLen];

        for (int value : nums) {
            bucket[value]++;
        }
        // 从小到大
        int sortedIndex = 0;
        for (int j = 0; j < bucketLen; j++) {
            while (bucket[j] > 0) {
                nums[sortedIndex++] = j;
                bucket[j]--;
            }
        }
    }

    /**
     * 9. 桶排序
     * 计数排序的升级版。它利用了函数的映射关系，高效与否的关键就在于这个映射函数的确定。为了使桶排序更加高效，我们需要做到这两点：
     * <p>
     * 在额外空间充足的情况下，尽量增大桶的数量
     * 使用的映射函数能够将输入的 N 个数据均匀的分配到 K 个桶中
     * 同时，对于桶中元素的排序，选择何种比较排序算法对于性能的影响至关重要。
     * <p>
     * 1. 什么时候最快
     * 当输入的数据可以均匀的分配到每一个桶中。
     * <p>
     * 2. 什么时候最慢
     * 当输入的数据被分配到了同一个桶中。
     *
     * @param nums
     */
    public static void bucketSort(int[] nums, int bucketSize) {
        int minValue = nums[0];
        int maxValue = nums[0];
        for (int value : nums) {
            if (value < minValue) {
                minValue = value;
            } else if (value > maxValue) {
                maxValue = value;
            }
        }

        int bucketCount = (int) Math.floor(1.0 * (maxValue - minValue) / bucketSize) + 1;
        int[][] buckets = new int[bucketCount][0];

        // 利用映射函数将数据分配到各个桶中
        for (int i = 0; i < nums.length; i++) {
            int index = (int) Math.floor(1.0 * (nums[i] - minValue) / bucketSize);
            buckets[index] = Arrays.copyOf(buckets[index], buckets[index].length + 1);
            buckets[index][nums.length - 1] = nums[i];
        }

        int arrIndex = 0;
        for (int[] bucket : buckets) {
            if (bucket.length <= 0) {
                continue;
            }
            // 对每个桶进行排序，这里使用了插入排序
            insertSort(bucket);
            for (int value : bucket) {
                nums[arrIndex++] = value;
            }
        }
    }

    /**
     * 10. 基数排序是一种非比较型整数排序算法，
     * 其原理是将整数按位数切割成不同的数字，然后按每个位数分别比较。
     * 由于整数也可以表达字符串（比如名字或日期）和特定格式的浮点数，所以基数排序也不是只能使用于整数。
     * <p>
     * 基数排序 vs 计数排序 vs 桶排序
     * 基数排序有两种方法：
     * <p>
     * 这三种排序算法都利用了桶的概念，但对桶的使用方法上有明显差异：
     * 1. 计数排序：每个桶只存储单一键值；
     * 2. 桶排序：每个桶存储一定范围的数值；
     * 3. 基数排序：根据键值的每位数字来分配桶；
     *
     * @param nums
     */
    public static void baseSort(int[] nums) {
        radixSort(nums, getMaxDigit(nums));
    }

    /**
     * 获取最高位数
     */
    private static int getMaxDigit(int[] nums) {
        int maxValue = getMaxValue(nums);
        return getNumLenght(maxValue);
    }

    private static int getMaxValue(int[] nums) {
        int maxValue = nums[0];
        for (int value : nums) {
            if (maxValue < value) {
                maxValue = value;
            }
        }
        return maxValue;
    }

    protected static int getNumLenght(long num) {
        if (num == 0) {
            return 1;
        }
        int lenght = 0;
        for (long temp = num; temp != 0; temp /= 10) {
            lenght++;
        }
        return lenght;
    }

    private static int[] radixSort(int[] nums, int maxDigit) {
        int mod = 10;
        int dev = 1;

        for (int i = 0; i < maxDigit; i++, dev *= 10, mod *= 10) {
            // 考虑负数的情况，这里扩展一倍队列数，其中 [0-9]对应负数，[10-19]对应正数 (bucket + 10)
            int[][] counter = new int[mod * 2][0];

            for (int j = 0; j < nums.length; j++) {
                int bucket = ((nums[j] % mod) / dev) + mod;
                counter[bucket] = Arrays.copyOf(counter[bucket], counter[bucket].length + 1);
                nums[nums.length - 1] = nums[j];
            }

            int pos = 0;
            for (int[] bucket : counter) {
                for (int value : bucket) {
                    nums[pos++] = value;
                }
            }
        }

        return nums;
    }

    public static ListNode sortList(ListNode head) {
        if (head == null || head.next == null) return head;

        ListNode mid = findMidNode(head);
        ListNode rightHead = mid.next;
        // 断开链表
        mid.next = null;
        ListNode left = sortList(head);
        ListNode right = sortList(rightHead);
        return merge(left, right);
    }

    private static ListNode findMidNode(ListNode node) {
        if (node == null) return null;
        ListNode fast = node.next, slow = node;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        return slow;
    }

    private static ListNode merge(ListNode left, ListNode right) {
        ListNode dummy = new ListNode(-1);
        ListNode curr = dummy;
        while (left != null && right != null) {
            if (left.val < right.val) {
                curr.next = left;
                left = left.next;
            } else {
                curr.next = right;
                right = right.next;
            }
            curr = curr.next;
        }
        curr.next = left != null ? left : right;
        return dummy.next;
    }

    public static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }
}
