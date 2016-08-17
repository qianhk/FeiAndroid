package codegen.po;

import java.io.Serializable;
import java.util.List;

public class ManagerTotalPO implements Serializable {
    private long mManagerGroupId;
    private int mManagerGroupMaxCount;
    private List<ManagerDetailPO> mManagerDetailVOs;
    private List<String> mTestStrings;
    private String mManagerGroupName;

    private ManageModule1 mManageModule1;
    private ManageModule2PO mManageModule2VO;
    private ManageModule3PO mManageModule3PO;
    private List<ManageModule4> mManageModule4Xx;

    public ManagerTotalPO() {
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

}