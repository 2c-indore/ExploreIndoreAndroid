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



import org.kathmandulivinglabs.exploreindore.Activity.MainActivity;
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

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmObjectChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;

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
    }
}
