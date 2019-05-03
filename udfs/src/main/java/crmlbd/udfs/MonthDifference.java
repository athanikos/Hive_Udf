package crmlbd.udfs;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.StringObjectInspector;
import org.apache.hadoop.io.IntWritable;
import java.text.ParseException;
@Description(
		name = "MonthDifference",
		value = "_FUNC_(str,str) ",
		extended = "Example:\n" +
		"  > //select MonthDifference(date,date)\n" +"select MonthDifference(CURRENT_DATE;,'2011-10-24','YYYY-MM-dd');"
		)
public class MonthDifference extends GenericUDF {
    private transient String   dateFormat;
    private StringObjectInspector dateFormatInspector;
	PrimitiveObjectInspector outputOI;
	
	@Override
	public ObjectInspector initialize(ObjectInspector[] args) throws UDFArgumentException {
		  if (args.length != 3) 
		      throw new UDFArgumentLengthException("MonthDifference() requires 3 argument, got " + args.length);
		  outputOI = PrimitiveObjectInspectorFactory.writableIntObjectInspector;
		  dateFormatInspector=(StringObjectInspector) args[2];
		  return outputOI;
	}
 
	@Override
	public Object   evaluate(DeferredObject[] args) throws HiveException {
		if (args.length != 3) return null;
		if (args[0].get() == null || args[1].get() ==null ) 
		      return null ;

		dateFormat =  (String) dateFormatInspector.getPrimitiveJavaObject(args[2].get());
		
		try {
			return new IntWritable(MonthCalculator.calculate(MonthCalculator.toDate(args[0].get().toString(),dateFormat),MonthCalculator.toDate(args[1].get().toString(),dateFormat)));
		} catch (ParseException e) {
			throw new HiveException(e  + " while parsing  args:" + args[0].get().toString() + ", " +args[1].get().toString());
		}
	}

	@Override
	public String getDisplayString(String[] args) 
	{	                   
		return "Method call: MonthDifference(" + args[0] + ", " + args[1] +  ", " + args[2] + ")";
	}

}