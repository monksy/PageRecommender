/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package recommend;

import java.util.*;

/**
 *
 * @author steven
 */
public class SessionSplitter {
    private final Map<IPDateDatum, List<LogLine>> sessions = new HashMap<IPDateDatum, List<LogLine>>();
    private final Map<String, List<Long>> ipDateMaps= new HashMap<String, List<Long>>();
    private final long MAX_SESSION_TIME = 10 * 60 * 1000;
    private final long NO_CLOSEST_TIME = -1;
    
    public void addNewLine(LogLine item) {
        if (item == null) {
            throw new NullPointerException("Item Added is null");
        }
        
        if (item.getIpAddress() == null || item.getIpAddress().isEmpty()) {
            throw new NullPointerException("IP Address is invalid.");
        }
        
        IPDateDatum result = getSessionTime(item);
        
        //Add a new page to the session
        sessions.get(result).add(item);
    }
    
    protected boolean isWithinRange(Long originalDate, long curDate) {
       assert(originalDate != null);
       long normalizedTime = curDate - originalDate.longValue();
       return (normalizedTime >= 0) && (normalizedTime < MAX_SESSION_TIME);
    }
    
    private long findClosestTime(LogLine line) {
        long result = NO_CLOSEST_TIME;
        
        //If the ip address doesn't exist, then add it in 
        if (!ipDateMaps.containsKey(line.getIpAddress())) {
            List<Long> newItem = new ArrayList<Long>();
            newItem.add(line.getDateVisit().getTime());
            ipDateMaps.put(line.getIpAddress(), newItem);
            return NO_CLOSEST_TIME;
        }
        
        //Get the list of times
        for (Long time : ipDateMaps.get(line.getIpAddress())) {
            if (isWithinRange(time, line.getDateVisit().getTime())) {
                result = time.longValue();
            }
        }
        
        //Add to a new session time
        if (result == NO_CLOSEST_TIME) {
            ipDateMaps.get(line.getIpAddress()).add(line.getDateVisit().getTime());
        }
        
        return result;
    }
    
    private IPDateDatum getSessionTime(LogLine line) {
        long nearestSession = findClosestTime(line);
        IPDateDatum result = new IPDateDatum(line.getIpAddress(), new Date(nearestSession));
        
        //Create the session if it doesn't
        if (nearestSession == NO_CLOSEST_TIME) {
           result = new IPDateDatum(line.getIpAddress(), line.getDateVisit()); 
           sessions.put(result, new ArrayList<LogLine>());
        }
        return result;
    }
    
    public Map<IPDateDatum, List<LogLine>> getSessions() {
        return Collections.unmodifiableMap(sessions);
    }
}
