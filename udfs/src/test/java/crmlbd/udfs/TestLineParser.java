package crmlbd.udfs;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.codehaus.commons.compiler.CompileException;
import org.junit.Test;

public class TestLineParser {
	@Test
	public void TEstStringToString() throws CompileException, InvocationTargetException, HiveException, IOException
	{
		HashMap<String, String> lookup= new HashMap<String, String>();
	    lookup.put("ActiveCards" ,"x>=0 && x<=3 ? \"01\" : \"00\""   );
		assertEquals(LineParser.getRightPart("ActiveCards", lookup),"x>=0 && x<=3 ? \"01\" : \"00\"");
    } 
}
