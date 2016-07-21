package codegen.po;

import java.io.Serializable;

public class ManageModulePO implements Serializable {
    private int mBubble;
    private boolean mAllowed;
    private String mName;

    public ManageModulePO() {
    }

    public int getBubble() {
        return this.mBubble;
    }

    public void setBubble(int value) {
        this.mBubble = value;
    }

    public boolean getAllowed() {
        return this.mAllowed;
    }

    public void setAllowed(boolean value) {
        this.mAllowed = value;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String value) {
        this.mName = value;
    }

}
