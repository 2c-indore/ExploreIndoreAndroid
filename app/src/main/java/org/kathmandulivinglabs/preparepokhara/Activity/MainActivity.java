package org.kathmandulivinglabs.preparepokhara.Activity;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.EventLog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
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
import org.kathmandulivinglabs.preparepokhara.Adapter.ExpandableMenuAdapter;
import org.kathmandulivinglabs.preparepokhara.Adapter.ExpandedMenuModel;
import org.kathmandulivinglabs.preparepokhara.Adapter.FragmentAdapter;
import org.kathmandulivinglabs.preparepokhara.Api_helper.ApiHelper;
import org.kathmandulivinglabs.preparepokhara.Api_helper.ApiInterface;
import org.kathmandulivinglabs.preparepokhara.Api_helper.Tags;
import org.kathmandulivinglabs.preparepokhara.Api_helper.Wards;
import org.kathmandulivinglabs.preparepokhara.BuildConfig;
import org.kathmandulivinglabs.preparepokhara.Customclass.CustomViewPager;
import org.kathmandulivinglabs.preparepokhara.FilterParcel;
import org.kathmandulivinglabs.preparepokhara.Fragment.InsightFragment;
import org.kathmandulivinglabs.preparepokhara.Fragment.MapFragment;
import org.kathmandulivinglabs.preparepokhara.Helper.Connectivity;

import org.kathmandulivinglabs.preparepokhara.Interface.ToggleTabVisibilityListener;
import org.kathmandulivinglabs.preparepokhara.R;
import org.kathmandulivinglabs.preparepokhara.Realmstore.AttractionSchema;
import org.kathmandulivinglabs.preparepokhara.Realmstore.Bank;
import org.kathmandulivinglabs.preparepokhara.Realmstore.ExploreSchema;
import org.kathmandulivinglabs.preparepokhara.Realmstore.FilterSchema;
import org.kathmandulivinglabs.preparepokhara.Realmstore.PokharaBoundary;
import org.kathmandulivinglabs.preparepokhara.Realmstore.School;
import org.kathmandulivinglabs.preparepokhara.Realmstore.Tag;
import org.kathmandulivinglabs.preparepokhara.Realmstore.Ward;
import org.kathmandulivinglabs.preparepokhara.RetrofitPOJOs.Attractions;
import org.kathmandulivinglabs.preparepokhara.RetrofitPOJOs.Features;
import org.kathmandulivinglabs.preparepokhara.RetrofitPOJOs.Filter;
import org.kathmandulivinglabs.preparepokhara.View.ProgressDialogFragment;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;

import io.realm.RealmQuery;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, InsightFragment.onInsightSelected, ToggleTabVisibilityListener  {

    public interface OnTaskCompleted{
        void onTaskCompleted();
    }

    public static String def_type="hospital";
    public static String def_type_category = "Hospitals";
    public static boolean infoScreen = false;
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
    HashMap<ExpandedMenuModel,List<String>> listDataChild;
    Map<String,String> tagMp;
    Map<String,String> tagTouism;
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

    private static final String ACTION_CUSTOM_BROADCAST =
            BuildConfig.APPLICATION_ID + ".ACTION_CUSTOM_BROADCAST";


    public void setMyClassListener(Backlistner listener) {
        this.mBack = listener;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        expandableList = findViewById(R.id.expand_nav);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mNotificationManager = NotificationManagerCompat.from(this);
        mBuilder = new NotificationCompat.Builder(getApplicationContext(), "CHANNEL_ID")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Explore Pokhara")
                .setContentText("Pokhara Map for Offline Use")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        String channelId = "explore_download";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Downloading";
            String description = "Data is being downloaded in background";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if(navigationView!=null){
            setupDrawerContent(navigationView);
        }
        prepareListData();
        filter_param =new HashMap<>();
        oldtag = def_type;

        tagMp = new HashMap<>();
        //tagMp.put("attraction","Attractions");
        tagMp.put("school","Schools");
        tagMp.put("hindu","Hinduism");
        tagMp.put("police","Police Stations");
        tagMp.put("hospital","Hospitals");
        tagMp.put("clinic","Clinics");
        tagMp.put("health_post","Health Posts");
        tagMp.put("pharmacy","Pharmacies");
        tagMp.put("dentist","Dentists");
        tagMp.put("veterinarians","Veterinarians");
        tagMp.put("government","Government Offices");
        tagMp.put("ngo","NGOs");
        tagMp.put("bank","Banks");
        tagMp.put("fuel","Fuel Station");
        tagMp.put("radio","FM Stations");
        tagMp.put("television","TV Stations");
        tagMp.put("newspaper","Newspapers");
        tagMp.put("college","Colleges");
        tagMp.put("university","Universities");
        tagMp.put("kindergarten","Kindergartens");
        tagMp.put("buddhist","Buddhism");
        tagMp.put("christian","Christianity");
        tagMp.put("muslim","Islam");
        tagMp.put("atm","ATMs");
        tagMp.put("restaurant","Restaurants");
        tagMp.put("museum","Museums");
        tagMp.put("park","Parks");
        tagMp.put("storage_tank","Public Water Tanks");
        tagMp.put("water_tap","Public Taps");
        tagMp.put("water_well","Wells");
        //tagMp.put("gas","Gas");
        tagMp.put("cooperative","Co-operatives");
        tagMp.put("hotel","Hotels");
        tagMp.put("kirat","Kirat");
        tagMp.put("sikh","Sikhism");
        tagMp.put("judaism","Judaism");
        tagMp.put("other-religion","Others Religions");

        tagTouism = new HashMap<>();

        tagTouism.put("stupa","Buddhist Stupa/Monastery");
        tagTouism.put("caves","Caves");
        tagTouism.put("temples","Hindu Temples");
        tagTouism.put("viewtower","View Points/Towers");


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
                        if(filter_param!=null) filter_param.clear();
                        filter_param = new HashMap<>();
                        drawer.closeDrawers();
                        oldtag = entry.getKey();
                        makeMapData(entry.getKey());

                        Log.d(entry.getKey(), "child");
                    }
                }
                    if(childValue.equals("Update")){
                        drawer.closeDrawers();
                        updateRealm(oldtag);
                        if(oldtag.equals("attractions")) {
                            if (tabs.getTabAt(1)!=null) tabs.removeTabAt(1);
                            getSupportActionBar().setTitle(def_type_category);
                        }
                        else
                            getSupportActionBar().setTitle(oldtag);
                   //Remove and Save data to realm
                    }
                    else if(childValue.equals("Offline map")){
                        drawer.closeDrawers();
                        if(Connectivity.isConnected(Mapbox.getApplicationContext())) {
                            downloadStarted();
                            downloadBaseMap();
                        }
                        else Snackbar.make(MainActivity.this.findViewById(android.R.id.content), "No internet connection", Snackbar.LENGTH_LONG).show();
                        if(oldtag.equals("attractions")) {
                            if (tabs.getTabAt(1)!=null) tabs.removeTabAt(1);
                            getSupportActionBar().setTitle(def_type_category);
                        }
                        else
                            getSupportActionBar().setTitle(oldtag);
                    }
