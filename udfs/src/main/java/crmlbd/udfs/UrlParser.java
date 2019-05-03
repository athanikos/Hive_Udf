package crmlbd.udfs;

public class UrlParser {

	//hdfs://hdp.galaxy.com:8020/user/admin/descri2
	public static String  ParseUrl(String url)
	{
		int lastIndexOfDoubleQuotes = url.lastIndexOf(':');
		int endIndex = url.indexOf('/',lastIndexOfDoubleQuotes);
		return url.substring(0, endIndex);
	}		
}
