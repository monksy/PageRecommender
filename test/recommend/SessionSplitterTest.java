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
public class SessionSplitterTest {

    private SessionSplitter instance;

    @Before
    public void setUp() {
        instance = new SessionSplitter();
    }

    @Test
    public void isBeforeRange() {
        Date originalDate = new Date(112, 1, 1, 1, 1, 1);
        Date actual = new Date(111, 1, 1, 1, 1, 1);

        assertFalse(instance.isWithinRange(originalDate.getTime(), actual.getTime()));
    }

    @Test
    public void isExactlyOnRange() {
        Date originalDate = new Date(112, 1, 1, 1, 1, 1);
        Date actual = new Date(112, 1, 1, 1, 1, 1);

        assertTrue(instance.isWithinRange(originalDate.getTime(), actual.getTime()));
    }

    @Test
    public void isInRange() {
        Date originalDate = new Date(112, 1, 1, 1, 1, 1);
        Date actual = new Date(112, 1, 1, 1, 10, 1);

        assertTrue(instance.isWithinRange(originalDate.getTime(), actual.getTime()));
    }

    @Test
    public void isOnRangeEnd() {
        Date originalDate = new Date(112, 1, 1, 1, 1, 1);
        Date actual = new Date(112, 1, 1, 1, 11, 1);

        assertFalse(instance.isWithinRange(originalDate.getTime(), actual.getTime()));
    }

    @Test
    public void isOutsideRange() {
        Date originalDate = new Date(112, 1, 1, 1, 13, 1);
        Date actual = new Date(112, 1, 1, 1, 1, 1);

        assertFalse(instance.isWithinRange(originalDate.getTime(), actual.getTime()));

    }
    
    @Test(expected=NullPointerException.class)
    public void addNullNewLine() {
        instance.addNewLine(null);
    }

    @Test
    public void addNewLineBase() {
        LogLine test = 
                new LogLine("182.159.110.144 - - [29/Apr/2012:01:17:35 +0000] \"GET /p/VexSystem HTTP/1.1\" 200 4773 \"-\" \"Java/1.6.0_20");
        //Expected ipDateMaps
        Map<String, List<Long>> expectedIpDateMaps = new HashMap<String, List<Long>>();
        expectedIpDateMaps.put("182.159.110.144", Arrays.asList(new Date(112, 3, 29, 1, 17, 35).getTime()));
 
        //Expected sessions
        Map<IPDateDatum, List<LogLine>> expectedSession = 
                new HashMap<IPDateDatum, List<LogLine>>();
        expectedSession.put(new IPDateDatum("182.159.110.144", new Date(112, 3, 29, 1, 17, 35)), Arrays.asList(test));
        
        instance.addNewLine(test);

        Map<String, List<Long>> actualIpDateMaps = 
                (Map<String, List<Long>>) PrivateAccessor.getPrivateField(instance, "ipDateMaps");
       Map<IPDateDatum, List<LogLine>> actualSessions = 
               (Map<IPDateDatum, List<LogLine>>) PrivateAccessor.getPrivateField(instance, "sessions");
        SharedTestUtilities.assertMapArray(expectedSession, actualSessions);
        SharedTestUtilities.assertMapArray(expectedIpDateMaps, actualIpDateMaps);
    }
    
    @Test
    public void addNewLineTwoItemsSameSession() {
        LogLine first = 
                new LogLine("182.159.110.144 - - [29/Apr/2012:01:17:35 +0000] \"GET /p/VexSystem HTTP/1.1\" 200 4773 \"-\" \"Java/1.6.0_20");
        LogLine second = 
                new LogLine("182.159.110.144 - - [29/Apr/2012:01:18:35 +0000] \"GET /p/PageTwo HTTP/1.1\" 200 4773 \"-\" \"Java/1.6.0_20");
        
        //Expected ipDateMaps
        Map<String, List<Long>> expectedIpDateMaps = new HashMap<String, List<Long>>();
        expectedIpDateMaps.put("182.159.110.144", Arrays.asList(new Date(112, 3, 29, 1, 17, 35).getTime()));
 
        //Expected sessions
        Map<IPDateDatum, List<LogLine>> expectedSession = 
                new HashMap<IPDateDatum, List<LogLine>>();
        expectedSession.put(new IPDateDatum("182.159.110.144", new Date(112, 3, 29, 1, 17, 35)), 
                Arrays.asList(first, second));
        
        instance.addNewLine(first);
        instance.addNewLine(second);

        Map<String, List<Long>> actualIpDateMaps = 
                (Map<String, List<Long>>) PrivateAccessor.getPrivateField(instance, "ipDateMaps");
       Map<IPDateDatum, List<LogLine>> actualSessions = 
               (Map<IPDateDatum, List<LogLine>>) PrivateAccessor.getPrivateField(instance, "sessions");
        SharedTestUtilities.assertMapArray(expectedSession, actualSessions);
        SharedTestUtilities.assertMapArray(expectedIpDateMaps, actualIpDateMaps);
    }
    
