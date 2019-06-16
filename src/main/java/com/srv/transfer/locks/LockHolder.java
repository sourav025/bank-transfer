package com.srv.transfer.locks;

public interface LockHolder {
    void lock(String[] ids);

    void unlock(String[] ids);
}
