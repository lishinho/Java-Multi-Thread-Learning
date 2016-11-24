package com.sedion.mynawang.advanced._volatile.pra2_atomicity;

/**
 * volatile非原子特性及解决方案
 * @auther mynawang
 * @create 2016-11-18 10:20
 */
public class VolatileThreadSyn extends Thread{
    volatile public static int count;

    synchronized private static void addCount() {
        for (int i = 0; i < 100; i++) {
            count++;
        }
        System.out.println("count=" + count);
    }

    @Override
    public void run() {
        addCount();
    }


    /**
     * 在方法private static void addCount()前加synchronized关键字，也就没必要用volatile关键字声明count变量
     *
     * 关键字volatile主要使用的场合是多个线程中可以感知实例变量被更改了，并且可以获得最新的值使用，也就是用
     * 多线程读取共享变量时可以获得最新值使用。
     * 关键字volatile提示线程每次从共享内存中读取变量，而不是从私有内存中读取，这样就保证了同步数据的可见性。
     * 但是这里需要注意的是：如果修改实例变量中的数据，如i++，也就是i=i+1，则这样的操作其实并不是一个原子操作，
     * 也就是非线程安全的。表达式i++操作步骤分解如下：
     * 1.从内存中取出i的值
     * 2.计算i的值
     * 3.将i的值写到内存中
     * 如果在第2步计算值的时候，另外一个线程也修改i的值，那么这个时候就会出现脏数据。解决办法就是使用synchronized
     * 关键字。
     * volatile本身并不处理数据的原子性而是强制对数据的读写及时影响到主内存的
     *
     *
               线程工作内存                                                      主工作线程
       ---------------------------                                      ---------------------------
       |                         |                                      |                         |
       |         read    <-------|--------------------------------------|-------- count变量   <---|---- 从外部更改count变量的值
       |                         |                                      |           /\            |
       |         load加载   ---  |                                      |            |            |
       |                      |  |                                      |            |            |
       |         use操作    -----|---------> 这3步非原子性              |            |            |
       |                      |  |                                      |            |            |
       |         assign赋值 ---  |                                      |            |            |
       |                         |                                      |            |            |
       |         store           |                                      |            |            |
       |                         |                                      |            |            |
       |         write    -------|-------------同步到主内存-------------|-------------            |
       |                         |                                      |                         |
       |                         |                                      |                         |
       ---------------------------                                      ---------------------------

        在多线程环境中，use和sign是多次出现的，但这一操作并不是原子性，也就是read和load之后，如果主内存count变量
     发生修改之后，线程工作内存中的值由于已经加载，不会产生对应的变化，也就是私有内存和公共内存中的变量不同步，所以
     计算出来的结果会和预期不一样，也就是出现了非线程安全问题。
        对于用volatile变量，JVM虚拟机只是保证从主内存加载到线程工作内存的值是最新的，例如线程1和线程2在进行read和load
     操作中，发现主内存中的count值都是5，那么都会加载这个最新的值。也就是说，volatile关键字解决的是变量读时的可见性
     问题，但无法保证原子性，对于多个线程访问同一个实例变量还是需要加同步锁。

















     *
     */

}