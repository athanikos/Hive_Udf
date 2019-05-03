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
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.StringObjectInspector;
import org.apache.hadoop.io.Text;
import org.codehaus.commons.compiler.CompileException;

@Description(
		name = "ComputeDescription",
		value = "_FUNC_(str,str,str)  - Converts a code value (01, 02 ... ) to string equivelent using an hdfs file as lookup (16-25, 26-40 etc)",
		extended = "Example:\n" +
		"  > //select ComputeDescription('AgeBand','01','hdfs://hdp.ey:8020/user/admin/testdata/PopulationFile2')\n" +
		"  16-25"
		+ "to install:\n //DROP FUNCTION MapCodeToDescription;\n" + 
		"//CREATE FUNCTION MapCodeToDescription as 'crmlbd.udfs.ComputeDescription' \n" + 
		"//USING JAR 'hdfs:///udf/udfs-0.0.1-SNAPSHOT.jar';"
		)
public class ComputeDescription extends GenericUDF {
	private StringObjectInspector fieldNameInspector;
	private StringObjectInspector fileInspector;
	private StringObjectInspector firstInputValueInspector;
	private Map<String,String> lookup;
	HdfsLookup hdfslookup;
	
	
	
	private transient Evaluator _evaluator = new Evaluator();
	
	
	@Override
	public ObjectInspector initialize(ObjectInspector[] args) throws UDFArgumentException {
			 	if (args.length != 3 )   
			 		throw new UDFArgumentLengthException("ComputeDescription needs 3 arguments ");
		
			 	this.fieldNameInspector = (StringObjectInspector) args[0];
				this.firstInputValueInspector = (StringObjectInspector) args[1];
				this.fileInspector = (StringObjectInspector) args[args.length-1];
				hdfslookup = new HdfsLookup();
				return PrimitiveObjectInspectorFactory.javaStringObjectInspector;
	} 
 
	@Override
	public Text evaluate(DeferredObject[] args) throws HiveException {
			    String fieldName   = fieldNameInspector.getPrimitiveJavaObject(args[0].get());
				String inputValue =  firstInputValueInspector.getPrimitiveJavaObject(args[1].get());
				String filepath =  fileInspector.getPrimitiveJavaObject(args[args.length-1].get()).toString();
				
				String rightPart="";
				if (lookup==null)
				try 
				{
						lookup  =	hdfslookup.populate(filepath);
				} catch (IOException e2) {
						throw new HiveException(e2  + " while reading  getRightPart  " +filepath );
				}
				
				try  
				{	
					rightPart = LineParser.getRightPart(fieldName,  lookup);
				} 
				catch (IOException e1) 
				{
					throw new HiveException(e1  + " while evaluating getRightPart " );
				}
				
				try 
				{
					return new Text( _evaluator.evaluateScript(rightPart,inputValue));
				} 
				catch (CompileException e )         { throw new HiveException(e + " while evaluating expression " + rightPart);  }
				catch (InvocationTargetException e) { throw new HiveException(e  + " while evaluating expression " + rightPart); }
	}

	@Override
	public String getDisplayString(String[] args) 
	{	                   
		return "Method call: ComputeDescription(" + args[0] + ", " + args[1] + ", " + args[2] + ")";
	}

}