package com.orbbec.easythread;


import com.lzh.easythread.Callback;
import com.lzh.easythread.EasyThread;

import java.util.concurrent.locks.LockSupport;

/**
 * @author tanzhuohui
 * @date 2018/4/17
 */

public final class ThreadManager {
    private final static EasyThread io;
    private final static EasyThread cache;
    private final static EasyThread calculator;
    private final static EasyThread file;

    public static EasyThread getIO () {
        return io;
    }

    public static EasyThread getCache() {
        return cache;
    }

    public static EasyThread getCalculator() {
        return calculator;
    }

    public static EasyThread getFile() {
        return file;
    }

    static {
        io = EasyThread.Builder.createFixed(6).setName("IO").setPriority(7).setCallback(new DefaultCallback()).build();
        cache = EasyThread.Builder.createCacheable().setName("cache").setCallback(new DefaultCallback()).build();
        calculator = EasyThread.Builder.createFixed(4).setName("calculator").setPriority(Thread.MAX_PRIORITY).setCallback(new DefaultCallback()).build();
        file = EasyThread.Builder.createFixed(4).setName("file").setPriority(3).setCallback(new DefaultCallback()).build();
    }

    private static class DefaultCallback implements Callback {

        @Override
        public void onError(String threadName, Throwable t) {
            //默认处理
        }

        @Override
        public void onCompleted(String threadName) {
            //默认处理
        }

        @Override
        public void onStart(String threadName) {
            //默认处理
        }
    }

    /**
     * 阻塞线程
     * @param runnable 要阻塞的任务
     */
    public static void join(Runnable runnable){
        LockSupport.park(runnable);
    }

    /**
     * 唤醒被阻塞的线程 , 与join方法配合使用
     * @param thread 要叫醒的线程
     */
    public static void wakeup(Thread thread){
        LockSupport.unpark(thread);
    }
}
