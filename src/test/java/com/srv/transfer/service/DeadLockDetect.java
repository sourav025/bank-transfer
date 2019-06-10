package com.srv.transfer.service;

import org.junit.Test;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class DeadLockDetect {
    @Test
    public void detectDeadlock(){
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        long[] threadIds = bean.findDeadlockedThreads(); // Returns null if no threads are deadlocked.

        if (threadIds != null) {
            ThreadInfo[] infos = bean.getThreadInfo(threadIds);

            for (ThreadInfo info : infos) {
                StackTraceElement[] stack = info.getStackTrace();
                System.out.println(stack);
            }
        }
    }
}
