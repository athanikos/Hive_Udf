package crmlbd.udfs;

import static org.junit.Assert.*;
import java.time.LocalDate;
import org.junit.Test;
import com.google.protobuf.TextFormat.ParseException;

public class TestMonthCalculator {
 

	@Test 
	public void testSameDate() throws ParseException, java.text.ParseException
	{
	  LocalDate current = LocalDate.of(2011,1,1);
	  LocalDate past = LocalDate.of(2011,1,1);
  	  int  months = 	MonthCalculator.calculate(past ,current);
	  assertEquals(months,0);
	} 
	
	@Test 
	public void testSameYearMonth() throws ParseException, java.text.ParseException
	{
		  LocalDate current = LocalDate.of(2011,1,11); 
		  LocalDate past = LocalDate.of(2011,1,1);
	  	  int  months = 	MonthCalculator.calculate(past,current);
	  assertEquals(months,0);
	}
	
	@Test 
	public void testpreviousMonth() throws ParseException, java.text.ParseException
	{
		  LocalDate current = LocalDate.of(2011,1,1);
		  LocalDate past = LocalDate.of(2010,12,31);
	  	  int  months = 	MonthCalculator.calculate(past,current);
	  assertEquals(months,1);
	}
	
	
	@Test 
	public void testpreviousMonth2() throws ParseException, java.text.ParseException
	{
		  LocalDate current = LocalDate.of(2011,1,11);
		  LocalDate past = LocalDate.of(2010,12,10);
	      int  months = 	MonthCalculator.calculate(past,current);
	       assertEquals(months,1);
	}
	
	@Test 
	public void testCurrentBeforePast() throws ParseException, java.text.ParseException
	{
		  LocalDate current = LocalDate.of(2010,12,24);
		  LocalDate past = LocalDate.of(2011,10,24);
	    	int  months = 	MonthCalculator.calculate(past,current);
	     assertEquals(months,-1);
	}
	
	
	
	@Test 
	public void testDifferentyear() throws ParseException, java.text.ParseException
	{
		
		  LocalDate past = LocalDate.of(2011,10,24);
		  LocalDate current = LocalDate.of(2018,10,24);
	    	int  months = 	MonthCalculator.calculate(past,current);
	     assertEquals(months,7*12);
	}
	
	
	
	@Test 
	public void testtoDate() throws ParseException, java.text.ParseException
	{
		LocalDate expectedLd = LocalDate.of(2018, 10, 1);
		assertEquals(MonthCalculator.toDate("2018-10-01","YYYY-MM-dd").getMonthValue(),expectedLd.getMonthValue()); 
		assertEquals(MonthCalculator.toDate("2018-10-01","YYYY-MM-dd").getMonthValue(),expectedLd.getMonthValue()); 
		assertEquals(MonthCalculator.toDate("2018-10-01","YYYY-MM-dd").getYear(),expectedLd.getYear()); 
	    
		expectedLd = LocalDate.of(2012, 07, 1);
		assertEquals(MonthCalculator.toDate("2012-07-01","YYYY-MM-dd").getMonthValue(),expectedLd.getMonthValue()); 
	 
	}
	
	 
	
	 
}
