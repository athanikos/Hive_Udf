package crmlbd.udfs;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;



public class MonthCalculator {

	 
	public static int  calculate(LocalDate past, LocalDate current)
	{ 
		if (current==null || past == null )
			return -1;
	 	int res =  getMonthsDifference(past,current);
		return res;
	}

	public static LocalDate toDate(String dateString,String theFormat) throws ParseException
	{
		    DateTimeFormatter DATEFORMATTER1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		    DateTimeFormatter DATEFORMATTER = new DateTimeFormatterBuilder().append(DATEFORMATTER1)
		    .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
		    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
		    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
		    .toFormatter();
		    return  LocalDateTime.parse(dateString, DATEFORMATTER).toLocalDate();
	}
	

	private  static final int  getMonthsDifference(LocalDate past, LocalDate current) {
	
		if (past.compareTo(current)>0)
			return -1;
		
		return  Period.between(past.withDayOfMonth(1),current.withDayOfMonth(1)).getMonths() +  Period.between(
						past.withDayOfMonth(1),current.withDayOfMonth(1)).getYears()*12;
		
	} 
	
	
}
