/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package recommend;

import java.util.Date;

/**
 *
 * @author steven
 */
public class IPDateDatum {
    private String ipAddress;
    private Date startingDate;

    public IPDateDatum(String ipAddress, Date startingDate) {
        this.ipAddress = ipAddress;
        this.startingDate = startingDate;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public Date getStartingDate() {
        return startingDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IPDateDatum other = (IPDateDatum) obj;
        if ((this.ipAddress == null) ? (other.ipAddress != null) : !this.ipAddress.equalsIgnoreCase(other.ipAddress)) {
            return false;
        }
        if (this.startingDate != other.startingDate && (this.startingDate == null || !this.startingDate.equals(other.startingDate))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.ipAddress != null ? this.ipAddress.toLowerCase().hashCode() : 0);
        hash = 29 * hash + (this.startingDate != null ? this.startingDate.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "IPDateDatum{" + "ipAddress=" + ipAddress + ", startingDate=" + startingDate + '}';
    }

    
    
}
