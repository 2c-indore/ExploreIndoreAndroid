package org.kathmandulivinglabs.exploreindore.Activity;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineRegionError;
import com.mapbox.mapboxsdk.offline.OfflineRegionStatus;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;

import org.json.JSONObject;
import org.kathmandulivinglabs.exploreindore.Adapter.ExpandableMenuAdapter;
import org.kathmandulivinglabs.exploreindore.Adapter.ExpandedMenuModel;
import org.kathmandulivinglabs.exploreindore.Adapter.FragmentAdapter;
import org.kathmandulivinglabs.exploreindore.Api_helper.ApiHelper;
import org.kathmandulivinglabs.exploreindore.Api_helper.ApiInterface;
import org.kathmandulivinglabs.exploreindore.RetrofitPOJOs.Data;
import org.kathmandulivinglabs.exploreindore.RetrofitPOJOs.Tags;
import org.kathmandulivinglabs.exploreindore.Api_helper.Wards;
//import org.kathmandulivinglabs.exploreindore.BuildConfig;
import org.kathmandulivinglabs.exploreindore.BuildConfig;
import org.kathmandulivinglabs.exploreindore.Customclass.CustomViewPager;
import org.kathmandulivinglabs.exploreindore.FilterParcel;
import org.kathmandulivinglabs.exploreindore.Fragment.InsightFragment;
import org.kathmandulivinglabs.exploreindore.Fragment.MapFragment;
import org.kathmandulivinglabs.exploreindore.Helper.Connectivity;

import org.kathmandulivinglabs.exploreindore.Interface.ToggleTabVisibilityListener;
import org.kathmandulivinglabs.exploreindore.R;

import org.kathmandulivinglabs.exploreindore.Realmstore.ExploreSchema;
import org.kathmandulivinglabs.exploreindore.Realmstore.FilterSchema;
import org.kathmandulivinglabs.exploreindore.Realmstore.PokharaBoundary;
import org.kathmandulivinglabs.exploreindore.Realmstore.Tag;
import org.kathmandulivinglabs.exploreindore.Realmstore.Ward;

import org.kathmandulivinglabs.exploreindore.RetrofitPOJOs.Features;
import org.kathmandulivinglabs.exploreindore.RetrofitPOJOs.Filter;
import org.kathmandulivinglabs.exploreindore.View.ProgressDialogFragment;


import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmList;

import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, InsightFragment.onInsightSelected, ToggleTabVisibilityListener {

    public String updateText() {
        mSharedPref = getSharedPreferences(getString(R.string.prefrence_file_key), Context.MODE_PRIVATE);
        String user = mSharedPref.getString(LoginActivity.AUTHUSERNAME, "Explore Indore");
        if (user.equals("Explore Indore"))
            user = mSharedPref.getString(LoginActivity.AUTHEMAIL, "Explore Indore");
        Auth = mSharedPref.getBoolean(LoginActivity.AUTHENTICATED, false);
        Log.wtf(user, String.valueOf(Auth));
        if (Auth) this.tv.setText(user);
        return user;
    }


    public interface OnTaskCompleted {
        void onTaskCompleted();
    }

    public static String def_type = "public_hospitals";
    public static String def_type_category = "Public Hospitals";
    public static boolean infoScreen = false;
    public static SharedPreferences mSharedPref;
    private CustomViewPager viewPager;
    private boolean filter_applied = false, navClicked = false;
    private NavigationView navigationView;
    private boolean update_data = false;
    public static boolean updateMapView = false, destroyMapView = false;
    private boolean mapsDownloading = false;
    private NotificationManagerCompat mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private DrawerLayout drawer;
    private ExpandableMenuAdapter menuAdapter;
    private ExpandableListView expandableList;
    private TabLayout tabs;
    List<ExpandedMenuModel> listDataHeader;
    HashMap<ExpandedMenuModel, List<String>> listDataChild;
    Map<String, String> tagMp;
    Map<String, String> tagHospitals, tagClincs, tagOthers;
    private static String oldtag;
    public static Map<String, String> filter_param;
    private static boolean downloadalldata = false;
    ProgressDialogFragment progressDialogFragment;
    private Snackbar snackbar;
    private NotificationChannel channel;
    private NotificationManager notificationManager;
    private Backlistner mBack = null;
    private boolean backPressed = false;

    int PROGRESS_MAX = 100;
    int PROGRESS_CURRENT = 0;
    double progress = 0;
    boolean Auth;
    private static final String ACTION_CUSTOM_BROADCAST =
            BuildConfig.APPLICATION_ID + ".ACTION_CUSTOM_BROADCAST";


    public void setMyClassListener(Backlistner listener) {
        this.mBack = listener;
    }

    private TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv = findViewById(R.id.btn_login);
        String user = updateText();
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawers();
                if (!Auth) {
                    Intent intentabout = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intentabout);
                } else showAlertDialogButtonClicked(user);
            }
        });
        drawer = findViewById(R.id.drawer_layout);
        expandableList = findViewById(R.id.expand_nav);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        String channelId = "map_download";

        mNotificationManager = NotificationManagerCompat.from(this);
        mBuilder = new NotificationCompat.Builder(getApplicationContext(), "CHANNEL_ID")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Downloading")
                .setContentText("Data is being downloaded in background")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setChannelId(channelId)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true);

