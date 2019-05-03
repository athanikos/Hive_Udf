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
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.StringObjectInspector;
import org.apache.hadoop.io.Text;

@Description(
		name = "GetFieldItemAt",
		value = "_FUNC_(str,int,str) "				+ "",
		extended = "Example:\n" +
		"  > //select GetFieldItemAt('ActiveCards',1,'hdfs://hdp.ey:8020/user/admin/testdata/PopulationFile2')\n" 
       )
public class GetFieldItemAt extends GenericUDF {
	private PrimitiveObjectInspector fieldNameInspector;
	private PrimitiveObjectInspector firstInputValueInspector;
	private StringObjectInspector fileInspector;
	private Map<String,String> lookup;
    private String innerDelimeter = ":"; 
	private HdfsLookup  hdfslookup; 
	
	@Override
	public ObjectInspector initialize(ObjectInspector[] args) throws UDFArgumentException {
				if (args.length != 3  ) throw new UDFArgumentLengthException("GetFieldItemAt needs  3   arguments ");

				this.fieldNameInspector = (StringObjectInspector) args[0];
				this.firstInputValueInspector = (PrimitiveObjectInspector) args[1];
			 	this.fileInspector = (StringObjectInspector) args[args.length-1];
			    hdfslookup = new HdfsLookup();
		        
			    return PrimitiveObjectInspectorFactory.javaStringObjectInspector;
	} 
  
	@Override
	public Text evaluate(DeferredObject[] args) throws HiveException
	{
			    String fieldName = (String) fieldNameInspector.getPrimitiveJavaObject(args[0].get());
		        int inputValue = Integer.parseInt(firstInputValueInspector.getPrimitiveJavaObject(args[1].get()).toString());
		        String rightPart="";
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
			      rightPart = LineParser.getRightPart(fieldName, lookup);
				}
			    catch (IOException e1)
			    {
					throw new HiveException(e1  + " while LineParser getRightPart " );
				}
			    
				try 
				{
					String[] items = rightPart.split(Pattern.quote(innerDelimeter));
		            String innerItem = items[inputValue];
					if (innerItem==null )
					return null;
					return new Text(innerItem  );
				} 
				catch (Exception e)
				{
					throw new HiveException(e  + " while evaluating expression " + rightPart);
				}
	}

	@Override
	public String getDisplayString(String[] args) 
	{	                   
		return "Method call: GetFieldItemAt(" + args[0] + ", " + args[1] + ", " + args[2] + ")";
	}

}