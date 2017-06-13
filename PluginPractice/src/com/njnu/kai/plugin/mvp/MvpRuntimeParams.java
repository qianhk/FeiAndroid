package com.njnu.kai.plugin.mvp;

import com.intellij.openapi.project.Project;

/**
 * Created by kai
 * since 16/8/31
 */
public class MvpRuntimeParams {

    private boolean mUseNuwa;

    private boolean mUserCreateNuwaBinder;

    public MvpRuntimeParams setUserCreateNuwaBinder(boolean userCreateNuwaBinder) {
        mUserCreateNuwaBinder = userCreateNuwaBinder;
        return this;
    }

    public boolean isUserCreateNuwaBinder() {
        return mUserCreateNuwaBinder;
    }

    public void setUseNuwa(boolean useNuwa) {
        mUseNuwa = useNuwa;
    }

    public boolean isUseNuwa() {
        return mUseNuwa;
    }

    private String mEntityName;




    public interface Action {
        void run(MvpRuntimeParams context);
    }

    private Action mAction;

    private Project mProject;

    private String mListActivityPackageName;
    private String mListFragmentPackageName;
    private String mListAdapterPackageName;
    private String mListPresenterPackageName;

    private String mNuwaBinderPackageName;
    private String mNuwaVOPackageName;

    private boolean mCheckActivity;
    private boolean mCheckListFragment;
    private boolean mCheckListAdapter;
    private boolean mCheckListPresenter;



    public MvpRuntimeParams(Action action, Project project) {
        mAction = action;
        mProject = project;
    }

    public String getNuwaBinderPackageName() {
        return mNuwaBinderPackageName;
    }

    public MvpRuntimeParams setNuwaBinderPackageName(String nuwaBinderPackageName) {
        mNuwaBinderPackageName = nuwaBinderPackageName;
        return this;
    }

    public String getNuwaVOPackageName() {
        return mNuwaVOPackageName;
    }

    public MvpRuntimeParams setNuwaVOPackageName(String nuwaVOPackageName) {
        mNuwaVOPackageName = nuwaVOPackageName;
        return this;
    }

    public String getListActivityPackageName() {
        return mListActivityPackageName;
    }

    public void setListActivityPackageName(String listActivityPackageName) {
        mListActivityPackageName = listActivityPackageName;
    }

    public String getListFragmentPackageName() {
        return mListFragmentPackageName;
    }

    public void setListFragmentPackageName(String listFragmentPackageName) {
        mListFragmentPackageName = listFragmentPackageName;
    }

    public String getListAdapterPackageName() {
        return mListAdapterPackageName;
    }

    public void setListAdapterPackageName(String listAdapterPackageName) {
        mListAdapterPackageName = listAdapterPackageName;
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

    public String getListPresenterPackageName() {
        return mListPresenterPackageName;
    }

    public void setListPresenterPackageName(String listPresenterPackageName) {
        mListPresenterPackageName = listPresenterPackageName;
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



    public String getListActivityClassName() {
        return mEntityName + "ListActivity";
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

    public String getNuwaBinderClassName() {
        return mEntityName + "NuwaBinder";
    }

    public String getNuwaVOClassName() {
        return mEntityName + "NwaVO";
    }


    //可选带包名的全名称,便于修正import时自动导入
    public String getFullListActivityClassName() {
        return mListActivityPackageName + "." + mEntityName + "ListActivity";
    }

    public String getFullListFragmentClassName() {
        return mListFragmentPackageName + "." + mEntityName + "ListFragment";
    }

    public String getFullListAdapterClassName() {
        return mListAdapterPackageName + "." + mEntityName + "ListAdapter";
    }

    public String getFullNuwaBinderClassName() {
        return mNuwaBinderPackageName + "." + mEntityName + "NuwaBinder";
    }

    public String getFullNuwaVOClassName() {
        return mNuwaBinderPackageName + "." + mEntityName + "NuwaVO";
    }

    public String getFullListPresenterClassName() {
        return mListPresenterPackageName + "." + mEntityName + "ListPresenter";
    }
}
