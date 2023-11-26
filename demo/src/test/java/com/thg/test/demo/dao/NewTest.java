package com.thg.test.demo.dao;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import lombok.AllArgsConstructor;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/3/25 23:57
 **/
public class NewTest {

    static final int totalPeople = 5;
    static final CyclicBarrier cyclicBarrier = new CyclicBarrier(totalPeople);

    public static synchronized int waitSeconds() {
        Random random = new Random();
        return (Math.abs(random.nextInt() % 8) + 1)*10;
    }

    @AllArgsConstructor
    public static class ByteDanceEmployee extends Thread {
        String name;
        @Override
        public void run(){
            try {
                Thread.sleep(waitSeconds());
                System.out.println(name+"已到集合地点");
                cyclicBarrier.await();
                System.out.println(name+"上车出发了");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args){
        new ByteDanceEmployee("路人1").start();
        new ByteDanceEmployee("路人2").start();
        new ByteDanceEmployee("路人3").start();
        new ByteDanceEmployee("路人4").start();
        new ByteDanceEmployee("路人5").start();
    }
}
