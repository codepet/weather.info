package com.gc.weather.entity;

import java.io.Serializable;

public class Weather implements Serializable {

    protected String date; // 今天日期
    protected String week; // 今日星期
    protected String curTemp; // 当前温度
    protected String aqi; // PM值
    protected String fengxiang; // 风向
    protected String fengli;// 风力
    protected String hightemp; // 最高温度
    protected String lowtemp; // 最低温度
    protected String type; // 天气状态

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getCurTemp() {
        return curTemp;
    }

    public void setCurTemp(String curTemp) {
        this.curTemp = curTemp;
    }

    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public String getFengxiang() {
        return fengxiang;
    }

    public void setFengxiang(String fengxiang) {
        this.fengxiang = fengxiang;
    }

    public String getFengli() {
        return fengli;
    }

    public void setFengli(String fengli) {
        this.fengli = fengli;
    }

    public String getHightemp() {
        return hightemp;
    }

    public void setHightemp(String hightemp) {
        this.hightemp = hightemp;
    }

    public String getLowtemp() {
        return lowtemp;
    }

    public void setLowtemp(String lowtemp) {
        this.lowtemp = lowtemp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Weather\n\t[\n\t\tdate=" + date + "," + "\n\t\t" + "week=" + week + ","
                + "\n\t\t" + "curTemp=" + curTemp + ", " + "\n\t\t" + "aqi=" + aqi
                + ", " + "\n\t\t" + "fengxiang=" + fengxiang + ", " + "\n\t\t"
                + "fengli=" + fengli + ", " + "\n\t\t" + "hightemp=" + hightemp
                + ", " + "\n\t\t" + "lowtemp=" + lowtemp + ", " + "\n\t\t" + "type="
                + type + "]\n\t";
    }

}
