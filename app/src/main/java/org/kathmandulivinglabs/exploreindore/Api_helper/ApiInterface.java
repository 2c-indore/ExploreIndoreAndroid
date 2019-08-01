package org.kathmandulivinglabs.exploreindore.Api_helper;

import org.json.JSONObject;
import org.kathmandulivinglabs.exploreindore.RetrofitPOJOs.AccessToken;

import org.kathmandulivinglabs.exploreindore.RetrofitPOJOs.AuthenticateModel;
import org.kathmandulivinglabs.exploreindore.RetrofitPOJOs.EditParam;
import org.kathmandulivinglabs.exploreindore.RetrofitPOJOs.Features;
import org.kathmandulivinglabs.exploreindore.RetrofitPOJOs.LoginModel;
import org.kathmandulivinglabs.exploreindore.RetrofitPOJOs.Tags;

import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
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

    @PUT("http://159.65.10.210:5080/api/amenities/update/mobile/{id}")
    Call<AuthenticateModel> getSuccessResponse(@Header("Authorization") String authorization, @Path("id") String id, @Body EditParam data);

    @PUT("http://159.65.10.210:5080/api/users/password/reset")
    Call<AuthenticateModel> getResetResponse(@Header("Authorization") String authorization, @Body LoginModel params);
}
