package org.kathmandulivinglabs.exploreindore.Api_helper;

import org.kathmandulivinglabs.exploreindore.RetrofitPOJOs.AccessToken;

import org.kathmandulivinglabs.exploreindore.RetrofitPOJOs.AuthenticateModel;
import org.kathmandulivinglabs.exploreindore.RetrofitPOJOs.Features;
import org.kathmandulivinglabs.exploreindore.RetrofitPOJOs.LoginModel;
import org.kathmandulivinglabs.exploreindore.RetrofitPOJOs.Tags;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by Bhawak on 3/11/2018.
 */

public interface ApiInterface {
    @GET("http://159.65.10.210:5080/api/amenities/data")
    Call<Features> getFeature(@Query("type") String type, @Query("platform") String client);

    @GET("http://159.65.10.210:5080/api/amenities/tags")
    Call<Tags> getTag();

    @POST("http://159.65.10.210:5080/api/users/authenticate")
    Call<AuthenticateModel> getAuthenticateResponse(@Body LoginModel loginModel);

}
