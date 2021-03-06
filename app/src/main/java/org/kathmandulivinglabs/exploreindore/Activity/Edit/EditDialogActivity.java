package org.kathmandulivinglabs.exploreindore.Activity.Edit;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.util.DisplayMetrics;
import android.util.Log;

import android.util.TypedValue;
import android.view.View;

import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.mapbox.mapboxsdk.annotations.Marker;

import org.json.JSONException;
import org.json.JSONObject;
import org.kathmandulivinglabs.exploreindore.Activity.LoginActivity;
import org.kathmandulivinglabs.exploreindore.Activity.MainActivity;
import org.kathmandulivinglabs.exploreindore.Api_helper.ApiHelper;
import org.kathmandulivinglabs.exploreindore.Api_helper.ApiInterface;
import org.kathmandulivinglabs.exploreindore.Helper.Connectivity;
import org.kathmandulivinglabs.exploreindore.Helper.Utils;
import org.kathmandulivinglabs.exploreindore.R;
import org.kathmandulivinglabs.exploreindore.Realmstore.ExploreSchema;
import org.kathmandulivinglabs.exploreindore.Realmstore.Tag;
import org.kathmandulivinglabs.exploreindore.RetrofitPOJOs.AuthenticateModel;
import org.kathmandulivinglabs.exploreindore.RetrofitPOJOs.EditParam;
import org.kathmandulivinglabs.exploreindore.View.ProgressDialogFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditDialogActivity extends AppCompatActivity implements MainActivity.OnTaskCompleted {

    @Override
    public void onTaskCompleted() {
        this.dismissProgressDialog();
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                final ExploreSchema explore = realm.where(ExploreSchema.class).contains("osm_id", osmId).findFirst();
                for (Map.Entry<String, String> maps : realm_key_value.entrySet()
                ) {
                    String vals = maps.getValue();
                    try {
                        if (vals != null) {

                        }
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

    public String osmId, previouslySelectedMarker;
    public static Map<String, String> edit_key_value = new HashMap<>();
    public static Map<String, String> realm_key_value = new HashMap<>();
    public static String amenityTopass;
    ProgressDialogFragment progressDialogFragment;
    private Marker marker;
    SharedPreferences sharedPreferences;
    private LinearLayout bottomLayout, container;
    private RelativeLayout rootLayout;
    private FancyButton btnApply, btnDiscard;
    private String[] editKey, dbKey;
    private List<String> tags, labels;
    private View[] view;
    private TextView[] editTag;
    private AppCompatEditText[] editValue;
    private int size = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

        amenitySelected = getIntent().getStringArrayExtra("amenity");
        previouslySelectedMarker = getIntent().getStringExtra("marker");
        setContentView(R.layout.edit_dialog);
        bottomLayout = findViewById(R.id.bottomLayout);
        rootLayout = findViewById(R.id.rootLayout);
        container = findViewById(R.id.editLayout);
        btnApply = findViewById(R.id.apply_edit);
        btnDiscard = findViewById(R.id.btnDiscard);
        setUpKeyboard();
        setTitle("Edit");
        Realm realm = Realm.getDefaultInstance();
        if (amenitySelected != null) {
            amenityTopass = amenitySelected[0];
            Log.wtf("aminitySelected", amenityTopass);
            size = realm.where(Tag.class).equalTo("amenity", amenitySelected[0]).findFirst().getOsmtags().size();
            RealmResults<Tag> tag = realm.where(Tag.class).equalTo("amenity", amenitySelected[0]).findAll();
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
                tags = dbvalue.getTag_type();
                labels = dbvalue.getTag_lable();

//                Log.wtf(allValue,"vals");
                osmId = dbvalue.getOsm_id();

                editTag = new TextView[size];
                editValue = new AppCompatEditText[size];
                view = new View[size];


                editKey = new String[size];
                dbKey = new String[size];
                for (Tag tg : tag) {

                    int i = 0;
                    for (String lis : tg.getOsmtags()) {
                        String keyValue = tg.getOsmtags().get(i);
                        view[i] = getLayoutInflater().inflate(R.layout.edittextgenerator, container, false);
                        editTag[i] = view[i].findViewById(R.id.attribute_type);
                        editValue[i] = view[i].findViewById(R.id.attribute_value);
                        editTag[i].setText(Utils.toTitleCase(lis.replace("_", " ")));
                        dbKey[i] = lis;
                        try {
                            if (tags.contains(keyValue)) {
                                editKey[i] = tg.getOsmtags().get(i);
                                int p = tags.indexOf(keyValue);
                                editValue[i].setText(labels.get(p));
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                        container.addView(view[i]);
                        i++;
                    }
                }

                btnApply.setOnClickListener(v -> {
                    Log.wtf(String.valueOf(size), "size");
                    JSONObject json = seeIfUserEditedAnything();
                    if (Connectivity.isConnected(this)) {
                        if (json.length() == 0) {
                            Toast.makeText(this, "Nothing edited", Toast.LENGTH_LONG).show();
                        } else {
                            showProgressDialog();
                            try {
                                update(json, osmId);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Toast.makeText(this, "Please Connect to the internet", Toast.LENGTH_LONG).show();
                    }
                });
                btnDiscard.setOnClickListener(View -> actionDiscard());
            }
        }

    }

    private void actionDiscard() {
        JSONObject json = seeIfUserEditedAnything();
        if (json.length() == 0) onBackPressed();
        else {
            // setup the alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Discard");
            builder.setMessage("Are you sure you want to discard the changes?");
            builder.setNegativeButton("Yes", (dialog, which) -> {
                dialog.dismiss();
                onBackPressed();
            });
            builder.setPositiveButton("Cancel", (dialog, which) ->
                    dialog.dismiss());

            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#E64228"));
        }
    }

    private JSONObject seeIfUserEditedAnything() {
        JSONObject json = new JSONObject();
        HashMap<String, String> updatedValue = new HashMap<>();
        for (int a = 0; a < size; a++) {
            realm_key_value.put(dbKey[a], editValue[a].getText().toString());
            if (!(editValue[a].getText() == null || editValue[a].getText().toString().equals(""))) {
                if (tags.indexOf(dbKey[a]) >= 0) {
                    if (!editValue[a].getText().toString().equals(labels.get(tags.indexOf(dbKey[a])))) {
                        updatedValue.put(dbKey[a], editValue[a].getText().toString());
                    }
                } else {
                    updatedValue.put(dbKey[a], editValue[a].getText().toString());
                }
            }
        }
        for (HashMap.Entry<String, String> h : updatedValue.entrySet()
        ) {
            try {
                json.put(h.getKey(), h.getValue());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return json;
    }

    private void setUpKeyboard() {
        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = rootLayout.getRootView().getHeight() - rootLayout.getHeight();
                if (heightDiff > dpToPx(EditDialogActivity.this, 200)) {
                    bottomLayout.setVisibility(View.GONE);
                } else bottomLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    private void showProgressDialog() {
        FragmentManager fm = getSupportFragmentManager();
        progressDialogFragment = ProgressDialogFragment.newInstance();
        progressDialogFragment.show(fm, "Progress Fragment");
    }

    private void dismissProgressDialog() {
        if (progressDialogFragment != null)
            progressDialogFragment.dismiss();
    }

    private void update(JSONObject jsonObject, String osmId) throws JSONException {
        Log.wtf("json object", String.valueOf(jsonObject));
        EditParam edits = new EditParam();
        edits.setData(String.valueOf(jsonObject));
        String token = MainActivity.mSharedPref.getString(LoginActivity.TOKEN, null);
        if (token != null) {
            ApiInterface api = new ApiHelper().getApiInterface();
            Call<AuthenticateModel> call = api.getSuccessResponse("Bearer " + token, osmId, edits);
            call.enqueue(new Callback<AuthenticateModel>() {
                @Override
                public void onResponse(Call<AuthenticateModel> call, Response<AuthenticateModel> response) {
                    Log.wtf(String.valueOf(response.raw()), "Raw response");
                    Log.wtf(String.valueOf(call.request()), "Call request");
                    if (response.isSuccessful()) {
                        if (response.body() != null && response.body().getSuccess() == 1) {
                            updateDB();
                            dismissProgressDialog();
                            Toast.makeText(getApplicationContext(), "Sucessfully edited", Toast.LENGTH_LONG).show();
                        } else {
                            dismissProgressDialog();
                            Toast.makeText(getApplicationContext(), "Unable to update", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        dismissProgressDialog();
                        Toast.makeText(getApplicationContext(), "Unable to update", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<AuthenticateModel> call, Throwable t) {
                    dismissProgressDialog();
                    Toast.makeText(getApplicationContext(), "Cannot connect to the server. Please try again later.", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void updateDB() {

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ExploreSchema explore = realm.where(ExploreSchema.class).contains("osm_id", osmId).findFirst();
                RealmList<String> tag = new RealmList<>();
                RealmList<String> value = new RealmList<>();
                int i = 0;
                for (HashMap.Entry<String, String> hs : realm_key_value.entrySet()) {
                    Log.wtf(hs.getKey(), hs.getValue());
                    tag.add(i, hs.getKey());
                    value.add(i, hs.getValue());
                    i++;
                }
                for (HashMap.Entry<String, String> hs : realm_key_value.entrySet()) {
                    Log.wtf(hs.getKey(), hs.getValue());
                    switch (hs.getKey()) {
                        case "name":
                            explore.setName(hs.getValue());
                            break;
                        case "name_hindi":
                            explore.setNamein(hs.getValue());
                            break;
                        case "phone":
                            explore.setContact_phone(hs.getValue());
                            break;
                        case "email":
                            explore.setContact_email(hs.getValue());
                            break;
                        case "website":
                            explore.setWeb(hs.getValue());
                            break;
                        case "capacity_beds":
                            if ((hs.getValue() != null) && (hs.getValue().matches("-?\\d+")))
                                explore.setCapacity_beds(Integer.parseInt(hs.getValue()));
                            break;
                        case "personnel_count":
                            if ((hs.getValue() != null) && (hs.getValue().matches("-?\\d+")))
                                explore.setPersonnel_count(Integer.parseInt(hs.getValue()));
                            break;
                        case "ward_no":
                            explore.setWard_id(hs.getValue());
                            break;
                        case "ward_name":
                            explore.setWard_name(hs.getValue());
                            break;

                        default:
                            break;
                    }
                }
                explore.setTag_type(tag);
                explore.setTag_lable(value);
                realm.insertOrUpdate(explore);
            }
        });
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("amenityedited", amenityTopass);
        String[] editInfo = {amenitySelected[2], amenitySelected[3], amenitySelected[1],};
        i.putExtra("marker", editInfo);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