//                    else if(childValue.equals("")){
//                        drawer.closeDrawers();
//                        if(oldtag.equals("attractions")) {
//                            if (tabs.getTabAt(1)!=null) tabs.removeTabAt(1);
//                            getSupportActionBar().setTitle(def_type_category);
//                        }
//                        else
//                            getSupportActionBar().setTitle(oldtag);
//                        downloadalldata =true;
//                        downloadAll();
//                    }
                    else if(childValue.equals("About Us")){
                        drawer.closeDrawers();
                        Log.d("About Us","entry");
                        Intent intentabout = new Intent(getApplicationContext(), AboutActivity.class);
                        String[] abInfo = {def_type, def_type_category};
                        intentabout.putExtra("amenityType",abInfo);
                        startActivity(intentabout);

                    }
                    else if(listDataHeader.get(i).getIconName().equals("Places of Attractions")){
                        for (Map.Entry<String, String> entry : tagTouism.entrySet()) {
                            if (entry.getValue().equals(childValue)) {
                                if(filter_param!=null) filter_param.clear();
                                filter_param = new HashMap<>();
                                drawer.closeDrawers();
                                oldtag = "attractions";
                                makeMapData("attractions");
                                def_type_category = entry.getValue();
                                Log.d(entry.getKey(), "child");
                                tabs.removeTabAt(1);
                            }
                        }


//                    drawer.closeDrawers();
//                    oldtag = "attractions";
//                    makeMapData("attractions");
                    }

                return false;
            }
        });
        expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                String childValue = listDataHeader.get(i).getIconName();
