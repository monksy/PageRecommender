/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package recommend;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author steven
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {

            //This keeps track of the last page that each user looked at
            Map<String, String> lastVisitedPage = new HashMap<String, String>();

            //Handles the log files utiltity
            //HandleLog hl = new HandleLog();
            SessionSplitter spliter = new SessionSplitter();
            BufferedReader bre = new BufferedReader(new InputStreamReader(System.in));
            //  BufferedReader bre = new BufferedReader(new FileReader("results"));
            String line;
            
            while ((line = bre.readLine()) != null) {
                LogLine logl = new LogLine(line);
                String page = logl.getPage();

                //If the page is valid, lets add it to the list of visited pages.
                if (logl.isPageValid()) {
                    spliter.addNewLine(logl);
                    lastVisitedPage.put(logl.getIpAddress(), page);
                }
            }
            // hl.commit();
            Map<String, List<PageCount>> processedItems = 
                    PageTrees.convertSessionsIntoPageTrees(spliter.getSessions());
            
            //Output the results to the console
            XStream xstream = new XStream(new DomDriver("UTF-8"));
            xstream.processAnnotations(PageCount.class);
            xstream.registerConverter(new MapConverterFix(xstream.getMapper(), "page"));
            System.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
            xstream.toXML(processedItems, System.out);

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
