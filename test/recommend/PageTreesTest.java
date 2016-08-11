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
public class PageTreesTest {

    public static final String SAMPLE_IP = "182.159.110.144";
    public static final String PAGE_TWO = "PageTwo";
    public static final String PAGE_ONE = "PageOne";
    public static final String PAGE_THREE = "PageThree";

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

    @Test(expected = NullPointerException.class)
    public void testConvertSessionsIntoPageTreesNull() {
        Map<IPDateDatum, List<LogLine>> sessions = null;
        PageTrees.convertSessionsIntoPageTrees(sessions);

    }

    @Test
    public void testConvertSessionsIntoPageTreesFlat() {
        Map<IPDateDatum, List<LogLine>> sessions = new HashMap<IPDateDatum, List<LogLine>>();
        LogLine first = new LogLine.Builder().dateVisit("29/Apr/2012:01:17:35").ipAddress(SAMPLE_IP).page(PAGE_ONE).build();
        LogLine second = new LogLine.Builder().dateVisit("29/Apr/2012:01:18:35").ipAddress(SAMPLE_IP).page(PAGE_TWO).build();
        LogLine third = new LogLine.Builder().dateVisit("29/Apr/2012:01:19:35").ipAddress(SAMPLE_IP).page(PAGE_THREE).build();
        sessions.put(new IPDateDatum(SAMPLE_IP, new Date(112, 3, 29, 01, 17, 35)), Arrays.asList(first));
        sessions.put(new IPDateDatum(SAMPLE_IP, new Date(112, 3, 29, 02, 17, 35)), Arrays.asList(second));
        sessions.put(new IPDateDatum(SAMPLE_IP, new Date(112, 3, 29, 03, 17, 35)), Arrays.asList(third));


        Map<String, List<PageCount>> expected = new HashMap<String, List<PageCount>>();
        expected.put(PAGE_ONE, new ArrayList<PageCount>());
        expected.put(PAGE_TWO, new ArrayList<PageCount>());
        expected.put(PAGE_THREE, new ArrayList<PageCount>());

        Map<String, List<PageCount>> actual = PageTrees.convertSessionsIntoPageTrees(sessions);

        SharedTestUtilities.assertMapArray(expected, actual);
    }

    //Single Page with children

    @Test
    public void testConvertSessionsIntoPagesWithChildren() {
        Map<IPDateDatum, List<LogLine>> sessions = new HashMap<IPDateDatum, List<LogLine>>();
        LogLine first = new LogLine.Builder().dateVisit("29/Apr/2012:01:17:35").ipAddress(SAMPLE_IP).page(PAGE_ONE).build();
        LogLine second = new LogLine.Builder().dateVisit("29/Apr/2012:01:18:35").ipAddress(SAMPLE_IP).page(PAGE_TWO).build();
        LogLine third = new LogLine.Builder().dateVisit("29/Apr/2012:01:19:35").ipAddress(SAMPLE_IP).page(PAGE_THREE).build();
        sessions.put(new IPDateDatum(SAMPLE_IP, new Date(112, 3, 29, 01, 17, 35)), Arrays.asList(first, second, third));

        Map<String, List<PageCount>> expected = new HashMap<String, List<PageCount>>();
        expected.put(PAGE_ONE, Arrays.asList(new PageCount(PAGE_TWO)));
        expected.put(PAGE_TWO, Arrays.asList(new PageCount(PAGE_THREE)));
        expected.put(PAGE_THREE, Collections.<PageCount>emptyList());

        Map<String, List<PageCount>> actual = PageTrees.convertSessionsIntoPageTrees(sessions);

        SharedTestUtilities.assertMapArray(expected, actual);
    }

    //Two pages with children

    @Test
    public void testConvertSessionsIntoMultiplePagesWithChildren() {
        Map<IPDateDatum, List<LogLine>> sessions = new HashMap<IPDateDatum, List<LogLine>>();
        LogLine first = new LogLine.Builder().dateVisit("29/Apr/2012:01:17:35").ipAddress(SAMPLE_IP).page(PAGE_ONE).build();
        LogLine second = new LogLine.Builder().dateVisit("29/Apr/2012:01:18:35").ipAddress(SAMPLE_IP).page(PAGE_TWO).build();
        LogLine third = new LogLine.Builder().dateVisit("29/Apr/2012:01:19:35").ipAddress(SAMPLE_IP).page(PAGE_THREE).build();

        sessions.put(new IPDateDatum(SAMPLE_IP, new Date(112, 3, 29, 01, 17, 35)), Arrays.asList(first, second, third));
        sessions.put(new IPDateDatum("182.159.110.145", new Date(112, 3, 29, 01, 20, 35)), Arrays.asList(second, third));
        sessions.put(new IPDateDatum("182.159.110.146", new Date(112, 3, 29, 01, 17, 35)), Arrays.asList(first, third));

        Map<String, List<PageCount>> expected = new HashMap<String, List<PageCount>>();
        expected.put(PAGE_ONE, Arrays.asList(new PageCount(PAGE_TWO), new PageCount(PAGE_THREE)));
        expected.put(PAGE_TWO, Arrays.asList(new PageCount(PAGE_THREE).increment(1)));
        expected.put(PAGE_THREE, Collections.<PageCount>emptyList());

        Map<String, List<PageCount>> actual = PageTrees.convertSessionsIntoPageTrees(sessions);

        SharedTestUtilities.assertMapArray(expected, actual);
    }
}
