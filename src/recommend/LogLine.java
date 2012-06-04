/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package recommend;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author steven
 */
public class LogLine {

    private final String logEntryPattern = "^.*(\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b).*\\[([\\w:/]+\\s[+\\-]\\d{4})\\] \\\"GET \\/p\\/(.+?) HTTP.*\\\".*$";
    private final String INVALID_PAGE_NAMES = "?@{}.%";
    private String ipAddress = "";
    private String page = "";
    private Date dateVisit;
    public static final int NUM_FIELDS = 3;

    public LogLine(String logEntryLine) {

        Pattern p = Pattern.compile(logEntryPattern);
        Matcher matcher = p.matcher(logEntryLine);
        if (!matcher.matches()
                || NUM_FIELDS != matcher.groupCount()) {
            System.err.println("Bad log entry (or problem with RE?):");
            System.err.printf("%s-%d\n", logEntryLine, matcher.groupCount());
            return;
        }

        ipAddress = matcher.group(1);
        String dateString = matcher.group(2);
        page = matcher.group(3);
// Z

        DateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy:hh:mm:ss");
        try {
            dateVisit = (Date) formatter.parse(dateString);
        } catch (ParseException ex) {
            Logger.getLogger(LogLine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Date getDateVisit() {
        return dateVisit;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getPage() {
        return page;
    }

    public boolean isPageValid() {
        for (char chItem : INVALID_PAGE_NAMES.toCharArray()) {
            if (page.indexOf(chItem) >= 0) {
                return false;
            }
        }
        return true;
    }
}
