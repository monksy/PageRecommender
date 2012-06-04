/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package recommend;

import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author steven
 */
public class PageTreesTest {
    
    public PageTreesTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test(expected=NullPointerException.class)
    public void testConvertSessionsIntoPageTreesNull() {
        Map<IPDateDatum, List<LogLine>> sessions = null;
        PageTrees.convertSessionsIntoPageTrees(sessions);
        
    }
    
    @Test
    public void testConvertSessionsIntoPageTreesFlat() {
        Map<IPDateDatum, List<LogLine>> sessions = new HashMap<IPDateDatum, List<LogLine>>();
        LogLine first = 
                new LogLine("182.159.110.144 - - [29/Apr/2012:01:17:35 +0000] \"GET /p/PageOne HTTP/1.1\" 200 4773 \"-\" \"Java/1.6.0_20");
        LogLine second = 
                new LogLine("182.159.110.144 - - [29/Apr/2012:02:17:35 +0000] \"GET /p/PageTwo HTTP/1.1\" 200 4773 \"-\" \"Java/1.6.0_20");
        LogLine third = 
                new LogLine("182.159.110.144 - - [29/Apr/2012:03:18:35 +0000] \"GET /p/PageThree HTTP/1.1\" 200 4773 \"-\" \"Java/1.6.0_20");
        sessions.put(new IPDateDatum("182.159.110.144", new Date(112, 3, 29, 01, 17, 35)), Arrays.asList(first));
        sessions.put(new IPDateDatum("182.159.110.144", new Date(112, 3, 29, 02, 17, 35)), Arrays.asList(second));
        sessions.put(new IPDateDatum("182.159.110.144", new Date(112, 3, 29, 03, 17, 35)), Arrays.asList(third));
        
       
        Map<String, List<PageCount>> expected = new HashMap<String, List<PageCount>>();
        expected.put("PageOne", new ArrayList<PageCount>());
        expected.put("PageTwo", new ArrayList<PageCount>());
        expected.put("PageThree", new ArrayList<PageCount>());
        
        Map<String, List<PageCount>> actual = PageTrees.convertSessionsIntoPageTrees(sessions);
        
        SharedTestUtilities.assertMapArray(expected, actual);
    }
    
    //Single Page with children
    
    @Test
    public void testConvertSessionsIntoPagesWithChildren() {
        Map<IPDateDatum, List<LogLine>> sessions = new HashMap<IPDateDatum, List<LogLine>>();
        LogLine first = 
                new LogLine("182.159.110.144 - - [29/Apr/2012:01:17:35 +0000] \"GET /p/PageOne HTTP/1.1\" 200 4773 \"-\" \"Java/1.6.0_20");
        LogLine second = 
                new LogLine("182.159.110.144 - - [29/Apr/2012:01:18:35 +0000] \"GET /p/PageTwo HTTP/1.1\" 200 4773 \"-\" \"Java/1.6.0_20");
        LogLine third = 
                new LogLine("182.159.110.144 - - [29/Apr/2012:01:19:35 +0000] \"GET /p/PageThree HTTP/1.1\" 200 4773 \"-\" \"Java/1.6.0_20");
        sessions.put(new IPDateDatum("182.159.110.144", new Date(112, 3, 29, 01, 17, 35)), Arrays.asList(first, second, third));
        
        Map<String, List<PageCount>> expected = new HashMap<String, List<PageCount>>();
        expected.put("PageOne", Arrays.asList(new PageCount("PageTwo")));
        expected.put("PageTwo", Arrays.asList(new PageCount("PageThree")));
        expected.put("PageThree", Collections.<PageCount>emptyList());
        
        Map<String, List<PageCount>> actual = PageTrees.convertSessionsIntoPageTrees(sessions);
        
        SharedTestUtilities.assertMapArray(expected, actual);
    }
    
    //Two pages with children
    
    @Test
    public void testConvertSessionsIntoMultiplePagesWithChildren() {
        Map<IPDateDatum, List<LogLine>> sessions = new HashMap<IPDateDatum, List<LogLine>>();
        LogLine first = 
                new LogLine("182.159.110.144 - - [29/Apr/2012:01:17:35 +0000] \"GET /p/PageOne HTTP/1.1\" 200 4773 \"-\" \"Java/1.6.0_20");
        LogLine second = 
                new LogLine("182.159.110.144 - - [29/Apr/2012:01:18:35 +0000] \"GET /p/PageTwo HTTP/1.1\" 200 4773 \"-\" \"Java/1.6.0_20");
        LogLine third = 
                new LogLine("182.159.110.144 - - [29/Apr/2012:01:19:35 +0000] \"GET /p/PageThree HTTP/1.1\" 200 4773 \"-\" \"Java/1.6.0_20");
        sessions.put(new IPDateDatum("182.159.110.144", new Date(112, 3, 29, 01, 17, 35)), Arrays.asList(first, second, third));
        sessions.put(new IPDateDatum("182.159.110.145", new Date(112, 3, 29, 01, 20, 35)), Arrays.asList(second, third));
      sessions.put(new IPDateDatum("182.159.110.146", new Date(112, 3, 29, 01, 17, 35)), Arrays.asList(first, third));
        
        
        Map<String, List<PageCount>> expected = new HashMap<String, List<PageCount>>();
        expected.put("PageOne", Arrays.asList(new PageCount("PageTwo"), new PageCount("PageThree")));
        expected.put("PageTwo", Arrays.asList(new PageCount("PageThree").increment(1)));
        expected.put("PageThree", Collections.<PageCount>emptyList());
        
        Map<String, List<PageCount>> actual = PageTrees.convertSessionsIntoPageTrees(sessions);
        
        SharedTestUtilities.assertMapArray(expected, actual);
    }
}
