package org.kathmandulivinglabs.exploreindore.Fragment;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.EventLog;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.Polygon;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.cluster.clustering.Cluster;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.v5.navigation.NavigationUnitType;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.mapbox.services.android.telemetry.location.LostLocationEngine;
import com.mapbox.services.android.telemetry.permissions.PermissionsListener;
import com.mapbox.services.android.telemetry.permissions.PermissionsManager;

import org.kathmandulivinglabs.exploreindore.Activity.MainActivity;
import org.kathmandulivinglabs.exploreindore.Customclass.CustomClusterItem;
import org.kathmandulivinglabs.exploreindore.Customclass.CustomClusterManagerPlugin;
import org.kathmandulivinglabs.exploreindore.FilterParcel;
import org.kathmandulivinglabs.exploreindore.Activity.Edit.EditDialogActivity;
import org.kathmandulivinglabs.exploreindore.Helper.Utils;

import org.kathmandulivinglabs.exploreindore.Interface.ToggleTabVisibilityListener;
import org.kathmandulivinglabs.exploreindore.R;
import org.kathmandulivinglabs.exploreindore.Realmstore.ExploreSchema;
import org.kathmandulivinglabs.exploreindore.Realmstore.PokharaBoundary;
import org.kathmandulivinglabs.exploreindore.Realmstore.Tag;
import org.kathmandulivinglabs.exploreindore.Realmstore.Ward;

import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


import io.realm.RealmCollection;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static android.content.Context.LOCATION_SERVICE;

public class MapFragment extends Fragment implements PermissionsListener, LocationEngineListener, MainActivity.Backlistner {
    private MapView mapView;
    private CustomClusterManagerPlugin<MyItem> clusterManagerPlugin;
    private MapboxMap mapboxMap;
    private LinearLayout lm;
    private LinearLayout small_info;
    private TextView testText, detailNepaliTitle, detailEnglishTitle, detailPhone, detailWeb, detailMail;
    private ViewGroup mapScreen;
    private LinearLayout detail_screen;
    private View amenityInfo, touristInfo;
    private LinearLayout containera, attraction_tags_container;
    private SwipeRefreshLayout attraction_swipe;
    int screenHeight = 0;
    private GestureDetectorCompat mDetector;
    Animation slideUpAnimation, slideDownAnimation;
    private static final int SWIPE_MIN_DISTANCE = 80;
    private static final int SWIPE_MAX_OFF_PATH = 60;
    private static final int SWIPE_THRESHOLD_VELOCITY = 100;
    private int swipeValue = 0;
    private static Map<String, Boolean> filter = new HashMap<>();
    private static Boolean atm;
    private static String wardId_hos, wardId_sch, wardId_bank, operator;
    private static Integer bedMin, bedMaxx, studentMin, studentMaxx;
    FilterParcel insightfilter;
    private static String selectedType = MainActivity.def_type;
    private com.mapbox.mapboxsdk.annotations.Icon hospital_icon, bank_icon, school_icon;
    private com.mapbox.mapboxsdk.annotations.Icon hospital_selected, bank_selected, school_selected;
    TextView phone, email, website;
    Marker previous_selected;

    private ToggleTabVisibilityListener toggleTabVisibilityListener;


    //Location_plugin_variable
    private PermissionsManager permissionsManager;
    private LocationLayerPlugin locationPlugin;
    private LocationEngine locationEngine;
    private Location originLocation;
    private Marker destinationMarker;
    private LatLng originCoord;
    private LatLng destinationCoord;
    private Point originPosition;
    private Point destinationPosition;
    private DirectionsRoute currentRoute;
    private static final String TAG = "DirectionsActivity";
    private NavigationMapRoute navigationMapRoute;
    private Button edit_btn;
    private ImageButton gps, zoomtoextant, closebtn, expandinfo, attraction_close;

    private boolean iff_ondown = false, iff_onswipe = false;

    private Button navButton;
    NestedScrollView scroll;
    private String editName, editLat, editLong;
    private Map<String, com.mapbox.mapboxsdk.annotations.Icon> tagMp_blue;
    private Map<String, com.mapbox.mapboxsdk.annotations.Icon> tagMp_orange;
    ListView listView;
    private Map<LatLng, String> uniList;
    SearchView searchView;
    LinearLayout.LayoutParams llp;

    @Override
    public boolean onBackPressed() {
        Log.wtf("back", "pressed");
        final boolean[] b = {true};
        if ((detail_screen != null && detail_screen.getChildCount() > 0) || (lm.getVisibility() == View.VISIBLE)) {
            swipeValue = 0;
            lm.setLayoutParams(llp);
            small_info.setVisibility(View.VISIBLE);
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
            toggleTabVisibilityListener.showTabs();
            lm.setVisibility(View.GONE);
            if (navigationMapRoute != null) navigationMapRoute.removeRoute();
            detail_screen.removeAllViews();
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    getActivity());
            alertDialog.setTitle("");
            alertDialog.setMessage("Are you sure you want to exit?");

            alertDialog.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Objects.requireNonNull(getActivity()).finish();
                            getActivity().finishAffinity();
                            System.exit(0);
                            b[0] = false;
                        }
                    });

            alertDialog.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            b[0] = true;
                        }
                    });

            alertDialog.show();
        }
        return b[0];
    }


    public static class Search {
        LatLng cord;
        String name;

        public Search(LatLng cord, String name) {
            this.cord = cord;
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }

    }

    ArrayAdapter<Search> adapter;
    ArrayList<Search> searches;
    boolean filterflag = false;
    private TextView attraction_title, attraction_title_np, attraction_detail;
    private ImageView attraction_image;
    private boolean detailbool;
    private int detail_height;
    private List<LatLng> polygon;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem mSearchMenuItem = menu.findItem(R.id.search);
