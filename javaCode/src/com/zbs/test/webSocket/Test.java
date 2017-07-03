package com.zbs.test.webSocket;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by moilions on 2017/4/25.
 */
public class Test {

    private volatile String name;
    private static int ssize ;
    private static ReentrantLock lock = new ReentrantLock();

    public static void test00(){

        try {
            lock.lock();
//            Thread.sleep(Thread.currentThread().getId() * 100);
            ssize += 1;
            System.out.println("只有我输出" + ssize + " thread id is " + Thread.currentThread().getId());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {

        for( int i = 1 , sum = 6 ; i < sum ; i ++ ){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    test00();
                }
            }).start();
        }

    }

}
