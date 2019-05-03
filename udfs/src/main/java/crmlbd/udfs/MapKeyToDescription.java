package crmlbd.udfs;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.StringObjectInspector;
import org.apache.hadoop.io.Text;

@Description(
		name = "MapKeyToDescription",
		value = "_FUNC_(str,str,str)  - Converts a code value (AgeBand01, AgeBand02 ... ) to string equivelent using an hdfs file as lookup (16-25, 26-40 etc)",
		extended = "Example:\n" +
		"  > //select ComputeDescription('AgeBand','01','hdfs://hdp.ey:8020/user/admin/testdata/PopulationFile2')\n" +
		"  16-25"
		+ "to install:\n //DROP FUNCTION MapKeyToDescription;\n" + 
		"//CREATE FUNCTION ComputeDescription as 'crmlbd.udfs.MapKeyToDescription' \n" + 
		"//USING JAR 'hdfs:///udf/udfs-0.0.1-SNAPSHOT.jar';"
		)
public class  MapKeyToDescription  extends GenericUDF {
	private StringObjectInspector fieldNameInspector;
	private StringObjectInspector fileInspector;
	private StringObjectInspector firstInputValueInspector;
    private String innerDelimeter = ":"; 
	private Map<String,String> lookup;
	HdfsLookup hdfslookup;
	
	@Override
	public ObjectInspector initialize(ObjectInspector[] args) throws UDFArgumentException {
			 	if (args.length != 2 )   
			 		throw new UDFArgumentLengthException("MapToDescription needs 2 arguments ");
		
			 	this.fieldNameInspector = (StringObjectInspector) args[0];
				this.fileInspector = (StringObjectInspector) args[args.length-1];
				return PrimitiveObjectInspectorFactory.javaStringObjectInspector;
	} 

	@Override
	public Text evaluate(DeferredObject[] args) throws HiveException {
	
		        String fieldName   = fieldNameInspector.getPrimitiveJavaObject(args[0].get());
				Integer inputValue = Integer.parseInt(firstInputValueInspector.getPrimitiveJavaObject(args[1].get()).toString());
				String filepath =  fileInspector.getPrimitiveJavaObject(args[args.length-1].get()).toString();
				
				if (lookup==null)
					try 
					{
							lookup  =	hdfslookup.populate(filepath);
					} catch (IOException e2) {
							throw new HiveException(e2  + " while reading  getRightPart  " +filepath );
					}
				
				try  
				{	
					String result =  LineParser.getRightPart(fieldName, lookup);
					if (result ==null )
						return null;
				
					return new Text(  result.split(Pattern.quote(innerDelimeter))[inputValue] );
				
				} 
				catch (IOException e1) 
				{
					throw new HiveException(e1  + " while evaluating getRightPart " );
				}
			
	}

	@Override
	public String getDisplayString(String[] args) 
	{	                   
		return "Method call: MapKeyToDescription(" + args[0] + ", " + args[1] + ", " + args[2] + ")";
	}

}