//        String channelId = "explore_download";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Downloading";
            String description = "Data is being downloaded in background";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }

        navigationView = findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        prepareListData();
        filter_param = new HashMap<>();
        oldtag = def_type;

        tagMp = new HashMap<>();
        tagMp.put("public_hospitals", "Public Hospitals");
        tagMp.put("private_hospitals", "Private Hospitals");
        tagMp.put("public_clinics", "Public Clinics and Government Centers");
        tagMp.put("private_clinics", "Private Clinics");
        tagMp.put("dentists", "Dentists");
        tagMp.put("veterinaries", "Veterinaries");
        tagMp.put("patho_radio_labs", "Pathology and Radiology Labs");
        tagMp.put("anganwadi", "Anganwadis");
        tagMp.put("blood_banks", "Blood Banks");
        tagMp.put("mental_health_centers", "Mental Health Centers");
        tagMp.put("bus_stops", "Bus Stops");


        menuAdapter = new ExpandableMenuAdapter(this, listDataHeader, listDataChild, expandableList);

        // setting list adapter
        expandableList.setAdapter(menuAdapter);
        expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                String childValue = listDataChild.get(listDataHeader.get(i)).get(i1);
                getSupportActionBar().setTitle(childValue);
                for (Map.Entry<String, String> entry : tagMp.entrySet()) {
                    if (entry.getValue().equals(childValue)) {
                        tabs.setupWithViewPager(viewPager);
                        if (filter_param != null) filter_param.clear();
                        filter_param = new HashMap<>();
                        drawer.closeDrawers();
                        oldtag = entry.getKey();
                        makeMapData(entry.getKey());

                        Log.d(entry.getKey(), "child");
                    }
                }
                switch (childValue) {
                    case "Update":
                        drawer.closeDrawers();
                        updateRealm(oldtag);
                        getSupportActionBar().setTitle(tagMp.get(def_type));
//
                        break;
                    case "Offline map":
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(tagMp.get(def_type));
                        if (Connectivity.isConnected(Mapbox.getApplicationContext())) {
//                            downloadStarted();
                            downloadBaseMap();
                        } else
                            Snackbar.make(MainActivity.this.findViewById(android.R.id.content), "No internet connection", Snackbar.LENGTH_LONG).show();
//
                        break;
//
                    case "About Us":
                        drawer.closeDrawers();
                        Log.d("About Us", def_type);
                        Intent intentabout = new Intent(getApplicationContext(), AboutActivity.class);
                        intentabout.putExtra("amenityType", def_type);
                        startActivity(intentabout);

                        break;
                }


                return false;
            }
        });
        expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                String childValue = listDataHeader.get(i).getIconName();
                return false;
            }
        });
        Realm realm = Realm.getDefaultInstance();
        //TODO

        final RealmResults<Tag> tag = realm.where(Tag.class).findAll();
        final RealmResults<ExploreSchema> explore_data = realm.where(ExploreSchema.class).findAll();
        List<String> amenity_tag = new ArrayList<>();
        if (tag.size() == 0)
            savetag();

        if (explore_data.size() == 0) {
            showProgressDialog();
//            downloadAll();
            saveDataFromV2Api(def_type);
        }
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(1);
        // Set Tabs inside Toolbar
        tabs = findViewById(R.id.result_tabs);
        tabs.setupWithViewPager(viewPager);
        Intent i = getIntent();
        String amenityedited;
        String fromabout;
        if (i != null) {
            amenityedited = i.getStringExtra("amenityedited");
            fromabout = i.getStringExtra("about");
            Log.wtf(fromabout, "ABout");
            if (fromabout == null) {
                for (Map.Entry<String, String> entry : tagMp.entrySet()) {
                    if (entry.getKey().equals(amenityedited)) {
                        amenityedited = entry.getValue();
                    }
                }
                if (amenityedited != null) {
                    getSupportActionBar().setTitle(amenityedited);
                } else {
                    getSupportActionBar().setTitle("Public Hospitals");
//                    tabs.removeTabAt(1);
                }
            } else getSupportActionBar().setTitle(tagMp.get(fromabout));

        } else {
            getSupportActionBar().setTitle("Public Hospitals");
//            tabs.removeTabAt(1);
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d(String.valueOf(position), "onPageSelected: ");
                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void showAlertDialogButtonClicked(String user) {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Account Setting");
        builder.setMessage("Setting for user: " + user);

        // add the buttons
        builder.setPositiveButton("Log out", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences.Editor memory = mSharedPref.edit();
                memory.remove(LoginActivity.TOKEN);
                memory.remove(LoginActivity.AUTHEMAIL);
                memory.putBoolean(LoginActivity.AUTHENTICATED, false);
                Auth = false;
                memory.apply();
                tv.setText("  Log In");
                expandableList.refreshDrawableState();
                fragmentRefresh();
            }
        });
        builder.setNeutralButton("Cancel", null);
        builder.setNegativeButton("Reset Password", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intentabout = new Intent(getApplicationContext(), ResetPasswordActivity.class);
                startActivity(intentabout);
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#1bd393"));
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setAllCaps(false);
//        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.parseColor("#"));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#1bd393"));
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawer.closeDrawers();
                        return true;
                    }
                });
    }

    private void downloadAll() {

        showProgressDialog();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
//                saveward();
                savetag();
                for (Map.Entry<String, String> attribs : tagMp.entrySet()
                ) {
                    removedata(attribs.getKey());
                    saveDataFromV2Api(attribs.getKey());
                }
//                downloadalldata=false;
            }
        });
        downloadalldata = true;
        thread.start();
    }

    private void makeMapData(String selectedkey) {
        Realm realm = Realm.getDefaultInstance();
        //TODO
        final RealmResults<ExploreSchema> explore = realm.where(ExploreSchema.class).contains("tag", selectedkey).findAll();
        realm.close();
        def_type = selectedkey;

        if (explore.size() == 0) {
            showProgressDialog();
            saveDataFromV2Api(selectedkey);
        } else {
            updateMapView = true;
            fragmentRefresh();
        }

    }

    private void updateRealm(String selectedkey) {
        showProgressDialog();
        removedata(selectedkey);
//        saveward();
        savetag();
        saveDataFromV2Api(selectedkey);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<ExpandedMenuModel>();
        listDataChild = new HashMap<ExpandedMenuModel, List<String>>();
        ExpandedMenuModel hospitals = new ExpandedMenuModel();
        hospitals.setIconName("Hospitals");
        listDataHeader.add(hospitals);

        ExpandedMenuModel clinics = new ExpandedMenuModel();
        clinics.setIconName("Clinics");
        listDataHeader.add(clinics);

        ExpandedMenuModel others = new ExpandedMenuModel();
        others.setIconName("Others");
        listDataHeader.add(others);


        ExpandedMenuModel download = new ExpandedMenuModel();
        download.setIconName("Download");
        listDataHeader.add(download);


        ExpandedMenuModel about = new ExpandedMenuModel();
        about.setIconName("About the Project");
        listDataHeader.add(about);

        // Adding child data
        List<String> hospitalslist = new ArrayList<>();
        List<String> clinicslist = new ArrayList<String>();
        List<String> otherslist = new ArrayList<String>();
        List<String> downloadlist = new ArrayList<String>();
        List<String> aboutlist = new ArrayList<String>();

        hospitalslist.add("Public Hospitals");
        hospitalslist.add("Private Hospitals");

        clinicslist.add("Public Clinics and Government Centers");
        clinicslist.add("Private Clinics");
        clinicslist.add("Dentists");
        clinicslist.add("Veterinaries");
        clinicslist.add("Pathology and Radiology Labs");

        otherslist.add("Anganwadis");
        otherslist.add("Blood Banks");
        otherslist.add("Mental Health Centers");
        otherslist.add("Bus Stops");

        downloadlist.add("Update");
        // downloadlist.add("Download map data");
        downloadlist.add("Offline map");

        aboutlist.add("About Us");

        listDataChild.put(listDataHeader.get(0), hospitalslist);
        listDataChild.put(listDataHeader.get(1), clinicslist);
        listDataChild.put(listDataHeader.get(2), otherslist);
        listDataChild.put(listDataHeader.get(3), downloadlist);
        listDataChild.put(listDataHeader.get(4), aboutlist);


    }


    public String getSelected() {
        return def_type;
    }

    private void setupViewPager(CustomViewPager viewPager) {

        Bundle args = new Bundle();
        args.putString("selectedType", def_type);
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), args);
        adapter.addFragment(new MapFragment(), "Map");
        adapter.addFragment(new InsightFragment(), "Filters");
        viewPager.setAdapter(adapter);

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

    private void showProgressDialog() {
        FragmentManager fm = getSupportFragmentManager();
        progressDialogFragment = ProgressDialogFragment.newInstance();
        progressDialogFragment.show(fm, "Progress Fragment");
    }

    private void dismissProgressDialog() {
        if (progressDialogFragment != null)
            progressDialogFragment.dismiss();
    }

    private void saveDataFromV2Api(final String def_type) {
        ApiInterface apiInterface = new ApiHelper().getApiInterface();

        Call<Features> call = apiInterface.getFeature(def_type, "mobile");
        //  showProgressDialog();
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
//                                    org.kathmandulivinglabs.exploreindore.RetrofitPOJOs.Tags tg = feature.getProperties().getTags();
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

//                                    if (tg.getCapacityBeds() != null && tg.getCapacityBeds().matches("-?\\d+"))
//                                        realmObject.setCapacity_beds(Integer.parseInt(tg.getCapacityBeds()));
//                                    else realmObject.setCapacity_beds(null);


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
                            updateMapView = true;
                            if (!downloadalldata)
                                fragmentRefresh();
                            Log.d("realm closed", "map data ");
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Server is not responding? Please try again later", Toast.LENGTH_LONG).show();
                    updateMapView = true;
                    if (!downloadalldata)
                        fragmentRefresh();
                }
                if (!downloadalldata)
                    dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<Features> call, Throwable t) {
                t.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "Are you connected to internet? If not, connect and update the data", Toast.LENGTH_LONG).show();
