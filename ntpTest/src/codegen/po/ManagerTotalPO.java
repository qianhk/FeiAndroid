package codegen.po;

import java.io.Serializable;
import java.util.List;

public class ManagerTotalPO implements Serializable {
    @SuppressWarnings(
            "c"
    )
    private long mManagerGroupId;

    @SuppressWarnings(
            "a"
    )
    private int mManagerGroupMaxCount;

    @SuppressWarnings(
            "b"
    )
    private List<ManagerDetailPO> mManagerDetailVOs;

    @SuppressWarnings(
            "c"
    )
    private List<String> mTestStrings;

    @SuppressWarnings(
            "d"
    )
    private String mManagerGroupName;

    @SuppressWarnings(
            "e"
    )
    private ManageModule1 mManageModule1;

    @SuppressWarnings(
            "c"
    )
    private ManageModule2PO mManageModule2VO;

    @SuppressWarnings(
            "c"
    )
    private ManageModule3PO mManageModule3PO;

    @SuppressWarnings(
            "c"
    )
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

    public ManageModule1 getManageModule1() {
        return mManageModule1;
    }

    public void setManageModule1(ManageModule1 manageModule1) {
        mManageModule1 = manageModule1;
    }

    public ManageModule2PO getManageModule2VO() {
        return mManageModule2VO;
    }

    public void setManageModule2VO(ManageModule2PO manageModule2VO) {
        mManageModule2VO = manageModule2VO;
    }

    public List<ManageModule4> getManageModule4Xx() {
        return mManageModule4Xx;
    }

    public void setManageModule4Xx(List<ManageModule4> manageModule4Xx) {
        mManageModule4Xx = manageModule4Xx;
    }
}