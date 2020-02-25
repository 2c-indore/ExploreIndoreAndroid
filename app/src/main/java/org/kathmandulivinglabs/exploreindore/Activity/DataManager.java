package org.kathmandulivinglabs.exploreindore.Activity;

import android.app.IntentService;
import android.content.Intent;

import android.os.Bundle;
import android.os.ResultReceiver;

import androidx.annotation.Nullable;

import android.util.Log;
import android.widget.Toast;

import org.kathmandulivinglabs.exploreindore.Api_helper.ApiHelper;
import org.kathmandulivinglabs.exploreindore.Api_helper.ApiInterface;
import org.kathmandulivinglabs.exploreindore.Api_helper.Wards;
import org.kathmandulivinglabs.exploreindore.Realmstore.ExploreSchema;
import org.kathmandulivinglabs.exploreindore.Realmstore.FilterSchema;
import org.kathmandulivinglabs.exploreindore.Realmstore.PokharaBoundary;
import org.kathmandulivinglabs.exploreindore.Realmstore.Tag;
import org.kathmandulivinglabs.exploreindore.Realmstore.Ward;
import org.kathmandulivinglabs.exploreindore.RetrofitPOJOs.Data;
import org.kathmandulivinglabs.exploreindore.RetrofitPOJOs.Features;
import org.kathmandulivinglabs.exploreindore.RetrofitPOJOs.Filter;
import org.kathmandulivinglabs.exploreindore.RetrofitPOJOs.Tags;
import org.kathmandulivinglabs.exploreindore.models.POI.POIGeometry;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static org.kathmandulivinglabs.exploreindore.Activity.MainActivity.saveward;


public class DataManager extends IntentService {

    public static final String ACTION = "org.kathmandulivinglabs.org.Activity.DataManager";

    public DataManager() {
        super("DataManager");
    }

