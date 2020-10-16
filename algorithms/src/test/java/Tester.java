import com.google.common.collect.Lists;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Properties;
import java.util.Stack;

/**
 * @Author wufish
 * @Create time: 2020/7/5 0005
 * @Description:
 */
public class Tester {
    public static void main(String[] args) {
        String s = minWindow("aa", "aa");
        int calculate = calculate(new int[]{0}, "3+2*2");
        int i = maxSubArray(new int[]{12,-1,-1,10});
        boolean b = canPartition(new int[]{1, 5, 11, 5});
        String multiply = multiply("99999", "9999");
        int[][] neighbor = new int[][]{
            {1, 3},
            {0, 4, 2},
            {1, 5},
            {0, 4},
            {3, 1, 5},
            {4, 2}
        };
        Tester tester = new Tester();
        int i1 = tester.minEatingSpeed(new int[]{3, 6, 7, 11}, 8);
        KafkaConsumer kafkaConsumer = new KafkaConsumer<>(new Properties());
        kafkaConsumer.subscribe(Lists.newArrayList());
        ConsumerRecords poll = kafkaConsumer.poll(100);
        System.out.println();
    }

    // 滑动窗口
    public static String minWindow(String s, String t) {
        // 用来统计滑动窗口中每个字符出现次数
        int[] window = new int[128];
        // 用来统计t中每个字符出现次数
        int[] need = new int[128];
        for (int i = 0; i < t.length(); i++) {
            need[t.charAt(i)]++;
        }
        int tcount = 0;
        for (int i = 0; i < need.length; i++) {
            if (need[i] > 0) {
                tcount++;
            }
        }

        int left = 0, right = 0;
        // 记录有效字符的个数
        int count = 0;
        // 记录最小覆盖子串的起始索引和长度
        int start = 0, len = s.length() + 1;
        while (right < s.length()) {
            // c是将要移入窗口的字符
            char c = s.charAt(right);
            // 右移窗口
            right++;
            // 进行窗口内数据的一系列更新
            if (need[c] > 0) {
                window[c]++;
                if (window[c] == need[c]) {
                    count++;
                }
            }
            // 判断左侧窗口是否要收缩
            while (count == tcount) {
                // 在这里更新最小覆盖子串
                if (right - left < len) {
                    start = left;
                    len = right - left;
                }
                // d 是将移出窗口的字符
                char d = s.charAt(left);
                // 左移窗口
                left++;
                // 进行窗口内数据的一系列更新
                if (need[d] > 0) {
                    if (window[d] == need[d])
                        count--;
                    window[d]--;
                }
            }
        }
        // 返回最小覆盖子串
        return len > s.length() ? "" : s.substring(start, start + len);
    }

    private static int calculate(int[] start, String s){
        Stack<Integer> stk = new Stack<>();
        int num = 0;
        char sign = '+';
        for(int i=start[0]; i<s.length(); i++){
            char c = s.charAt(i);
            // 1. 判断数字，并转换数字
            if(Character.isDigit(c)){
                num = 10*num + (c - '0');
            }
            if((!Character.isDigit(c) && c != ' ') || i == s.length() -1){// 2. 空格跳过
                // 3. 判断符号
                if(c == '('){
                    start[0] = i+1;
                    num = calculate(start, s);
                    i = start[0];
                }
                if(sign == '+'){
                    stk.push(num);
                }else if(sign == '-'){
                    stk.push(-num);
                }else if(sign == '*'){
                    stk.push( stk.pop()*num);
                }else if(sign == '/'){
                    stk.push( stk.pop()/num);
                }
                sign = c;
                num = 0;
                if(c == ')'){
                    start[0]= i;
                    break;
                }
            }
        }
        while(!stk.isEmpty()){
            num += stk.pop();
        }
        return num;
    }
    // 动态规划
    public static int maxSubArray(int[] nums) {
        // 1. 确定状态i,j;
        // 2. 确定dp[i]含义，以nums[i]结尾元素的最大和
        // 3. 确定状态转移；
        // 4. 确定base
        int[] dp = new int[nums.length];
        for(int i=0; i<nums.length; i++){
            dp[i] = nums[i];
        }
        for(int i=1; i<nums.length; i++){
            dp[i] = Math.max(nums[i], nums[i] + dp[i-1]);
        }
        int max = Integer.MIN_VALUE;
        for(int i=0; i<dp.length; i++){
            max = dp[i] > max ? dp[i] : max;
        }
        return max;
    }

    // 可转换0-1背包问题，最大背包容量sum/2
    public static boolean canPartition(int[] nums) {
        // 求解最大背包容量
        int sum = 0;
        for(int num : nums){
            sum += num;
        }
        if(sum%2 != 0) return false;
        sum /= 2;
        int N = nums.length;
        // 1. 确定状态，i前i个物品，最大容量j
        // 2. 确定dp[i][j] 是否正好装满；
        // 3. 状态转移，
        // 4. 确定base
        boolean[][] dp = new boolean[sum+1][sum+1];
        for(int i=0; i< N; i++){
            // 没有容量时，表示装满了
            dp[i][0] = true;
        }
        for(int i=1; i<= N; i++){
            for(int j=1; j <= sum; j++){
                if(j >= nums[i-1]){
                    // 能装下
                    dp[i][j] = dp[i-1][j] || dp[i-1][j- nums[i-1]];
                }else{
                    dp[i][j] = dp[i-1][j];
                }
            }
        }
        return dp[N][sum];
    }

    public static String multiply(String num1, String num2) {
        if (num1.equals("0") || num2.equals("0")) {
            return "0";
        }
        int m = num1.length(), n = num2.length();
        int [] res = new int [m+n];
        for(int i = m -1; i>=0; i--){
            for(int j=n-1; j>=0; j--){
                int val = (num2.charAt(j)-'0')*(num1.charAt(i) - '0');
                int p1 = i+j, p2 = p1+1;
                int sum= val + res[p2];
                res[p2] = sum%10;
                //此处的+=是为了处理进位用的，例如19*19，列出竖式看一下就知道了。
                res[p1] += sum/10;
            }
        }
        StringBuilder result = new StringBuilder();
        for(int i=0; i<res.length; i++){
            //这里的i==0是因为只可能出现首位为0的情况，例如一个三位数乘一个两位数不可能出现结果是一个三位数的情况。所以只需要判断首位即可。
            if(res[i]==0 && i==0){
                continue;
            }
            result.append(res[i]);
        }
        return result.toString();
    }

    public int minEatingSpeed(int[] piles, int H) {
        // 左闭右开，二分求左区间
        int left = 1, right = getMax(piles)+1;
        while(left < right){
            int mid = left + (right - left)/2;
            if(canFinish(piles, mid, H)){
                right = mid;
            }else{
                left = mid+1;
            }
        }
        return left;
    }

    private int getMax(int[] piles){
        int max = 0;
        for(int p : piles){
            max = p>max?p:max;
        }
        return max;
    }

    private boolean canFinish(int[] piles, int speed, int H){
        int time = 0;
        for(int p : piles){
            time = time + p/speed + (p%speed > 0 ? 1 : 0);
        }
        return time <=H;
    }
}
