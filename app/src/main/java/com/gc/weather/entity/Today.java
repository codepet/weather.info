package com.gc.weather.entity;

import java.io.Serializable;
import java.util.List;

public class Today extends Weather implements Serializable{

    private List<Index> index; // 指标列表

    public List<Index> getIndex() {
        return index;
    }

    public void setIndex(List<Index> index) {
        this.index = index;
    }

}
