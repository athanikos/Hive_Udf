package crmlbd.udfs;
import java.io.IOException;
import java.util.Map;
import org.apache.hadoop.hive.ql.metadata.HiveException;

public class LineParser {
	  
	   public static String getRightPart(String fieldName, Map<String,String> lookup) throws HiveException, IOException
	   {
			if (lookup.containsKey(fieldName) )
			{	 
				String rightPart ="";
				try  
				{
					rightPart =   lookup.get(fieldName);
					return rightPart;
				} 
				catch (Exception  e ) {
	               			throw new HiveException(e  + " while parsining line " + rightPart);
				}
			}
			else
			return null;
	   }	  
}
