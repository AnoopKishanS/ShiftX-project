package com.shiftx.shiftpattern;

import java.sql.Date;
import java.time.*;

public class ShiftService {
	public void processShiftPattern(String shiftPattern) throws ServiceException {
		
		String[] dayWisePattern = shiftPattern.split(",");
		// 0900-1730-B30
		for (int i = 0; i < dayWisePattern.length; i++) {
			if (!dayWisePattern[i].equals("OFF")) {
				processDayPattern(dayWisePattern[i]);
			}
		}
	}

	public void processDayPattern(String pattern) throws ServiceException {
		DayInfoVO dayInfoVO = parseDayInfo(pattern);
	}

	public DayInfoVO parseDayInfo(String pattern) throws ServiceException {
		if(pattern == null) {
			throw new ServiceException("Pattern should not be null");
		}
		DayInfoVO dayInfoVO = new DayInfoVO();
			String[] dayInfo = pattern.split("-");
			if(dayInfo.length != 3) {
				throw new ServiceException("Invalid Pattern");
			}
			dayInfoVO.setStartTimeStr(dayInfo[0]);
			dayInfoVO.setEndTimeStr(dayInfo[1]);
			dayInfoVO.setBreakMinsStr(dayInfo[2].substring(1));
			
			Instant startTime=convertStringTimeToInstant(dayInfoVO.getStartTimeStr());
			Instant endTime=convertStringTimeToInstant(dayInfoVO.getEndTimeStr());
			
			int shiftDuration=minutesBetween(startTime,endTime);
			
			System.out.println(startTime);
			System.out.println(endTime);
			System.out.println(shiftDuration);
			
		return dayInfoVO;
	}
	public Instant convertStringTimeToInstant(String data) throws ServiceException {
		LocalDateTime localDatetime;
		//0900
		if(data.length()!=4) {
			throw new ServiceException("Invalid time format");
		}
		int hour=Integer.parseInt(data.substring(0,2));
		if(hour>23) {
			throw new ServiceException("Invalid hours format");
		}
		int minutes=Integer.parseInt(data.substring(2,4));
		if(minutes>59) {
			throw new ServiceException("Invalid minutes format");
		}
		
		LocalDate currentdate=LocalDate.now();
		
		LocalTime localTime=LocalTime.of(hour, minutes);
		localDatetime=LocalDateTime.of(currentdate, localTime);
		
		
		return localDatetime.toInstant(ZoneOffset.UTC);
	}
	public int minutesBetween(Instant fromTime,Instant toTime) throws ServiceException {
		if(fromTime.compareTo(toTime)>0) {
			throw new ServiceException("Invalid time range: The end time cannot be less than the start time.");
		}
		Duration duration=Duration.between(fromTime, toTime);
		long minutes=duration.toMinutes();

		
		return (int) minutes;
	}
}
