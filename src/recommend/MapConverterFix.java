/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package recommend;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.MapConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author steven
 */
public class MapConverterFix<T> extends MapConverter {

    private final String attributename;

    public MapConverterFix(Mapper mapper, String attributename) {
        super(mapper);
        this.attributename = attributename;
    }

    public boolean canConvert(Class type) {
        return type == HashMap.class;
    }

    public void marshal(Object source, HierarchicalStreamWriter writer,
            MarshallingContext context) {
        Map<String, T> map = (Map<String, T>) source;
        for (Map.Entry<String, T> entry : map.entrySet()) {
            T value = entry.getValue();
            writer.startNode(mapper().serializedClass(value.getClass()));
            writer.addAttribute(attributename, entry.getKey());
            context.convertAnother(value);
            writer.endNode();
        }
    }

    public Object unmarshal(HierarchicalStreamReader reader,
            UnmarshallingContext context) {
        Map<String, T> map = new HashMap<String, T>();
        populateStringMap(reader, context, map);
        return map;
    }

    protected void populateStringMap(HierarchicalStreamReader reader,
            UnmarshallingContext context,
            Map<String, T> map) {
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            String key = reader.getAttribute(attributename);
            T value = (T) readItem(reader, context, map);
            reader.moveUp();
            map.put(key, value);
        }
    }
}
