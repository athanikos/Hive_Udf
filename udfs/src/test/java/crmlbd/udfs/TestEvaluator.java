package crmlbd.udfs;

import static org.junit.Assert.*;
import java.lang.reflect.InvocationTargetException;
import org.codehaus.commons.compiler.CompileException;
import org.codehaus.janino.ScriptEvaluator;
import org.junit.Test;

public class TestEvaluator {
	
	private String monthDifferenceBand = "x>=0 && x<=3 ? \"01\" : x>=3 && x<=6 ? \"02\" :x>=6 && x<=12 ? \"03\" : x>12 ? \"04\" : x==-999 ? \"05\" : \"00\" ";
	private String aegeanSpecifications = "y>=1 && y<=9 && x>=0 && x<=3 ? \"01\" : y>=1 && y<=9 && x>=3 && x<=6 ? \"02\" : y>=1 && y<=9 && x>=6 && x<=12 ? \"03\" : y>=1 && y<=9 && x>12 ? \"04\" : y>=1 && y<=9 && x==-999 ? \"05\" : \"00\" ";
	private String activeCards = "x==1 ? \"01\" :  x==2 ? \"02\" : x==3 ? \"03\" : x==4 ? \"04\" :  x==5 ? \"05\" :  x==6 ? \"06\" : x==7 ? \"07\" : x==8 ? \"08\" : x==9 ? \"09\" : x>9 ? \"0A\":  \"00\"";
	private String redemptionFrequency = "x==0 ? \"01\" :x==1 ? \"02\" : x>=2 && x<= 3 ? \"03\" : x>=4 && x<= 5 ? \"04\" : x>=6 && x<=10 ? \"05\" : x>10 ? \"06\" : \"00\" ";
	private String pointsredeemed = "x==0 ? \"01\" :x==1 ? \"02\" : x>=2 && x<= 3 ? \"03\" : x>=4 && x<= 5 ? \"04\" : x>=6 && x<=10 ? \"05\" : x>10 ? \"06\" : \"00\" ";
	private String averagePointsRedeemed= "x>=1 && x<=1000 ? \"01\" :x>=1001 && x<=2000 ? \"02\" : x>=2001 && x<= 3000 ? \"03\" : x>=3001 && x<=4000 ? \"04\" : x>=4001 && x<=5000 ? \"05\" : x>=5001 && x<=6000 ? \"06\" : x>=6001 && x<=10000 ? \"07\" : x>=10001 && x<=15000 ? \"08\" :  x>=15001 && x<=20000 ? \"09\" : x>=20001 && x<=25000 ? \"10\" : x>=25001 && x<=30000 ? \"11\" : x>=30001 && x<=50000 ? \"12\" : x>=50001 && x<=75000 ? \"13\" : x>=75001 && x<=100000 ? \"14\" : x>=100001 && x<=200000 ? \"15\" : x>200000 ? \"16\" : \"00\" ";
	private String bonusSchemeSegmetation = "x>=200 && x<=600 && y>=1 && y<=11 || x>=600 && x<=800 && y>=6 && y<=16 ? \"01\" :x>0 && x<200 && y>=1 && y<=11 || x>=200 && x<=600 && y>=12 && y<= 16 ? \"02\" : x>0 && x< 200 && y> 12 || x>=200 && x<= 600 && y>20 ? \"03\" : x>=600 && x<= 800 && y>=1 && y<=5 || x>800 && y>=1 && y<=16 ? \"04\" : x>=200 && x<=600 && y>=16 && y<=19 || x>=600 && x<=800 && y>16 ? \"05\" : x>800 && y>16 ? \"06\" : \"00\" ";
	private String sectorPenetration = "x>=0 && x<=3 ? \"01\" : x>=3 && x<=6 ? \"02\" :x>=6 && x<=12 ? \"03\" : x>12 ? \"04\" : x==-999 && y!= -999 ? \"05\" : x==-999 && y== -999 ? \"06\" : \"00\" ";
	private static Evaluator _evaluator = new Evaluator();
	
	@Test
    public void testSwitch() throws CompileException, InvocationTargetException  {
        ScriptEvaluator se = new ScriptEvaluator();
        se.setReturnType(String.class);
        se.cook("switch(1) { case 1: return \"42\"; default: return null; }");
        assertEquals("42", se.evaluate(new Object[0]));
    }  
    
