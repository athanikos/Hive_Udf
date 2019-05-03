package crmlbd.udfs;

import static org.junit.Assert.*;
import org.junit.Test;


public class TestUrlParser {

	
	@Test 
	public void testUrlParser1() 
	{ 
		assertEquals("hdfs://hdp.galaxy.com:8020",UrlParser.ParseUrl("hdfs://hdp.galaxy.com:8020/user/admin/descri2"));
	} 
	
}
 