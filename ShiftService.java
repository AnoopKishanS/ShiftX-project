package com.shiftx.shiftpattern;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ShiftService {
	public void processShiftPattern(String shiftPattern) throws ServiceException {

		String[] dayWisePattern = shiftPattern.split(",");
		// 0900-1730-B30
		Map<Integer, DayInfoVO> daysMap = new HashMap<>();
		for (int i = 0; i < dayWisePattern.length; i++) {
			if (!dayWisePattern[i].equals("OFF")) {
				daysMap.put(i + 1, processDayPattern(dayWisePattern[i]));
			}
		}
			
		String shiftType=findShiftType(daysMap);
		boolean minutesBetweenEachShift=isValidShift(daysMap);
		System.out.println(minutesBetweenEachShift);
	}

	public String findShiftType(Map<Integer,DayInfoVO> daysMap) {
		String shiftType;
		String startEndTime="";
		boolean allEqual=true;
		
		for(DayInfoVO shiftTime: daysMap.values()) {
			String shiftStartEndTime = shiftTime.getStartTime() + "to" + shiftTime.getEndTime();
			 
			 if(startEndTime.equals("")) {
				 startEndTime=shiftStartEndTime;
			 }
			 else if(!startEndTime.equals(shiftStartEndTime)) {
				 allEqual=false;
				 break;
			 }
		}
		if(allEqual) {
			shiftType="Regular";
		}
		else {
			shiftType="Variable";
		}
		return shiftType;
	}

	public boolean isValidShift(Map<Integer, DayInfoVO> daysMap) {
		Duration minimumDurationBetweenShifts = Duration.ofHours(8);
		
		for(Integer key: daysMap.keySet()) {
			DayInfoVO prevDayEndTime=daysMap.get(key);
			Instant dayEndTime=prevDayEndTime.getEndTime();
		    
			DayInfoVO nextDayStartTime=daysMap.get(key);
			Instant dayStartTime=nextDayStartTime.getStartTime();

			Duration hoursBetweenNextShift=Duration.between(dayEndTime, dayStartTime);
	
			if(hoursBetweenNextShift.compareTo(minimumDurationBetweenShifts)<0) {
				return true;
			}
		
		}
		
		return false;
	
	}

	public DayInfoVO processDayPattern(String pattern) throws ServiceException {
		DayInfoVO dayInfoVO = parseDayInfo(pattern);
		dayInfoVO.setStartTime(convertStringTimeToInstant(dayInfoVO.getStartTimeStr()));
		dayInfoVO.setEndTime(convertStringTimeToInstant(dayInfoVO.getEndTimeStr()));
		dayInfoVO.setShiftTimeInMinutes(minutesBetween(dayInfoVO.getStartTime(), dayInfoVO.getEndTime()));

		return dayInfoVO;
	}

	public DayInfoVO parseDayInfo(String pattern) throws ServiceException {
		if (pattern == null) {
			throw new ServiceException("Pattern should not be null");
		}
		DayInfoVO dayInfoVO = new DayInfoVO();
		String[] dayInfo = pattern.split("-");
		if (dayInfo.length != 3) {
			throw new ServiceException("Invalid Pattern");
		}
		dayInfoVO.setStartTimeStr(dayInfo[0]);
		dayInfoVO.setEndTimeStr(dayInfo[1]);
		dayInfoVO.setBreakMinsStr(dayInfo[2].substring(1));

		return dayInfoVO;
	}

	public Instant convertStringTimeToInstant(String data) throws ServiceException {
		LocalDateTime localDatetime;
		// 0900
		if (data.length() != 4) {
			throw new ServiceException("Invalid time format");
		}
		int hour = Integer.parseInt(data.substring(0, 2));
		if (hour > 23) {
			throw new ServiceException("Invalid hours format");
		}
		int minutes = Integer.parseInt(data.substring(2, 4));
		if (minutes > 59) {
			throw new ServiceException("Invalid minutes format");
		}

		LocalDate currentdate = LocalDate.now();

		LocalTime localTime = LocalTime.of(hour, minutes);
		localDatetime = LocalDateTime.of(currentdate, localTime);

		return localDatetime.toInstant(ZoneOffset.UTC);
	}

	public int minutesBetween(Instant fromTime, Instant toTime) throws ServiceException {
		if (fromTime.compareTo(toTime) > 0) {
			throw new ServiceException("Invalid time range: The end time cannot be less than the start time.");
		}
		Duration duration = Duration.between(fromTime, toTime);
		long minutes = duration.toMinutes();

		return (int) minutes;
	}
}
