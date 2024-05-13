package com.shiftx.shiftpattern;

import java.util.List;

public class Shift {
	private String shiftType;
	private List<DayInfoVO> days;

	public String getShiftType() {
		return shiftType;
	}

	public void setShiftType(String shiftType) {
		this.shiftType = shiftType;
	}

	public List<DayInfoVO> getDays() {
		return days;
	}

	public void setDays(List<DayInfoVO> days) {
		this.days = days;
	}

}
