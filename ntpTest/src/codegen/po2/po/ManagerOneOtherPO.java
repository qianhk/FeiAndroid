package codegen.po2.po;

import java.io.Serializable;
import java.util.List;

public class ManagerOneOtherPO implements Serializable {
    public long mManagerGroupId;
    public int mManagerGroupMaxCount;
    public String mManagerGroupName;
    public ManageModule3PO mManageModule3PO;
    public List<ManageModule3PO> mManageModuleVOs;

}