    @Test
    public void addNewLineTwoItemsSeperateSessions() {
        LogLine first = 
                new LogLine("182.159.110.144 - - [29/Apr/2012:01:17:35 +0000] \"GET /p/VexSystem HTTP/1.1\" 200 4773 \"-\" \"Java/1.6.0_20");
        LogLine second = 
                new LogLine("182.159.110.144 - - [29/Apr/2012:03:18:35 +0000] \"GET /p/PageTwo HTTP/1.1\" 200 4773 \"-\" \"Java/1.6.0_20");
        
        //Expected ipDateMaps
        Map<String, List<Long>> expectedIpDateMaps = new HashMap<String, List<Long>>();
        expectedIpDateMaps.put("182.159.110.144", Arrays.asList(new Date(112, 3, 29, 1, 17, 35).getTime(), 
                new Date(112, 3, 29, 3, 18, 35).getTime()));
 
        //Expected sessions
        Map<IPDateDatum, List<LogLine>> expectedSession = 
                new HashMap<IPDateDatum, List<LogLine>>();
        expectedSession.put(new IPDateDatum("182.159.110.144", new Date(112, 3, 29, 1, 17, 35)), 
                Arrays.asList(first));
        expectedSession.put(new IPDateDatum("182.159.110.144", new Date(112, 3, 29, 3, 18, 35)), 
                Arrays.asList(second));
        
        
        instance.addNewLine(first);
        instance.addNewLine(second);

        Map<String, List<Long>> actualIpDateMaps = 
                (Map<String, List<Long>>) PrivateAccessor.getPrivateField(instance, "ipDateMaps");
       Map<IPDateDatum, List<LogLine>> actualSessions = 
               (Map<IPDateDatum, List<LogLine>>) PrivateAccessor.getPrivateField(instance, "sessions");
        SharedTestUtilities.assertMapArray(expectedSession, actualSessions);
        SharedTestUtilities.assertMapArray(expectedIpDateMaps, actualIpDateMaps);
        
    }
    
    @Test
    public void addNewLineTwoSeperateIps() {
        LogLine first = 
                new LogLine("182.159.110.144 - - [29/Apr/2012:01:17:35 +0000] \"GET /p/VexSystem HTTP/1.1\" 200 4773 \"-\" \"Java/1.6.0_20");
        LogLine second = 
                new LogLine("181.159.110.144 - - [29/Apr/2012:01:18:35 +0000] \"GET /p/PageTwo HTTP/1.1\" 200 4773 \"-\" \"Java/1.6.0_20");
        
        //Expected ipDateMaps
        Map<String, List<Long>> expectedIpDateMaps = new HashMap<String, List<Long>>();
        expectedIpDateMaps.put("182.159.110.144", Arrays.asList(new Date(112, 3, 29, 1, 17, 35).getTime()));
        expectedIpDateMaps.put("181.159.110.144", Arrays.asList(new Date(112, 3, 29, 1, 18, 35).getTime()));
       
        //Expected sessions
        Map<IPDateDatum, List<LogLine>> expectedSession = 
                new HashMap<IPDateDatum, List<LogLine>>();
        expectedSession.put(new IPDateDatum("182.159.110.144", new Date(112, 3, 29, 1, 17, 35)), 
                Arrays.asList(first));
        expectedSession.put(new IPDateDatum("181.159.110.144", new Date(112, 3, 29, 1, 18, 35)), 
                Arrays.asList(second));
        
        
        instance.addNewLine(first);
        instance.addNewLine(second);

        Map<String, List<Long>> actualIpDateMaps = 
                (Map<String, List<Long>>) PrivateAccessor.getPrivateField(instance, "ipDateMaps");
       Map<IPDateDatum, List<LogLine>> actualSessions = 
               (Map<IPDateDatum, List<LogLine>>) PrivateAccessor.getPrivateField(instance, "sessions");
        SharedTestUtilities.assertMapArray(expectedSession, actualSessions);
        SharedTestUtilities.assertMapArray(expectedIpDateMaps, actualIpDateMaps);        
    }
    
