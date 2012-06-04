/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package recommend;

import java.lang.reflect.Field;
import junit.framework.Assert;

/**
 *http://snippets.dzone.com/posts/show/2242
 * @author steven
 */
public class PrivateAccessor {

    public static Object getPrivateField(Object o, String fieldName) {
        // Check we have valid arguments...
        Assert.assertNotNull(o);
        Assert.assertNotNull(fieldName);

        // Go and find the private field...
        final Field fields[] = o.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; ++i) {
            if (fieldName.equals(fields[i].getName())) {
                try {
                    fields[i].setAccessible(true);
                    return fields[i].get(o);
                } catch (IllegalAccessException ex) {
                    Assert.fail("IllegalAccessException accessing " + fieldName);
                }
            }
        }
        Assert.fail("Field '" + fieldName + "' not found");
        return null;
    }
    
    public static void setPrivateField(Object o, String fieldName, int value) {
        // Check we have valid arguments...
        Assert.assertNotNull(o);
        Assert.assertNotNull(fieldName);

        // Go and find the private field...
        final Field fields[] = o.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; ++i) {
            if (fieldName.equals(fields[i].getName())) {
                try {
                    fields[i].setAccessible(true);
                    fields[i].setInt(o, value);
                    return;
                } catch (IllegalAccessException ex) {
                    Assert.fail("IllegalAccessException accessing " + fieldName);
                }
            }
        }
        Assert.fail("Field '" + fieldName + "' not found");
    }
    
    
}
