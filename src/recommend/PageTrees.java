/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package recommend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is responsible for converting user sessions into page trees [which
 * contain information about times referred, etc].
 *
 * @author steven
 */
public class PageTrees {

    public static Map<String, List<PageCount>> convertSessionsIntoPageTrees(Map<IPDateDatum, List<LogLine>> sessions) {
        if (sessions == null) {
            throw new NullPointerException("Session parameter should not be null.");
        }

        Map<String, List<PageCount>> results = new HashMap<String, List<PageCount>>();

        //Go through all logs
        for (Map.Entry<IPDateDatum, List<LogLine>> kv : sessions.entrySet()) {
            String lastPageVisited = "";
            for (LogLine ll : kv.getValue()) {
                String curPage = ll.getPage();

                if (!results.containsKey(curPage)) {
                    results.put(ll.getPage(), new ArrayList<PageCount>());
                }

                //get/put new key value
                if (!lastPageVisited.isEmpty()) {
                    List<PageCount> pages = results.get(lastPageVisited);
                    PageCount page = new PageCount(curPage);
                    
                    int index = pages.indexOf(page);
                    if (index == -1) {
                        pages.add(page);
                    } else {
                        page = pages.get(index);
                        page.increment(1);
                    }

                }

                lastPageVisited = ll.getPage();
            }
        }
        return Collections.unmodifiableMap(results);
    }
}