//        mSearchMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                listView.setVisibility(View.VISIBLE);
//                return false;
//            }
//        });
        searchView = (SearchView) mSearchMenuItem.getActionView();
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setVisibility(View.VISIBLE);
            }
        });
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) listView.setVisibility(View.VISIBLE);
                if (!hasFocus) listView.setVisibility(View.GONE);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                //clickmarker();
                listView.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                newText = newText.toLowerCase();
                ArrayList<Search> search = new ArrayList<>();
                int flag = 0;
                // List<String> searchString = new ArrayList<>();
                if (uniList.size() > 0) {
                    for (Map.Entry<LatLng, String> aitem : uniList.entrySet()
                    ) {
                        if (aitem.getValue().toLowerCase().contains(newText)) {
                            Search hs = new Search(null, null);
                            hs.cord = aitem.getKey();
                            hs.name = aitem.getValue();
                            search.add(hs);
                        }
                    }
                    if (search.isEmpty()) {
                        Search hs = new Search(null, null);
                        hs.cord = null;
                        hs.name = "Could not find your search. Check if you have the correct Spelling.";
                        search.add(hs);
                    }
                    searches.clear();
                    searches.addAll(search);
                    adapter.notifyDataSetChanged();
                }
                return false;
            }
        });

    }

    private void clickmarker(LatLng markerCord, String markerName) {
        if (markerCord != null) {
            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerCord, 17), 400);
            //markerclickAction(clickedmarker);
            listView.setVisibility(View.GONE);
            View view = this.getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
//           searchView.setIconified(true);
            searchView.clearFocus();
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map_fragment, container, false);
        mapScreen = v.findViewById(R.id.mapScreen);

        ViewTreeObserver vto = mapScreen.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                screenHeight = mapScreen.getHeight();
                mapScreen.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        detail_screen = v.findViewById(R.id.detailView);
        ImageButton close_btn = v.findViewById(R.id.close_btn);
        gps = v.findViewById(R.id.gps_btn);
        zoomtoextant = v.findViewById(R.id.zoom_fix);
        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d(String.valueOf(gps.getTag()),"Gps Button Clicked");
                if (gps.getTag().equals("gps_off")) {
                    gps_checker();
                } else if (gps.getTag().equals("gps_searching")) {
                    //  Log.d("gps_searching","gps_searching");
                    if (locationEngine != null) {
                        gps_checker();
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        locationEngine.requestLocationUpdates();
                    }
                    if (locationPlugin != null) {
                        locationPlugin.onStart();
                    }
                } else if (gps.getTag().equals("gps_fixed")) {
                    if (locationEngine != null) {
                        locationEngine.requestLocationUpdates();
                    }
                    if (locationPlugin != null) {
                        locationPlugin.onStart();
                    }
                }
            }
        });
        zoomtoextant.setEnabled(false);
        zoomtoextant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(28.2380, 83.9956), 9.3), 500);
            }
        });
        LayoutInflater inflate_info = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Mapbox.getInstance(getContext(), "pk.eyJ1IjoiYmhhd2FrIiwiYSI6ImNpeHNrOHp4ODAwMDYzMW52cDM1a2xyd3MifQ.iQQzvoiIaVKbg8RRkvhvTA");
        mapView = v.findViewById(R.id.mapView);
        lm = v.findViewById(R.id.container);

        final LinearLayout.LayoutParams lp_shrink = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 2f);
        llp = lp_shrink;
        final LinearLayout.LayoutParams lp_expand = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 18f);
        final LinearLayout.LayoutParams lp_diminish = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0f);
        detail_height = lp_expand.height;
        mDetector = new GestureDetectorCompat(getContext(), new MyGestureListener());
        slideUpAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_up);
        slideDownAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_down);
        small_info = v.findViewById(R.id.small_info);
        testText = v.findViewById(R.id.hos_name);
        expandinfo = v.findViewById(R.id.detail_btn);
        insightfilter = new FilterParcel();
        IconFactory mIconFactory = IconFactory.getInstance(getActivity());
        tagMp_blue = new HashMap<>();
        tagMp_blue.put("school", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.school)));
        tagMp_blue.put("hindu", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.hindu)));
        tagMp_blue.put("Hindu Temples", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.hindu)));
        tagMp_blue.put("View Points/Towers", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.view_point)));


        tagMp_blue.put("police", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.police)));
        tagMp_blue.put("hospital", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.hospital)));
        tagMp_blue.put("clinic", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.clinic)));
        tagMp_blue.put("health_post", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.health_post)));
        tagMp_blue.put("pharmacy", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.pharmacy)));
        tagMp_blue.put("dentist", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.dentist)));
        tagMp_blue.put("veterinarians", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.veterinary)));
        tagMp_blue.put("government", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.government)));
        tagMp_blue.put("ngo", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.ngo)));
        tagMp_blue.put("bank", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.bank)));
        tagMp_blue.put("fuel", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.fuel)));
        tagMp_blue.put("radio", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.radio)));
        tagMp_blue.put("television", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.tv)));
        tagMp_blue.put("newspaper", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.newspaper)));
        tagMp_blue.put("college", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.university)));
        tagMp_blue.put("university", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.university)));
        tagMp_blue.put("kindergarten", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.kindergarten)));
        tagMp_blue.put("buddhist", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.buddhist)));

        tagMp_blue.put("Buddhist Stupa/Monastery", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.buddhist)));


        tagMp_blue.put("christian", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.christian)));
        tagMp_blue.put("muslim", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.muslim)));
        tagMp_blue.put("atm", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.atm)));
        tagMp_blue.put("restaurant", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.resturant)));
        tagMp_blue.put("museum", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.museum)));
        tagMp_blue.put("park", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.museum)));
        tagMp_blue.put("storage_tank", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.storage_tank)));
        tagMp_blue.put("water_tap", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.public_tap)));
        tagMp_blue.put("water_well", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.water_well)));
        tagMp_blue.put("gas", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.map_marker_dark)));
        tagMp_blue.put("cooperative", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.bank)));
        tagMp_blue.put("hotel", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.hotel)));
        tagMp_blue.put("kirat", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.map_marker_dark)));
        tagMp_blue.put("sikh", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.map_marker_dark)));
        tagMp_blue.put("judaism", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.map_marker_dark)));
        tagMp_blue.put("other-religion", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.map_marker_dark)));


        tagMp_orange = new HashMap<>();
        tagMp_orange.put("Hindu Temples", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.orange_hindu)));
        tagMp_orange.put("View Points/Towers", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.orange_view_point)));
        tagMp_orange.put("Buddhist Stupa/Monastery", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.orange_buddhist)));


        tagMp_orange.put("school", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.orange_school)));
        tagMp_orange.put("hindu", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.orange_hindu)));
        tagMp_orange.put("police", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.orange_police)));
        tagMp_orange.put("hospital", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.orange_hospital)));
        tagMp_orange.put("clinic", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.orange_clinic)));
        tagMp_orange.put("health_post", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.orange_health_post)));
        tagMp_orange.put("pharmacy", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.orange_pharmacy)));
        tagMp_orange.put("dentist", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.orange_dentist)));
        tagMp_orange.put("veterinarians", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.orange_veterinary)));
        tagMp_orange.put("government", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.orange_government)));
        tagMp_orange.put("ngo", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.orange_ngo)));
        tagMp_orange.put("bank", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.orange_bank)));
        tagMp_orange.put("fuel", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.orange_fuel)));
        tagMp_orange.put("radio", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.orange_radio)));
        tagMp_orange.put("television", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.orange_tv)));
        tagMp_orange.put("newspaper", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.orange_newspaper)));
        tagMp_orange.put("college", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.orange_university)));
        tagMp_orange.put("university", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.orange_university)));
        tagMp_orange.put("kindergarten", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.orange_kindergarten)));
        tagMp_orange.put("buddhist", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.orange_buddhist)));
        tagMp_orange.put("christian", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.orange_christian)));
        tagMp_orange.put("muslim", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.orange_muslim)));
        tagMp_orange.put("atm", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.orange_atm)));
        tagMp_orange.put("restaurant", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.orange_resturant)));
        tagMp_orange.put("museum", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.orange_museum)));
        tagMp_orange.put("park", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.orange_museum)));
        tagMp_orange.put("storage_tank", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.orange_storage_tank)));
        tagMp_orange.put("water_tap", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.orange_public_tap)));
        tagMp_orange.put("water_well", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.orange_water_well)));
        tagMp_orange.put("gas", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.map_marker_light)));
        tagMp_orange.put("cooperative", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.orange_bank)));
        tagMp_orange.put("hotel", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.orange_hotel)));
        tagMp_orange.put("kirat", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.map_marker_light)));
        tagMp_orange.put("sikh", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.map_marker_light)));
        tagMp_orange.put("judaism", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.map_marker_light)));
        tagMp_orange.put("other-religion", mIconFactory.fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.map_marker_light)));
