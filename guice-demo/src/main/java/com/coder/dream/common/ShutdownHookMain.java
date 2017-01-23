package com.coder.dream.common;

/**
 * Created by konghang on 2017/1/23.
 */
public class ShutdownHookMain {

    public static void main(String[] args) {
        System.out.println("start");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("end print by hook");
            }
        });
        System.out.println("end print by main thread");
    }
}
