/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package recommend;

import java.util.*;

import org.junit.*;

import static org.junit.Assert.*;

/**
 * @author steven
 */
public class SessionSplitterTest {

    public static final String BASE_PAGE = "[%s +0000] %s TLSv1.2 ECDHE-RSA-AES256-GCM-SHA384 \"GET /p/%s HTTP/1.1\" 4345-3";

    public static final String SAMPLE_IP = "182.159.110.144";
    public static final String SECOND_IP = "181.159.110.144";
    public static final String THIRD_IP = "181.159.110.141";

    public static final String VEXPAGE = "VexSystem";
    public static final String TWOPAGE = "PageTwo";


    public static final String VEXPAGE_11735 = String.format(BASE_PAGE, "29/Apr/2012:01:17:35", SAMPLE_IP, VEXPAGE);
    public static final String PAGE_TWO = String.format(BASE_PAGE, "29/Apr/2012:01:18:35", SAMPLE_IP, TWOPAGE);
    public static final String ALT_PAGE_TWO = String.format(BASE_PAGE, "29/Apr/2012:01:18:35", THIRD_IP, TWOPAGE);
    public static final String PAGE_TWO_318 = String.format(BASE_PAGE, "29/Apr/2012:03:18:35", SAMPLE_IP, TWOPAGE);
    public static final String SECOND_ALT_PG_TWO = String.format(BASE_PAGE, "29/Apr/2012:01:38:35", SAMPLE_IP, TWOPAGE);
    public static final String SECOND_IP_P2 = String.format(BASE_PAGE, "29/Apr/2012:01:18:35", SECOND_IP, TWOPAGE);


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

    @Test(expected = NullPointerException.class)
    public void addNullNewLine() {
        instance.addNewLine(null);
    }

    @Test
    public void addNewLineBase() {
        LogLine test =
                new LogLine(VEXPAGE_11735);
        //Expected ipDateMaps
        Map<String, List<Long>> expectedIpDateMaps = new HashMap<String, List<Long>>();
        expectedIpDateMaps.put(SAMPLE_IP, Arrays.asList(new Date(112, 3, 29, 1, 17, 35).getTime()));

        //Expected sessions
        Map<IPDateDatum, List<LogLine>> expectedSession =
                new HashMap<IPDateDatum, List<LogLine>>();
        expectedSession.put(new IPDateDatum(SAMPLE_IP, new Date(112, 3, 29, 1, 17, 35)), Arrays.asList(test));

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
                new LogLine(VEXPAGE_11735);
        LogLine second =
                new LogLine(PAGE_TWO);

        //Expected ipDateMaps
        Map<String, List<Long>> expectedIpDateMaps = new HashMap<String, List<Long>>();
        expectedIpDateMaps.put(SAMPLE_IP, Arrays.asList(new Date(112, 3, 29, 1, 17, 35).getTime()));

        //Expected sessions
        Map<IPDateDatum, List<LogLine>> expectedSession =
                new HashMap<IPDateDatum, List<LogLine>>();
        expectedSession.put(new IPDateDatum(SAMPLE_IP, new Date(112, 3, 29, 1, 17, 35)),
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
                new LogLine(VEXPAGE_11735);
        LogLine second =
                new LogLine(PAGE_TWO_318);

        //Expected ipDateMaps
        Map<String, List<Long>> expectedIpDateMaps = new HashMap<String, List<Long>>();
        expectedIpDateMaps.put(SAMPLE_IP, Arrays.asList(new Date(112, 3, 29, 1, 17, 35).getTime(),
                new Date(112, 3, 29, 3, 18, 35).getTime()));

        //Expected sessions
        Map<IPDateDatum, List<LogLine>> expectedSession =
                new HashMap<IPDateDatum, List<LogLine>>();
        expectedSession.put(new IPDateDatum(SAMPLE_IP, new Date(112, 3, 29, 1, 17, 35)),
                Arrays.asList(first));
        expectedSession.put(new IPDateDatum(SAMPLE_IP, new Date(112, 3, 29, 3, 18, 35)),
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
                new LogLine(VEXPAGE_11735);
        LogLine second =
                new LogLine(SECOND_IP_P2);

        //Expected ipDateMaps
        Map<String, List<Long>> expectedIpDateMaps = new HashMap<String, List<Long>>();
        expectedIpDateMaps.put(SAMPLE_IP, Arrays.asList(new Date(112, 3, 29, 1, 17, 35).getTime()));
        expectedIpDateMaps.put(SECOND_IP, Arrays.asList(new Date(112, 3, 29, 1, 18, 35).getTime()));

        //Expected sessions
        Map<IPDateDatum, List<LogLine>> expectedSession = new HashMap<>();
        expectedSession.put(new IPDateDatum(SAMPLE_IP, new Date(112, 3, 29, 1, 17, 35)),
                Arrays.asList(first));
        expectedSession.put(new IPDateDatum(SECOND_IP, new Date(112, 3, 29, 1, 18, 35)),
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
                new LogLine(VEXPAGE_11735);
        LogLine second =
                new LogLine(ALT_PAGE_TWO);
        LogLine three =
                new LogLine(PAGE_TWO);

        //Expected ipDateMaps
        Map<String, List<Long>> expectedIpDateMaps = new HashMap<String, List<Long>>();
        expectedIpDateMaps.put(SAMPLE_IP, Arrays.asList(new Date(112, 3, 29, 1, 17, 35).getTime()));
        expectedIpDateMaps.put(THIRD_IP, Arrays.asList(new Date(112, 3, 29, 1, 18, 35).getTime()));

        //Expected sessions
        Map<IPDateDatum, List<LogLine>> expectedSession =
                new HashMap<IPDateDatum, List<LogLine>>();
        expectedSession.put(new IPDateDatum(SAMPLE_IP, new Date(112, 3, 29, 1, 17, 35)),
                Arrays.asList(first, three));
        expectedSession.put(new IPDateDatum(THIRD_IP, new Date(112, 3, 29, 1, 18, 35)),
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
                new LogLine(VEXPAGE_11735);
        LogLine second =
                new LogLine(ALT_PAGE_TWO);
        LogLine three =
                new LogLine(SECOND_ALT_PG_TWO);

        //Expected ipDateMaps
        Map<String, List<Long>> expectedIpDateMaps = new HashMap<String, List<Long>>();
        expectedIpDateMaps.put(SAMPLE_IP, Arrays.asList(new Date(112, 3, 29, 1, 17, 35).getTime(), new Date(112, 3, 29, 1, 38, 35).getTime()));
        expectedIpDateMaps.put(THIRD_IP, Arrays.asList(new Date(112, 3, 29, 1, 18, 35).getTime()));

        //Expected sessions
        Map<IPDateDatum, List<LogLine>> expectedSession =
                new HashMap<IPDateDatum, List<LogLine>>();
        expectedSession.put(new IPDateDatum(SAMPLE_IP, new Date(112, 3, 29, 1, 17, 35)),
                Arrays.asList(first));
        expectedSession.put(new IPDateDatum(THIRD_IP, new Date(112, 3, 29, 1, 18, 35)),
                Arrays.asList(second));
        expectedSession.put(new IPDateDatum(SAMPLE_IP, new Date(112, 3, 29, 1, 38, 35)),
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
