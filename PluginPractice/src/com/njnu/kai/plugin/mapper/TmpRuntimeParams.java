package com.njnu.kai.plugin.mapper;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;

class TmpRuntimeParams {

    interface Action {
        void run(TmpRuntimeParams context);
    }

    Action mAction;
    Project mProject;
    PsiClass mOriginClass;
    String mVoClassCanonicalName;
    String mMapperClassCanonicalName;
    boolean mSupportList;

    public TmpRuntimeParams(Action action, Project project, PsiClass originClass) {
        mAction = action;
        mProject = project;
        mOriginClass = originClass;
    }

    public Action getAction() {
        return mAction;
    }

    public Project getProject() {
        return mProject;
    }

    public PsiClass getOriginClass() {
        return mOriginClass;
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

    public boolean isSupportList() {
        return mSupportList;
    }

    public void setSupportList(boolean supportList) {
        mSupportList = supportList;
    }
}
