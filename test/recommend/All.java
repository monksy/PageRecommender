/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package recommend;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author steven
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({recommend.LogLineTest.class, PageCountTest.class, 
    IPDateDatumTest.class, PageTreesTest.class, SessionSplitterTest.class})
public class All {

    @Before
    public void setUp() throws Exception {
    }

}
