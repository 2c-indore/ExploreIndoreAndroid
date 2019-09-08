package org.kathmandulivinglabs.exploreindore.Activity;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;

import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DataManager extends IntentService {

    public static final String ACTION = "org.kathmandulivinglabs.org.Activity.DataManager";

    public DataManager() {
        super("DataManager");
    }

    private void removedata(String amenity_type) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                final RealmResults<ExploreSchema> explore = realm.where(ExploreSchema.class).contains("tag", amenity_type).findAll();
                if (explore.size() != 0) explore.deleteAllFromRealm();
                final RealmResults<FilterSchema> filter = realm.where(FilterSchema.class).contains("amenity", amenity_type).findAll();
                if (filter.size() != 0) filter.deleteAllFromRealm();
                realm.delete(Ward.class);
            }
        });
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

            }
        });
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(PokharaBoundary.class);
//                realm.delete(Ward.class);
                realm.delete(Tag.class);
            }
        });
        realm.close();

    }

    private void saveDataFromV2Api(final String def_type, Intent intent) {
        ApiInterface apiInterface = new ApiHelper().getApiInterface();

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
                                            case "phone":
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
/*
                            Intent in = new Intent(ACTION);
                            // Put extras into the intent as usual
                            in.putExtra("resultCode", Activity.RESULT_OK);
                            in.putExtra("resultValue", "My Result Value. Passed in: " + def_type);
                            // Fire the broadcast with intent packaged
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(in);
                            // or sendBroadcast(in) for a normal broadcast;
  */

                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            realm.commitTransaction();
                            realm.close();
                        }
                    }
                } else {
//                    Toast.makeText(getApplicationContext(), "Server is not responding? Please try again later", Toast.LENGTH_LONG).show();

                }
//                    dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<Features> call, Throwable t) {
                t.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "Are you connected to internet? If not, connect and update the data", Toast.LENGTH_LONG).show();
//                    updateMapView = true;
//                    setSnackbar("Could not update data. Please connect to the internet and hit 'Retry'");

                }

        });

    }

    private void saveward(Realm realm, Wards.Boundary bound, List<Wards.BoundaryWithWards.Feature> ward_bounds) {
        for (List<List<Double>> bound_prop : bound.getFeatures().get(0).getGeometry().getCoordinates().get(0)) {
            for (List<Double> bound_coord : bound_prop
            ) {
                PokharaBoundary pb = realm.createObject(PokharaBoundary.class);
                pb.setTag("all_boundary");
                pb.setCoordinateslong(bound_coord.get(0));
                pb.setCoordinateslat(bound_coord.get(1));
            }
        }
        for (Wards.BoundaryWithWards.Feature ward_prop : ward_bounds
        ) {
            Ward ward = realm.createObject(Ward.class);
            String wardname = ward_prop.getProperties().getWard_name();
//
            String dbname = ward_prop.getProperties().getWard_no();
            int wardno = Integer.parseInt(dbname);
//            Log.wtf(wardname, "ward");
//
            ward.setName(wardname);
            ward.setNumber(wardno);
            ward.setOsmID(dbname);
//
            Wards.BoundaryWithWards.Feature.Geometry_ geom = ward_prop.getGeometry();
            RealmList<PokharaBoundary> pbound;
            pbound = new RealmList<>();

            for (List<List<Double>> sds : geom.getCoordinates()
            ) {
                for (List<Double> coord : sds
                ) {
                    PokharaBoundary pb = realm.createObject(PokharaBoundary.class);
                    pb.setTag("ward_boundary");
                    pb.setCoordinateslong(coord.get(0));
                    pb.setCoordinateslat(coord.get(1));
                    pbound.add(pb);
                }

            }
            ward.setBoundry(pbound);
        }

    }

    private void savetag() {
        Log.d("onSave", "savefunc: ");
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
                data.putInt("progress",i);
                receiver.send(450, data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
        }
        try {
            savetag();
            Thread.sleep(1000);
            data.putInt("progress",i);
            receiver.send(450, data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
