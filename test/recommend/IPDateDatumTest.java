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
public class IPDateDatumTest {

    private IPDateDatum empty;
    private IPDateDatum instance;

    @Before
    public void setUp() {
        empty = new IPDateDatum(null, null);
        instance = new IPDateDatum("120.143.124.5", new Date(112, 0, 1));
    }
    
    @Test
    public void emptyGetIPAddress() {
        assertNull(empty.getIpAddress());
    }
    
    @Test
    public void sampleGetIpAddress() {
        String expected = "120.143.124.5";
        assertEquals(expected, instance.getIpAddress());
    }

    @Test
    public void emptyGetDate() {
        assertNull(empty.getStartingDate());
    }
    
    @Test
    public void sampleGetDate() {
        Date expected = new Date(112, 0, 1);
        assertEquals(expected, instance.getStartingDate());
    }
    
    @Test
    public void emptyEquals() {
        IPDateDatum compare = new IPDateDatum(null, null);
        assertEquals(compare, empty);
    }

    @Test
    public void sampleEquals() {
        IPDateDatum expected = new IPDateDatum("120.143.124.5", new Date(112, 0, 1));
        assertEquals(expected, instance);
    }
}
