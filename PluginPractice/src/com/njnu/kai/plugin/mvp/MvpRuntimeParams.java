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
    private String mListFragmentCanonicalName;
    private String mListAdapterCanonicalName;
    private String mListPresenterCanonicalName;

    private boolean mCheckActivity;
    private boolean mCheckListFragment;
    private boolean mCheckListAdapter;
    private boolean mCheckListPresenter;

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

    public String getListFragmentCanonicalName() {
        return mListFragmentCanonicalName;
    }

    public void setListFragmentCanonicalName(String listFragmentCanonicalName) {
        mListFragmentCanonicalName = listFragmentCanonicalName;
    }

    public String getListAdapterCanonicalName() {
        return mListAdapterCanonicalName;
    }

    public void setListAdapterCanonicalName(String listAdapterCanonicalName) {
        mListAdapterCanonicalName = listAdapterCanonicalName;
    }

    public boolean isCheckActivity() {
        return mCheckActivity;
    }

    public void setCheckActivity(boolean checkActivity) {
        mCheckActivity = checkActivity;
    }

    public boolean isCheckListFragment() {
        return mCheckListFragment;
    }

    public void setCheckListFragment(boolean checkListFragment) {
        mCheckListFragment = checkListFragment;
    }

    public boolean isCheckListAdapter() {
        return mCheckListAdapter;
    }

    public void setCheckListAdapter(boolean checkListAdapter) {
        mCheckListAdapter = checkListAdapter;
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

    public String getListPresenterCanonicalName() {
        return mListPresenterCanonicalName;
    }

    public void setListPresenterCanonicalName(String listPresenterCanonicalName) {
        mListPresenterCanonicalName = listPresenterCanonicalName;
    }

    public boolean isCheckListPresenter() {
        return mCheckListPresenter;
    }

    public void setCheckListPresenter(boolean checkListPresenter) {
        mCheckListPresenter = checkListPresenter;
    }

    public void run() {
        mAction.run(this);
    }



    public String getActivityClassName() {
        return mEntityName + "Activity";
    }

    public String getListFragmentClassName() {
        return mEntityName + "ListFragment";
    }

    public String getListAdapterClassName() {
        return mEntityName + "ListAdapter";
    }

    public String getListPresenterClassName() {
        return mEntityName + "ListPresenter";
    }
}
