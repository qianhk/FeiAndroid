package com.njnu.kai.plugin.mapper.model;

import java.util.ArrayList;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16-7-29
 */
public class WaitPOManager {

    private final static Object[] SYNC_OBJECT = new Object[0];

    private static WaitPOManager mManager;

    private ArrayList<WaitPOItem> mMappedPOList = new ArrayList<>();
    private ArrayList<WaitPOItem> mWaitingPOList = new ArrayList<>();

    public static WaitPOManager getInstance() {
        if (mManager == null) {
            synchronized (SYNC_OBJECT) {
                if (mManager == null) {
                    mManager = new WaitPOManager();
                }
            }
        }
        return mManager;
    }

    public void clear() {
        mMappedPOList.clear();
        mWaitingPOList.clear();
    }

    public void push(WaitPOItem item) {
        if (!mMappedPOList.contains(item) && !mWaitingPOList.contains(item)) {
            mWaitingPOList.add(item);
        }
    }

    public WaitPOItem pop() {
        if (mWaitingPOList.isEmpty()) {
            return null;
        } else {
            final WaitPOItem poItem = mWaitingPOList.remove(0);
            mMappedPOList.add(poItem);
            return poItem;
        }
    }

    public boolean empty() {
        return mWaitingPOList.isEmpty();
    }

}
