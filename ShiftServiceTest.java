package com.shiftx.shiftpattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

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
			assertEquals(dayInfoVO,expectedDayInfo);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testParseDayInfo01() {
		ShiftService shiftService = new ShiftService();
		try {
			DayInfoVO dayInfoVO= shiftService.parseDayInfo(null);
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
		try {
			Instant instant= shiftService.convertStringTimeToInstant("0967");
		} catch (ServiceException e) {
			assertEquals("Invalid minutes format", e.getMessage());
		}
	
	}
	
	@Test
	public void testConvertStringTime03() {
		ShiftService shiftService = new ShiftService();
		try {
			Instant instant= shiftService.convertStringTimeToInstant("0900");
		      Instant expectedInstant = Instant.parse("2024-05-10T09:00:00Z");
			assertEquals(expectedInstant, instant);
		} catch (ServiceException e) {
			
		}
	
	}
	
	@Test
	public void testMinutesBetween() {
		Instant fromTime=Instant.parse("2024-05-08T09:00:00Z");
		Instant toTime=Instant.parse("2024-05-08T17:30:00Z");
		long expectedMinutes = Duration.between(fromTime, toTime).toMinutes();
		assertEquals(510,expectedMinutes);
	}
	
	@Test 
	public void testFindShiftType() {
		ShiftService shiftService=new ShiftService();
		String input="0900-1730-B30,0900-1730-B30,0900-1730-B30,OFF,OFF,0900-1730-B30,0900-1730-B30";
		try {
			Map<Integer,DayInfoVO> newMap=shiftService.processShiftPattern(input);
			String shiftType=shiftService.findShiftType(newMap);
			assertEquals("Regular", shiftType);
		} catch (ServiceException e) {	
		}
	}
	
	@Test 
	public void testFindShiftType01() {
		ShiftService shiftService=new ShiftService();
		String input="0900-1730-B30,0900-1730-B30,0900-1730-B30,OFF,OFF,0100-1730-B30,0900-1730-B30";
			Map<Integer, DayInfoVO> newMap;
			try {
				newMap = shiftService.processShiftPattern(input);
				String shiftType=shiftService.findShiftType(newMap);
				assertEquals("Variable",shiftType );
			} catch (ServiceException e) {

				e.printStackTrace();
			}
	}
	
	@Test 
	public void testFindShiftType02() {
		ShiftService shiftService=new ShiftService();
		String input="0900-1730-B40,0900-1730-B20,0900-1730-B50,OFF,OFF,0900-1730-B30,0900-1730-B30";
			Map<Integer, DayInfoVO> newMap;
			try {
				newMap = shiftService.processShiftPattern(input);
				String shiftType=shiftService.findShiftType(newMap);
				assertEquals("Regular",shiftType );
			} catch (ServiceException e) {

				e.printStackTrace();
			}	
	}
	
	@Test 
	public void testisValidShift() {
		ShiftService shiftService=new ShiftService();
		String input="0900-1730-B40,0900-1730-B20,0900-1730-B50,OFF,OFF,0900-1730-B30,0900-1730-B30";
			Map<Integer, DayInfoVO> newMap;
			try {
				newMap = shiftService.processShiftPattern(input);
				boolean isValid=shiftService.isValidShift(newMap);
				assertEquals(true,isValid );
			} catch (ServiceException e) {

				e.printStackTrace();
			}	
	}
	@Test 
	public void testisValidShift01() {
		ShiftService shiftService=new ShiftService();
		String input="0900-1730-B40,OFF,0900-1730-B20,0900-1730-B50,OFF,0900-1730-B30,0900-1730-B30";
			Map<Integer, DayInfoVO> newMap;
			try {
				newMap = shiftService.processShiftPattern(input);
				boolean isValid=shiftService.isValidShift(newMap);
				assertEquals(true,isValid );
			} catch (ServiceException e) {

				e.printStackTrace();
			}	
	}
	
	@Test 
	public void testisValidShift02() {
		ShiftService shiftService=new ShiftService();
		String input="0900-1730-B40,0030-1730-B20,0900-1730-B50,OFF,OFF,0900-1730-B30,0900-1730-B30";
			Map<Integer, DayInfoVO> newMap;
			try {
				newMap = shiftService.processShiftPattern(input);
				boolean isValid=shiftService.isValidShift(newMap);
				assertEquals(false, isValid);
			} catch (ServiceException e) {

				e.printStackTrace();
			}	
	}

}
