/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package recommend;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author steven
 */
public class PageCountTest {

    private PageCount empty;
    private PageCount increment;

    @Before
    public void setUp() {
        empty = new PageCount("Test");
        increment = new PageCount("OneOff");
        increment.increment(2);
    }

    @Test
    public void emptyIncrement() {
        int expected = 1;
        assertEquals(expected, empty.getCount());
        empty.increment(1);
        expected = 2;
        assertEquals(expected, empty.getCount());
        expected = 7;
        empty.increment(5);
        assertEquals(expected, empty.getCount());
    }

    @Test
    public void emptyGetCount() {
        int expected = 1;
        assertEquals(expected, empty.getCount());
    }

    @Test
    public void incrementGetCount() {
        int expected = 3;
        assertEquals(expected, increment.getCount());
    }

    @Test
    public void emptyGetPage() {
        String expected = "Test";
        assertEquals(expected, empty.getPage());
    }

    @Test
    public void incrementGetPage() {
        String expected = "OneOff";
        assertEquals(expected, increment.getPage());
    }

    @Test
    public void nullMerge() {
        int expected = 1;
        empty.merge(null);
        assertEquals(expected, empty.getCount());
    }

    @Test
    public void badTitleMerge() {
        int expected = 3;
        increment.merge(empty);
        
        assertEquals(expected, increment.getCount());
    }

    @Test
    public void properMerge() {
        PageCount other = new PageCount("OneOff");
        increment.merge(other);
        int expected = 4;
        assertEquals(expected, increment.getCount());
    }

    @Test
    public void notPageEquals() {
        PageCount notTitle = new PageCount("Test1");
        assertFalse(empty.equals(notTitle));
    }
    
    @Test
    public void notCountEquals() {
        PageCount notCount = new PageCount("Test");
        notCount.increment(1);
        assertTrue(empty.equals(notCount));
    }
    
    @Test 
    public void bothEqualCountAndPageEquals() {
        PageCount bothEquals = new PageCount("Test");
        assertTrue(empty.equals(bothEquals));
    }


    @Test
    public void equalCompares() {
        PageCount equals = new PageCount("Test");
        int expected = 0;
        assertEquals(expected, empty.compareTo(equals));
    }
    
    @Test
    public void incrementCompares() {
        int expected = 1;
        assertEquals(expected, empty.compareTo(increment));
    }
    
    @Test
    public void emptyCompares() {
        int expected = -1;
        assertEquals(expected, increment.compareTo(empty));
    }
    
}
