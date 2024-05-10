package com.shiftx.shiftpattern;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

public class ShiftService {
	public Map<Integer, DayInfoVO> processShiftPattern(String shiftPattern) throws ServiceException {

		String[] dayWisePattern = shiftPattern.split(",");
		// 0900-1730-B30
		Map<Integer, DayInfoVO> daysMap = new HashMap<>();
		for (int i = 0; i < dayWisePattern.length; i++) {
			if (!dayWisePattern[i].equals("OFF")) {
				daysMap.put(i + 1, processDayPattern(dayWisePattern[i]));
			}
		}

		String shiftType = findShiftType(daysMap);
		boolean minutesBetweenEachShift = isValidShift(daysMap);
		return daysMap;
	}

	public String findShiftType(Map<Integer, DayInfoVO> daysMap) {
		String shiftType;
		String shiftStartEnd = "";
		boolean allEqual = true;

		for (DayInfoVO shiftTime : daysMap.values()) {
			String shiftStartEndTime = shiftTime.getStartTime() + "to" + shiftTime.getEndTime();

			if (shiftStartEnd.equals("")) {
				shiftStartEnd = shiftStartEndTime;
			} else if (!shiftStartEnd.equals(shiftStartEndTime)) {
				allEqual = false;
				break;
			}
		}
		if (allEqual) {
			shiftType = "Regular";
		} else {
			shiftType = "Variable";
		}
		return shiftType;
	}

	public boolean isValidShift(Map<Integer, DayInfoVO> daysMap) {
		boolean isValid = true;
		Duration hoursBetweenNextShift;

		for (int i = 1; i <= 6; i++) {
			DayInfoVO currentDay = daysMap.get(i);
			DayInfoVO nextDay = daysMap.get(i + 1);

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
					isValid = false;
					break;
				} else {
					isValid = true;
				}
			}

		}
		return isValid;
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
