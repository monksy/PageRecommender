/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package recommend;

import java.util.Map;
import static org.junit.Assert.*;

/**
 *
 * @author steven
 */
public class SharedTestUtilities {

    public static void assertMapArray(Map expected, Map result) {
        for (Object o : result.keySet()) {
            if (expected.containsKey(o)) {
                assertEquals("Error: Values do not match up for key: " + o,
                        expected.get(o), result.get(o));
            } else {
                fail("Error: Does not contain key: " + o);
            }
        }
    }
}