    private void removedata(String amenity_type) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> {
            final RealmResults<ExploreSchema> explore = realm1.where(ExploreSchema.class).contains("tag", amenity_type).findAll();
            if (explore.size() != 0) explore.deleteAllFromRealm();
            final RealmResults<FilterSchema> filter = realm1.where(FilterSchema.class).contains("amenity", amenity_type).findAll();
            if (filter.size() != 0) filter.deleteAllFromRealm();
            realm1.delete(Ward.class);
        });
        realm.executeTransaction(realm12 -> {

        });
        realm.executeTransaction(realm13 -> {
            realm13.delete(PokharaBoundary.class);
//                realm.delete(Ward.class);
            realm13.delete(Tag.class);
        });
        realm.close();

    }

    private void saveDataFromV2Api(final String def_type, Intent intent) {
        ApiInterface apiInterface = new ApiHelper().getApiInterface();
        Log.d(TAG, "saveDataFromV2Api: ");
        Call<Features> call = apiInterface.getFeature(def_type, "mobile");
        call.enqueue(new Callback<Features>() {
            @Override
            public void onResponse(Call<Features> call, Response<Features> response) {
                if (response.body() != null) {
                    if (response.body().getSuccess() == 1) {
                        Realm realm = Realm.getDefaultInstance();
                        try {
                            realm.beginTransaction();
                            List<Features.Geometries.Pois.Feature> features = response.body().getGeometries().getPois().getFeatures();
                            for (Features.Geometries.Pois.Feature feature : features) {
                                List<Filter.Option> tg = feature.getProperties().getTags();
                                ExploreSchema realmObject = realm.createObject(ExploreSchema.class);
                                RealmList<String> a_tag = new RealmList<>();
                                RealmList<String> a_value = new RealmList<>();
                                if (tg.size() > 0) {
                                    for (Filter.Option op : tg
                                    ) {
                                        a_tag.add(op.getLabel());
                                        a_value.add(op.getValue());
                                        switch (op.getLabel()) {
                                            case "name":
                                                realmObject.setName(op.getValue());
                                                break;
                                            case "name_hindi":
                                                realmObject.setNamein(op.getValue());
                                                break;
                                            case "phone_number":
                                                realmObject.setContact_phone(op.getValue());
                                                break;
                                            case "email":
                                                realmObject.setContact_email(op.getValue());
                                                break;
                                            case "website":
                                                realmObject.setWeb(op.getValue());
                                                break;
                                            case "capacity_beds":
                                                if ((op.getValue() != null) && (op.getValue().matches("-?\\d+")))
                                                    realmObject.setCapacity_beds(Integer.parseInt(op.getValue()));
                                                break;
                                            case "personnel_count":
                                                if ((op.getValue() != null) && (op.getValue().matches("-?\\d+")))
                                                    realmObject.setPersonnel_count(Integer.parseInt(op.getValue()));
                                                break;
                                            case "ward_no":
                                                realmObject.setWard_id(op.getValue());
                                                break;
                                            case "ward_name":
                                                realmObject.setWard_name(op.getValue());
                                                break;

                                            default:
                                                break;
                                        }
                                    }
                                }
                                realmObject.setTag(def_type);

                                realmObject.setOsm_id(feature.getId());

                                realmObject.setId(feature.getProperties().getId());
                                realmObject.setTag_type(a_tag);
                                realmObject.setTag_lable(a_value);


                                realmObject.setCoordinateslat(feature.getGeometry().getCoordinates().get(0));
                                realmObject.setCoordinateslong(feature.getGeometry().getCoordinates().get(1));
                                realmObject.setType(feature.getGeometry().getType());
                            }
                            List<Filter> filters = response.body().getFilters();
                            for (Filter filter : filters) {
                                FilterSchema filterObject = realm.createObject(FilterSchema.class);
                                filterObject.setAmenity(def_type);
                                filterObject.setType(filter.getType());
                                filterObject.setParameter_name(filter.parameterName);
                                filterObject.setLabel(filter.label);
                                filterObject.set_boolean(filter._boolean);
                                filterObject.setDbkey(filter.getDatabase_schema_key());
                                filterObject.setHigh(filter.getRange() == null ? null : filter.getRange().getHigh());
                                filterObject.setLow(filter.getRange() == null ? null : filter.getRange().getLow());
                                filterObject.setMax(filter.getRange() == null ? null : filter.getRange().getMax());
                                filterObject.setMin(filter.getRange() == null ? null : filter.getRange().getMin());
                                RealmList<String> option_label = new RealmList<>();
                                RealmList<String> option_value = new RealmList<>();
                                RealmList<String> option_dbkey = new RealmList<>();
                                if (filter.getOptions() != null) {
                                    for (Filter.Option option : filter.getOptions()) {
                                        option_label.add(option.label);
                                        option_value.add(option.value);
                                        option_dbkey.add(option.getDatabase_schema_key());
                                    }
                                    filterObject.setOption_lable(option_label);
                                    filterObject.setOption_value(option_value);
                                    filterObject.setOption_key(option_dbkey);
                                }

                            }
                            final RealmResults<Ward> ward = realm.where(Ward.class).findAll();
                            if (ward.size() == 0) {
                                Wards.Boundary bound = response.body().getGeometries().getBoundary();
                                List<Wards.BoundaryWithWards.Feature> ward_bounds = response.body().getGeometries().getBoundaryWithWards().getFeatures();
                                saveward(realm, bound, ward_bounds);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            realm.commitTransaction();
                            realm.close();
                        }
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<Features> call, Throwable t) {
                t.printStackTrace();
            }

        });

    }

    private void savetag() {
        ApiInterface api = new ApiHelper().getApiInterface();
        Call<Tags> call = api.getTag();
        // showProgressDialog();
        call.enqueue(new Callback<Tags>() {
            @Override
            public void onResponse(Call<Tags> call, Response<Tags> response) {
                if (response.body() != null) {
                    if (response.body().getSuccess().equals("1")) {
                        Realm realm = Realm.getDefaultInstance();
                        Data[] amenityTag = response.body().getData();
                        realm.beginTransaction();
                        try {
                            for (Data ame : amenityTag
                            ) {
                                String amenity_name = ame.getAmenity();
                                String[] amenity_tag = ame.getTags();
                                RealmList<String> osm_tag = new RealmList<>();
                                osm_tag.addAll(Arrays.asList(amenity_tag));
                                Tag tagData = realm.createObject(Tag.class);
                                tagData.setAmenity(amenity_name);
                                tagData.setOsmtags(osm_tag);
                            }
                        } finally {
                            realm.commitTransaction();
                            realm.close();
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<Tags> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ResultReceiver receiver = intent.getParcelableExtra("receiver");

        // data that will be send into ResultReceiver
        Bundle data = new Bundle();
        int i = 0;
        for (Map.Entry<String, String> entry : MainActivity.tagMp.entrySet()) {
            removedata(entry.getKey());
            try {
                saveDataFromV2Api(entry.getKey(), intent);
                Thread.sleep(1000);
                Log.d(TAG, "onHandleIntent: " + i);
                data.putInt("progress", i);
                receiver.send(450, data);
            } catch (InterruptedException e) {
                Log.d(TAG, "onHandleIntent: " + e.getMessage());
                e.printStackTrace();
            }
            i++;
        }
        try {
            Log.d(TAG, "onHandleIntent: " + i);
            savetag();
            Thread.sleep(1000);
            data.putInt("progress", i);
            receiver.send(450, data);
        } catch (InterruptedException e) {
            Log.d(TAG, "onHandleIntent: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