//        Bitmap hos_icon = mIconFactory.fromBitmap(BitmapFactory.decodeResource(
//                getActivity().getResources(), R.drawable.hospital);
//        Bitmap bnk_icon = mIconFactory.fromBitmap(BitmapFactory.decodeResource(
//                getActivity().getResources(), R.drawable.bank);
//        Bitmap scol_icon = mIconFactory.fromBitmap(BitmapFactory.decodeResource(
//                getActivity().getResources(), R.drawable.school);
//        hospital_icon = mIconFactory.fromBitmap(hos_icon);
//        bank_icon = mIconFactory.fromBitmap(bnk_icon);
//        school_icon = mIconFactory.fromBitmap(scol_icon);
//        Bitmap hos_selected = mIconFactory.fromBitmap(BitmapFactory.decodeResource(
//                getActivity().getResources(), R.drawable.orange_hospital);
//        Bitmap bnk_selected = mIconFactory.fromBitmap(BitmapFactory.decodeResource(
//                getActivity().getResources(), R.drawable.orange_bank);
//        Bitmap scol_selected = mIconFactory.fromBitmap(BitmapFactory.decodeResource(
//                getActivity().getResources(), R.drawable.orange_school);
        navButton = v.findViewById(R.id.startButton);
//        hospital_selected = mIconFactory.fromBitmap(hos_selected);
//        bank_selected = mIconFactory.fromBitmap(bnk_selected);
//        school_selected = mIconFactory.fromBitmap(scol_selected);
        if (getArguments() != null) {
            FilterParcel filterdata = getArguments().getParcelable("FilterValue");
            selectedType = getArguments().getString("selectedType", "attractions");
            if (filterdata != null) {
                for (Map.Entry<String, String> filter_data : filterdata.getFilter_parameter().entrySet()
                ) {
                }
            }
        }


//        if (selectedType.equals("attractions")) {
//            amenityInfo = inflate_info.inflate(R.layout.places_of_attraction, null);
//            attraction_title = amenityInfo.findViewById(R.id.attraction_title);
//            attraction_title_np = amenityInfo.findViewById(R.id.attraction_title_np);
//            attraction_detail = amenityInfo.findViewById(R.id.attraction_detail);
//            attraction_image = amenityInfo.findViewById(R.id.attraction_image);
//            attraction_close = amenityInfo.findViewById(R.id.attraction_close);
//            attraction_tags_container = amenityInfo.findViewById(R.id.contentOsm);
//            attraction_swipe = amenityInfo.findViewById(R.id.swipe_container);
//            detailbool = false;
//
//            attraction_close.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    swipeValue = 0;
//                    lm.setLayoutParams(lp_shrink);
//                    small_info.setVisibility(View.VISIBLE);
//                    ((AppCompatActivity) getActivity()).getSupportActionBar().show();
//                    toggleTabVisibilityListener.showTabs();
//                    lm.setVisibility(View.GONE);
//                    if (navigationMapRoute != null) navigationMapRoute.removeRoute();
//                    detail_screen.removeAllViews();
//                }
//            });
//            attraction_swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//                @Override
//                public void onRefresh() {
//                    attraction_swipe.setRefreshing(false);
//                    lm.setLayoutParams(lp_shrink);
//                    small_info.setVisibility(View.VISIBLE);
//                    detail_screen.removeView(amenityInfo);
//                    iff_ondown = false;
//                    iff_onswipe = false;
//                }
//            });
//        } else {
            detailbool = true;
            amenityInfo = inflate_info.inflate(R.layout.detailview, null);
            containera = amenityInfo.findViewById(R.id.detailLayout);
            closebtn = amenityInfo.findViewById(R.id.btn_close);
            detailEnglishTitle = amenityInfo.findViewById(R.id.txt_detail_enname);
            detailNepaliTitle = amenityInfo.findViewById(R.id.txt_detail_nename);
            detailPhone = amenityInfo.findViewById(R.id.txt_detail_phone);
            detailWeb = amenityInfo.findViewById(R.id.txt_detail_web);
            detailMail = amenityInfo.findViewById(R.id.txt_detail_email);
            edit_btn = amenityInfo.findViewById(R.id.edit_btn);

            edit_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((AppCompatActivity) getActivity()).getSupportActionBar().show();
                    toggleTabVisibilityListener.showTabs();
                    editAmenity(selectedType);
                }
            });
            closebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swipeValue = 0;
                    lm.setLayoutParams(lp_shrink);
                    small_info.setVisibility(View.VISIBLE);
                    ((AppCompatActivity) getActivity()).getSupportActionBar().show();
                    toggleTabVisibilityListener.showTabs();
                    if (navigationMapRoute != null) navigationMapRoute.removeRoute();
                    lm.setVisibility(View.GONE);
                    detail_screen.removeAllViews();
                    // mapView.refreshDrawableState();
                }
            });
