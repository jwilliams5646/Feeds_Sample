
package com.theplatform.feeds_sample.TokenModels;

import java.util.HashMap;
import java.util.Map;


public class SignIn {

    private SignInResponse signInResponse;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The signInResponse
     */
    public SignInResponse getSignInResponse() {
        return signInResponse;
    }

    /**
     * 
     * @param signInResponse
     *     The signInResponse
     */
    public void setSignInResponse(SignInResponse signInResponse) {
        this.signInResponse = signInResponse;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
