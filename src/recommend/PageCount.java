/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package recommend;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 *
 * @author steven
 */
@XStreamAlias("RecommendedPage")
class PageCount implements Comparable<PageCount> {
    @XStreamAsAttribute
    private String page;
    
    private int count;

    public PageCount(String page) {
        this.page = page;
        count = 1;
    }


    public PageCount increment(int times) {
        count+= times;
        return this;
    }

    public void merge(PageCount pc) {
        if (pc != null) {
            if (pc.getPage().equals(page)) {
                increment(pc.getCount());
            }
        }
    }

    public int getCount() {
        return count;
    }

    public String getPage() {
        return page;
    }


    /**
     * Maybe the only equals needed is String comparison
     * @param obj
     * @return 
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        
        if (obj.getClass() == String.class) {
            return page.equalsIgnoreCase((String)obj);
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final PageCount other = (PageCount) obj;
        if ((this.page == null) ? (other.page != null) : !this.page.equals(other.page)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (this.page != null ? this.page.hashCode() : 0);
        return hash;
    }

    public int compareTo(PageCount o) {
        return ((Integer)o.getCount()).compareTo(count);
    }

    @Override
    public String toString() {
        return "PageCount{" + "page=" + page + ", count=" + count + '}';
    }


}