//        }

        lm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mDetector.onTouchEvent(motionEvent);
                Log.d(String.valueOf(swipeValue), "Swipe Value");
                // if(swipeValue==1 || (iff_ondown && ! iff_onswipe))
                if (swipeValue == 1 || (iff_ondown && !iff_onswipe)) {
                    ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
                    toggleTabVisibilityListener.hideTabs();
                    small_info.setVisibility(View.GONE);
                    lm.setLayoutParams(lp_expand);
                    detail_screen.removeAllViews();
                    detailView(detailbool);
                    detail_screen.addView(amenityInfo);
                    swipeValue = 1;
                } else if (swipeValue == 2) {
                    if (lm.getLayoutParams() == lp_shrink) {
                        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
                        toggleTabVisibilityListener.showTabs();
                        //if (navigationMapRoute != null) navigationMapRoute.removeRoute();
                        lm.setVisibility(View.GONE);
                        detail_screen.removeView(amenityInfo);
                    } else {
                        lm.setLayoutParams(lp_shrink);
                        small_info.setVisibility(View.VISIBLE);
                        detail_screen.removeView(amenityInfo);
                        iff_ondown = false;
                        iff_onswipe = false;
                    }
                }
                return false;
            }
        });
        expandinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
                toggleTabVisibilityListener.hideTabs();
                small_info.setVisibility(View.GONE);
                lm.setLayoutParams(lp_expand);
                detail_screen.removeAllViews();
                detailView(detailbool);
                detail_screen.addView(amenityInfo);
            }
        });


        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeValue = 0;
                lm.setLayoutParams(lp_shrink);
                small_info.setVisibility(View.VISIBLE);
                ((AppCompatActivity) getActivity()).getSupportActionBar().show();
                toggleTabVisibilityListener.showTabs();
                lm.setVisibility(View.GONE);
                if (navigationMapRoute != null) navigationMapRoute.removeRoute();
                detail_screen.removeAllViews();
            }
        });
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (navButton.getText().toString().equals("Route")) {

                    if (originLocation != null) {
//                        Log.wtf(String.valueOf(originLocation),"Origin Location");
                        Toast.makeText(getContext(), "Please wait the gps is locating you", Toast.LENGTH_LONG).show();
                        originCoord = new LatLng(originLocation.getLatitude(), originLocation.getLongitude());
                        originPosition = Point.fromLngLat(originCoord.getLongitude(), originCoord.getLatitude());
                        getRoute(originPosition, destinationPosition);
                    } else {
                        if (gps.getTag().equals("gps_off")) {
                            gps_checker();
                            Toast.makeText(getContext(), "Please wait the gps is locating you", Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(getContext(), "Could not find you", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (navigationMapRoute != null) {
                        Point origin = originPosition;
                        Point destination = destinationPosition;
                        // Pass in your Amazon Polly pool id for speech synthesis using Amazon Polly
                        // Set to null to use the default Android speech synthesizer
                        String awsPoolId = null;
                        boolean simulateRoute = false;
                        NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                                .origin(origin)
                                .destination(destination)
                                .awsPoolId(awsPoolId)
                                .shouldSimulateRoute(simulateRoute)
                                .unitType(NavigationUnitType.TYPE_METRIC)
                                .build();

                        // Call this method with Context from within an Activity
                        NavigationLauncher.startNavigation(getActivity(), options);
                    } else {
                        Toast.makeText(getContext(), "Could not find you", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                .include(new LatLng(22.8202, 76.0467)) // Northeast
                .include(new LatLng(22.6248, 75.7202)) // Southwest
                .build();

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                MapFragment.this.mapboxMap = mapboxMap;
                mapboxMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 50));
                mapboxMap.setLatLngBoundsForCameraTarget(latLngBounds);
                mapboxMap.setMaxZoomPreference(18);
                mapboxMap.setMinZoomPreference(9);
                // mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(28.2380, 83.9956), 9.3), 500);
                zoomtoextant.setEnabled(true);
                clusterManagerPlugin = new CustomClusterManagerPlugin<MyItem>(getContext(), mapboxMap);

                //addmarkeronTouch();
                initCameraListener();
                enableLocationPlugin();
            }
        });
        listView = v.findViewById(R.id.listView);
        uniList = new HashMap<>();
        searches = new ArrayList<>();
        adapter = new ArrayAdapter<Search>(this.getContext(), android.R.layout.simple_list_item_1, searches);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Search selected = (Search) listView.getItemAtPosition(position);
                clickmarker(selected.cord, selected.name);

            }
        });
        return v;
    }

    static String loadGeoJsonFromAsset(Context context, String filename) {
        try {
            // Load GeoJSON file from local asset folder
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ToggleTabVisibilityListener) {
            toggleTabVisibilityListener = (ToggleTabVisibilityListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        toggleTabVisibilityListener = null;
    }

    public void getSelected(String selection) {
        selectedType = selection;
    }

    public void detailView(boolean detailbool) {
        if (detailbool) {
            Realm realm = Realm.getDefaultInstance();


            RealmQuery<ExploreSchema> query = realm.where(ExploreSchema.class);
            ExploreSchema dbvalue = query.equalTo("name", editName).findFirst();
            int size = dbvalue.getTag_type().size();
            realm.close();

            detailEnglishTitle.setText(dbvalue.getName());
            detailNepaliTitle.setText(dbvalue.getNamein());

            if (dbvalue.getWeb() != null) {
                detailWeb.setText(dbvalue.getWeb());
                detailWeb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = dbvalue.getWeb();
                        if (!url.startsWith("http://") && !url.startsWith("https://"))
                            url = "http://" + url;
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    }
                });
            } else {
                detailWeb.setText("-");
            }
            if (dbvalue.getContact_email() != null) {
                detailMail.setText(dbvalue.getContact_email());
            } else {
                detailMail.setText("-");
            }

            View[] view;
            TextView[] detailTag;
            TextView[] detailValue;
            detailTag = new TextView[size];
            detailValue = new TextView[size];
            view = new View[size];
            if (containera != null) {
                containera.removeAllViews();
            }
            RealmList<String> keys = dbvalue.getTag_type();
            RealmList<String> labels = dbvalue.getTag_lable();
            int i = 0;
            String mob = null;
            for (String tg : dbvalue.getTag_type()) {
                String label = Utils.toTitleCase(labels.get(i));
                String key = Utils.toTitleCase(keys.get(i).replace("_"," "));
                Log.wtf(label,key);
                if(key.equals("Mobile")) mob = label;
                if (!key.equals("Name") && !key.equals("Name Hindi") && !key.equals("Phone Number") &&
                        !key.equals("Email Address") && !key.equals("Id") && !key.equals("Latitude") && !key.equals("Longitude")
                        && !key.equals("Precision") && !key.equals("Mobile")) {
                    view[i] = getLayoutInflater().inflate(R.layout.detailtextgenerator, containera, false);
                    detailTag[i] = view[i].findViewById(R.id.detail_type);
                    detailValue[i] = view[i].findViewById(R.id.detail_value);
                    detailTag[i].setText(key);
                    detailValue[i].setText(label);
                    containera.addView(view[i]);
                }
                i++;
            }
            if (dbvalue.getContact_phone() != null || mob!=null) {
                String pho = null;
                if(dbvalue.getContact_phone() != null) pho = dbvalue.getContact_phone();
                if (dbvalue.getContact_phone() != null) {
                    Log.wtf(dbvalue.getContact_phone().toString(),"phone");
                }
                if(mob!=null) {
                    if (pho != null)
                        pho = pho + "," + mob;
                    else pho = mob;
                }
                detailPhone.setText(pho);
                detailPhone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + detailPhone.getText().toString().split(",")[0]));
                        startActivity(intent);
                    }
                });
            } else {
                detailPhone.setText("-");
            }
        }

    }

    public void gps_checker() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if(originLocation!=null) {
                if ((originLocation.getLatitude() > 28.31285 || originLocation.getLatitude() < 28.11532
                        || originLocation.getLongitude() > 84.14949 || originLocation.getLongitude() < 83.84905)) {
                    Toast.makeText(getContext(), "You are not in Pokhara", Toast.LENGTH_SHORT).show();
                } else {
                    gps.setImageResource(0);
                    gps.setImageResource(R.drawable.ic_action_gps_searching);
                    gps.setTag("gps_searching");
                    Toast.makeText(getContext(), "GPS is locating you", Toast.LENGTH_SHORT).show();

                }
            }
            else {
                initializeLocationEngine();
            }
        } else {
            showGPSDisabledAlertToUser();
        }
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void setFilter(Boolean fp) {
        filterflag = fp;
        //mapView.refreshDrawableState();
        navigationMapRoute = null;
        FragmentTransaction ftr = getFragmentManager().beginTransaction();
        ftr.detach(MapFragment.this).attach(MapFragment.this).commit();
    }

    protected void initCameraListener() {
        try {
            addItemsToClusterPlugin();
            mapboxMap.addOnCameraIdleListener(clusterManagerPlugin);
//            GeoJsonSource boundary = new GeoJsonSource("boundary", loadGeoJsonFromAsset(getContext(), "pokhara_ward_boundary.geojson"));
//
//            mapboxMap.addSource(boundary);
//            LineLayer boundaryLine = new LineLayer("boundaryLayer", "boundary");
//            boundaryLine.setProperties(PropertyFactory.lineWidth(1f), PropertyFactory.lineColor(Color.parseColor("#753b3b")));
//            int allLayer = mapboxMap.getLayers().size();
//            mapboxMap.addLayerAt(boundaryLine, allLayer - 10);
            List<LatLng> polygon = new ArrayList<>();
            Realm realm = Realm.getDefaultInstance();
            RealmResults<PokharaBoundary> wardResult = realm.where(PokharaBoundary.class).contains("tag","all_boundary").findAll();
            realm.close();
            if(polygon.size()>0)polygon.clear();
            polygon = new ArrayList<>();
            for (PokharaBoundary pbs:wardResult
                    ) {
                polygon.add( new LatLng(pbs.getCoordinateslat(), pbs.getCoordinateslong()));
            }

//            wardBound(polygon);
            mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull final Marker marker) {
                    markerclickAction(marker);
                    return true;
                }
            });

                clusterManagerPlugin.getRenderer().setOnClusterClickListener(new CustomClusterManagerPlugin.OnClusterClickListener<MyItem>() {
                    @Override
                    public boolean onClusterClick(Cluster<MyItem> cluster) {
                        Log.d(String.valueOf(cluster.getSize()),"cluster icon id");
                        return false;
                    }
                });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void wardBound(List<LatLng> polygon){


        List<LatLng> polygonbound = new ArrayList<>();
        polygonbound.add(new LatLng(28.8711,85.5011));
        polygonbound.add(new LatLng(27.3185,85.5011));
        polygonbound.add(new LatLng(27.3185,82.8891));
        polygonbound.add(new LatLng(28.8711, 82.8891));
        polygonbound.add(new LatLng(28.8711,85.5011));
        mapboxMap.addPolygon(new PolygonOptions()
                .addAll(polygonbound)
                .addHole(polygon)
                .alpha(0.5f)
                .fillColor(Color.parseColor("#000000")));
    }
    private void markerclickAction(Marker marker){
        String markerText = marker.getTitle();
        testText.setText(markerText);
        double zoom = mapboxMap.getCameraPosition().zoom;
        if (zoom < 10)
            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 12), 500);
        else if (zoom < 14)
            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 14), 400);
        else if (zoom < 16)
            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), zoom + 0.5), 300);
        else if (zoom < 18)
            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), zoom + 0.2), 200);
        if (marker.getTitle() != null) {
            destinationCoord = marker.getPosition();
            destinationPosition = Point.fromLngLat(destinationCoord.getLongitude(), destinationCoord.getLatitude());
            if (previous_selected != null) {
                if (previous_selected != marker) {
                    for (Map.Entry<String,com.mapbox.mapboxsdk.annotations.Icon > entry : tagMp_blue.entrySet()) {
//                        if (entry.getKey().equals(selectedType)) {
//
//                        }
                        if(!selectedType.equals("attractions")) {
                            if (entry.getKey().equals(selectedType)) {
                                previous_selected.setIcon(entry.getValue());
                            }
                        }
                        else if(entry.getKey().equals(MainActivity.def_type_category)){
                            previous_selected.setIcon(entry.getValue());
                        }
                    }

                }
            }
            previous_selected = marker;
            for (Map.Entry<String,com.mapbox.mapboxsdk.annotations.Icon > entry : tagMp_orange.entrySet()) {
                if(!selectedType.equals("attractions")) {
                    if (entry.getKey().equals(selectedType)) {
                        marker.setIcon(entry.getValue());
                    }
                }
                else if(entry.getKey().equals(MainActivity.def_type_category)){
                    marker.setIcon(entry.getValue());
                }
            }
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            toggleTabVisibilityListener.hideTabs();
            lm.setVisibility(View.VISIBLE);
            editName = marker.getTitle();
            editLat = String.valueOf(marker.getPosition().getLatitude());
            editLong = String.valueOf(marker.getPosition().getLongitude());
            //swipeValue = 1;
            iff_ondown = false;
            iff_onswipe = false;
            navButton.setBackgroundColor(getResources().getColor(R.color.tertiaryText));
            navButton.setText("Route");
            if (navigationMapRoute != null) navigationMapRoute.removeRoute();
            Log.d(String.valueOf(detail_screen.getLayoutParams().height),"Detail Screen Height");
            Log.d(String.valueOf(detail_height),"Detail Screen Calculated");
            if(swipeValue==1) {
                if(detailPhone!=null)
                detailPhone.setClickable(false);
                detail_screen.removeAllViews();
                detailView(detailbool);
                detail_screen.addView(amenityInfo);
            }
        }
        }



    private void addItemsToClusterPlugin() {
        populateMap(selectedType);
    }

    private void populateMap(String amenity) {
                com.mapbox.mapboxsdk.annotations.Icon icn = getItemIcon();
                Realm realm = Realm.getDefaultInstance();
                RealmQuery<ExploreSchema> query = realm.where(ExploreSchema.class).equalTo("tag", amenity);
                RealmList<ExploreSchema> querycollection = new RealmList<>();

                if (MainActivity.filter_param.size() > 0 && filterflag) {
                    String container1 = null, container2 = null;
                    Map<String, String> check1 = new HashMap<>();
                    Map<String, String> check2 = new HashMap<>();

                    for (Map.Entry<String, String> filter_data : MainActivity.filter_param.entrySet()
                            ) {
                        Log.wtf(filter_data.getValue(), filter_data.getKey());
                        try {
                        if ((filter_data.getKey().equals("wardid") && !filter_data.getValue().equals("all"))) {
                            RealmResults<Ward> wardR = realm.where(Ward.class).contains("osmID", filter_data.getValue()).findAll();
                            RealmList<PokharaBoundary> pbound = wardR.get(0).getBoundry();
                            polygon = new ArrayList<>();
                            double bbboxlat1 = 0, bbboxlat2 = 22.7230, bbboxlong1 = 0, bbboxlong2 = 75.8572;
                            for (int i = 0; i < pbound.size(); i++) {
                                bbboxlat1 = pbound.get(i).getCoordinateslat() > bbboxlat1 ? pbound.get(i).getCoordinateslat() : bbboxlat1;
                                bbboxlong1 = pbound.get(i).getCoordinateslong() > bbboxlong1 ? pbound.get(i).getCoordinateslong() : bbboxlong1;
                                bbboxlat2 = pbound.get(i).getCoordinateslat() < bbboxlat2 ? pbound.get(i).getCoordinateslat() : bbboxlat2;
                                bbboxlong2 = pbound.get(i).getCoordinateslong() < bbboxlong2 ? pbound.get(i).getCoordinateslong() : bbboxlong2;
                                polygon.add(new LatLng(pbound.get(i).getCoordinateslat(), pbound.get(i).getCoordinateslong()));
                                Log.wtf(String.valueOf(pbound.get(i).getCoordinateslat()), "Lat");
                            }
                            LatLngBounds latLngBounds = new LatLngBounds.Builder()
                                    .include(new LatLng(bbboxlat1, bbboxlong1)) // Northeast
                                    .include(new LatLng(bbboxlat2, bbboxlong2)) // Southwest
                                    .build();
                            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 50));
                            mapboxMap.setLatLngBoundsForCameraTarget(latLngBounds);
//                            wardBound(polygon);
                        }
                        if (filter_data.getKey().endsWith("max")) {
                            String rangeMax = filter_data.getKey().split("max")[0];
                            int max = Integer.parseInt(filter_data.getValue());
                            query.beginGroup().lessThanOrEqualTo(rangeMax, max).or().isNull(rangeMax).endGroup();
                        } else if (filter_data.getKey().endsWith("min")) {
                            String rangeMin = filter_data.getKey().split("min")[0];
                            int min = Integer.parseInt(filter_data.getValue());
                            Log.d(String.valueOf(min), "Minimun Value");
                            if (min > 0) query.greaterThanOrEqualTo(rangeMin, min);
                            else
                                query.beginGroup().greaterThanOrEqualTo(rangeMin, min).or().isNull(rangeMin).endGroup();
                        } else if (filter_data.getKey().endsWith("check")) {
                            Log.wtf("check","not null");
                            String qp = filter_data.getKey().split("check")[0];

                            if (container1 == null) container1 = filter_data.getValue();
                            else if (!container1.equals(filter_data.getValue()))
                                container2 = filter_data.getValue();
                            if (container1.equals(filter_data.getValue())) {
                                check1.put(qp, filter_data.getValue());
                                //query.equalTo(filter_data.getValue(), qp, Case.INSENSITIVE);
                            } else if (container2 != null && container2.equals(filter_data.getValue())) {
                                check2.put(qp, filter_data.getValue());
                                //query.equalTo(filter_data.getValue(), qp, Case.INSENSITIVE);

                            }
                        }
//                else if(filter_data.getKey().equals("wardid")){
//                    if(!filter_data.getValue().equals("all")){
//                        Log.wtf(filter_data.getValue(),filter_data.getKey());
//                        query.equalTo(filter_data.getKey(),filter_data.getValue(),Case.INSENSITIVE);
//                    }
//                }
                        else {
                            if (!(filter_data.getKey().equals("wardid") && filter_data.getValue().equals("all"))) {
//                                query.contains("tag_lable", filter_data.getValue(), Case.INSENSITIVE);
                                Log.wtf("TAADF", "bbb");
//                                for (ExploreSchema ep : query.findAll()
//                                ) {
//                                    RealmList<String> key = ep.getTag_type();
//                                    RealmList<String> value = ep.getTag_lable();
//                                    int i = 0;
//
//                                    for (String str : key
//                                    ) {
//                                        if (value.get(i).matches(filter_data.getValue())) {
//                                            querycollection.add(ep);
//                                        }
//                                        i++;
//                                    }
//
//                                }
                            }

                        }
                    } catch (Exception E){
                            E.printStackTrace();
                        }
                    }
                        int i = 0;
                        if (check1.size() > 0)
                            for (Map.Entry<String, String> check_set : check1.entrySet()
                            ) {
                                Log.wtf("check 1", check_set.getKey());
                                try {
                                    if (i == 0 && i != check1.size() - 1) {
                                        query.beginGroup().contains(check_set.getValue(), check_set.getKey(), Case.INSENSITIVE);
                                    } else if (i == 0 && i == check1.size() - 1) {
                                        query.contains(check_set.getValue(), check_set.getKey(), Case.INSENSITIVE);
                                    } else if ((i < check1.size() - 1)) {
                                        query.or().contains(check_set.getValue(), check_set.getKey(), Case.INSENSITIVE);
                                    } else if (i == check1.size() - 1) {
                                        query.or().contains(check_set.getValue(), check_set.getKey(), Case.INSENSITIVE).endGroup();
                                    }
                                } catch (Exception e){
                                    e.printStackTrace();
                                }
                                i++;
                            }
                        i = 0;
                        if (check2.size() > 0)
                            for (Map.Entry<String, String> check_set : check2.entrySet()
                            ) {
                                Log.wtf("check 2", check_set.getKey());
                                try {

                                    if (i == 0 && i != check2.size() - 1) {
//                                query.find(query,check_set.getValue(),)
                                        query.beginGroup().contains(check_set.getValue(), check_set.getKey(), Case.INSENSITIVE);
                                    } else if (i == 0 && i == check2.size() - 1) {
                                        query.contains(check_set.getValue(), check_set.getKey(), Case.INSENSITIVE);
                                    } else if ((i < check2.size() - 1)) {
                                        query.or().contains(check_set.getValue(), check_set.getKey(), Case.INSENSITIVE);
                                    } else if (i == check2.size() - 1) {
                                        query.or().contains(check_set.getValue(), check_set.getKey(), Case.INSENSITIVE).endGroup();
                                    }
                                } catch (Exception e){
                                    e.printStackTrace();
                                }
                                i++;
                            }
                    check1.clear();
                    check2.clear();
                }

                MainActivity.filter_param.clear();
                RealmResults<ExploreSchema> results = query.findAll();
