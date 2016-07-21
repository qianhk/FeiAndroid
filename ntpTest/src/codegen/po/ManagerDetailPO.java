package codegen.po;

import java.io.Serializable;

public class ManagerDetailPO implements Serializable {

    private int mGender;
    private String mUserNickName;
    private long mUserId;
    private String mUserUrl;

    public ManagerDetailPO() {
    }

    public int getGender() {
        return this.mGender;
    }

    public void setGender(int value) {
        this.mGender = value;
    }

    public String getUserNickName() {
        return this.mUserNickName;
    }

    public void setUserNickName(String value) {
        this.mUserNickName = value;
    }

    public long getUserId() {
        return this.mUserId;
    }

    public void setUserId(long value) {
        this.mUserId = value;
    }

    public String getUserUrl() {
        return this.mUserUrl;
    }

    public void setUserUrl(String value) {
        this.mUserUrl = value;
    }
}
