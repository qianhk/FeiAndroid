package codegen.po;

import java.io.Serializable;

public class ManageModule1 implements Serializable {

    private int mBubble;
    private boolean mAllowed;
    private boolean mAllowed2;
    private String mName;

    public ManageModule1() {
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

    public boolean isAllowed2() {
        return mAllowed2;
    }

    public void setAllowed2(boolean allowed2) {
        mAllowed2 = allowed2;
    }
}
