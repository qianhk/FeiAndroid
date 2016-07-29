package com.njnu.kai.plugin.mapper.model;

import com.intellij.psi.PsiClass;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16-7-29
 */
public class WaitPOItem {

    private PsiClass mPoClass;
    private String mVoClassCanonicalName;
    private String mMapperClassCanonicalName;

    public PsiClass getPoClass() {
        return mPoClass;
    }

    public void setPoClass(PsiClass poClass) {
        mPoClass = poClass;
    }

    public String getVoClassCanonicalName() {
        return mVoClassCanonicalName;
    }

    public void setVoClassCanonicalName(String voClassCanonicalName) {
        mVoClassCanonicalName = voClassCanonicalName;
    }

    public String getMapperClassCanonicalName() {
        return mMapperClassCanonicalName;
    }

    public void setMapperClassCanonicalName(String mapperClassCanonicalName) {
        mMapperClassCanonicalName = mapperClassCanonicalName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WaitPOItem that = (WaitPOItem) o;

        if (!mPoClass.equals(that.mPoClass)) return false;
        if (!mVoClassCanonicalName.equals(that.mVoClassCanonicalName)) return false;
        return mMapperClassCanonicalName.equals(that.mMapperClassCanonicalName);

    }

    @Override
    public int hashCode() {
        int result = mPoClass.hashCode();
        result = 31 * result + mVoClassCanonicalName.hashCode();
        result = 31 * result + mMapperClassCanonicalName.hashCode();
        return result;
    }
}
