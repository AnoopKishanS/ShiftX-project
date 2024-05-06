import java.time.LocalTime;
import java.util.ArrayList;
import java.time.Duration;


public class ShiftTiming {
	String time;
	LocalTime start_time;
	LocalTime end_time;
	String sttime[];
	String patterns;
	String datas;
	
		public String getData(){
			String data="0900-1730-B30,0900-1730-B30,OFF,0900-1730-B30,OFF,0900-1730-B30,0900-1730-B30";
		return data;
		}
		

	//Method to Display start time,end time,break and duration
public String Timing(String t,int interval) {
		time=t;
		String shifthours;
		LocalTime start_time;
		LocalTime end_time;
		
		if(time.length()>3) {
		String [] daytime=time.split("-");
		
		int starttimehours=Integer.parseInt(daytime[0].toString().substring(0,2));
		int starttimeminutes=Integer.parseInt(daytime[0].toString().substring(2,4));
		start_time=LocalTime.of(starttimehours,starttimeminutes);
		String shift_start_time=start_time.toString();
		
		int endtimehours=Integer.parseInt(daytime[1].toString().substring(0,2));
		int endtimeminutes=Integer.parseInt(daytime[1].toString().substring(2,4));
		end_time=LocalTime.of(endtimehours,endtimeminutes);
		String shift_end_time=end_time.toString();
		
		String breaktime=daytime[2].toString().substring(1,3);
		
		Duration duration;
		int comp=start_time.compareTo(end_time);
		if(comp<0) {
			duration=Duration.between(start_time,end_time);
		}
		else
		{
			duration=Duration.between(end_time,start_time);
		}
		
		shifthours=shift_start_time+" to "+shift_end_time+" break:"+breaktime+" minutes" + " Shift Duration is "+ duration+
				 " Interval is " + (interval / 60) + " hours and " + (interval % 60) + " minutes";
		
		}
		else
		{
			shifthours="Off day";
		}
		return shifthours;
	}

//Method to find Shift pattern
public String Pattern(String sp []) {
  
	boolean equal = true;
	for(int i=0;i<sp.length;i++) {
		if(sp[i].length()>3) {
			for(int j=i+1;j<sp.length;j++) {
				if(sp[j].length()>3)
				if(!sp[i].equals(sp[j])) {
					equal=false;
					break;
				}}}}
	if(equal) {
		return patterns="Regular";
	}
	else
	{
		return patterns="Variable";
	}}

//Method to find interval between each shift

public int[] Interval(String[] shifttype) {
	
	ArrayList<String> starttime=new ArrayList<String>();
	ArrayList<String> endtime=new ArrayList<String>();
	
	for(int i=0;i<shifttype.length;i++) {
		String Data=shifttype[i].toString().replace("OFF", "0000-0000");
		String Timex[]=Data.split("-");
		starttime.add(Timex[0]);
		endtime.add(Timex[1]);
	}
	
	 ArrayList<Integer> startMin = convertToMinutes(starttime);
     ArrayList<Integer> endMin = convertToMinutes(endtime);

     int[] intervals = new int[endMin.size()];
     int difference;
	 if(startMin.size()>0) {
		 for(int i=0;i<endMin.size()-1;i++) {
		 int shiftendtime=endMin.get(i);
		 int shiftstarttime=startMin.get(i+1);
		 if (shiftendtime == 0 || shiftstarttime == 0) {
			 shiftstarttime=startMin.get(i+2);
			 difference=shiftstarttime-shiftendtime+60*24;
         }
		 else if(shiftstarttime>shiftendtime)
		 {
			 difference=(shiftstarttime-720)-shiftendtime;
		 }
		 else		 
		 {
		    difference=shiftendtime-shiftstarttime;
		 
		 }
		 intervals[i]=difference;
		 }
	 }
	
	 return  intervals;
    	
	
}
private ArrayList<Integer> convertToMinutes(ArrayList<String> times) {
    ArrayList<Integer> minutes = new ArrayList<>();
    for (String time : times) {
        int hours = Integer.parseInt(time.substring(0, 2));
        int minutesOfDay = Integer.parseInt(time.substring(2, 4));
        minutes.add(hours * 60 + minutesOfDay);
    }
    return minutes;
}




	public static void main(String[] args) {
		ShiftTiming s=new ShiftTiming();
		
//		String data="0900-1730-B30,0900-1730-B30,OFF,0800-1730-B30,0900-0330-B30,2130-0330-B30,OFF";
		String data=s.getData();
		
		String [] Days=data.split(",");
		
		String [] shifttype=data.split(",");
		
		 int[] intervals = s.Interval(shifttype);
				
		for(int i=0;i<Days.length;i++) {
			Days[i]=s.Timing(Days[i].toString(),intervals[i]);
			System.out.println(Days[i]);
		}
		
		String shift_pattern=s.Pattern(shifttype);
		System.out.println(shift_pattern);
		
		
	}

}
