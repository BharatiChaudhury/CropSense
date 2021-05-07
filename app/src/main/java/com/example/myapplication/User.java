package com.example.myapplication;

public class User {
    private String email;
    private String institute;
    private String designation;
    private String expert;
    private String yoe;
    private String mobile;
    private boolean advanced;

    public User(String email, String institute, String designation, String expert, String yoe, String mobile, boolean advanced) {
        this.email = email;
        this.institute = institute;
        this.designation = designation;
        this.expert = expert;
        this.yoe = yoe;
        this.mobile = mobile;
        this.advanced = advanced;
    }

    public String getEmail() {
        return email;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getExpert() {
        return expert;
    }

    public void setExpert(String expert) {
        this.expert = expert;
    }

    public String getYoe() {
        return yoe;
    }

    public void setYoe(String yoe) {
        this.yoe = yoe;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public boolean isAdvanced() {
        return advanced;
    }

    public void setAdvanced(boolean advanced) {
        this.advanced = advanced;
    }
}