    @Test
    public void addNewLineTwoMultipleIpsAndSessions() {
        LogLine first = 
                new LogLine("182.159.110.144 - - [29/Apr/2012:01:17:35 +0000] \"GET /p/VexSystem HTTP/1.1\" 200 4773 \"-\" \"Java/1.6.0_20");
        LogLine second = 
                new LogLine("181.159.110.141 - - [29/Apr/2012:01:18:35 +0000] \"GET /p/PageTwo HTTP/1.1\" 200 4773 \"-\" \"Java/1.6.0_20");
        LogLine three = 
                new LogLine("182.159.110.144 - - [29/Apr/2012:01:18:35 +0000] \"GET /p/PageTwo HTTP/1.1\" 200 4773 \"-\" \"Java/1.6.0_20");
        
        //Expected ipDateMaps
        Map<String, List<Long>> expectedIpDateMaps = new HashMap<String, List<Long>>();
        expectedIpDateMaps.put("182.159.110.144", Arrays.asList(new Date(112, 3, 29, 1, 17, 35).getTime()));
        expectedIpDateMaps.put("181.159.110.141", Arrays.asList(new Date(112, 3, 29, 1, 18, 35).getTime()));
       
        //Expected sessions
        Map<IPDateDatum, List<LogLine>> expectedSession = 
                new HashMap<IPDateDatum, List<LogLine>>();
        expectedSession.put(new IPDateDatum("182.159.110.144", new Date(112, 3, 29, 1, 17, 35)), 
                Arrays.asList(first, three));
        expectedSession.put(new IPDateDatum("181.159.110.141", new Date(112, 3, 29, 1, 18, 35)), 
                Arrays.asList(second));
        
        
        instance.addNewLine(first);
        instance.addNewLine(second);
        instance.addNewLine(three);

        Map<String, List<Long>> actualIpDateMaps = 
                (Map<String, List<Long>>) PrivateAccessor.getPrivateField(instance, "ipDateMaps");
       Map<IPDateDatum, List<LogLine>> actualSessions = 
               (Map<IPDateDatum, List<LogLine>>) PrivateAccessor.getPrivateField(instance, "sessions");
        SharedTestUtilities.assertMapArray(expectedSession, actualSessions);
        SharedTestUtilities.assertMapArray(expectedIpDateMaps, actualIpDateMaps);        
    }
   
    @Test
    public void addNewLineThreeSeperateSections() {
        LogLine first = 
                new LogLine("182.159.110.144 - - [29/Apr/2012:01:17:35 +0000] \"GET /p/VexSystem HTTP/1.1\" 200 4773 \"-\" \"Java/1.6.0_20");
        LogLine second = 
                new LogLine("181.159.110.141 - - [29/Apr/2012:01:18:35 +0000] \"GET /p/PageTwo HTTP/1.1\" 200 4773 \"-\" \"Java/1.6.0_20");
        LogLine three = 
                new LogLine("182.159.110.144 - - [29/Apr/2012:01:38:35 +0000] \"GET /p/PageTwo HTTP/1.1\" 200 4773 \"-\" \"Java/1.6.0_20");
        
        //Expected ipDateMaps
        Map<String, List<Long>> expectedIpDateMaps = new HashMap<String, List<Long>>();
        expectedIpDateMaps.put("182.159.110.144", Arrays.asList(new Date(112, 3, 29, 1, 17, 35).getTime(), new Date(112, 3, 29, 1, 38, 35).getTime()));
        expectedIpDateMaps.put("181.159.110.141", Arrays.asList(new Date(112, 3, 29, 1, 18, 35).getTime()));
       
        //Expected sessions
        Map<IPDateDatum, List<LogLine>> expectedSession = 
                new HashMap<IPDateDatum, List<LogLine>>();
        expectedSession.put(new IPDateDatum("182.159.110.144", new Date(112, 3, 29, 1, 17, 35)), 
                Arrays.asList(first));
        expectedSession.put(new IPDateDatum("181.159.110.141", new Date(112, 3, 29, 1, 18, 35)), 
                Arrays.asList(second));
        expectedSession.put(new IPDateDatum("182.159.110.144", new Date(112, 3, 29, 1, 38, 35)), 
                Arrays.asList(three));
        
        
        instance.addNewLine(first);
        instance.addNewLine(second);
        instance.addNewLine(three);

        Map<String, List<Long>> actualIpDateMaps = 
                (Map<String, List<Long>>) PrivateAccessor.getPrivateField(instance, "ipDateMaps");
       Map<IPDateDatum, List<LogLine>> actualSessions = 
               (Map<IPDateDatum, List<LogLine>>) PrivateAccessor.getPrivateField(instance, "sessions");
        SharedTestUtilities.assertMapArray(expectedSession, actualSessions);
        SharedTestUtilities.assertMapArray(expectedIpDateMaps, actualIpDateMaps); 
    }
    
     
}
