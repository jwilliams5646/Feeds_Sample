package com.theplatform.feeds_sample;

import com.theplatform.feeds_sample.TokenModels.SignIn;

import java.util.Map;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.QueryMap;

/**
 * Created by John Williams on 12/31/2015.
 */
public interface ThePlatformAPI {

@GET("Authentication/signIn?schema=1.1&form=json")
    Call<SignIn> signIn(@QueryMap Map<String,String> login);
}
