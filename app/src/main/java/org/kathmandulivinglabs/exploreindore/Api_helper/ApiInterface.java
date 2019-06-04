package org.kathmandulivinglabs.exploreindore.Api_helper;

import org.kathmandulivinglabs.exploreindore.RetrofitPOJOs.AccessToken;
import org.kathmandulivinglabs.exploreindore.RetrofitPOJOs.Attractions;
import org.kathmandulivinglabs.exploreindore.RetrofitPOJOs.Features;

import retrofit2.Call;
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
    @GET("http://preparepokhara.org/api/v2/features")
    Call<Features> getFeature(@Query("type") String type);

    @GET("http://preparepokhara.org/api/v2/features/tags")
    Call<Tags> getTag();

    @GET("https://preparepokhara.org/api/v2/features/attractions")
    Call<Attractions> getAttraction();

}
