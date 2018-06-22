package gc;

/**
 * @Author 58
 * @Create time: 2018/06/09 14:34
 * @Description: VM 参数：-verbose:gc -Xms:20M -Xmx:20M -Xmn:10M -XX:+PrintGCDetails -XX:SurviorRation=8
 */
public class GCStrategy {
    private static final int _1MB = 1024 * 1024;

    /**
     * 1. 新建对象优先放入新生代，当空间不足时，进行一次Minor GC
     */
    public static void testAllocation() {
        byte[] allocation1, allocation2, allocation3, allocation4;
        allocation1 = new byte[2 * 1024];
        allocation2 = new byte[2 * 1024];
        allocation3 = new byte[2 * 1024];
        // 出现一次 Minor GC
        allocation4 = new byte[2 * 1024];
    }

    /**
     * 2. 大对象优先进入老年代
     * -XX:PretenureSizeThreshold=3145728
     */
    public static void testPretenureSizeThreshold() {
        byte[] allocation;
        allocation = new byte[4 * _1MB];
    }

    /**
     * 3. 长期存活对象进入老年代
     * -XX:MaxTenuringThreshold=1
     * -XX:+PrintTenuringDistribution
     */
    @SuppressWarnings("unused")
    public static void testTenuringThreshold() {
        byte[] allocation1, allocation2, allocation3;
        allocation1 = new byte[_1MB / 4];
        // 什么时候进入老年代取决于MaxTenuringThreshold
        allocation2 = new byte[4 * _1MB];
        allocation3 = new byte[4 * _1MB];
        allocation3 = null;
        allocation3 = new byte[4 * _1MB];
    }

    /**
     * 4. 动态对象年龄判断，相同年龄的对象超过Survior空间的一般，则都进入老年代
     * -XX:MaxTenuringThreshold=15
     * -XX:+PrintTenuringDistribution
     */
    @SuppressWarnings("unused")
    public static void testTenuringThreshold2() {
        byte[] allocation1, allocation2, allocation3, allocation4;
        allocation1 = new byte[_1MB / 4];
        // allocation1 + allocation2 超过Survior一半
        allocation2 = new byte[_1MB / 4];
        allocation3 = new byte[4 * _1MB];
        allocation4 = new byte[4 * _1MB];
        allocation4 = null;
        allocation4 = new byte[4 * _1MB];
    }

    /**
     * 4. 空间担保策略, 当发生Minor GC需要老年代担保，首先判断老年代是否能容纳存活对象，
     * 如果不行，判断是否允许担保策略失败（HandlerPromotionFailure）
     * 如果允许担保策略失败，取之前每一次回收晋升到老年代对象容量的平均值作为经验值，与老年代空间进行比较。
     * -XX:-HandlerPromotionFailure
     */
    @SuppressWarnings("unused")
    public static void testHandlerPromotion() {
        byte[] allocation1, allocation2, allocation3, allocation4, allocation5, allocation6, allocation7, allocation8;
        allocation1 = new byte[2 * _1MB];
        allocation2 = new byte[2 * _1MB];
        allocation3 = new byte[2 * _1MB];
        allocation1 = null;
        allocation4 = new byte[2 * _1MB];
        allocation5 = new byte[2 * _1MB];
        allocation6 = new byte[2 * _1MB];
        allocation4 = null;
        allocation5 = null;
        allocation6 = null;
        allocation7 = new byte[2 * _1MB];
    }


    public static void main(String[] args) {
        testAllocation();
    }

}
