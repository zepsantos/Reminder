package com.josepgrs.reminder.Model;



public class NewsInfo {
    private String Info;

    public NewsInfo() {
    }

    public NewsInfo(String Info) {
        this.Info = Info;

    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String Info) {
        this.Info = Info;
    }

    public void delInfo() {

        this.Info = null;
    }


}