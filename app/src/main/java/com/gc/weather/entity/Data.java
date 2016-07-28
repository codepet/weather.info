package com.gc.weather.entity;

import java.io.Serializable;
import java.util.List;

public class Data implements Serializable{

	private String city;
	private String cityid;
	private Today today;
	private List<Weather> forecast;// 未来预报列表
	private List<Weather> history;// 历史天气列表

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCityId() {
		return cityid;
	}

	public void setCityId(String cityid) {
		this.cityid = cityid;
	}

	public Today getToday() {
		return today;
	}

	public void setToday(Today today) {
		this.today = today;
	}

	public List<Weather> getForecast() {
		return forecast;
	}

	public void setForecast(List<Weather> forecast) {
		this.forecast = forecast;
	}

	public List<Weather> getHistory() {
		return history;
	}

	public void setHistory(List<Weather> history) {
		this.history = history;
	}

	@Override
	public String toString() {
		return "Data [" + "\n" + "city=" + city + "," + "\n" + "cityid="
				+ cityid + ", " + "\n" + "today=" + today + ", " + "\n"
				+ "forecast=" + forecast + "," + "\n" + " history=" + history
				+ "]";
	}

}
