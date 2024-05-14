package com.shiftx.shiftpattern;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShiftService {
	public List<DayInfoVO> processShiftPattern(String shiftPattern) throws ServiceException {

		String[] dayWisePattern = shiftPattern.split(",");
		// 0900-1730-B30

		Shift shift = new Shift();
		shift.setDays(new ArrayList<DayInfoVO>());

//		Map<Integer, DayInfoVO> daysMap = new HashMap<>();

		for (int i = 0; i < dayWisePattern.length; i++) {
			if (!dayWisePattern[i].equals("OFF")) {
//				daysMap.put(i + 1, processDayPattern(dayWisePattern[i]));
				DayInfoVO dayInfoVO = processDayPattern(dayWisePattern[i]);
				dayInfoVO.setDay(i + 1);
				shift.getDays().add(dayInfoVO);
			}
		}
		List<DayInfoVO> daysList = shift.getDays();

		shift.setShiftType(findShiftType(daysList));
	
		boolean isValid=isValidShift(daysList);
		
		if(!isValidShift(daysList)) {
			throw new ServiceException("Duration between two shifts not meeting the norms");
		}
		return daysList;
	}

//	public String findShiftType(Map<Integer, DayInfoVO> daysMap) {	
//		Instant prevShiftStart=null;
//		Instant prevShiftEnd=null;
//
//		for (DayInfoVO dayInfoVO : daysMap.values()) {
//			if( prevShiftStart != null && prevShiftEnd != null && (!prevShiftStart.equals(dayInfoVO.getStartTime()) || !prevShiftEnd.equals(dayInfoVO.getEndTime())))  {
//			return "Variable";
//			}
//			prevShiftStart=dayInfoVO.getStartTime();
//			prevShiftEnd=dayInfoVO.getEndTime();
//		}
//		return "Regular";
//	}
	public String findShiftType(List<DayInfoVO> daysList) {
		Instant prevShiftStart = null;
		Instant prevShiftEnd = null;

		for (DayInfoVO dayInfoVO : daysList) {
			if (prevShiftStart != null && prevShiftEnd != null && (!prevShiftStart.equals(dayInfoVO.getStartTime())
					|| !prevShiftEnd.equals(dayInfoVO.getEndTime()))) {
				return "Variable";
			}
			prevShiftStart = dayInfoVO.getStartTime();
			prevShiftEnd = dayInfoVO.getEndTime();
		}
		return "Regular";
	}

	public boolean isValidShift(List<DayInfoVO> daysList){
		boolean isValid = true;
		Duration hoursBetweenNextShift;

		List<DayInfoVO> newDaysList = new ArrayList<>();
		newDaysList.addAll(daysList);
		
		addNullForMissingNumbers(newDaysList);

		for (int i = 0; i < newDaysList.size() - 1; i++) {
			DayInfoVO currentDay;
			DayInfoVO nextDay;	

			if((i+1)!=newDaysList.size()-1) {
				currentDay = newDaysList.get(i);
				nextDay = newDaysList.get(i + 1);
			}
			else {
				currentDay = newDaysList.get(i+1);
				nextDay = newDaysList.get(0);
			}
			
			Instant curentDayEndTime = currentDay != null ? currentDay.getEndTime() : null;
			Instant nextDayStartTime = nextDay != null ? nextDay.getStartTime() : null;

			if (curentDayEndTime == null && nextDayStartTime == null || nextDayStartTime == null
					|| curentDayEndTime == null) {
				isValid = true;
			} else {
				hoursBetweenNextShift = Duration.between(curentDayEndTime, nextDayStartTime);
				if (hoursBetweenNextShift.isNegative()) {
					hoursBetweenNextShift = hoursBetweenNextShift.plus(Duration.ofHours(24));
				}
				if (hoursBetweenNextShift.toMinutes() < 8 * 60) {
					isValid=false;
					break;
				}
			}

		}
		
		return isValid;
	}

	private void addNullForMissingNumbers(List<DayInfoVO> newDaysList) {
		List<Integer> days = new ArrayList<>();
		for (DayInfoVO dayInfoVO : newDaysList) {
			if (dayInfoVO != null) {
			days.add(dayInfoVO.getDay());
			}
		}
		for (int i = 0; i < 7; i++) {
			if (!days.contains(i + 1)) {
				newDaysList.add(i, null);
			}
		}
	}

//	public boolean isValidShift(Map<Integer, DayInfoVO> daysMap) {
//		boolean isValid = true;
//		Duration hoursBetweenNextShift;
//
//		for (int i = 1; i <= 6; i++) {
//			DayInfoVO currentDay = daysMap.get(i);
//			DayInfoVO nextDay = daysMap.get(i + 1);
//
//			Instant curentDayEndTime = currentDay != null ? currentDay.getEndTime() : null;
//			Instant nextDayStartTime = nextDay != null ? nextDay.getStartTime() : null;
//
//			if (curentDayEndTime == null && nextDayStartTime == null || nextDayStartTime == null
//					|| curentDayEndTime == null) {
//				isValid = true;
//			} else {
//				hoursBetweenNextShift = Duration.between(curentDayEndTime, nextDayStartTime);
//				if (hoursBetweenNextShift.isNegative()) {
//					hoursBetweenNextShift = hoursBetweenNextShift.plus(Duration.ofHours(24));
//				}
//				if (hoursBetweenNextShift.toMinutes() < 8 * 60) {
//					isValid = false;
//					break;
//				} else {
//					isValid = true;
//				}
//			}
//
//		}
//		return isValid;
//	}

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

	public int minutesBetween(Instant fromTime, Instant toTime) {
		Duration duration;
		if (fromTime.compareTo(toTime) > 0) {
			duration = Duration.between(toTime, fromTime);
		} else {
			duration = Duration.between(fromTime, toTime);
		}
		long minutes = duration.toMinutes();

		return (int) minutes;
	}
}