//        RealmList<ExploreSchema> results = querycollection;
        realm.close();
                List<MyItem> items = new ArrayList<MyItem>();
                if (searches.size() > 0) {
                    searches.clear();
                    uniList.clear();
                    adapter.clear();
                }
                for (int i = 0; i < results.size(); i++) {
                    String title;
                    String snippet = "Swipe up for more detail";
                    double lat = results.get(i).getCoordinateslong();
                    double lng = results.get(i).getCoordinateslat();
                    if (results.get(i).getName() != null) {
                        title = results.get(i).getName();
                        Search sh = new Search(null, null);
                        sh.cord = new LatLng(lat, lng);
                        sh.name = title;
                        searches.add(sh);
//                HashMap<String,String> hsh = new HashMap<>();
//                hsh.put(results.get(i).getOsm_id(), title);
//                list.add(i,hsh);
                        uniList.put(new LatLng(lat, lng), title);
                    } else title = amenity;
                    items.add(new MyItem(lat, lng, title, snippet, icn));
                }
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();


                //TODO check for applied filters

                clusterManagerPlugin.addItems(items);
    }

    private Icon getItemIcon() {
        for (Map.Entry<String,com.mapbox.mapboxsdk.annotations.Icon > entry : tagMp_blue.entrySet()) {
            if(!selectedType.equals("attractions")) {
                if (entry.getKey().equals(selectedType)) {
                    return entry.getValue();
                }
            }
            else if(entry.getKey().equals(MainActivity.def_type_category)){
                return entry.getValue();
            }
        }
        return IconFactory.getInstance(getActivity()).fromBitmap(BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.map_marker_dark));
    }





    //Location_code
    @SuppressWarnings({"MissingPermission"})
    private void enableLocationPlugin() {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(getContext())) {
            // Create an instance of LOST location engine
            initializeLocationEngine();

            locationPlugin = new LocationLayerPlugin(mapView, mapboxMap, locationEngine);
            locationPlugin.setLocationLayerEnabled(LocationLayerMode.TRACKING);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }

    @SuppressWarnings({"MissingPermission"})
    private void initializeLocationEngine() {
        locationEngine = new LostLocationEngine(getContext());
        //locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();

        Location lastLocation = locationEngine.getLastLocation();
        if (lastLocation != null) {
            originLocation = lastLocation;
            if (originLocation.getLatitude() > 28.31285 || originLocation.getLatitude() < 28.11532
                    || originLocation.getLongitude() > 84.14949 || originLocation.getLongitude() < 83.84905) {
                // Toast.makeText(getContext(), "You are not in Pokhara", Toast.LENGTH_SHORT).show();
            } else {
                gps.setImageResource(0);
                gps.setImageResource(R.drawable.ic_action_gps_searching);
                gps.setTag("gps_searching");
                Toast.makeText(getContext(), "GPS is locating you", Toast.LENGTH_SHORT).show();
                setCameraPosition(lastLocation);
            }
        } else {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                Log.d(String.valueOf(gps.getTag()), "At addLocationEngineListner");
                gps.setImageResource(0);
                gps.setImageResource(R.drawable.ic_action_gps_searching);
                gps.setTag("gps_searching");
                Toast.makeText(getContext(), "GPS is locating you", Toast.LENGTH_SHORT).show();
            }
            locationEngine.addLocationEngineListener(this);
        }
    }



    private void getRoute(Point origin, Point destination) {
        NavigationRoute.builder()
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        // You can get the generic HTTP info about the response
                        //Log.d(TAG, "Response code: " + response.code());
                        if (response.body() == null) {
                            //Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            //Log.e(TAG, "No routes found");
                            return;
                        }

                        currentRoute = response.body().routes().get(0);

                        // Draw the route on the map
                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                        if (navigationMapRoute != null) {
                            navButton.setText("Navigate");
                            navButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                            //navButton.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
//                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//                            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//                            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
//                            params.bottomMargin = 5;
//                            navButton.setLayoutParams(params);

                        }
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                    throwable.printStackTrace();
                    }
                });
    }

    //edit
    private void editAmenity(String amenity) {
//        Intent i = new Intent(getContext(), EditDialogActivity.class);
//        String[] editInfo = {amenity, editName,editLat,editLong};
//        i.putExtra("amenity", editInfo);
//        startActivity(i);


    }


    private void setCameraPosition(Location location) {
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 13));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    @SuppressWarnings({"MissingPermission"})
    public void onStart() {
        super.onStart();
        if (locationEngine != null) {
            locationEngine.requestLocationUpdates();
        }
        if (locationPlugin != null) {
            locationPlugin.onStart();
        }
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }
    public RealmQuery<ExploreSchema> find(RealmQuery<ExploreSchema> qe, String fieldName, String[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("EMPTY_VALUES");
        }
        qe.beginGroup().equalTo(fieldName, values[0]);
        for (int i = 1; i < values.length; i++) {
            qe.or().equalTo(fieldName, values[i]);
        }
        return qe.endGroup();
    }