//                if(childValue.equals("Update")){
//                   // updateRealm();
//                    //Remove and Save data to realm
//                }
//                else if(childValue.equals("Offline Map")){
//
//                    downloadStarted();
//                    downloadBaseMap();
//                }
//                else if(childValue.equals("About Us")){
//                    Log.d("About Us","entry");
//                    Intent intentabout = new Intent(getApplicationContext(), AboutActivity.class);
//                    startActivity(intentabout);
//
//                }
                return false;
            }
        });
        Realm realm = Realm.getDefaultInstance();
        //TODO
        final RealmResults<AttractionSchema> initial_data = realm.where(AttractionSchema.class).findAll();
        final RealmResults<Ward> ward = realm.where(Ward.class).findAll();
        final RealmResults<Tag> tag = realm.where(Tag.class).findAll();
        final RealmResults<ExploreSchema> explore_data = realm.where(ExploreSchema.class).findAll();
        List<String> amenity_tag = new ArrayList<>();
        if (tag.size() == 0)
            savetag();
        if (ward.size() == 0) {
            saveward();
        }

        if (explore_data.size() == 0) {
            showProgressDialog();
//            downloadAll();
            saveDataFromV2Api(def_type);
//            if(explore_data.size()==0)
//            for (Map.Entry<String,String> variables: tagMp.entrySet()
//                 ) {
//                saveDataFromV2Api(variables.getKey());
//            }
            //saveDataFromV2Api("school");
            //saveDataFromV2Api("bank");
        }
        viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(1);
        // Set Tabs inside Toolbar
        tabs = (TabLayout) findViewById(R.id.result_tabs);
        tabs.setupWithViewPager(viewPager);
        Intent  i = getIntent();
        String amenityedited;
        String[] fromabout;
        if(i!=null){
            amenityedited = i.getStringExtra("amenityedited");
            fromabout = i.getStringArrayExtra("about");
            if(fromabout==null) {
                for (Map.Entry<String, String> entry : tagMp.entrySet()) {
                    if (entry.getKey().equals(amenityedited)) {
                        amenityedited = entry.getValue();
                    }
                }
                if (amenityedited != null) {
                    getSupportActionBar().setTitle(amenityedited);
                } else {
                    getSupportActionBar().setTitle("Hospitals");
//                    tabs.removeTabAt(1);
                }
            }
            else {
                if(!fromabout[0].equals("attractions"))
                getSupportActionBar().setTitle(fromabout[0]);
                else{
                    getSupportActionBar().setTitle(fromabout[1]);
                    tabs.removeTabAt(1);
                }
            }

        }
        else  {
            getSupportActionBar().setTitle("Hospitals");
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
//                if (position == 1) {
//                    Log.d(String.valueOf(position), "onPageSelected: ");
//                    fragmentRefresh();
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        HomeFragment homeFragment = new HomeFragment();
//
//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.content_frame, homeFragment, "HOME")
//                .commit();
    }
    private void setupDrawerContent(NavigationView navigationView){
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
    private void downloadAll(){

        showProgressDialog();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                saveward();
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
        //dismissProgressDialog();
        //fragmentRefresh();
    }

    private void makeMapData(String selectedkey){
        Realm realm = Realm.getDefaultInstance();
        //TODO
        final RealmResults<ExploreSchema> explore = realm.where(ExploreSchema.class).contains("tag",selectedkey).findAll();
        final RealmResults<AttractionSchema> attract = realm.where(AttractionSchema.class).findAll();
        realm.close();
        def_type = selectedkey;

        if(selectedkey.equals("attractions") && attract.size()==0){
            showProgressDialog();
                saveDataFromV2Api(selectedkey);
        }
        else if(!selectedkey.equals("attractions") && explore.size()==0)
        {
            showProgressDialog();
            saveDataFromV2Api(selectedkey);
        }
        else {
            updateMapView = true;
            fragmentRefresh();
        }

    }
    private void updateRealm(String selectedkey){
        showProgressDialog();
        removedata(selectedkey);
        saveward();
        savetag();
        saveDataFromV2Api(selectedkey);
    }
    private void prepareListData() {
        listDataHeader = new ArrayList<ExpandedMenuModel>();
        listDataChild = new HashMap<ExpandedMenuModel, List<String>>();
        ExpandedMenuModel attraction = new ExpandedMenuModel();
        attraction.setIconName("Places of Attractions");
        listDataHeader.add(attraction);

        ExpandedMenuModel education = new ExpandedMenuModel();
        education.setIconName("Education");
        listDataHeader.add(education);

        ExpandedMenuModel health = new ExpandedMenuModel();
        health.setIconName("Health");
        listDataHeader.add(health);

        ExpandedMenuModel finance = new ExpandedMenuModel();
        finance.setIconName("Financial Institutions");
        listDataHeader.add(finance);

        ExpandedMenuModel governance = new ExpandedMenuModel();
        governance.setIconName("Governance");
        listDataHeader.add(governance);

        ExpandedMenuModel tourism = new ExpandedMenuModel();
        tourism.setIconName("Tourism");
        listDataHeader.add(tourism);

        ExpandedMenuModel water = new ExpandedMenuModel();
        water.setIconName("Water");
        listDataHeader.add(water);

        ExpandedMenuModel energy = new ExpandedMenuModel();
        energy.setIconName("Energy");
        listDataHeader.add(energy);

        ExpandedMenuModel communication = new ExpandedMenuModel();
        communication.setIconName("Communication");
        listDataHeader.add(communication);

        ExpandedMenuModel worship = new ExpandedMenuModel();
        worship.setIconName("Places of Worship");
        listDataHeader.add(worship);

        ExpandedMenuModel security = new ExpandedMenuModel();
        security.setIconName("Security");
        listDataHeader.add(security);

        ExpandedMenuModel download = new ExpandedMenuModel();
        download.setIconName("Download");
        listDataHeader.add(download);

//        ExpandedMenuModel update = new ExpandedMenuModel();
//        update.setIconName("Update");
//        update.setIconImg(R.drawable.ic_update_black_24dp);
//        listDataHeader.add(update);
//
//        ExpandedMenuModel offline_map = new ExpandedMenuModel();
//        offline_map.setIconName("Offline Map");
//        offline_map.setIconImg(R.drawable.download_begin);
//        listDataHeader.add(offline_map);
//
//        ExpandedMenuModel about = new ExpandedMenuModel();
//        about.setIconName("About Us");
//        about.setIconImg(R.drawable.ic_list);
//        listDataHeader.add(about);

        ExpandedMenuModel about = new ExpandedMenuModel();
        about.setIconName("About the Project");
        listDataHeader.add(about);

        // Adding child data
        List<String> attractionlist = new ArrayList<>();
        List<String> educationlist = new ArrayList<String>();
        List<String> healthlist = new ArrayList<String>();
        List<String> financelist = new ArrayList<String>();
        List<String> govlist = new ArrayList<String>();
        List<String> tourismlist = new ArrayList<String>();
        List<String> waterlist = new ArrayList<String>();
        List<String> energylist = new ArrayList<String>();
        List<String> communicationlist = new ArrayList<String>();
        List<String> worshiplist = new ArrayList<String>();
        List<String> securitylist = new ArrayList<String>();
        List<String> downloadlist = new ArrayList<String>();
        List<String> aboutlist = new ArrayList<String>();

        attractionlist.add("Buddhist Stupa/Monastery");
        attractionlist.add("Caves");
        attractionlist.add("Hindu Temples");
        attractionlist.add("View Points/Towers");

        educationlist.add("Kindergartens");
        educationlist.add("Schools");
        educationlist.add("Colleges");
        educationlist.add("Universities");

        healthlist.add("Hospitals");
        healthlist.add("Clinics");
        healthlist.add("Health Posts");
        healthlist.add("Pharmacies");
        healthlist.add("Dentists");

        financelist.add("Banks");
        financelist.add("Co-operatives");
        financelist.add("ATMs");

        govlist.add("Government Offices");
        govlist.add("NGOs");

        tourismlist.add("Hotels");
        tourismlist.add("Restaurants");
        tourismlist.add("Museums");

        waterlist.add("Public Water Tanks");
        waterlist.add("Public Taps");
        waterlist.add("Wells");

        energylist.add("Fuel Station");
        //energylist.add("Gas");

        communicationlist.add("FM Stations");
        communicationlist.add("TV Stations");
        communicationlist.add("Newspapers");

        worshiplist.add("Hinduism");
        worshiplist.add("Islam");
        worshiplist.add("Buddhism");
        worshiplist.add("Christianity");
        worshiplist.add("Kirat");
        worshiplist.add("Sikhism");
        worshiplist.add("Judaism");
        worshiplist.add("Other Religions");

        securitylist.add("Police Stations");

        downloadlist.add("Update");
       // downloadlist.add("Download map data");
        downloadlist.add("Offline map");

        aboutlist.add("About Us");

        listDataChild.put(listDataHeader.get(0),attractionlist);
        listDataChild.put(listDataHeader.get(1),educationlist);
        listDataChild.put(listDataHeader.get(2),healthlist);
        listDataChild.put(listDataHeader.get(3),financelist);
        listDataChild.put(listDataHeader.get(4),govlist);
        listDataChild.put(listDataHeader.get(5),tourismlist);
        listDataChild.put(listDataHeader.get(6),waterlist);
        listDataChild.put(listDataHeader.get(7),energylist);
        listDataChild.put(listDataHeader.get(8),communicationlist);
        listDataChild.put(listDataHeader.get(9),worshiplist);
        listDataChild.put(listDataHeader.get(10),securitylist);
        listDataChild.put(listDataHeader.get(11),downloadlist);
        listDataChild.put(listDataHeader.get(12),aboutlist);


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
//    private String tagFormat(String text){
//        char c = text.charAt(1);
//        int _index = text.indexOf("_");
//        text = Character.toUpperCase(c)+text.substring(2,_index)+text.substring(_index+1);
//        Log.d(text,"tagFormat: ");
//        return text;
//    }
//    private void addMenuItems(List<String> menuItem){
//        int i = 0;
//        for (String item:menuItem
//             ) {
//            navigationView.getMenu().add(R.id.nav_infrastructure,i,i,item);
//            i++;
//        }
//    }

    private void removedata(String amenity_type) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                final RealmResults<ExploreSchema> explore = realm.where(ExploreSchema.class).contains("tag",amenity_type).findAll();
                if(explore.size()!=0)
                explore.deleteAllFromRealm();
                if(amenity_type.equals("attractions")){
                    realm.delete(AttractionSchema.class);

                }
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
                realm.delete(Ward.class);
                realm.delete(Tag.class);
                realm.delete(FilterSchema.class);
            }
        });
        realm.close();

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

    private void saveDataFromV2Api(final String def_type) {
        ApiInterface apiInterface = new ApiHelper().getApiInterface();
        if(def_type.equals("attractions")) {
            saveAttraction();
        }
        else {
            Call<Features> call = apiInterface.getFeature(def_type);
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
                                    org.kathmandulivinglabs.preparepokhara.RetrofitPOJOs.Tags tg = feature.getProperties().getTags();
                                    ExploreSchema realmObject = realm.createObject(ExploreSchema.class);
                                    realmObject.setTag(def_type);
                                    realmObject.setAmenity(tg.getAmenity());
                                    realmObject.setOsm_id(feature.getId());
                                    realmObject.setWardid(feature.getWardId());
                                    realmObject.setId((long) feature.getProperties().getId());
                                    realmObject.setName(tg.getName());
                                    realmObject.setName_ne(tg.getNameNe());

                                /*TODO add values to Tags POJO
                                realmObject.setName_ne(tg.getNameNe());
                                realmObject.setName_en(tg.getNameEn());
                                realmObject.setMail(tg.getEmail());
                                realmObject.setWeb(tg.getWebsite());
                                */
                                    realmObject.setContact_phone(tg.getPhone());
                                    realmObject.setContact_email(tg.getEmail());
                                    realmObject.setWeb(tg.getWebsite());

                                    if (tg.getCapacityBeds() != null && tg.getCapacityBeds().matches("-?\\d+"))
                                        realmObject.setCapacity_beds(Integer.parseInt(tg.getCapacityBeds()));
                                    else realmObject.setCapacity_beds(null);

                                    if (tg.getPersonnelCount() != null && tg.getPersonnelCount().matches("-?\\d+"))
                                        realmObject.setPersonnel_count(Integer.parseInt(tg.getPersonnelCount()));
                                    else realmObject.setPersonnel_count(null);

                                    if (tg.getStudentCount() != null && tg.getStudentCount().matches("-?\\d+"))
                                        realmObject.setStudent_count(Integer.parseInt(tg.getStudentCount()));
                                    else realmObject.setStudent_count(null);

                                    if (tg.getFrequency() != null && tg.getFrequency().matches("[+-]?([0-9]*[.])?[0-9]+"))
                                        realmObject.setFrequency(Double.parseDouble(tg.getFrequency()));
                                    else realmObject.setFrequency(null);


                                    realmObject.setOpening_hours(tg.getOpeningHours());
                                    realmObject.setFacility_xray(tg.getFacilityXRay());
                                    realmObject.setFacility_icu(tg.getFacilityIcu());
                                    realmObject.setFacility_nicu(tg.getFacilityNicu());
                                    realmObject.setFacility_ventilator(tg.getFacilityVentilator());
                                    realmObject.setFacility_ambulance(tg.getFacilityAmbulance());
                                    realmObject.setFacility_operating_theatre(tg.getFacilityOperatingTheater());
                                    realmObject.setEmergency(tg.getEmergency());
                                    realmObject.setEmergency_services(tg.getEmergencyServices());
                                    realmObject.setHealthcare_speciality(tg.getHealthcare_speciality());
                                    realmObject.setNote(tg.getNote());
                                    realmObject.setAtm(tg.getAtm());
                                    realmObject.setNrb_class(tg.getNrbClass());
                                    realmObject.setOperator_type(tg.getOperatorType());
                                    realmObject.setDrinking_water(tg.getFacility_drinking_water());
                                    realmObject.setToilet(tg.getFacility_toilet());
                                    realmObject.setNetwork(tg.getNetwork());



                                    if (tg.getBeds() != null && tg.getBeds().matches("-?\\d+"))
                                        realmObject.setBeds(Integer.parseInt(tg.getBeds()));
                                    else realmObject.setBeds(null);
                                    if (tg.getStars() != null && tg.getStars().matches("-?\\d+"))
                                        realmObject.setStars(Integer.parseInt(tg.getStars()));
                                    else realmObject.setStars(null);
                                    if (tg.getRooms() != null && tg.getRooms().matches("-?\\d+"))
                                        realmObject.setRooms(Integer.parseInt(tg.getRooms()));
                                    else realmObject.setRooms(null);



                                    //TODO Add null checks
                                    //realmObject.setPersonnel_count(Integer.valueOf(tg.getPersonnelCount()));
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


                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                realm.commitTransaction();
                                realm.close();
                                updateMapView = true;
                                if(!downloadalldata)
                                fragmentRefresh();
                                Log.d("realm closed", "map data ");
                            }
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Server is not responding? Please try again later", Toast.LENGTH_LONG).show();
                        updateMapView = true;
                        if(!downloadalldata)
                        fragmentRefresh();
                    }
                    if(!downloadalldata)
                    dismissProgressDialog();
                }

                @Override
                public void onFailure(Call<Features> call, Throwable t) {
                    t.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "Are you connected to internet? If not, connect and update the data", Toast.LENGTH_LONG).show();
