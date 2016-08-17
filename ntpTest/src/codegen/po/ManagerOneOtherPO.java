package codegen.po;

import java.io.Serializable;
import java.util.List;

public class ManagerOneOtherPO implements Serializable {
    private long mManagerGroupId;
    private int mManagerGroupMaxCount;
    private String mManagerGroupName;
    private ManageModule3PO mManageModule3PO;
    private List<ManageModule3PO> mManageModuleVOs;

    public ManagerOneOtherPO() {
    }

    public ManageModule3PO getManageModule3PO() {
        return mManageModule3PO;
    }

    public void setManageModule3PO(ManageModule3PO manageModule3PO) {
        mManageModule3PO = manageModule3PO;
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

    public String getManagerGroupName() {
        return this.mManagerGroupName;
    }

    public void setManagerGroupName(String value) {
        this.mManagerGroupName = value;
    }

    public List<ManageModule3PO> getManageModuleVOs() {
        return mManageModuleVOs;
    }

    public void setManageModuleVOs(List<ManageModule3PO> manageModuleVOs) {
        mManageModuleVOs = manageModuleVOs;
    }
}