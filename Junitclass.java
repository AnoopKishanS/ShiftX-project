import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class Junitclass {

	@Test
	public void testData() {
		ShiftTiming st=new ShiftTiming();
		String data=st.getData();
		boolean isEmpty=data.isEmpty();
		assertFalse(isEmpty);
	}

	@Test
	public void testDatasize() {
		ShiftTiming st=new ShiftTiming();
		String data=st.getData();
		String days[]=data.split(",");
		assertEquals(7,days.length);
	}
	
	@Test
	public void testRegularshiftpattern(){
		ShiftTiming st=new ShiftTiming();
		String ShiftPattern=st.getData();
		String [] Pattern=ShiftPattern.split(",");
		
		String RegularPattern="0900-1730-B30";
		for(int i=0;i<Pattern.length;i++) {
			if(Pattern[i].length() > 3) {
			assertEquals(RegularPattern,Pattern[i]);
			}
		}
 	}
	
	@Test
	public void testVariableshiftpattern(){
		ShiftTiming st=new ShiftTiming();
		String ShiftPattern=st.getData();
		String [] Pattern=ShiftPattern.split(",");
		boolean anyNotEqual = false;
		
		String RegularPattern="0900-1730-B30";
		for(int i=0;i<Pattern.length;i++) {
			if(Pattern[i].length() > 3 && !Pattern[i].equals(RegularPattern)) {
				anyNotEqual=true;
				break;
			}
		}
		assertTrue(anyNotEqual);
 	}
	
	@Test
	public void testOfftype(){
		ShiftTiming st=new ShiftTiming();
		String ShiftPattern=st.getData();
		String [] Pattern=ShiftPattern.split(",");
		boolean offtype=false;
	
		for(int i=0;i<Pattern.length;i++) {
			if(Pattern[i].length() == 3 && !Pattern[i].equals("OFF")) {
			offtype=true;
			}
		}
		assertFalse(offtype);
 	}
	
	@Test
	public void timeListlength() {
		ShiftTiming st=new ShiftTiming();
		String ShiftPattern=st.getData();
		String [] shifttype=ShiftPattern.split(",");
		ArrayList<String> starttime=new ArrayList<String>();
		ArrayList<String> endtime=new ArrayList<String>();
		
		for(int i=0;i<shifttype.length;i++) {
			String Data=shifttype[i].toString().replace("OFF", "0000-0000");
			String Timex[]=Data.split("-");
			starttime.add(Timex[0]);
			endtime.add(Timex[1]);
			
	}
		assertEquals(starttime.size(),endtime.size());
	}
	
	 @Test
	    public void testTimingWithOffDay() {
	        ShiftTiming st = new ShiftTiming();
	        String shift = "OFF";
	        int interval = 0; 
	        String expectedOutput = "Off day";
	        
	        String timing = st.Timing(shift, interval);
	        
	        assertEquals(expectedOutput, timing);
	    }
	
	


	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
