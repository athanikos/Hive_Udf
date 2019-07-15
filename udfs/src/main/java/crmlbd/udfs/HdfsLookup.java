package crmlbd.udfs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import com.google.protobuf.TextFormat.ParseException;

public class HdfsLookup {
	
	  private  HashMap<String, String> lookup;
	  private String delimeter;
	
	  HdfsLookup()
	  {
		  lookup =new HashMap<String, String>();
		  delimeter = "|";
	  }
	   
	   public   HashMap<String, String> populate(String lookupFile) throws HiveException, IOException 
	   {
		       BufferedReader bufferedReader =null;
		       try	
		       {  
					        Path path = new Path(lookupFile);
					        Configuration configuration = new Configuration();
					        FileSystem fileSystem = FileSystem.get(new URI(UrlParser.ParseUrl(lookupFile)),configuration);
					        bufferedReader = new BufferedReader(new InputStreamReader(fileSystem.open(path)));
					        String line = null;
					        while ((line = bufferedReader.readLine()) != null) {
					            String[] items = line.split(Pattern.quote(delimeter));
					            lookup.put(items[0], items[1]);
					    	    line = bufferedReader.readLine();
					        }
					       
					        return lookup;		        
			} 
			catch (Exception e) 
		        {
					throw new HiveException(e + ": when attempting to access: " + lookupFile);
			} 
		        finally 
		        {
		        	if (bufferedReader!=null)
		        	bufferedReader.close();
		        }
	   	}
	
	   public LocalDate toDate(String dateString,String theFormat) throws ParseException, java.text.ParseException
	   {
			SimpleDateFormat formatter = new SimpleDateFormat(theFormat);
		  	Date dt =   formatter.parse(dateString);
			return LocalDate.parse( new SimpleDateFormat(theFormat).format(dt) );
	   }
}
