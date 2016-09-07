package com.njnu.kai.plugin.mvp;

import com.intellij.openapi.project.Project;

/**
 * Created by kai
 * since 16/8/31
 */
public class MvpRuntimeParams {

    public interface Action {
        void run(MvpRuntimeParams context);
    }

    private Action mAction;

    private Project mProject;

    private String mActivityCanonicalName;
    private String mFragmentCanonicalName;
    private String mAdapterCanonicalName;
    private String mPresenterCanonicalName;

    private boolean mCheckActivity;
    private boolean mCheckFragment;
    private boolean mCheckAdapter;
    private boolean mCheckPresenter;

    private String mEntityName;

    public MvpRuntimeParams(Action action, Project project) {
        mAction = action;
        mProject = project;
    }

    public String getActivityCanonicalName() {
        return mActivityCanonicalName;
    }

    public void setActivityCanonicalName(String activityCanonicalName) {
        mActivityCanonicalName = activityCanonicalName;
    }

    public String getFragmentCanonicalName() {
        return mFragmentCanonicalName;
    }

    public void setFragmentCanonicalName(String fragmentCanonicalName) {
        mFragmentCanonicalName = fragmentCanonicalName;
    }

    public String getAdapterCanonicalName() {
        return mAdapterCanonicalName;
    }

    public void setAdapterCanonicalName(String adapterCanonicalName) {
        mAdapterCanonicalName = adapterCanonicalName;
    }

    public boolean isCheckActivity() {
        return mCheckActivity;
    }

    public void setCheckActivity(boolean checkActivity) {
        mCheckActivity = checkActivity;
    }

    public boolean isCheckFragment() {
        return mCheckFragment;
    }

    public void setCheckFragment(boolean checkFragment) {
        mCheckFragment = checkFragment;
    }

    public boolean isCheckAdapter() {
        return mCheckAdapter;
    }

    public void setCheckAdapter(boolean checkAdapter) {
        mCheckAdapter = checkAdapter;
    }

    public String getEntityName() {
        return mEntityName;
    }

    public void setEntityName(String entityName) {
        mEntityName = entityName;
    }

    public Project getProject() {
        return mProject;
    }

    public String getPresenterCanonicalName() {
        return mPresenterCanonicalName;
    }

    public void setPresenterCanonicalName(String presenterCanonicalName) {
        mPresenterCanonicalName = presenterCanonicalName;
    }

    public boolean isCheckPresenter() {
        return mCheckPresenter;
    }

    public void setCheckPresenter(boolean checkPresenter) {
        mCheckPresenter = checkPresenter;
    }

    public void run() {
        mAction.run(this);
    }
}
