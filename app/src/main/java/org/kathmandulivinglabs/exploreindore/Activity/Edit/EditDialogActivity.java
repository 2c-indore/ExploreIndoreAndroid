package org.kathmandulivinglabs.exploreindore.Activity.Edit;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.util.XmlUtils;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.kathmandulivinglabs.exploreindore.Activity.MainActivity;
import org.kathmandulivinglabs.exploreindore.Api_helper.OSMAuthApi;
import org.kathmandulivinglabs.exploreindore.Helper.Connectivity;
import org.kathmandulivinglabs.exploreindore.Helper.Utils;
import org.kathmandulivinglabs.exploreindore.R;
import org.kathmandulivinglabs.exploreindore.Realmstore.ExploreSchema;
import org.kathmandulivinglabs.exploreindore.Realmstore.Tag;
import org.kathmandulivinglabs.exploreindore.View.ProgressDialogFragment;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.zip.Inflater;

import javax.annotation.Nullable;

import de.westnordost.osmapi.OsmConnection;
import de.westnordost.osmapi.changesets.Changeset;
import de.westnordost.osmapi.changesets.ChangesetInfo;
import de.westnordost.osmapi.changesets.ChangesetsDao;
import de.westnordost.osmapi.common.XmlParser;

import de.westnordost.osmapi.common.XmlWriter;
import de.westnordost.osmapi.common.errors.OsmConnectionException;
import de.westnordost.osmapi.map.MapDataDao;
import de.westnordost.osmapi.map.changes.MapDataChangesWriter;
import de.westnordost.osmapi.map.data.Element;
import de.westnordost.osmapi.notes.NotesDao;
import io.realm.ObjectChangeSet;
import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmObjectChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.http.HttpRequest;
import oauth.signpost.signature.OAuthMessageSigner;
import oauth.signpost.signature.SigningStrategy;

import static de.westnordost.osmapi.map.data.Element.Type.WAY;

/**
 * Created by Bhawak on 6/1/18.
 */
public class EditDialogActivity extends AppCompatActivity implements MainActivity.OnTaskCompleted {

