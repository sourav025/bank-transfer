package com.srv.transfer.utils;

import com.google.common.util.concurrent.Striped;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.locks.ReadWriteLock;

public class StripLockHolder {
    private Striped<ReadWriteLock> readWriteLockStriped = Striped.readWriteLock(10000);

    public void lock(String[] ids) {
        Arrays.sort(ids);

        ReadWriteLock firstLock = readWriteLockStriped.get(ids[0]);
        ReadWriteLock secondLock = readWriteLockStriped.get(ids[1]);
        while(true){
            boolean first = firstLock.writeLock().tryLock();
            boolean second = secondLock.writeLock().tryLock();
            if(first&&second) return;
            if(first) firstLock.writeLock().unlock();
            if(second) secondLock.writeLock().unlock();
        }
    }

    public void unlock(String[] ids){
        Arrays.sort(ids, Collections.reverseOrder());
        ReadWriteLock firstLock = readWriteLockStriped.get(ids[0]);
        ReadWriteLock secondLock = readWriteLockStriped.get(ids[1]);
        firstLock.writeLock().unlock();
        secondLock.writeLock().unlock();
    }
}
