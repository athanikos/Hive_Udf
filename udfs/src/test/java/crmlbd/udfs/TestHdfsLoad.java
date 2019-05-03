package crmlbd.udfs;

import static org.junit.Assert.*;
import java.util.regex.Pattern;
import org.junit.Test;

public class TestHdfsLoad {
    @Test
    public void TestSplit()
    {
    	String line = "AgeBand|x==01,x==02,x==03|16-25,26-40,41-60";
   		String[] items = line.split(Pattern.quote("|"));
   		assertEquals(line.trim(),"AgeBand|x==01,x==02,x==03|16-25,26-40,41-60");
   		assertEquals(items[0],"AgeBand");
    }
    @Test
    public void TestSplitWithDoubleQuotes()
    {
    	String line = "AgeBand:x==01,x==02,x==03|16-25,26-40,41-60";
   		String[] items = line.split(Pattern.quote(":"));
    		assertEquals(items[0],"AgeBand");
    }
   
}