    @Override
    public void onTaskCompleted() {
        this.dismissProgressDialog();
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                final ExploreSchema explore = realm.where(ExploreSchema.class).contains("osm_id",osmId).findFirst();
                for (Map.Entry<String,String> maps:realm_key_value.entrySet()
                     ) {
                    String vals = maps.getValue();
                    try {
//                        if(vals!=null) {
//                            if (maps.getKey().equals("name"))
//                                explore.setName(vals);
//                            if (maps.getKey().equals("name_ne"))
//                                explore.setName_ne(vals);
//                            if (maps.getKey().equals("contact_phone"))
//                                explore.setContact_phone(vals);
//                            if (maps.getKey().equals("contact_email"))
//                                explore.setContact_email(vals);
//                            if (maps.getKey().equals("web"))
//                                explore.setWeb(vals);
//
//                            if (maps.getKey().equals("capacity_beds"))
//                                if (vals != null && vals.matches("-?\\d+"))
//                                    explore.setCapacity_beds(Integer.parseInt(vals));
//                                else explore.setCapacity_beds(null);
//
//                            if (maps.getKey().equals("personnel_count"))
//                                if (vals != null && vals.matches("-?\\d+"))
//                                    explore.setPersonnel_count(Integer.parseInt(vals));
//                                else explore.setPersonnel_count(null);
//
//                            if (maps.getKey().equals("student_count"))
//                                if (vals != null && vals.matches("-?\\d+"))
//                                    explore.setStudent_count(Integer.parseInt(vals));
//                                else explore.setStudent_count(null);
//
//                            if (maps.getKey().equals("frequency"))
//                                if (vals != null && vals.matches("[+-]?([0-9]*[.])?[0-9]+"))
//                                    explore.setFrequency(Double.parseDouble(vals));
//                                else explore.setFrequency(null);
//
//                            if (maps.getKey().equals("opening_hours"))
//                                explore.setOpening_hours(vals);
//                            if (maps.getKey().equals("facility_xray"))
//                                explore.setFacility_xray(vals);
//                            if (maps.getKey().equals("facility_icu"))
//                                explore.setFacility_icu(vals);
//                            if (maps.getKey().equals("facility_nicu"))
//                                explore.setFacility_nicu(vals);
//                            if (maps.getKey().equals("facility_ventilator"))
//                                explore.setFacility_ventilator(vals);
//                            if (maps.getKey().equals("facility_ambulance"))
//                                explore.setFacility_ambulance(vals);
//                            if (maps.getKey().equals("facility_operating_theatre"))
//                                explore.setFacility_operating_theatre(vals);
//                            explore.setEmergency(vals);
//
//                            if (maps.getKey().equals("atm"))
//                                explore.setAtm(vals);
//                            if (maps.getKey().equals("nrb_class"))
//                                explore.setNrb_class(vals);
//                            if (maps.getKey().equals("operator_type"))
//                                explore.setOperator_type(vals);
//                            if (maps.getKey().equals("drinking_water"))
//                                explore.setDrinking_water(vals);
//                            if (maps.getKey().equals("toilet"))
//                                explore.setToilet(vals);
//                            if (maps.getKey().equals("network"))
//                                explore.setNetwork(vals);
//
//
//                            if (maps.getKey().equals("beds"))
//                                if (vals != null && vals.matches("-?\\d+"))
//                                    explore.setBeds(Integer.parseInt(vals));
//                                else explore.setBeds(null);
//
//                            if (maps.getKey().equals("stars"))
//                                if (vals != null && vals.matches("-?\\d+"))
//                                    explore.setStars(Integer.parseInt(vals));
//                                else explore.setStars(null);
//
//                            if (maps.getKey().equals("rooms"))
//                                if (vals != null && vals.matches("-?\\d+"))
//                                    explore.setRooms(Integer.parseInt(vals));
//                                else explore.setRooms(null);
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        setContentView(R.layout.edit_success);
        Button btnOk = findViewById(R.id.btn_successsubmission);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    String[] amenitySelected;
    private final String clientId = "1e7ToHZDNdUdhghvcEBvQeZS4WhTsU2eTRHKb3d2";
    private final String clientSecret = "GRSFEeSnNoCQ35y7OrOsOUJwww9KoDqOy3l8p03U";

    ////dev key
//    private final String clientId = "bkBSLB2MCbyCp5rowUb5XuGxdeqEN5lA9S2QFC1H";
//    private final String clientSecret = "OQq4ZunirBTi8m9dOVe5qd1wFQIAeryzB4ifUiyH";

    private final String redirectUri = "explorepokhara://authorize";

    public final String OAUTH_TOKEN_KEY = "oauth_key";
    public final String OAUTH_TOKEN_SECRET = "oauth_secret";
    public String osmId;
    public static Map<String,String> edit_key_value = new HashMap<>();
    public static Map<String,String> realm_key_value = new HashMap<>();
    public  static String amenityTopass;
    ProgressDialogFragment progressDialogFragment;



    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

        amenitySelected = getIntent().getStringArrayExtra("amenity");
        setContentView(R.layout.edit_dialog);
        setTitle("Edit");
        LinearLayout container = findViewById(R.id.editLayout);
        Button applybtn = findViewById(R.id.apply_edit);
        Realm realm = Realm.getDefaultInstance();
        if (amenitySelected != null) {
            amenityTopass = amenitySelected[0];
            int size = realm.where(Tag.class).equalTo("amenity", amenitySelected[0]).findAll().get(0).getTagslabel().size();
            RealmResults<Tag> tag = realm.where(Tag.class).equalTo("amenity", amenitySelected[0]).findAll();
            //ExploreSchema hosdet = realm.where(ExploreSchema.class).equalTo("name", amenitySelected[1]).findFirst();
            RealmQuery<ExploreSchema> query = realm.where(ExploreSchema.class);
            realm.close();
            ExploreSchema dbvalue;
//            if (!amenitySelected[0].equals(amenitySelected[1])) {
//                dbvalue = query.equalTo("name", amenitySelected[1]).findFirst();
//            }
//            else {
                double coordinateslat = Double.parseDouble(amenitySelected[2]);
                double coordinateslong = Double.parseDouble(amenitySelected[3]);
                dbvalue = query.equalTo("coordinateslong", coordinateslat).equalTo("coordinateslat", coordinateslong).findFirst();
//            }
            if (dbvalue != null) {
                String allValue;
                allValue = dbvalue.toString();
                osmId = dbvalue.getOsm_id();
                View view[];
                TextView editTag[];
                AppCompatEditText editValue[];
                editTag = new TextView[size];
                editValue = new AppCompatEditText[size];
                view = new View[size];
                EditPojo keyvalue = new EditPojo();
                Map<String, String> edits = new HashMap<>();
                String[] editKey;
                String[] dbKey;
                editKey = new String[size];
                dbKey = new  String[size];
                for (Tag tg : tag) {
                    Log.d(String.valueOf(tg.getOsmtags()), "osm");
                    Log.d(String.valueOf(tg.getTagslabel()), "lables");
                    Log.d(String.valueOf(tg.getTagkey()), "key");
                    int i = 0;
                    for (String lis : tg.getTagslabel()) {
                        String keyValue = tg.getTagkey().get(i);
                        view[i] = getLayoutInflater().inflate(R.layout.edittextgenerator, container, false);
                        editTag[i] = view[i].findViewById(R.id.attribute_type);
                        editValue[i] = view[i].findViewById(R.id.attribute_value);
                        editTag[i].setText(lis);
                        try {
                            if (allValue.contains(keyValue)) {
                                int j = allValue.lastIndexOf(keyValue + ":");
                                int k = allValue.indexOf("},", j);
                                String v = allValue.substring(j + (keyValue + ":").length(), k);
                                if (!v.equals("null")) {
                                    editValue[i].setText(v);
                                }
                                editKey[i] = tg.getOsmtags().get(i);
                                dbKey[i]=tg.getTagkey().get(i);
                                Log.d(v, "onCreate: ");
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                        container.addView(view[i]);
                        i++;
                    }
                }
                applybtn.setOnClickListener(v -> {

                    edit_key_value.clear();
                    edit_key_value = new HashMap<>();
                    Log.wtf(String.valueOf(size), "size");
                    for (int a = 0; a < size; a++) {
                        realm_key_value.put(dbKey[a],editValue[a].getText().toString());
                        if (!(editValue[a].getText() == null || editValue[a].getText().toString().equals(""))) {
                            Log.wtf(editKey[a], "keys");
                            edit_key_value.put(editKey[a], editValue[a].getText().toString());
                        }
                    }

                    if (Connectivity.isConnected(this)) {
                        showProgressDialog();
                        new OSMAuthneticator().execute("");
                    } else {
                        Toast.makeText(this, "Please Connect to the internet", Toast.LENGTH_LONG).show();
                    }

//            keyvalue.setEdits(edits);
//            if (keyvalue.getEdits().size() > 0) {
//                for (Map.Entry<String, String> entry : keyvalue.getEdits().entrySet()) {
//                    Log.d(entry.getKey(), entry.getValue());
//                }
//            }
                    //onBackPressed();
                });
            }
        }

    }
    private void showProgressDialog() {
        FragmentManager fm = getSupportFragmentManager();
        progressDialogFragment = ProgressDialogFragment.newInstance();
        progressDialogFragment.show(fm, "Progress Fragment");
    }

    private void dismissProgressDialog(){
        if(progressDialogFragment!=null)
            progressDialogFragment.dismiss();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("amenityedited",amenityTopass);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // the intent filter defined in AndroidManifest will handle the return from ACTION_VIEW intent
        Uri uri = getIntent().getData();
//        showProgressDialog();
        if (uri != null && uri.toString().startsWith("explorepokhara://authorize")) {
            // use the parameter your API exposes for the code (mostly it's "code")
            String token = uri.getQueryParameter("oauth_token");
            String verifier = uri.getQueryParameter("oauth_verifier");

            if (token != null) {
              new OSMGetToken(this).execute(verifier);
                    dismissProgressDialog();
                    showProgressDialog();

           } else if (uri.getQueryParameter("error") != null) {
                setContentView(R.layout.edit_error);
                Button btnEr = findViewById(R.id.btn_errorsubmission);
                btnEr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                });
            }
            else Toast.makeText(this,"No connection please try again later.",Toast.LENGTH_LONG).show();


            //
           // }
        }
    }

    private class OSMAuthneticator extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                OAuth10aService service = new ServiceBuilder(clientId)
                        .apiSecret(clientSecret)
                        .callback(redirectUri)
                        .build(OSMAuthApi.instance());
                OAuth1RequestToken requestToken = service.getRequestToken();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                dismissProgressDialog();
                editor.putString(OAUTH_TOKEN_KEY, requestToken.getToken());
                editor.putString(OAUTH_TOKEN_SECRET,requestToken.getTokenSecret());
                editor.putString("Osmid",osmId);
                editor.commit();

                String authUrl = service.getAuthorizationUrl(requestToken);
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(authUrl));
                startActivity(intent);
                //final OAuth1AccessToken accessToken = service.getAccessToken(requestToken, "verifier you got from the user/callback");
                //Log.wtf("Access Token",accessToken.getToken());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                dismissProgressDialog();
                e.printStackTrace();
            }
            return "Executed";
        }
    }

    private class OSMGetToken extends AsyncTask<String, Void, String>  {
        private MainActivity.OnTaskCompleted listener;

        public OSMGetToken(MainActivity.OnTaskCompleted listener){
            this.listener = listener;
        }
        @Override
        protected void onPostExecute(String s) {
            listener.onTaskCompleted();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String verifier = params[0];
                OAuth10aService service = new ServiceBuilder(clientId)
                        .apiSecret(clientSecret)
                        .callback(redirectUri)
                        .build(OSMAuthApi.instance());
                String oauthToken = sharedPreferences.getString(OAUTH_TOKEN_KEY, " ");
                String oauthSecret = sharedPreferences.getString(OAUTH_TOKEN_SECRET, " ");
                osmId = sharedPreferences.getString("Osmid", " ");
                OAuth1RequestToken requestToken = new OAuth1RequestToken(oauthToken, oauthSecret);
                OAuth1AccessToken accessToken = service.getAccessToken(requestToken, verifier);
                Log.wtf("Access Token", accessToken.getRawResponse());

                // Log.wtf("Access Secret",accessToken.getTokenSecret());
                OAuthConsumer oauth = new OAuthConsumer() {
                    @Override
                    public void setMessageSigner(OAuthMessageSigner messageSigner) {

                    }

                    @Override
                    public void setAdditionalParameters(HttpParameters additionalParameters) {

                    }

                    @Override
                    public void setSigningStrategy(SigningStrategy signingStrategy) {

                    }

                    @Override
                    public void setSendEmptyTokens(boolean enable) {

                    }

                    @Override
                    public HttpRequest sign(HttpRequest request) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException {
                        return null;
                    }

                    @Override
                    public HttpRequest sign(Object request) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException {
                        return null;
                    }

                    @Override
                    public String sign(String url) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException {
                        return null;
                    }

                    @Override
                    public void setTokenWithSecret(String token, String tokenSecret) {

                    }

                    @Override
                    public String getToken() {
                        return accessToken.getToken();
                    }

                    @Override
                    public String getTokenSecret() {
                        return accessToken.getTokenSecret();
                    }

                    @Override
                    public String getConsumerKey() {
                        return "1e7ToHZDNdUdhghvcEBvQeZS4WhTsU2eTRHKb3d2";
                    }

                    @Override
                    public String getConsumerSecret() {
                        return "GRSFEeSnNoCQ35y7OrOsOUJwww9KoDqOy3l8p03U";
                    }

                    @Override
                    public HttpParameters getRequestParameters() {
                        HttpParameters oauthParams = new HttpParameters();
                        oauthParams.put("oauth_consumer_key", "1e7ToHZDNdUdhghvcEBvQeZS4WhTsU2eTRHKb3d2");
                        oauthParams.put("oauth_consumer_secret", "GRSFEeSnNoCQ35y7OrOsOUJwww9KoDqOy3l8p03U");
//                        oauthParams.put("oauth_token",oauthToken);
//                        oauthParams.put("oauth_token_secret",oauthSecret);
                        oauthParams.put("oauth_token", accessToken.getToken());
                        oauthParams.put("oauth_token_secret", accessToken.getTokenSecret());


                        return oauthParams;
                    }
                };

                //oauth.setTokenWithSecret(accessToken.getToken(),accessToken.getTokenSecret());
//                OsmConnection osm = new OsmConnection(
//                        "https://master.apis.dev.openstreetmap.org/api/0.6/",
//                        USER_SERVICE, oauth);

                OsmConnection osm = new OsmConnection(
                        "https://api.openstreetmap.org/api/0.6/",
                        USER_SERVICE, oauth);



                Log.wtf(osm.getApiUrl(), "osm uri");
                MapDataDao mapd = new MapDataDao(osm);
                String allosmId = osmId.split("/")[1];
                String node = osmId.split("/")[0];
                allosmId = allosmId.trim();
                long osmd = Long.parseLong(allosmId);
                Map<String, String> tags = new HashMap<>();
                tags.put("id", allosmId);
                tags.put("created_by", "ExplorePokhara");
                Long changesetId = mapd.openChangeset(tags);
                tags.remove("created_by");
                tags.put("changeset", String.valueOf(changesetId));
                tags.put("user",osm.getUserAgent());
                tags.put("comment","Edited tags by Explore Pokhara Mobile App");
                //tags.put("uid",);
//                tags.put("comment", "#explore-pokhara-mobile");
                tags.put("visible", "true");
                Map<String,String> mapData = new HashMap<>();
//                tags.put("'timestamp",)
                switch (node){
                    case "node":mapData = mapd.getNode(osmd).getTags();
                        tags.put("lat", String.valueOf(mapd.getNode(osmd).getPosition().getLatitude()));
                        tags.put("lon",String.valueOf(mapd.getNode(osmd).getPosition().getLongitude()));

                        tags.put("version", String.valueOf(mapd.getNode(osmd).getVersion()));
                        break;
                    case "way":mapData = mapd.getWay(osmd).getTags();
                        tags.put("version", String.valueOf(mapd.getWay(osmd).getVersion()));
                        break;
                    case "relation":mapData = mapd.getRelation(osmd).getTags();
                        tags.put("version", String.valueOf(mapd.getRelation(osmd).getVersion()));
                        break;
                }
                Map<String, String> wayTags = new HashMap<>();
                    for (Map.Entry<String, String> mapdata : mapData.entrySet()
                            ) {
//                        Log.wtf(mapdata.getKey(), mapdata.getValue());
                        for (Map.Entry<String, String> editdata : edit_key_value.entrySet()
                                ) {
                            if(editdata.getKey()!=null && mapdata.getKey()!=null)
                            try {if (editdata.getKey().equals(mapdata.getKey())) {
                                mapdata.setValue(editdata.getValue());
                            }}catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        wayTags.put(mapdata.getKey(), mapdata.getValue());
                    }
                    for(Map.Entry<String,String> neweditsTags : edit_key_value.entrySet()){
                      //  Log.wtf(neweditsTags.getKey(),neweditsTags.getValue());
                        if(!wayTags.containsKey(neweditsTags.getKey())){
                                wayTags.put(neweditsTags.getKey(),neweditsTags.getValue());
                        }
                    }
                    XmlWriter xmlTag = createOsmChangesetTagsWriter(node,tags, wayTags);
                    try {
                        osm.makeAuthenticatedRequest(osmId, "PUT",xmlTag, null);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mapd.closeChangeset(changesetId);
//                for (Map.Entry<String, String> mapdata : tags.entrySet()
//                        ) {
//                    Log.wtf(mapdata.getKey(), mapdata.getValue());
//                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return "Executed";

        }

        private XmlWriter createOsmChangesetTagsWriter(String node ,Map<String, String> tags, Map<String, String> tagdata) {
            return new XmlWriter() {
                protected void write() throws IOException {
                    begin("osm");
                   begin(node);
                    for (Map.Entry<String, String> tagdata : tags.entrySet()) {
                        attribute(tagdata.getKey(), tagdata.getValue());
                    }

                    for (Map.Entry<String, String> tag : tagdata.entrySet()) {
                        begin("tag");
                        attribute("k", tag.getKey());
                        attribute("v", tag.getValue());
                        end();
                    }

                    end();
                    end();
                }
            };
        }
    }


}
