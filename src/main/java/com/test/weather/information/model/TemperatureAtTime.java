package com.test.weather.information.model;

import java.time.LocalDateTime;

public class TemperatureAtTime implements Comparable<TemperatureAtTime> {
	private double value;
	private LocalDateTime time;
	private String zipcode;
	public TemperatureAtTime(double value, LocalDateTime time, double minValue ,String zipcode) {
		super();
		this.value = value;
		this.time = time;
		this.zipcode = zipcode;
	}


	public String getZipcode() {
		return zipcode;
	}


	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}


	public void setValue(double value) {
		this.value = value;
	}

	public double getValue() {
		return value;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	@Override
	public int compareTo(TemperatureAtTime o) {
		return Double.compare(getValue(), o.getValue());
	}
}
