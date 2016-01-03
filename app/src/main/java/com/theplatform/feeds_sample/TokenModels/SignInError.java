
package com.theplatform.feeds_sample.TokenModels;

import java.util.HashMap;
import java.util.Map;

public class SignInError {

    private Integer responseCode;
    private String title;
    private String correlationId;
    private String description;
    private Boolean isException;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The responseCode
     */
    public Integer getResponseCode() {
        return responseCode;
    }

    /**
     * 
     * @param responseCode
     *     The responseCode
     */
    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

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
     *     The correlationId
     */
    public String getCorrelationId() {
        return correlationId;
    }

    /**
     * 
     * @param correlationId
     *     The correlationId
     */
    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
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
     *     The isException
     */
    public Boolean getIsException() {
        return isException;
    }

    /**
     * 
     * @param isException
     *     The isException
     */
    public void setIsException(Boolean isException) {
        this.isException = isException;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
