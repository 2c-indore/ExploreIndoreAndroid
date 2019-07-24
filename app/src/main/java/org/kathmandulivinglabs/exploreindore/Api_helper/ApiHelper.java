package org.kathmandulivinglabs.exploreindore.Api_helper;

import android.text.TextUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Bhawak on 3/11/2018.
 */

public class ApiHelper {
    public static final String BASE_URL = "http://159.65.10.210:5080/";
    public static final String OSM_URL = "https://www.openstreetmap.org/";

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(OSM_URL)
                    .addConverterFactory(GsonConverterFactory.create());
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static Retrofit retrofit = builder.build();

    public ApiInterface getApiInterface() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(100, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiHelper.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
       ApiInterface api = retrofit.create(ApiInterface.class);

        return api;
    }

}
