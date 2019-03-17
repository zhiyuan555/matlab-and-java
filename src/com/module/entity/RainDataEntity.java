package com.module.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RainDataEntity {
	private String pointId;
	private double longitude;
	private double latitude;
	private double altitude;
	private Date time;
	private double rainData;
	
	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return "id: " + this.pointId + "\n" + "经度 ： " + this.longitude + "\n" + 
				"纬度: " + this.latitude + "\n" + "高程： " + this.altitude + "\n" +
				"监测时间： " + sdf.format(this.time) + "\n" + "雨量： "+ this.rainData;
	}
	
	public String getPointId() {
		return pointId;
	}
	public void setPointId(String pointId) {
		this.pointId = pointId;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getAltitude() {
		return altitude;
	}
	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public double getRainData() {
		return rainData;
	}
	public void setRainData(double rainData) {
		this.rainData = rainData;
	}
	
}