    @Test
    public void testSwitch3()  throws CompileException, InvocationTargetException  {
        ScriptEvaluator se = new ScriptEvaluator();
        se.setReturnType(String.class); 
        String expr = "switch(x) { case 01: return \"42\"; case 02: return \"43\"; default: return null; }";
        se.cook(expr.replace("x", "01"));      
        assertEquals("42", se.evaluate(new Object[0]));
        se.cook(expr.replace("x", "02"));
        assertEquals("43", se.evaluate(new Object[0]));
        se.cook(expr.replace("x", "0232"));
        assertEquals(null, se.evaluate(new Object[0]));
        
        String result =
        		_evaluator.evaluateScript("switch(x) { case 01: return \"42\"; case 02: return \"43\"; default: return null; }"
        		,"01"
        		);
        assertEquals(result, "42");
   
        result =
    		   _evaluator.evaluateScript("switch(x) { case 01: return \"42\"; case 02: return \"43\"; default: return null; }"
                		,"1"
                		);
                assertEquals(result, "42");
                
         result =
                    		_evaluator.evaluateScript("if (x==\"01\") return \"16-25\"; else if (x==\"02\") return \"26-40\"; else return null;   "
                                 		,"\"01\""
                                 		);
                                 assertEquals(result, "16-25");
                                 
         result =
                    		_evaluator.evaluateScript("if (x==\"01\") return \"16-25\"; else if (x==\"02\") return \"26-40\"; else return null;   "
                                         		,"\"02\""
                                         		);
                                         assertEquals(result, "26-40");
         result =
                    		  _evaluator.evaluateScript("if (x==\"01\") return \"16-25\"; else if (x==\"02\") return \"26-40\"; else return null;   "
                                                 		,"\"11\""
                                                 		);
                                                 assertEquals(result, null);                      
    }
         
	@Test
	public void TEstStringToString() throws CompileException, InvocationTargetException {
		assertEquals("M", _evaluator.evaluateExpression("x==\"00\"?\"M\":\"F\"", "00" ));
	} 

	@Test
	public void  TestAgeBandString() throws CompileException, InvocationTargetException {
		assertEquals("16-25", _evaluator.evaluateExpression("x==\"01\"?\"16-25\":\"26-40\"", "01" ));
		assertEquals("26-40", _evaluator.evaluateExpression("x==\"01\"?\"16-25\":\"26-40\"", "02" ));
	} 
	 
	@Test
	public void TestmonthDifferenceBand() throws CompileException, InvocationTargetException {
		assertEquals("01", _evaluator.evaluateExpression(monthDifferenceBand, 1, -999));
		assertEquals("02", _evaluator.evaluateExpression(monthDifferenceBand, 5, -999));
		assertEquals("03", _evaluator.evaluateExpression(monthDifferenceBand, 8, -999));
		assertEquals("04", _evaluator.evaluateExpression(monthDifferenceBand, 15, -999));
		assertEquals("05", _evaluator.evaluateExpression(monthDifferenceBand, -999, -999));
		assertEquals("00", _evaluator.evaluateExpression(monthDifferenceBand, -1, 5));
	}
	
	@Test
	public void TestaegeanSpecifications() throws CompileException, InvocationTargetException {
		assertEquals("01",_evaluator.evaluateExpression(aegeanSpecifications, 2, 1));
		assertEquals("02",_evaluator.evaluateExpression(aegeanSpecifications, 4, 1));
		assertEquals("03",_evaluator.evaluateExpression(aegeanSpecifications, 8, 1));
		assertEquals("04",_evaluator.evaluateExpression(aegeanSpecifications, 13, 1));
		assertEquals("05",_evaluator.evaluateExpression(aegeanSpecifications, -999, 1));
		assertEquals("00",_evaluator.evaluateExpression(aegeanSpecifications, -999, -999));
	}
	
	@Test
	public void TestActiveCards() throws CompileException, InvocationTargetException {
		assertEquals("00",_evaluator.evaluateExpression(activeCards, 0, 123));
		assertEquals("01",_evaluator.evaluateExpression(activeCards, 1, 123));
		assertEquals("02",_evaluator.evaluateExpression(activeCards, 2, 123));
		assertEquals("03",_evaluator.evaluateExpression(activeCards, 3, 123));
		assertEquals("04",_evaluator.evaluateExpression(activeCards, 4, 123));
		assertEquals("05",_evaluator.evaluateExpression(activeCards, 5, 123));
		assertEquals("06",_evaluator.evaluateExpression(activeCards, 6, 123));
		assertEquals("07",_evaluator.evaluateExpression(activeCards, 7, 123));
		assertEquals("08",_evaluator.evaluateExpression(activeCards, 8, 123));
		assertEquals("09",_evaluator.evaluateExpression(activeCards, 9, 123));
		assertEquals("0A",_evaluator.evaluateExpression(activeCards, 10, 123));
	}
	
	@Test
	public void TestredemptionFrequency() throws CompileException, InvocationTargetException {
		assertEquals("01",_evaluator.evaluateExpression(redemptionFrequency, 0, 123));
		assertEquals("02",_evaluator.evaluateExpression(redemptionFrequency, 1, 123));
		assertEquals("03",_evaluator.evaluateExpression(redemptionFrequency, 3, 123));
		assertEquals("04",_evaluator.evaluateExpression(redemptionFrequency, 4, 123));
		assertEquals("05",_evaluator.evaluateExpression(redemptionFrequency, 6, 123));
		assertEquals("06",_evaluator.evaluateExpression(redemptionFrequency, 11, 123));
		assertEquals("00",_evaluator.evaluateExpression(redemptionFrequency, -999, 123));
	}
	
