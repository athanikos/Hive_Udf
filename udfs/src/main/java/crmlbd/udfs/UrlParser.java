package crmlbd.udfs;

public class UrlParser {

	public static String  ParseUrl(String url)
	{
		int lastIndexOfDoubleQuotes = url.lastIndexOf(':');
		int endIndex = url.indexOf('/',lastIndexOfDoubleQuotes);
		return url.substring(0, endIndex);
	}		
}
