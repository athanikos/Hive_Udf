package crmlbd.udfs;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.StringObjectInspector;
import org.apache.hadoop.io.Text;
import org.codehaus.commons.compiler.CompileException;

@Description(
		name = "ComputeField",
		value = "_FUNC_(str,int,int,str) or _FUNC_(str,int,str)  - Converts a value to string equiavlent using an hdfs file as lookup",
		extended = "Example:\n" +
		"  > //select ComputeField('ActiveCards',1,"
		+ "'hdfs://localhost:8020/user/admin/testdata/PopulationFile2')\n" +
		"  01"
		+ " sample lookup entry (remove escape char)    ActiveCards|x==0 ? \"0A\" : x==1 ? \"01\" :  x==2 ? \"02\" : x==3 ? \"03\" : x==4 ? \"04\" :  x==5 ? \"05\" :  x==6 ? \"06\" : x==7 ? \"07\" : x==8 ? \"08\" : x==9 ? \"09\" : \"00\""
		+ "to install:\n //DROP FUNCTION ComputeField;\n" + 
		"//CREATE FUNCTION ComputeField as 'crmlbd.udfs.ComputeField' \n" + 
		"//USING JAR 'hdfs:///udf/udfs-0.0.1-SNAPSHOT.jar';"
		)
public class ComputeField extends GenericUDF { 
	
	private PrimitiveObjectInspector fieldNameInspector;
	private PrimitiveObjectInspector firstInputValueInspector;
	private PrimitiveObjectInspector secondInputValueInspector;
	private StringObjectInspector fileInspector;
	private Map<String,String> lookup;  
	HdfsLookup hdfslookup;
	private Evaluator _evaluator = new Evaluator();
	  
	@Override
	public ObjectInspector initialize(ObjectInspector[] args) throws UDFArgumentException {
				if (args.length != 3 &&  args.length  !=4 ) throw new UDFArgumentLengthException("ComputeField needs  3 or 4   arguments ");
			 
				this.fieldNameInspector = (StringObjectInspector) args[0];
				this.firstInputValueInspector = (PrimitiveObjectInspector) args[1];
				if (args.length>3)
				this.secondInputValueInspector = (PrimitiveObjectInspector) args[2];
				this.fileInspector = (StringObjectInspector) args[args.length-1];
			    hdfslookup = new HdfsLookup();
					
				return PrimitiveObjectInspectorFactory.javaStringObjectInspector;
	}
 
	@Override
	public Text evaluate(DeferredObject[] args) throws HiveException {
			    String fieldName = (String) fieldNameInspector.getPrimitiveJavaObject(args[0].get());
			    double inputValue = Double.parseDouble(firstInputValueInspector.getPrimitiveJavaObject(args[1].get()).toString());
				double secondValue = 0;
			
				if (args.length>3)
				secondValue = Double.parseDouble(secondInputValueInspector.getPrimitiveJavaObject(args[2].get()).toString());
				String filepath =  fileInspector.getPrimitiveJavaObject(args[args.length-1].get()).toString();
			
				try 
			    {
					if (lookup==null)
					lookup  =	hdfslookup.populate(filepath);
			     	String rightPart= LineParser.getRightPart(fieldName, lookup);
					return new Text( _evaluator.evaluateExpression(rightPart,inputValue,secondValue) );
				} 
				catch (IOException e1) 
				{	throw new HiveException(e1  + " while evaluating getRightPart " ); }
				catch (CompileException e) {
					throw new HiveException(e  + " while evaluating getRightPart " );
				} catch (InvocationTargetException e) {
					throw new HiveException(e  + " while evaluating getRightPart " );
				}
	}

	@Override
	public String getDisplayString(String[] args) 
	{	                   
		return "Method call: ComputeField(" + args[0] + ", " + args[1] + ", " + args[2] + ")";
	}

}