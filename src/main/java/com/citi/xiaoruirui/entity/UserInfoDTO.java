package com.citi.xiaoruirui.entity;

public class UserInfoDTO {
    private String userName;
    private String userPhoneNum;
    private String userAdd;
    private String expressNum;
    private String excelPath;

    public UserInfoDTO(String userName, String userPhoneNum, String userAdd, String expressNum, String excelPath) {
        this.userName = userName;
        this.userPhoneNum = userPhoneNum;
        this.userAdd = userAdd;
        this.expressNum = expressNum;
        this.excelPath = excelPath;
    }

    public String getExcelPath() {
        return excelPath;
    }

    public void setExcelPath(String excelPath) {
        this.excelPath = excelPath;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoneNum() {
        return userPhoneNum;
    }

    public void setUserPhoneNum(String userPhoneNum) {
        this.userPhoneNum = userPhoneNum;
    }

    public String getUserAdd() {
        return userAdd;
    }

    public void setUserAdd(String userAdd) {
        this.userAdd = userAdd;
    }

    public String getExpressNum() {
        return expressNum;
    }

    public void setExpressNum(String expressNum) {
        this.expressNum = expressNum;
    }
}
