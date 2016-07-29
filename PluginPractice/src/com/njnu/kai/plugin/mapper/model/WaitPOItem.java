package com.njnu.kai.plugin.mapper.model;

import com.intellij.psi.PsiClass;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16-7-29
 */
public class WaitPOItem {

    private String mWaitClass;
    private String mWaitPkg;
    private PsiClass mOriginClass;

    public PsiClass getOriginClass() {
        return mOriginClass;
    }

    public void setOriginClass(PsiClass originClass) {
        mOriginClass = originClass;
    }

    public String getWaitClass() {
        return mWaitClass;
    }

    public void setWaitClass(String waitClass) {
        mWaitClass = waitClass;
    }

    public String getWaitPkg() {
        return mWaitPkg;
    }

    public void setWaitPkg(String waitPkg) {
        mWaitPkg = waitPkg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WaitPOItem that = (WaitPOItem) o;

        if (mWaitClass != null ? !mWaitClass.equals(that.mWaitClass) : that.mWaitClass != null) return false;
        return mWaitPkg != null ? mWaitPkg.equals(that.mWaitPkg) : that.mWaitPkg == null;
    }

    @Override
    public int hashCode() {
        int result = mWaitClass != null ? mWaitClass.hashCode() : 0;
        result = 31 * result + (mWaitPkg != null ? mWaitPkg.hashCode() : 0);
        return result;
    }
}