//    public void doSomething(String myValue){
//        if(myValue!=null){
//            if (!selectedType.equals(myValue) || myValue.equals("first")){
//             //   MainActivity.updateMapView = false;
//                Log.d("inUpdate", "updateView: ");
////                Fragment frg = null;
////                String tagfeature = "android:switcher:" + R.id.viewpager + ":" + 0;
////                frg = (MapFragment) getFragmentManager().findFragmentByTag(tagfeature);
////                FragmentTransaction ft = getFragmentManager().beginTransaction();
////                if(frg!=null) {
////                    ft.detach(frg);
////                    ft.attach(frg);
////                    ft.commit();
////                }
//            }
//        }
//        // Do stuff in your fragment with myValue
//    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates();
        }
        if (locationPlugin != null) {
            locationPlugin.onStop();
        }
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    //    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mapView.onDestroy();
//        if (locationEngine != null) {
//            locationEngine.deactivate();
//        }
//    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (MainActivity.destroyMapView) {
            mapView.invalidate();
            mapView.onDestroy();
        }
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates();
            locationEngine.deactivate();
        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationPlugin();
        } else {
            getActivity().finish();
        }

    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    public void onConnected() {
        locationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            originLocation = location;
            if (originLocation.getLatitude() > 28.31285 || originLocation.getLatitude() < 28.11532
                    || originLocation.getLongitude() > 84.14949 || originLocation.getLongitude() < 83.84905) {
               // Toast.makeText(getContext(), "Out of Pokhara Bound", Toast.LENGTH_SHORT).show();
            } else {
                gps.setImageResource(0);
                gps.setImageResource(R.drawable.ic_action_gps_fixed);
                gps.setTag("gps_fixed");
                setCameraPosition(location);
            }
            locationEngine.removeLocationEngineListener(this);
        }
    }

    public static class MyItem implements CustomClusterItem {
        private final LatLng position;
        private String title;
        private String snippet;
        private com.mapbox.mapboxsdk.annotations.Icon icon;

        public MyItem(double lat, double lng) {
            position = new LatLng(lat, lng);
            title = null;
            snippet = null;
        }

        public MyItem(double lat, double lng, String title, String snippet, com.mapbox.mapboxsdk.annotations.Icon icon) {
            position = new LatLng(lat, lng);
            this.title = title;
            this.snippet = snippet;
            this.icon = icon;
        }

        @Override
        public LatLng getPosition() {
            return position;
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public String getSnippet() {
            return snippet;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setSnippet(String snippet) {
            this.snippet = snippet;
        }

        @Override
        public com.mapbox.mapboxsdk.annotations.Icon getIcon() {
            return icon;
        }
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onDown(MotionEvent event) {
            iff_ondown = true;
            swipeValue = 0;
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            iff_onswipe = true;
            if (Math.abs(event1.getY() - event2.getY()) > SWIPE_MAX_OFF_PATH) {
                final float distance = event1.getY() - event2.getY();
                final boolean enoughSpeed = Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY;
                if (distance > SWIPE_MIN_DISTANCE && enoughSpeed) {
                    swipeValue = 1;
                    return true;
                } else if (distance < SWIPE_MIN_DISTANCE && enoughSpeed) {
                    swipeValue = 2;

                    return true;
                } else {
                    return false;
                }
            }
            swipeValue = 0;
            return false;
        }
    }
//    private final void focusOnView(){
//        scroll.post(new Runnable() {
//            @Override
//            public void run() {
//                scroll.scrollTo(0, scroll.getBottom()-150);
//            }
//        });
//    }

}


