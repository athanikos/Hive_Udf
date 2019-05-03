package crmlbd.udfs;

import static org.junit.Assert.assertEquals;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class TestLookup  {	
    @Test
    public void MapContainsKey() {
    	 Map<String,String> lookup= new HashMap<String,String>();
    	 lookup.put("AgeBand","testme");
     	 assertEquals(lookup.containsKey("AgeBand"), true);
    }
}
  