package codegen.po;

import java.io.Serializable;
import java.util.List;

public class ManagerTotalPO implements Serializable {
    private long mManagerGroupId;
    private int mManagerGroupMaxCount;
    private List<ManagerDetailPO> mManagerDetailVOs;
    private List<ManageModule2> mManageModule2Xx;
    private List<String> mTestStrings;
    private String mManagerGroupName;
    private ManageModulePO mManageModulePO;
    private ManageModule2PO mManageModule2VO;
    private ManageModule mManageModule;

    public ManagerTotalPO() {
    }

    public ManageModulePO getManageModulePO() {
        return mManageModulePO;
    }

    public void setManageModulePO(ManageModulePO manageModulePO) {
        mManageModulePO = manageModulePO;
    }

    public long getManagerGroupId() {
        return this.mManagerGroupId;
    }

    public void setManagerGroupId(long value) {
        this.mManagerGroupId = value;
    }

    public int getManagerGroupMaxCount() {
        return this.mManagerGroupMaxCount;
    }

    public void setManagerGroupMaxCount(int value) {
        this.mManagerGroupMaxCount = value;
    }

    public List<ManagerDetailPO> getManagerDetailVOs() {
        return this.mManagerDetailVOs;
    }

    public void setManagerDetailVOs(List<ManagerDetailPO> value) {
        this.mManagerDetailVOs = value;
    }

    public String getManagerGroupName() {
        return this.mManagerGroupName;
    }

    public void setManagerGroupName(String value) {
        this.mManagerGroupName = value;
    }

    public List<String> getTestStrings() {
        return mTestStrings;
    }

    public void setTestStrings(List<String> testStrings) {
        mTestStrings = testStrings;
    }

    public List<ManageModule2> getManageModule2Xx() {
        return mManageModule2Xx;
    }

    public void setManageModule2Xx(List<ManageModule2> manageModule2Xx) {
        mManageModule2Xx = manageModule2Xx;
    }

    public ManageModule2PO getManageModule2VO() {
        return mManageModule2VO;
    }

    public void setManageModule2VO(ManageModule2PO manageModule2VO) {
        mManageModule2VO = manageModule2VO;
    }

    public ManageModule getManageModule() {
        return mManageModule;
    }

    public void setManageModule(ManageModule manageModule) {
        mManageModule = manageModule;
    }
}