package com.njnu.kai.plugin.mapper;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;

public class MapperRuntimeParams {

    public interface Action {
        void run(MapperRuntimeParams context);
    }

    Action mAction;
    Project mProject;
    PsiClass mOriginClass;
    String mVoClassCanonicalName;
    String mMapperClassCanonicalName;
    boolean mCascadeMapper;

    public MapperRuntimeParams(Action action, Project project, PsiClass originClass) {
        mAction = action;
        mProject = project;
        mOriginClass = originClass;
    }

    public void setCascadeMapper(boolean cascadeMapper) {
        mCascadeMapper = cascadeMapper;
    }

    public boolean isCascadeMapper() {
        return mCascadeMapper;
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

}
