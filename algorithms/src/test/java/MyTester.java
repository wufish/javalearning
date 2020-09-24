import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Author wzj@58ganji.com
 * @Create time: 2020/9/18 0018
 * @Description:
 */
public class MyTester {
    List<List<Integer>> res = new ArrayList<>();

    public List<List<Integer>> permuteUnique(int[] nums) {
        LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        queue.add(1);
        queue.add(2);
        queue.add(3);
        queue.add(4);
        for (int i = 0; i < 3; i++) {
            queue.poll();
            System.out.println();
        }
        queue.poll();
        queue.offer(5);

        Arrays.sort(nums);
        backtrack(nums, new boolean[nums.length], new LinkedList<>());
        return res;
    }

    private void backtrack(int[] nums, boolean[] visited, LinkedList<Integer> track) {
        if (track.size() == nums.length) {
            res.add(new ArrayList<>(track));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (visited[i]) continue;
            if (i != 0 && nums[i] == nums[i - 1] && visited[i - 1]) break;
            visited[i] = true;
            track.add(nums[i]);
            backtrack(nums, visited, track);
            track.removeLast();
            visited[i] = false;
        }
    }

    @Test
    public void test() {
        List<List<Integer>> lists = permuteUnique(new int[]{1, 1, 2});
        System.out.println();
    }
}
