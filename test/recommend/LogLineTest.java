/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package recommend;

import java.util.Date;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author steven
 */
public class LogLineTest {

    public LogLineTest() {
    }
    private LogLine empty;
    private LogLine sample;

    @Before
    public void setUp() {
        empty = new LogLine("");
        sample = new LogLine("182.159.110.144 - - [29/Apr/2012:01:17:35 +0000] \"GET /p/VexSystem HTTP/1.1\" 200 4773 \"-\" \"Java/1.6.0_20");
    }

    @Test
    public void emptyGetDateVisit() {
        assertNull(empty.getDateVisit());
    }

    @Test
    public void sampleGetDateVisit() {
        Date expected = new Date(112, 3, 29, 01, 17, 35);
        assertEquals(expected, sample.getDateVisit());
    }

    @Test
    public void emptyGetIpAddress() {
        assertEquals("", empty.getIpAddress());
    }

    @Test
    public void sampleGetIpAddress() {
        String expected = "182.159.110.144";
        assertEquals(expected, sample.getIpAddress());
    }

    @Test
    public void emptyGetPage() {
        assertEquals("", empty.getPage());
    }

    @Test
    public void sampleGetPage() {
        String expected = "VexSystem";
        assertEquals(expected, sample.getPage());
    }

    @Test
    public void emptyIsPageValid() {
        assertTrue(empty.isPageValid());
    }

    @Test
    public void sampleIsPageValid() {
        assertTrue(sample.isPageValid());
    }

    @Test
    public void questionIsPageValid() {
        LogLine bad = new LogLine("182.159.110.144 - - [29/Apr/2012:01:17:35 +0000] \"GET /p/VexSystem? HTTP/1.1\" 200 4773 \"-\" \"Java/1.6.0_20");
        assertFalse(bad.isPageValid());
    }
    
    
    @Test
    public void atIsPageValid() {
        LogLine bad = new LogLine("182.159.110.144 - - [29/Apr/2012:01:17:35 +0000] \"GET /p/VexSy@stem HTTP/1.1\" 200 4773 \"-\" \"Java/1.6.0_20");
        assertFalse(bad.isPageValid());
    }
    
    @Test
    public void leftCuryIsPageValid() {
        LogLine bad = new LogLine("182.159.110.144 - - [29/Apr/2012:01:17:35 +0000] \"GET /p/VexSystem{ HTTP/1.1\" 200 4773 \"-\" \"Java/1.6.0_20");
        assertFalse(bad.isPageValid());
    }
    
    @Test
    public void rightCurlyIsPageValid() {
        LogLine bad = new LogLine("182.159.110.144 - - [29/Apr/2012:01:17:35 +0000] \"GET /p/VexSystem} HTTP/1.1\" 200 4773 \"-\" \"Java/1.6.0_20");
        assertFalse(bad.isPageValid());
    }
    
    @Test
    public void periodIsPageValid() {
        LogLine bad = new LogLine("182.159.110.144 - - [29/Apr/2012:01:17:35 +0000] \"GET /p/Ve.xSystem HTTP/1.1\" 200 4773 \"-\" \"Java/1.6.0_20");
        assertFalse(bad.isPageValid());
    }
    
    @Test
    public void percentageIsPageValid() {
        LogLine bad = new LogLine("182.159.110.144 - - [29/Apr/2012:01:17:35 +0000] \"GET /p/VexSyst%em HTTP/1.1\" 200 4773 \"-\" \"Java/1.6.0_20");
        assertFalse(bad.isPageValid());
    }
    
    @Test
    public void twoInvalidsIsPageValid() {
        LogLine bad = new LogLine("182.159.110.144 - - [29/Apr/2012:01:17:35 +0000] \"GET /p/Vex.@System HTTP/1.1\" 200 4773 \"-\" \"Java/1.6.0_20");
        assertFalse(bad.isPageValid());
    }
    
    @Test
    public void threeInvalidsIsPageValid() {
        LogLine bad = new LogLine("182.159.110.144 - - [29/Apr/2012:01:17:35 +0000] \"GET /p/Vex.{}System HTTP/1.1\" 200 4773 \"-\" \"Java/1.6.0_20");
        assertFalse(bad.isPageValid());
    }
    
    
}
