
package com.theplatform.feeds_sample.FeedModels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Feed {

    private Integer startIndex;
    private Integer itemsPerPage;
    private Integer entryCount;
    public List<Entry> entries = new ArrayList<Entry>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The startIndex
     */
    public Integer getStartIndex() {
        return startIndex;
    }

    /**
     * 
     * @param startIndex
     *     The startIndex
     */
    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    /**
     * 
     * @return
     *     The itemsPerPage
     */
    public Integer getItemsPerPage() {
        return itemsPerPage;
    }

    /**
     * 
     * @param itemsPerPage
     *     The itemsPerPage
     */
    public void setItemsPerPage(Integer itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    /**
     * 
     * @return
     *     The entryCount
     */
    public Integer getEntryCount() {
        return entryCount;
    }

    /**
     * 
     * @param entryCount
     *     The entryCount
     */
    public void setEntryCount(Integer entryCount) {
        this.entryCount = entryCount;
    }

    /**
     * 
     * @return
     *     The entries
     */
    public List<Entry> getEntries() {
        return entries;
    }

    /**
     * 
     * @param entries
     *     The entries
     */
    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
