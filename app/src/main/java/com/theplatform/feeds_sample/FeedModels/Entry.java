
package com.theplatform.feeds_sample.FeedModels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Entry {

    private String title;
    private String description;
    private List<Content> content = new ArrayList<Content>();
    private String defaultThumbnailUrl;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 
     * @return
     *     The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     *     The content
     */
    public List<Content> getContent() {
        return content;
    }

    /**
     * 
     * @param content
     *     The content
     */
    public void setContent(List<Content> content) {
        this.content = content;
    }

    /**
     * 
     * @return
     *     The defaultThumbnailUrl
     */
    public String getDefaultThumbnailUrl() {
        return defaultThumbnailUrl;
    }

    /**
     * 
     * @param defaultThumbnailUrl
     *     The defaultThumbnailUrl
     */
    public void setDefaultThumbnailUrl(String defaultThumbnailUrl) {
        this.defaultThumbnailUrl = defaultThumbnailUrl;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
