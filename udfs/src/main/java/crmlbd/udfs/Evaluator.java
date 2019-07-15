package crmlbd.udfs;

import org.codehaus.commons.compiler.CompileException;
import org.codehaus.janino.ExpressionEvaluator;
import org.codehaus.janino.ScriptEvaluator;
import java.lang.reflect.InvocationTargetException;


public class Evaluator {
	public  String evaluateExpression(String expression, double parameter, double parameter1)  throws CompileException, InvocationTargetException
	{
	       ExpressionEvaluator ee = new ExpressionEvaluator();
		   ee.setParameters(new String[] { "x", "y" }, new Class[] { double.class, double.class });
	       ee.setExpressionType(String.class);  
	       ee.cook(expression);
	       return  ee.evaluate(new Object[] {parameter , parameter1 }).toString();
	}
	 
	public  String evaluateExpression(String expression, String  parameter)  throws CompileException, InvocationTargetException
	{
	        ExpressionEvaluator ee = new ExpressionEvaluator();  
		    ee.setParameters(new String[] { "x" }, new Class[] { String.class });
	        ee.setExpressionType(String.class);   
	     	ee.cook(expression);
			return  ee.evaluate(new Object[] {parameter  }).toString();
	}
	
	public  String evaluateScript(String expression, String  parameter) throws CompileException, InvocationTargetException
	{
		 ScriptEvaluator se = new ScriptEvaluator();
	     se.setReturnType(String.class);
   		 Object 	result = se.evaluate(new Object[0]);

   		 if (result ==null)
  	    	 return null; 
  	    
  	     return result.toString();
	}
}
