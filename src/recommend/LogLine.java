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
    private final String INVALID_PAGE_NAMES = "?@{}.%";
    private String ipAddress = "";
    private String page = "";
    private Date dateVisit;
    public static final int NUM_FIELDS = 3;
    private final Pattern LOG_PATTERN = Pattern.compile("^\\[(.*?)\\] (\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}).*GET /[ap]/(.*?) .*$");


    public LogLine(String logEntryLine) {
        Matcher matcher = LOG_PATTERN.matcher(logEntryLine);
        if (!matcher.matches()
                || NUM_FIELDS != matcher.groupCount()) {
            System.err.println("Bad log entry (or problem with RE?):");
            System.err.printf("%s-%d\n", logEntryLine, matcher.groupCount());
            return;
        }

        copyInto(new Builder().ipAddress(matcher.group(2)).dateVisit(matcher.group(1)).page(matcher.group(3)));

    }

    private LogLine(Builder builder) {
        copyInto(builder);
    }

    private void copyInto(Builder builder) {
        ipAddress = builder.ipAddress;
        page = builder.page;
        dateVisit = builder.dateVisit;
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


    public static final class Builder {
        private String ipAddress;
        private String page;
        private Date dateVisit;

        public Builder() {
        }

        public Builder ipAddress(String val) {
            ipAddress = val;
            return this;
        }

        public Builder page(String val) {
            page = val;
            return this;
        }

        public Builder dateVisit(Date val) {
            dateVisit = val;
            return this;
        }



        public LogLine build() {
            return new LogLine(this);
        }

        public Builder dateVisit(String group) {

            DateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy:hh:mm:ss");
            try {
                dateVisit = (Date) formatter.parse(group);
            } catch (ParseException ex) {
                Logger.getLogger(LogLine.class.getName()).log(Level.SEVERE, null, ex);
            }
            return this;
        }
    }
}
