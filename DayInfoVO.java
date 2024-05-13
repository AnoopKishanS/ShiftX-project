package com.shiftx.shiftpattern;

import java.time.Instant;
import java.util.Objects;

public class DayInfoVO {
	private String startTimeStr;
	private String endTimeStr;
	private String breakMinsStr;
	private Instant startTime;
	private Instant endTime;
	private int shiftTimeInMinutes;
	private int day;

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public Instant getStartTime() {
		return startTime;
	}

	public void setStartTime(Instant startTime) {
		this.startTime = startTime;
	}

	public Instant getEndTime() {
		return endTime;
	}

	public void setEndTime(Instant endTime) {
		this.endTime = endTime;
	}

	public int getShiftTimeInMinutes() {
		return shiftTimeInMinutes;
	}

	public void setShiftTimeInMinutes(int shiftTimeInMinutes) {
		this.shiftTimeInMinutes = shiftTimeInMinutes;
	}

	public String getStartTimeStr() {
		return startTimeStr;
	}

	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}

	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

	public String getBreakMinsStr() {
		return breakMinsStr;
	}

	public void setBreakMinsStr(String breakMinsStr) {
		this.breakMinsStr = breakMinsStr;
	}

	@Override
	public int hashCode() {
		return Objects.hash(breakMinsStr, endTimeStr, startTimeStr);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DayInfoVO other = (DayInfoVO) obj;
		return Objects.equals(breakMinsStr, other.breakMinsStr) && Objects.equals(endTimeStr, other.endTimeStr)
				&& Objects.equals(startTimeStr, other.startTimeStr);
	}

}