//                    updateMapView = true;
                    if(!downloadalldata) {
                        setSnackbar("Could not update data. Please connect to the internet and hit 'Retry'");
                        snackbar.show();
                        dismissProgressDialog();
                        fragmentRefresh();
                    }

                }
            });
        }

    }
    private void saveAttraction(){
        ApiInterface apiInterface = new ApiHelper().getApiInterface();
        Call<Attractions> call = apiInterface.getAttraction();
      //  showProgressDialog();
        call.enqueue(new Callback<Attractions>() {
            @Override
            public void onResponse(Call<Attractions> call, Response<Attractions> response) {
                if (response.body() != null) {
                    if (response.body().getSuccess() == 1) {
//                        Attractions attractions = response.body();
//                        Realm.init(MainActivity.this);
//                        RealmConfiguration config = new RealmConfiguration.Builder()
//                                .name("expl.realm")
//                                .schemaVersion(1)
//                                .deleteRealmIfMigrationNeeded()
//                                .build();
//                        Realm.setDefaultConfiguration(config);
//
//
//                        // add response to realm database
//                        Realm realma = Realm.getInstance(config);
//                        realma.beginTransaction();
//                        realma.copyToRealmOrUpdate();
//                        realma.commitTransaction();
//                        realma.close();





                        Realm realm = Realm.getDefaultInstance();
                        try {
                            realm.beginTransaction();
                            List<Attractions.Attraction> attractionarray = response.body().getAttractions();
                            for (Attractions.Attraction attractionlist:attractionarray
                                 ) {
                                for (Attractions.Attraction.Pois.Feature attractionFeature:attractionlist.getPois().getFeatures()) {
                                    AttractionSchema atrrObject=realm.createObject(AttractionSchema.class);
                                    atrrObject.setCategory(attractionlist.getCategory());
                                    atrrObject.setOsm_id(attractionFeature.getId());
                                    Attractions.Attraction.Pois.Feature.Properties.Tags attractionTags = attractionFeature.getProperties().getTags();
                                    Attractions.Attraction.Pois.Feature.Detail attractionDetail=attractionFeature.getDetail();
                                    Attractions.Attraction.Pois.Feature.Geometry attractionGeometry=attractionFeature.getGeometry();
                                    atrrObject.setAmenity(attractionTags.getAmenity());
                                    atrrObject.setName(attractionTags.getName());
                                    atrrObject.setName_ne(attractionTags.getNameNe());
                                    atrrObject.setReligion(attractionTags.getReligion());
                                    atrrObject.setToilet(attractionTags.getToilets());
                                    atrrObject.setDrinking_water(attractionTags.getDrinkingWater());
                                    atrrObject.setWater(attractionTags.getWater());
                                    atrrObject.setNatural(attractionTags.getNatural());
                                    atrrObject.setAccess(attractionTags.getAccess());
                                    atrrObject.setFee(attractionTags.getFee());
                                    atrrObject.setBarrier(attractionTags.getBarrier());
                                    atrrObject.setHistoric(attractionTags.getHistoric());
                                    atrrObject.setContent((String) attractionDetail.getContent());
                                    atrrObject.setPhoto((String) attractionDetail.getPhoto());

                                    atrrObject.setCoordinateslat(attractionGeometry.getCoordinates().get(0));
                                    atrrObject.setCoordinateslong(attractionGeometry.getCoordinates().get(1));
                                    atrrObject.setType(attractionGeometry.getType());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            realm.commitTransaction();
                            realm.close();
                            updateMapView = true;
                            if(!downloadalldata)
                            fragmentRefresh();
                            Log.d("realm closed", "map data ");
                        }
                    }
                }
                else {
                    if(!downloadalldata) {
                        Toast.makeText(getApplicationContext(), "Server is not responding? Please try again later", Toast.LENGTH_LONG).show();
                        updateMapView = true;
                        fragmentRefresh();
                    }
                }
                if (tabs.getTabAt(1)!=null) tabs.removeTabAt(1);
                if(!downloadalldata) dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<Attractions> call, Throwable t) {
                t.printStackTrace();
//                Toast.makeText(getApplicationContext(), "Are you connected to active internet connection? If not, connect and update the data", Toast.LENGTH_LONG).show();
//                updateMapView = true;
//
                if(!downloadalldata) {
                    setSnackbar("Could not update the data. Please connect to the internet and hit 'Retry'");
                    snackbar.show();
                    dismissProgressDialog();
                    fragmentRefresh();
                }
//                if (tabs.getTabAt(1)!=null) tabs.removeTabAt(1);
            }
        });
    }
    private void saveward() {
        ApiInterface apiInterface = new ApiHelper().getApiInterface();
            Call<Features> call = apiInterface.getFeature("hospital");
            //  showProgressDialog();
            call.enqueue(new Callback<Features>() {
                @Override
                public void onResponse(Call<Features> call, Response<Features> response) {
                    if (response.body() != null) {
                        if (response.body().getSuccess() == 1) {

                           Wards.Boundary bound = response.body().getGeometries().getBoundary();
//                            Wards.BoundaryWithWards ward_bound ward_bound= response.body().getGeometries().getBoundaryWithWards();
                            List <Wards.BoundaryWithWards.Feature> ward_bounds = response.body().getGeometries().getBoundaryWithWards().getFeatures();
                            Realm realm = Realm.getDefaultInstance();
                            try {
                                realm.beginTransaction();
                                for (List<List<Double>> bound_prop :bound.getGeometry().getCoordinates()){
                                    for (List<Double> bound_coord : bound_prop
                                            ) {
                                        PokharaBoundary pb = realm.createObject(PokharaBoundary.class);
                                        pb.setTag("all_boundary");
                                        pb.setCoordinateslong(bound_coord.get(0));
                                        pb.setCoordinateslat(bound_coord.get(1));
                                    }
                                }
                            for (Wards.BoundaryWithWards.Feature ward_prop: ward_bounds
                                 ) {
                                Ward ward = realm.createObject(Ward.class);
                                String wardname = ward_prop.getProperties().getName();
                                String dbname = (wardname.split("Pokhara Lekhnath Metropolitan Ward No.")[1]).trim();
                                int wardno = Integer.parseInt(dbname);
                                dbname = "Ward No. "+ dbname;
                                ward.setName(dbname);
                                ward.setNumber(wardno);
                                ward.setOsmID(ward_prop.getId());
                                ward.setName_ne(ward_prop.getProperties().getNameNe());
                                ward.setCoordinateslat(ward_prop.getCentroid().getCoordinates().get(1));
                                ward.setCoordinateslong(ward_prop.getCentroid().getCoordinates().get(0));
                                Wards.BoundaryWithWards.Feature.Geometry_ geom = ward_prop.getGeometry();
                                RealmList<PokharaBoundary> pbound;
                                pbound = new RealmList<>();

                                for (List<List<Double>> sds:geom.getCoordinates()
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

                                //Log.wtf(ward_prop.getId(),ward_prop.getProperties().getName());
                            }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                realm.commitTransaction();
                                realm.close();
                                if(!downloadalldata)
                                fragmentRefresh();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<Features> call, Throwable t) {
                    t.printStackTrace();
                }
            });
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
                    if (response.body().getSuccess() == 1) {
                        Realm realm = Realm.getDefaultInstance();
                        List<Tags.Tag> amenityTag = response.body().getTags();
                        realm.beginTransaction();
                        try {
                            for (Tags.Tag ame : amenityTag
                                    ) {
                                String amenity_name = ame.getAmenity();
                                List<Tags.Tag.Tag_> amenity_tag = ame.getTags();
                                RealmList<String> osm_tag = new RealmList<>();
                                RealmList<String> osm_label = new RealmList<>();
                                RealmList<String> db_key = new RealmList<>();
                                for (Tags.Tag.Tag_ tags : amenity_tag
                                        ) {
                                    osm_tag.add(tags.getTag());
                                    osm_label.add(tags.getLabel());
                                    db_key.add(tags.getDatabase_schema_key());
                                }
                                Tag tagData = realm.createObject(Tag.class);
                                tagData.setAmenity(amenity_name);
                                tagData.setOsmtags(osm_tag);
                                tagData.setTagslabel(osm_label);
                                tagData.setTagkey(db_key);
                            }
                        } finally {
                            realm.commitTransaction();
                            realm.close();
                            if(!downloadalldata)
                            fragmentRefresh();
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
//        if (filter_applied || navClicked) {
//            viewPager.getAdapter().notifyDataSetChanged();
//        }
//        if(navClicked){
//            mpfrag.doSomething(def_type);
//        }

    if(viewPager.getAdapter()!=null)
        viewPager.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        // super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            String tagmap = "android:switcher:" + R.id.viewpager + ":" + 0;
            MapFragment mpfrag = (MapFragment) getSupportFragmentManager().findFragmentByTag(tagmap);

            if (mpfrag == null || !((Backlistner) mpfrag).onBackPressed()) {
                super.onBackPressed();
            }
        }
//        else {
//
//        }
//        if(!infoScreen) {
//            if (def_type.equals("attractions") && !def_type_category.equals("Buddhist Stupa/Monastery")) {
//                getSupportActionBar().setTitle("Buddhist Stupa/Monastery");
//
//                //  navigationView.setCheckedItem(R.id.nav_hospital);
//                def_type = "attractions";
//                def_type_category = "Buddhist Stupa/Monastery";
//
//                fragmentRefresh();
//                //super.onBackPressed();
//            } else if (!def_type.equals("attractions")) {
//                getSupportActionBar().setTitle("Buddhist Stupa/Monastery");
//                def_type_category = "Buddhist Stupa/Monastery";
//
//                //  navigationView.setCheckedItem(R.id.nav_hospital);
//                def_type = "attractions";
//                fragmentRefresh();
//                if (tabs.getTabAt(1) != null) tabs.removeTabAt(1);
//            } else goBack();
//        }
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
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
       // Realm realm = Realm.getDefaultInstance();
//        if (id == R.id.nav_hospital) {
//            def_type = "hospital";
//            getSupportActionBar().setTitle("Hospitals");
//            //TODO
//            final RealmResults<ExploreSchema> hospital = realm.where(ExploreSchema.class).findAll();
//            if (hospital.size() == 0) {
//                saveDataFromV2Api(def_type);
//            }
//            updateMapView = true;
//            fragmentRefresh();
//            // Handle the camera action
//        } else if (id == R.id.nav_school) {
//            def_type = "school";
//            getSupportActionBar().setTitle("Schools");
//            final RealmResults<School> school = realm.where(School.class).findAll();
//            if (school.size() == 0) {
//                saveDataFromV2Api(def_type);
//            }
//            updateMapView = true;
//            fragmentRefresh();
//        } else if (id == R.id.nav_bank) {
//            def_type = "bank";
//            getSupportActionBar().setTitle("Banks");
//            final RealmResults<Bank> bank = realm.where(Bank.class).findAll();
//            if (bank.size() == 0) {
//                saveDataFromV2Api(def_type);
//            }
//            updateMapView = true;
//            fragmentRefresh();
        //}
//         if (id == R.id.update) {
//            removedata();
//            saveDataFromV2Api(def_type);
//            saveward();
//            savetag();
//        } else if (id == R.id.nav_about) {
//            Intent i = new Intent(this, AboutActivity.class);
//            startActivity(i);
//            finish();
//        } else if (id == R.id.download_map) {
//            downloadStarted();
//            downloadBaseMap();
//        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onInsight(Boolean vals) {
        String tag = "android:switcher:" + R.id.viewpager + ":" + 0;
        filter_applied = vals;
//        MapFragment mpfrag = (MapFragment)getSupportFragmentManager().findFragmentByTag(tag);
        //  mpfrag.setFilter(true);
        Log.d(String.valueOf(vals), "onInsight: ");
        MapFragment mpfrag = (MapFragment) getSupportFragmentManager().findFragmentByTag(tag);
        Intent i = getIntent();
        FilterParcel object = i.getParcelableExtra("FilterValue");
        //if (object != null) {
           // mpfrag.setFilter(object);
//            Bundle bd = new Bundle();
//            bd.putParcelable("FilterValue", object);
//            bd.putString("selectedType", def_type);
//            Log.d(def_type, "onResume: ");
//            mpfrag.setArguments(bd);
//            viewPager.getAdapter().notifyDataSetChanged();

        //}
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
//            Log.d(String.valueOf(object.getEmergency()), "em");
//            MapFragment mpfrag = new MapFragment();
            String tagmap = "android:switcher:" + R.id.viewpager + ":" + 0;
            MapFragment mpfrag = (MapFragment) getSupportFragmentManager().findFragmentByTag(tagmap);
            Bundle bd = new Bundle();
            bd.putParcelable("FilterValue", object);
            bd.putString("selectedType", def_type);
            Log.d(def_type, "onResume: ");
            if (mpfrag != null) {
                mpfrag.setArguments(bd);
                if(viewPager.getAdapter()!=null)
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
        if (mapsDownloading == false) {
            Log.d("maps...", "downloadBaseMap: ");
            OfflineManager offlineManager = OfflineManager.getInstance(getApplicationContext());

            // Create a bounding box for the offline region
            LatLngBounds latLngBounds = new LatLngBounds.Builder()
                    .include(new LatLng(28.31285, 84.14949)) // Northeast
                    .include(new LatLng(28.11532, 83.84905)) // Southwest
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
                jsonObject.put("Region Name", "Pokhara Basemap");
                String json = jsonObject.toString();
                metadata = json.getBytes("UTF-8");
            } catch (Exception exception) {
                exception.printStackTrace();
                metadata = null;
            }


            mNotificationManager.notify(1, mBuilder.build());
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

                                    // Calculate the download percentage
                                    double percentage = status.getRequiredResourceCount() >= 0
                                            ? (100.0 * status.getCompletedResourceCount() / status.getRequiredResourceCount()) :
                                            0.0;
                                    mBuilder.setProgress(100, (int) percentage, false);


                                    if (status.isComplete()) {
                                        // Download complete
                                        Log.d("Basemap Download", "Region downloaded successfully.");
                                        mBuilder.setContentTitle("Download Successful")
                                                .setContentText("Offline Map has been succesfully downloaded")
                                                .setProgress(0, 0, false)
                                                .setAutoCancel(true);

                                    } else if (status.isRequiredResourceCountPrecise()) {
                                        Log.d("Basemap Download", String.valueOf(percentage));
                                    }
                                    mNotificationManager.notify(1, mBuilder.build());
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                        notificationManager.notify(1,mBuilder.build());

                                }

                                @Override
                                public void onError(OfflineRegionError error) {
                                    // If an error occurs, print to logcat
                                    //Log.e(TAG, "onError reason: " + error.getReason());
                                    //Log.e(TAG, "onError message: " + error.getMessage());
                                }

                                @Override
                                public void mapboxTileCountLimitExceeded(long limit) {
                                    // Notify if offline region exceeds maximum tile count
                                    //Log.e(TAG, "Mapbox tile count limit exceeded: " + limit);
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
        Toast.makeText(getApplicationContext(),"Map is downloading",Toast.LENGTH_SHORT).show();
        mNotificationManager.notify(1, mBuilder.build());
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

            Log.d("onlow memory","doSomethingMemoryIntensive: ");
        }
    }
    public void setSnackbar(String msg) {
        snackbar = Snackbar.make(this.findViewById(android.R.id.content), msg, Snackbar.LENGTH_INDEFINITE);
        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(5);
        final Snackbar finalSnackbar = snackbar;
        snackbar.setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Connectivity.isConnected(MainActivity.this)) {
                    updateRealm(oldtag);
                    finalSnackbar.dismiss();
                }
                else {
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


    public void downloaddataStarted(){
        mBuilder.setContentTitle("Downloading")
                .setContentText("Content is being downloaded in background")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Content is being downloaded in background"))
                .setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false)
                .setAutoCancel(true);
        mNotificationManager.notify(0, mBuilder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            notificationManager.notify(0,mBuilder.build());
    }

    public void downloadProgress(){
        PROGRESS_CURRENT = (int) progress;
        mBuilder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
        mBuilder.setContentTitle("Please wait...");

            if (PROGRESS_CURRENT < 100)
                mBuilder.setContentText(PROGRESS_CURRENT + "% " + "downloaded").setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(PROGRESS_CURRENT + "% " + "downloaded"));
            else mBuilder.setContentText("100% downloaded").setStyle(new NotificationCompat.BigTextStyle()
                    .bigText("100% downloaded"));



        mNotificationManager.notify(0, mBuilder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            notificationManager.notify(0,mBuilder.build());
    }
    public void partialDownloaded(){
        mBuilder.setContentTitle("Partially downloaded")
                .setContentText("Please check your connection.")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Download cannot be completed.\nPlease check your connection."))
                .setProgress(PROGRESS_MAX, PROGRESS_MAX, false)
                .setAutoCancel(true);
        mNotificationManager.notify(0, mBuilder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            notificationManager.notify(0,mBuilder.build());
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
            notificationManager.notify(0,mBuilder.build());
//        LocalBroadcastManager.getInstance(this)
//                .unregisterReceiver(myReceiver);
    }

    public void downloadInterrupted(){
        mBuilder.setContentTitle("Error")
                .setContentText("Please check your connection.")
                .setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Error while downloading.\nPlease check your connection."))
                .setAutoCancel(true);

        mNotificationManager.notify(0, mBuilder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            notificationManager.notify(0,mBuilder.build());

//        LocalBroadcastManager.getInstance(this)
//                .unregisterReceiver(myReceiver);
    }
    public static class Download extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
    }

}
