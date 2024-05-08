package com.shiftx.shiftpattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.time.Instant;

import org.junit.Test;

public class ShiftServiceTest {

	@Test
	public void testParseDayInfo() {
		ShiftService shiftService = new ShiftService();
		DayInfoVO dayInfoVO;
		try {
			dayInfoVO = shiftService.parseDayInfo("0900-1730-B30");
			DayInfoVO expectedDayInfo = new DayInfoVO();
			expectedDayInfo.setStartTimeStr("0900");
			expectedDayInfo.setEndTimeStr("1730");
			expectedDayInfo.setBreakMinsStr("30");
			assertEquals(dayInfoVO, expectedDayInfo);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void testParseDayInfo01() {
		ShiftService shiftService = new ShiftService();
		DayInfoVO dayInfoVO;
		try {
			dayInfoVO = shiftService.parseDayInfo(null);
		} catch (ServiceException e) {
			assertEquals("Pattern should not be null", e.getMessage());
		}

	}

	@Test
	public void testParseDayInfo02() {
		ShiftService shiftService = new ShiftService();
		DayInfoVO dayInfoVO;
		try {
			dayInfoVO = shiftService.parseDayInfo("09001730-B30");
		} catch (ServiceException e) {
			assertEquals("Invalid Pattern", e.getMessage());
		}

	}
	@Test
	public void testConvertStringTime() {
		ShiftService shiftService = new ShiftService();
		Instant instant;
		try {
			instant= shiftService.convertStringTimeToInstant("900");
		} catch (ServiceException e) {
			assertEquals("Invalid time format", e.getMessage());
		}
	
	}
	@Test
	public void testConvertStringTime01() {
		ShiftService shiftService = new ShiftService();
		try {
			Instant instant= shiftService.convertStringTimeToInstant("2400");
		} catch (ServiceException e) {
			assertEquals("Invalid hours format", e.getMessage());
		}
	
	}
	@Test
	public void testConvertStringTime02() {
		ShiftService shiftService = new ShiftService();
		Instant instant;
		try {
			instant= shiftService.convertStringTimeToInstant("0967");
		} catch (ServiceException e) {
			assertEquals("Invalid minutes format", e.getMessage());
		}
	
	}
	
	@Test
	public void testMinutesBetween() {
		ShiftService shiftService = new ShiftService();
		Instant fromTime=Instant.parse("2024-05-08T09:00:00Z");
		Instant toTime=Instant.parse("2024-05-08T17:30:00Z");
		long expectedMinutes = Duration.between(fromTime, toTime).toMinutes();
		assertEquals(510,expectedMinutes);
	}
	
	@Test
	public void testMinutesBetween01() {
		ShiftService shiftService = new ShiftService();
		Instant fromTime=Instant.parse("2024-05-08T17:30:00Z");
		Instant toTime=Instant.parse("2024-05-08T07:30:00Z");
		try {
			shiftService.minutesBetween(fromTime, toTime);
		} catch (ServiceException e) {
			assertEquals("Invalid time range: The end time cannot be less than the start time.", e.getMessage());
		}		
	}

}
