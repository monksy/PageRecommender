/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package recommend;

import java.util.Date;

import org.junit.*;

import static org.junit.Assert.*;

/**
 * @author steven
 */
public class LogLineTest {
    public static final String BASEPAGE = "[08/Aug/2016:20:59:05 +0000] 126.249.6.18 TLSv1.2 ECDHE-RSA-AES256-GCM-SHA384 \"GET %s HTTP/1.1\" 4345-3";
    public static final String VALID_LOG = "[08/Aug/2016:20:59:05 +0000] 126.249.6.18 TLSv1.2 ECDHE-RSA-AES256-GCM-SHA384 \"GET /p/APE HTTP/1.1\" 4345-3";

    public static final String QUESTIONINPAGE = String.format(BASEPAGE, "/p/VexSystem?");
    public static final String INVALIDPAGE = String.format(BASEPAGE, "/p/VexSy@stem");
    public static final String BADCHARACTERAFTERPAGE = String.format(BASEPAGE, "/p/VexSystem{");
    public static final String RIGHTCURLEYAFTER = String.format(BASEPAGE, "/p/VexSystem}");

    public LogLineTest() {
    }

    private LogLine empty;
    private LogLine sample;

    @Before
    public void setUp() {
        empty = new LogLine("");
        sample = new LogLine(VALID_LOG);
    }

    @Test
    public void emptyGetDateVisit() {
        assertNull(empty.getDateVisit());
    }

    @Test
    public void sampleGetDateVisit() {
        Date expected = new Date(116, 7, 8, 20, 59, 05);
        assertEquals(expected, sample.getDateVisit());
    }

    @Test
    public void emptyGetIpAddress() {
        assertEquals("", empty.getIpAddress());
    }

    @Test
    public void sampleGetIpAddress() {
        String expected = "126.249.6.18";
        assertEquals(expected, sample.getIpAddress());
    }

    @Test
    public void emptyGetPage() {
        assertEquals("", empty.getPage());
    }

    @Test
    public void sampleGetPage() {
        String expected = "APE";
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
        LogLine bad = new LogLine(QUESTIONINPAGE);
        assertFalse(bad.isPageValid());
    }


    @Test
    public void atIsPageValid() {
        LogLine bad = new LogLine(INVALIDPAGE);
        assertFalse(bad.isPageValid());
    }

    @Test
    public void leftCuryIsPageValid() {
        LogLine bad = new LogLine(BADCHARACTERAFTERPAGE);
        assertFalse(bad.isPageValid());
    }

    @Test
    public void rightCurlyIsPageValid() {
        LogLine bad = new LogLine(RIGHTCURLEYAFTER);
        assertFalse(bad.isPageValid());
    }


}
