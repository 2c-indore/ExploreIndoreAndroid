package org.kathmandulivinglabs.preparepokhara.Api_helper;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;

public class OSMAuthApi extends DefaultApi10a {

    public static final String AUTHORIZE_URL = "https://www.openstreetmap.org/oauth/authorize?oauth_token=%s";;

    public OSMAuthApi() {
    }

    private static class InstanceHolder {
        private static final OSMAuthApi INSTANCE = new OSMAuthApi();
    }

    public static OSMAuthApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return "https://www.openstreetmap.org/oauth/request_token";
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://www.openstreetmap.org/oauth/access_token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://www.openstreetmap.org/oauth/authorize";
    }

    @Override
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return String.format(AUTHORIZE_URL, requestToken.getToken());
    }


////dev server
//    public static final String AUTHORIZE_URL = "https://master.apis.dev.openstreetmap.org/oauth/authorize?oauth_token=%s";;
//
//    public OSMAuthApi() {
//    }
//
//    private static class InstanceHolder {
//        private static final OSMAuthApi INSTANCE = new OSMAuthApi();
//    }
//
//    public static OSMAuthApi instance() {
//        return InstanceHolder.INSTANCE;
//    }
//
//    @Override
//    public String getRequestTokenEndpoint() {
//        return "https://master.apis.dev.openstreetmap.org/oauth/request_token";
//    }
//
//    @Override
//    public String getAccessTokenEndpoint() {
//        return "https://master.apis.dev.openstreetmap.org/oauth/access_token";
//    }
//
//    @Override
//    protected String getAuthorizationBaseUrl() {
//        return "https://master.apis.dev.openstreetmap.org/oauth/authorize";
//    }
//
//    @Override
//    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
//        return String.format(AUTHORIZE_URL, requestToken.getToken());
//    }

}
