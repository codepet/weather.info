package com.gc.weather.entity;

import java.io.Serializable;

public class City implements Serializable {

    private String province_cn;
    private String district_cn;
    private String name_cn;
    private String name_en;
    private String area_id;

    public City() {

    }

    public String getProvince_cn() {
        return province_cn;
    }

    public void setProvince_cn(String province_cn) {
        this.province_cn = province_cn;
    }

    public String getDistrict_cn() {
        return district_cn;
    }

    public void setDistrict_cn(String district_cn) {
        this.district_cn = district_cn;
    }

    public String getName_cn() {
        return name_cn;
    }

    public void setName_cn(String name_cn) {
        this.name_cn = name_cn;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    @Override
    public String toString() {
        return "City{" +
                "province_cn='" + province_cn + '\'' +
                ", district_cn='" + district_cn + '\'' +
                ", name_cn='" + name_cn + '\'' +
                ", name_en='" + name_en + '\'' +
                ", area_id='" + area_id + '\'' +
                '}';
    }

}
