package com.njnu.kai.letterview;

public class UserInfo {

	private String userName;
	private String phoneNum;
	
	private String py;
	
	
	
	public UserInfo(String userName, String phoneNum,String py) {
		super();
		this.userName = userName;
		this.phoneNum = phoneNum;
	
		this.py=py;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getPy() {
		return py;
	}
	public void setPy(String py) {
		this.py = py;
	}
	@Override
	public String toString() {
		return "UserInfo [userName=" + userName + ", phoneNum=" + phoneNum
				+ ", py=" + py + "]";
	}
	
	
}
