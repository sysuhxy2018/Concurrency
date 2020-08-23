import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ThreadUnsafeExample {
    private int cnt = 0;
    private Lock lock = new ReentrantLock();

    // 线程不安全写法
    public void add() {
        cnt++;
    }

//    /**
//     * synchronized (JVM)
//     */
//    public void add() {
//        synchronized (this) {
//            cnt++;
//        }
//    }

//    /**
//     * reentrantlock (JDK)
//     */
//    public void add() {
//        lock.lock();
//        cnt++;
//        lock.unlock();
//    }

    public int get() {
        return cnt;
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        final int threadSize = 1000;
        ThreadUnsafeExample example = new ThreadUnsafeExample();
        final CountDownLatch countDownLatch = new CountDownLatch(threadSize);
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < threadSize; i++) {
            executorService.execute(()->{
                example.add();
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executorService.shutdown(); // 延迟执行，线程全部执行完后关闭executor
        System.out.println(example.get());
    }
}