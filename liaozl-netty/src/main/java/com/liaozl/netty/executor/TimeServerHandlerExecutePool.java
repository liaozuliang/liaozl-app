package com.liaozl.netty.executor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author liaozuliang
 * @date 2016-12-06
 */
public class TimeServerHandlerExecutePool {

    private ExecutorService executorService;

    public TimeServerHandlerExecutePool(int maxPoolSize, int queueSize) {
        executorService = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors(),
                maxPoolSize,
                120L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(queueSize));
    }

    public void execute(Runnable task) {
        executorService.execute(task);
    }

    public static void close(TimeServerHandlerExecutePool pool) {
        if (pool != null && pool.executorService != null) {
            System.out.println("close executor pool");
            pool.executorService.shutdown();
        }
    }
}
