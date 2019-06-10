package com.srv.transfer.utils;

import org.checkerframework.checker.units.qual.K;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

public class Lockholder {


    private ConcurrentHashMap<String, Lock> locks = new ConcurrentHashMap<>();

    public void lock(String[] keys) throws InterruptedException {
        Arrays.sort(keys);
        for(String key : keys) aquireLock(key);
    }

    public void unlock(String[] keys) throws InterruptedException {
        Arrays.sort(keys, Collections.reverseOrder());
        for(String key : keys) releaseLock(key);
    }



    public void aquireLock(String key) throws InterruptedException {
        while(true) {
            Lock lockObj = locks.get(key);
            if (lockObj == null) {
                Lock myLockObj = new Lock();
                lockObj = locks.putIfAbsent(key, myLockObj);
                if (lockObj == null) {
                    // successfully inserted, and so locked
                    return;
                }
            }
            // lockObj existed, lock it or wait in queue
            if (lockObj.tryLock()) {
                return;
            }
        }
    }



    public void releaseLock(String key) {
        Lock lockObj = locks.get(key);
        synchronized (lockObj) {
            lockObj.busy = false;
            if (lockObj.waitCount == 0) {
                locks.remove(key);
            } else {
                lockObj.notify();
            }
        }
    }

    class Lock {
        boolean busy = true; // locked state, a thread is working
        int waitCount = 0; // number of waiting threads

        /**
         * returns true if lock succeeded
         */
        synchronized boolean tryLock() throws InterruptedException {
            if (busy) {
                waitCount++;
            } else if (waitCount == 0) {
                // such values mean that the lock is deleted
                return false;
            } else {
                busy = true;
                return true;
            }
            while(true) {
                wait();
                if (!busy) {
                    waitCount--;
                    busy = true;
                    return true;
                }
            }
        }

    }

//    private WeakHashMap<String, ReentrantLock> weakHashMap = new WeakHashMap<>();
//
//    public synchronized void lock(String[] ids) {
//        Arrays.sort(ids);
//        for (int idIndex = 0; idIndex < ids.length; idIndex++) {
//            ReentrantLock lock = new ReentrantLock(true);
//            ReentrantLock oldLock = weakHashMap.putIfAbsent(ids[idIndex], lock);
//            if(oldLock!=null) lock=oldLock;
//            lock.lock();
//        }
//    }
//
//    public synchronized void unlock(String[] ids) {
//        Arrays.sort(ids);
//        for (int idIndex = ids.length-1; idIndex >=0; idIndex--) {
//            ReentrantLock lock = weakHashMap.get(ids[idIndex]);
//            lock.unlock();
//        }
//    }
}