//                    updateMapView = true;
                if (!downloadalldata) {
                    setSnackbar("Could not update data. Please connect to the internet and hit 'Retry'");
                    snackbar.show();
                    dismissProgressDialog();
                    fragmentRefresh();
                }

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
            Log.wtf(wardname, "ward");
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
//                            if(!downloadalldata)
//                            fragmentRefresh();
//                            Log.d("realm closed", "onResponse: ");
                        }

                    }
                }
                // dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<Tags> call, Throwable t) {
                t.printStackTrace();
                // dismissProgressDialog();
            }
        });
    }

    private void fragmentRefresh() {
        Bundle args = new Bundle();
        args.putString("selectedType", def_type);
        String tagmap = "android:switcher:" + R.id.viewpager + ":" + 0;
        String tagfeature = "android:switcher:" + R.id.viewpager + ":" + 1;
        MapFragment mpfrag = (MapFragment) getSupportFragmentManager().findFragmentByTag(tagmap);
        if (mpfrag != null) {
            Log.d("inside mapfrag", "inside");
            mpfrag.setArguments(args);
            if (update_data) {
//                mpfrag.doSomething("first");
                update_data = false;
            }
            mpfrag.setFilter(true);
//            else mpfrag.doSomething(def_type);

        }
        InsightFragment infrag = (InsightFragment) getSupportFragmentManager().findFragmentByTag(tagfeature);
        if (infrag != null) {
            infrag.setArguments(args);
        }

        if (viewPager.getAdapter() != null)
            viewPager.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        // super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            String tagmap = "android:switcher:" + R.id.viewpager + ":" + 0;
            MapFragment mpfrag = (MapFragment) getSupportFragmentManager().findFragmentByTag(tagmap);
            if (filter_applied) {
                filter_applied = false;
                fragmentRefresh();
            } else if (mpfrag == null || !((Backlistner) mpfrag).onBackPressed()) {
                super.onBackPressed();
            }
        }
    }

    public interface Backlistner {
        // add whatever methods you need here
        boolean onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        navClicked = true;
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onInsight(Boolean vals) {
        String tag = "android:switcher:" + R.id.viewpager + ":" + 0;
        filter_applied = vals;
        Log.d(String.valueOf(vals), "onInsight: ");
        MapFragment mpfrag = (MapFragment) getSupportFragmentManager().findFragmentByTag(tag);
        Intent i = getIntent();
        FilterParcel object = i.getParcelableExtra("FilterValue");
        mpfrag.setFilter(vals);
        viewPager.setCurrentItem(0);
    }

    @Override
    protected void onResume() {
        super.onResume();


        // the intent filter defined in AndroidManifest will handle the return from ACTION_VIEW intent
        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith("explorepokhara://authorize")) {
            // use the parameter your API exposes for the code (mostly it's "code")

            String code = uri.getQueryParameter("oauth_token");
            if (code != null) {

                // get access token
                // we'll do that in a minute
            } else if (uri.getQueryParameter("error") != null) {
                // show an error message here
            }
        }


        Log.d(String.valueOf(filter_applied), "onResume: ");
        Intent i = getIntent();
        FilterParcel object = i.getParcelableExtra("FilterValue");
        if (object != null) {
            Log.d("value changed", "onResume");
            String tagmap = "android:switcher:" + R.id.viewpager + ":" + 0;
            MapFragment mpfrag = (MapFragment) getSupportFragmentManager().findFragmentByTag(tagmap);
            Bundle bd = new Bundle();
            bd.putParcelable("FilterValue", object);
            bd.putString("selectedType", def_type);
            Log.d(def_type, "onResume: ");
            if (mpfrag != null) {
                mpfrag.setArguments(bd);
                if (viewPager.getAdapter() != null)
                    viewPager.getAdapter().notifyDataSetChanged();
            } else {
                MapFragment newmpfrag = new MapFragment();
                newmpfrag.setArguments(bd);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.viewpager, newmpfrag).addToBackStack(null).commit();
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (viewPager.getCurrentItem() == 1) {
                viewPager.setCurrentItem(0, true);
            } else if (viewPager.getCurrentItem() == 0) {
                onBackPressed();
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public void goBack() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                this);

        alertDialog.setTitle("");
        alertDialog.setMessage("Are you sure you want to exit?");

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        finishAffinity();
                        System.exit(0);
                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    private void downloadBaseMap() {
        if (!mapsDownloading) {
            Log.d("maps...", "downloadBaseMap: ");
            OfflineManager offlineManager = OfflineManager.getInstance(getApplicationContext());
            offlineManager.setOfflineMapboxTileCountLimit(10000);
            // Create a bounding box for the offline region
            LatLngBounds latLngBounds = new LatLngBounds.Builder()
                    .include(new LatLng(22.6276, 76.0276)) // Northeast
                    .include(new LatLng(22.8252, 75.7565)) // Southwest
                    .build();

            // Define the offline region
            OfflineTilePyramidRegionDefinition definition = new OfflineTilePyramidRegionDefinition(
                    "mapbox://styles/mapbox/streets-v10",
                    latLngBounds,
                    9,
                    16,
                    MainActivity.this.getResources().getDisplayMetrics().density);

            // Implementation that uses JSON to store Municipality Basemap as the offline region name.
            byte[] metadata;
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Region Name", "Indore Basemap");
                String json = jsonObject.toString();
                metadata = json.getBytes(StandardCharsets.UTF_8);
            } catch (Exception exception) {
                exception.printStackTrace();
                metadata = null;
            }


            mNotificationManager.notify(0, mBuilder.build());
            // Create the region asynchronously
            offlineManager.createOfflineRegion(definition, metadata,
                    new OfflineManager.CreateOfflineRegionCallback() {
                        @Override
                        public void onCreate(OfflineRegion offlineRegion) {
                            offlineRegion.setDownloadState(OfflineRegion.STATE_ACTIVE);

                            // Monitor the download progress using setObserver
                            offlineRegion.setObserver(new OfflineRegion.OfflineRegionObserver() {
                                @Override
                                public void onStatusChanged(OfflineRegionStatus status) {


                                    double percentage = status.getRequiredResourceCount() >= 0
                                            ? (100.0 * status.getCompletedResourceCount() / status.getRequiredResourceCount()) :
                                            0.0;
                                    if (!status.isComplete()) {
                                        mBuilder.setProgress(100, (int) percentage, false);
                                        mBuilder.setContentTitle("Please wait...");
                                        int per = (int) percentage;

                                        mBuilder.setContentText(per + "% " + "downloaded").setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText(per + "% " + "downloaded"));

                                    } else {
                                        status.isComplete();
                                        // Download complete
                                        Log.d("Basemap Download", "Region downloaded successfully.");
                                        mBuilder.setContentTitle("Download Successful")
                                                .setContentText("Offline Map has been succesfully downloaded")
                                                .setStyle(new NotificationCompat.BigTextStyle()
                                                        .bigText("Download has been successfully completed."))
                                                .setProgress(0, 0, false)
                                                .setAutoCancel(true);
                                    }
                                    mNotificationManager.notify(0, mBuilder.build());
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                        notificationManager.notify(0, mBuilder.build());

                                }

                                @Override
                                public void onError(OfflineRegionError error) {
                                    // If an error occurs, print to logcat
//                                    Log.e("onError reason: ", error.getReason());
//                                    Log.e( "onError message: " , error.getMessage());
                                    mBuilder.setContentTitle("Download canceled")
                                            .setContentText("Offline map cannot be downloaded, please try again later.")
                                            .setProgress(0, 0, false)
                                            .setAutoCancel(true);
                                    mNotificationManager.notify(0, mBuilder.build());
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                        notificationManager.notify(0, mBuilder.build());
                                }

                                @Override
                                public void mapboxTileCountLimitExceeded(long limit) {
                                    mBuilder.setContentTitle("Download canceled")
                                            .setContentText("Offline map cannot be downloaded, please try again later.")
                                            .setProgress(0, 0, false)
                                            .setAutoCancel(true);
                                    mNotificationManager.notify(0, mBuilder.build());
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                        notificationManager.notify(0, mBuilder.build());
                                    // Notify if offline region exceeds maximum tile count
//                                    Log.e(String.valueOf(limit), "Mapbox tile count limit exceeded: " + limit);
                                }

                            });
                        }

                        @Override
                        public void onError(String error) {
                            //Log.e(TAG, "Error: " + error);
                        }
                    });

        }

    }

    public void downloadStarted() {
//        String channelId = "explore_download";
//        mBuilder = new NotificationCompat.Builder(getApplicationContext(), "CHANNEL_ID")
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("Downloading")
//                .setContentText("Data is being downloaded in background")
//                .setPriority(NotificationCompat.PRIORITY_MAX)
//                .setChannelId(channelId)
//                .setAutoCancel(true)
//                .setOnlyAlertOnce(true);
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = "Downloading";
//            String description = "Data is being downloaded in background";
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            channel = new NotificationChannel(channelId, name, importance);
//            channel.setDescription(description);
//            notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//            assert notificationManager != null;
//            notificationManager.createNotificationChannel(channel);
//        }


        mBuilder.setProgress(100, 0, false);
        Toast.makeText(getApplicationContext(), "Map is downloading", Toast.LENGTH_SHORT).show();
        mNotificationManager.notify(0, mBuilder.build());
    }

    @Override
    public void hideTabs() {
        tabs.setVisibility(View.GONE);

    }

    @Override
    public void showTabs() {
        tabs.setVisibility(View.VISIBLE);
    }

    public void doSomethingMemoryIntensive() {

        // Before doing something that requires a lot of memory,
        // check to see whether the device is in a low memory state.
        ActivityManager.MemoryInfo memoryInfo = getAvailableMemory();

        if (!memoryInfo.lowMemory) {

            Log.d("onlow memory", "doSomethingMemoryIntensive: ");
        }
    }

    public void setSnackbar(String msg) {
        snackbar = Snackbar.make(this.findViewById(android.R.id.content), msg, Snackbar.LENGTH_INDEFINITE);
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(5);
        final Snackbar finalSnackbar = snackbar;
        snackbar.setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Connectivity.isConnected(MainActivity.this)) {
                    updateRealm(oldtag);
                    finalSnackbar.dismiss();
                } else {
                    setSnackbar("Could not update the data. Please connect to the internet and hit 'Retry'");
                    snackbar.show();
                }
            }
        });
    }


    // Get a MemoryInfo object for the device's current memory status.
    private ActivityManager.MemoryInfo getAvailableMemory() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo;
    }


    public void downloaddataStarted() {
        mBuilder.setContentTitle("Downloading")
                .setContentText("Content is being downloaded in background")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Content is being downloaded in background"))
                .setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false)
                .setAutoCancel(true);
        mNotificationManager.notify(0, mBuilder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            notificationManager.notify(0, mBuilder.build());
    }

    public void downloadProgress() {
        PROGRESS_CURRENT = (int) progress;
        mBuilder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
        mBuilder.setContentTitle("Please wait...");

        if (PROGRESS_CURRENT < 100)
            mBuilder.setContentText(PROGRESS_CURRENT + "% " + "downloaded").setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(PROGRESS_CURRENT + "% " + "downloaded"));
        else
            mBuilder.setContentText("100% downloaded").setStyle(new NotificationCompat.BigTextStyle()
                    .bigText("100% downloaded"));


        mNotificationManager.notify(0, mBuilder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            notificationManager.notify(0, mBuilder.build());
    }

    public void partialDownloaded() {
        mBuilder.setContentTitle("Partially downloaded")
                .setContentText("Please check your connection.")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Download cannot be completed.\nPlease check your connection."))
                .setProgress(PROGRESS_MAX, PROGRESS_MAX, false)
                .setAutoCancel(true);
        mNotificationManager.notify(0, mBuilder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            notificationManager.notify(0, mBuilder.build());
    }


    public void downloadCompleted() {
        mBuilder.setContentTitle("Download Complete")
                .setContentText("Download has been successfully completed.")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Download has been successfully completed."))
                .setProgress(0, 0, false)
                .setAutoCancel(true);

        mNotificationManager.notify(0, mBuilder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            notificationManager.notify(0, mBuilder.build());
//        LocalBroadcastManager.getInstance(this)
//                .unregisterReceiver(myReceiver);
    }

    public void downloadInterrupted() {
        mBuilder.setContentTitle("Error")
                .setContentText("Please check your connection.")
                .setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Error while downloading.\nPlease check your connection."))
                .setAutoCancel(true);

        mNotificationManager.notify(0, mBuilder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            notificationManager.notify(0, mBuilder.build());

//        LocalBroadcastManager.getInstance(this)
//                .unregisterReceiver(myReceiver);
    }

    public static class Download extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
    }

}