	@Test
	public void Testpointsredeemed() throws CompileException, InvocationTargetException {
		assertEquals("01",_evaluator.evaluateExpression(pointsredeemed, 0, 123));
		assertEquals("02",_evaluator.evaluateExpression(pointsredeemed, 1, 123));
		assertEquals("03",_evaluator.evaluateExpression(pointsredeemed, 3, 123));
		assertEquals("04",_evaluator.evaluateExpression(pointsredeemed, 4, 123));
		assertEquals("05",_evaluator.evaluateExpression(pointsredeemed, 6, 123));
		assertEquals("06",_evaluator.evaluateExpression(pointsredeemed, 11, 123));
		assertEquals("00",_evaluator.evaluateExpression(pointsredeemed, -999, 123));
	}
	
	@Test
	public void TestaveragePointsRedeemed() throws CompileException, InvocationTargetException {
		assertEquals("01",_evaluator.evaluateExpression(averagePointsRedeemed, 500, 123));
		assertEquals("02",_evaluator.evaluateExpression(averagePointsRedeemed, 1500, 123));
		assertEquals("03",_evaluator.evaluateExpression(averagePointsRedeemed, 2500, 123));
		assertEquals("04",_evaluator.evaluateExpression(averagePointsRedeemed, 3500, 123));
		assertEquals("05",_evaluator.evaluateExpression(averagePointsRedeemed, 4500, 123));
		assertEquals("06",_evaluator.evaluateExpression(averagePointsRedeemed, 5500, 123));
		assertEquals("07",_evaluator.evaluateExpression(averagePointsRedeemed, 8500, 123));
		assertEquals("08",_evaluator.evaluateExpression(averagePointsRedeemed, 13500, 123));
		assertEquals("09",_evaluator.evaluateExpression(averagePointsRedeemed, 18500, 123));
		assertEquals("10",_evaluator.evaluateExpression(averagePointsRedeemed, 20500, 123));
		assertEquals("11",_evaluator.evaluateExpression(averagePointsRedeemed, 27500, 123));
		assertEquals("12",_evaluator.evaluateExpression(averagePointsRedeemed, 40000, 123));
		assertEquals("13",_evaluator.evaluateExpression(averagePointsRedeemed, 60000, 123));
		assertEquals("14",_evaluator.evaluateExpression(averagePointsRedeemed, 90000, 123));
		assertEquals("15",_evaluator.evaluateExpression(averagePointsRedeemed, 150000, 123));
		assertEquals("16",_evaluator.evaluateExpression(averagePointsRedeemed, 350000, 123));
		assertEquals("00",_evaluator.evaluateExpression(averagePointsRedeemed, -999, 123));
	}
	
	@Test
	public void TestbonusSchemeSegmetation() throws CompileException, InvocationTargetException {
		assertEquals("01",_evaluator.evaluateExpression(bonusSchemeSegmetation, 300, 10));
		assertEquals("02",_evaluator.evaluateExpression(bonusSchemeSegmetation, 150, 10));
		assertEquals("03",_evaluator.evaluateExpression(bonusSchemeSegmetation, 150, 15));
		assertEquals("04",_evaluator.evaluateExpression(bonusSchemeSegmetation, 700, 2));
		assertEquals("05",_evaluator.evaluateExpression(bonusSchemeSegmetation, 700, 18));
		assertEquals("06",_evaluator.evaluateExpression(bonusSchemeSegmetation, 900, 18));
		assertEquals("00",_evaluator.evaluateExpression(bonusSchemeSegmetation, -999, 18));
	}
	
	@Test
	public void TestSectorPenetration() throws CompileException, InvocationTargetException {
		assertEquals("01",_evaluator.evaluateExpression(sectorPenetration, 1, -999));
		assertEquals("02",_evaluator.evaluateExpression(sectorPenetration, 5, -999));
		assertEquals("03",_evaluator.evaluateExpression(sectorPenetration, 8, -999));
		assertEquals("04",_evaluator.evaluateExpression(sectorPenetration, 15, -999));
		assertEquals("05",_evaluator.evaluateExpression(sectorPenetration, -999, 5));
		assertEquals("06",_evaluator.evaluateExpression(sectorPenetration, -999, -999));
		assertEquals("00",_evaluator.evaluateExpression(sectorPenetration, -1, 5));
	}
	

	@Test
	public void TestExpression() throws CompileException, InvocationTargetException 
	{
		assertEquals("01",_evaluator.evaluateExpression("x>=0 && x<=3 ? \"01\" : \"00\"", 2, 5));	
		assertEquals("00",_evaluator.evaluateExpression("x>=0 && x<=3 ? \"01\" : \"00\"", 7, 5));	
	}

}
