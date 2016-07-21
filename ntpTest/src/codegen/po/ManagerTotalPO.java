package codegen.po;

import java.io.Serializable;
import java.util.List;

public class ManagerTotalPO implements Serializable {
    private long mManagerGroupId;
    private int mManagerGroupMaxCount;
    private List<ManagerDetailPO> mManagerDetailVOs;
    private String mManagerGroupName;
    private ManageModulePO mManageModulePO;

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
}