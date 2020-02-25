package org.kathmandulivinglabs.exploreindore.Fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.appupdater.objects.Update;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;
import org.kathmandulivinglabs.exploreindore.Activity.DataManager;
import org.kathmandulivinglabs.exploreindore.Activity.LoginActivity;
import org.kathmandulivinglabs.exploreindore.Activity.MainActivity;
import org.kathmandulivinglabs.exploreindore.Adapter.SearchListAdapter;
import org.kathmandulivinglabs.exploreindore.Events.ShowDownloadAllDialogEvent;
import org.kathmandulivinglabs.exploreindore.FilterParcel;
import org.kathmandulivinglabs.exploreindore.Activity.Edit.EditDialogActivity;
import org.kathmandulivinglabs.exploreindore.Events.EditAmenityEvent;
import org.kathmandulivinglabs.exploreindore.Helper.Connectivity;
import org.kathmandulivinglabs.exploreindore.Helper.Keys;
import org.kathmandulivinglabs.exploreindore.Helper.Utils;

import org.kathmandulivinglabs.exploreindore.IndoreApp;
import org.kathmandulivinglabs.exploreindore.Interface.DownloadKeys;
import org.kathmandulivinglabs.exploreindore.Interface.ToggleTabVisibilityListener;
import org.kathmandulivinglabs.exploreindore.R;
import org.kathmandulivinglabs.exploreindore.Realmstore.ExploreSchema;
import org.kathmandulivinglabs.exploreindore.Realmstore.PokharaBoundary;
import org.kathmandulivinglabs.exploreindore.Realmstore.Ward;
import org.kathmandulivinglabs.exploreindore.models.POI.FeatureTag;
import org.kathmandulivinglabs.exploreindore.models.POI.POI;
import org.kathmandulivinglabs.exploreindore.models.POI.POIFeature;
import org.kathmandulivinglabs.exploreindore.models.POI.POIFeatureProperty;
import org.kathmandulivinglabs.exploreindore.models.POI.POIGeometry;

import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


import io.realm.RealmList;
import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import timber.log.Timber;

import static androidx.core.content.ContextCompat.checkSelfPermission;
import static com.mapbox.mapboxsdk.style.expressions.Expression.all;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.gte;
import static com.mapbox.mapboxsdk.style.expressions.Expression.has;
import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.expressions.Expression.log2;
import static com.mapbox.mapboxsdk.style.expressions.Expression.lt;
import static com.mapbox.mapboxsdk.style.expressions.Expression.match;
import static com.mapbox.mapboxsdk.style.expressions.Expression.step;
import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;
import static com.mapbox.mapboxsdk.style.expressions.Expression.toNumber;
import static com.mapbox.mapboxsdk.style.expressions.Expression.zoom;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleRadius;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textSize;

//public class MapFragment extends Fragment implements PermissionsListener, LocationEngineListener, MainActivity.Backlistner {
public class MapFragment extends Fragment implements PermissionsListener, MainActivity.Backlistner, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private static final String SINGLE_EARTHQUAKE_TRIANGLE_ICON_ID = "single-quake-icon-id";
    private static final String SELECTED_SINGLE_EARTHQUAKE_TRIANGLE_ICON_ID = "selected_single-quake-icon-id";
    private static final String EARTHQUAKE_SOURCE_ID = "earthquakes";
    private static final String POINT_COUNT = "point_count";
    private static final String UNCLUSTERED_POINTS = "unclustered-points";
    private static final String SELECTED_MARKER_LAYER = "selected-marker-layer";
    private static final String SELECTED_MARKER = "selected-marker";
    private MapView mapView;
    private MapboxMap mapboxMap;
    private Style style;
    public static LinearLayout lm;
    private LinearLayout small_info;
    private TextView testText, detailNepaliTitle, detailEnglishTitle, detailPhone, detailMobile, detailWeb, detailMail;
    private ViewGroup mapScreen;
    private LinearLayout detail_screen;
    private LinearLayout websiteLayout, emailLayout, mobileLayout;
    private View amenityInfo;
    private LinearLayout containera;
    int screenHeight = 0;
    private GestureDetectorCompat mDetector;
    Animation slideUpAnimation, slideDownAnimation;
    private static final int SWIPE_MIN_DISTANCE = 80;
    private static final int SWIPE_MAX_OFF_PATH = 60;
    private static final int SWIPE_THRESHOLD_VELOCITY = 100;
    private int swipeValue = 0, size = 0;
    FilterParcel insightfilter;
    public static String selectedType = MainActivity.def_type;
    Marker previous_selected;
    private ToggleTabVisibilityListener toggleTabVisibilityListener;
    private FeatureCollection featureCollection;
    private String poiFeatureId = "";
    private boolean markerSelected = false, firstTimeLocationAccessed = false, appUpdateDialogShown = false;
    private boolean isinIndore;

    private class zoomobj {
        private double lat, lng, zoom;

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public double getZoom() {
            return zoom;
        }

        public void setZoom(double zoom) {
            this.zoom = zoom;
        }
    }

    //Location_plugin_variable
    private Location mylocation;
    private GoogleApiClient googleApiClient;
    private LocationComponent locationComponent;
    private PermissionsManager permissionsManager;
    private NavigationMapRoute navigationMapRoute;
    private LatLng originCoord;
    private LatLng destinationCoord;
    private Point originPosition;
    private Point destinationPosition;
    private DirectionsRoute currentRoute;
    private static final String TAG = "Map Fragment";
    private Button edit_btn;
    private ImageButton btnGPS, zoomtoextant, closebtn, expandinfo, attraction_close;

    private boolean iff_ondown = false, iff_onswipe = false;
    private FancyButton btnRoute;
    NestedScrollView scroll;
    private String editName, editLat, editLong, editSnippet;
    private Map<String, com.mapbox.mapboxsdk.annotations.Icon> tagMp_blue;
    private Map<String, com.mapbox.mapboxsdk.annotations.Icon> tagMp_orange;
    private Map<String, Integer> blueMarkers;
    private Map<String, Integer> orangeMarkers;
    ListView listView;
    private Map<LatLng, String> uniList;
    SearchView searchView;
    LinearLayout.LayoutParams llp;
    private SearchListAdapter searchListAdapter;
    MenuItem mSearchMenuItem;
    zoomobj zoomb = new zoomobj();
    private int actionBarHeight = 0;
    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
    );
    SymbolLayer selectedMarkerSymbolLayer;


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EditAmenityEvent event) {
        clickmarker(new LatLng(Double.parseDouble(event.lat), Double.parseDouble(event.longitude)), event.name);
    }

    ArrayList<Search> searches;
    boolean filterflag = false;
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
        mSearchMenuItem = menu.findItem(R.id.search);

        searchView = (SearchView) mSearchMenuItem.getActionView();
        searchView.setOnSearchClickListener(View ->
                listView.setVisibility(View.VISIBLE));
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
                if (!newText.isEmpty()) {
                    SearchListAdapter.searchText = newText;
                } else SearchListAdapter.searchText = "";
                newText = newText.toLowerCase();
                ArrayList<Search> search = new ArrayList<>();
                if (uniList.size() > 0) {
                    for (Map.Entry<LatLng, String> aitem : uniList.entrySet()) {
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
                    searchListAdapter.notifyDataSetChanged();
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
            searchView.clearFocus();
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Mapbox.getInstance(getContext(), getString(R.string.mapbox_access_token));

        View v = inflater.inflate(R.layout.map_fragment, container, false);
        mapScreen = v.findViewById(R.id.mapScreen);

        //to check and inform users if there is app update in the playstore
        //not show dialog if users clicks may be later option 3 times
        if (IndoreApp.db().getInt(Keys.DONT_SHOW_APP_UPDATE) <= 3)
            checkAppRelease();

        ViewTreeObserver vto = mapScreen.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                screenHeight = mapScreen.getHeight();
                mapScreen.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        TypedValue tv = new TypedValue();

        if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics()) - 5;
        }
        detail_screen = v.findViewById(R.id.detailView);
        ImageButton close_btn = v.findViewById(R.id.close_btn);
        btnGPS = v.findViewById(R.id.gps_btn);
        zoomtoextant = v.findViewById(R.id.zoom_fix);
        btnGPS.setOnClickListener(view -> {
            if (mylocation != null)
                if (checkIfIsInIndore(mylocation))
                    showToastMessage("Your gps has already been enabled!");
                else
                    showToastMessage("Sorry, You are out of Indore!");
            else
                checkLocationPermission();
        });
        zoomb.setLat(22.7203851);
        zoomb.setLng(75.8682103);
        zoomb.setZoom(9.3);
        zoomtoextant.setEnabled(false);
        zoomtoextant.setOnClickListener(View -> {
            style.removeLayer(SELECTED_MARKER_LAYER);
            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(zoomb.getLat(), zoomb.getLng()), zoomb.getZoom()), 600);
        });
        LayoutInflater inflate_info = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mapView = v.findViewById(R.id.mapView);
        mapView.setLayoutParams(params);
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

        setUpNewMarkerIcons();
        btnRoute = v.findViewById(R.id.startButton);
        if (getArguments() != null) {
            FilterParcel filterdata = getArguments().getParcelable("FilterValue");
            selectedType = getArguments().getString("selectedType", "attractions");
        }
        detailbool = true;
        amenityInfo = inflate_info.inflate(R.layout.detailview, null);
        websiteLayout = amenityInfo.findViewById(R.id.websiteLayout);
        emailLayout = amenityInfo.findViewById(R.id.emailLayout);
        mobileLayout = amenityInfo.findViewById(R.id.mobileLayout);
        containera = amenityInfo.findViewById(R.id.detailLayout);
        closebtn = amenityInfo.findViewById(R.id.btn_close);
        detailEnglishTitle = amenityInfo.findViewById(R.id.txt_detail_enname);
        detailNepaliTitle = amenityInfo.findViewById(R.id.txt_detail_nename);
        detailPhone = amenityInfo.findViewById(R.id.txt_detail_phone);
        detailMobile = amenityInfo.findViewById(R.id.txt_detail_mobile);
        detailWeb = amenityInfo.findViewById(R.id.txt_detail_web);
        detailMail = amenityInfo.findViewById(R.id.txt_detail_email);
        edit_btn = amenityInfo.findViewById(R.id.edit_btn);
        boolean Auth = MainActivity.mSharedPref.getBoolean(LoginActivity.AUTHENTICATED, false);
        if (Auth) {
            edit_btn.setVisibility(View.VISIBLE);
        } else {
            edit_btn.setVisibility(View.GONE);
        }
        edit_btn.setOnClickListener(view -> {
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
            toggleTabVisibilityListener.showTabs();
            editAmenity(selectedType);
        });
        closebtn.setOnClickListener(view -> {
            swipeValue = 0;
            style.removeLayer(SELECTED_MARKER_LAYER);// to change red icon to blue
            lm.setLayoutParams(lp_shrink);
            small_info.setVisibility(View.VISIBLE);
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
            toggleTabVisibilityListener.showTabs();
            lm.setVisibility(View.GONE);
            if (navigationMapRoute != null) navigationMapRoute.removeRoute();
            detail_screen.removeAllViews();
        });

        lm.setOnTouchListener((view, motionEvent) -> {
            mDetector.onTouchEvent(motionEvent);
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
        });
        expandinfo.setOnClickListener(view -> {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            toggleTabVisibilityListener.hideTabs();
            small_info.setVisibility(View.GONE);
            lm.setLayoutParams(lp_expand);
            detail_screen.removeAllViews();
            detailView(detailbool);
            detail_screen.addView(amenityInfo);
        });

        close_btn.setOnClickListener(view -> {
                    swipeValue = 0;
                    style.removeLayer(SELECTED_MARKER_LAYER);//to change red icon to blue
                    if (navigationMapRoute != null) {
                        navigationMapRoute.removeRoute();
                    }
                    lm.setLayoutParams(lp_shrink);
                    small_info.setVisibility(View.VISIBLE);
                    ((AppCompatActivity) getActivity()).getSupportActionBar().show();
                    toggleTabVisibilityListener.showTabs();
                    lm.setVisibility(View.GONE);
                    detail_screen.removeAllViews();
                }
        );

        btnRoute.setOnClickListener(view ->
                performRouting());

        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                .include(new LatLng(22.8202, 76.0467)) // Northeast
                .include(new LatLng(22.6248, 75.7202)) // Southwest
                .build();

        mapView.getMapAsync(mapboxMap -> {
            MapFragment.this.mapboxMap = mapboxMap;
            mapboxMap.setStyle(getString(R.string.mapbox_style_mapbox_streets), style -> {
                MapFragment.this.style = style;
                mapboxMap.getUiSettings().setCompassMargins(0, actionBarHeight, 2, 0);
                mapboxMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 50));
                mapboxMap.setLatLngBoundsForCameraTarget(latLngBounds);
                mapboxMap.setMaxZoomPreference(18);
                mapboxMap.setMinZoomPreference(zoomb.getZoom());
                zoomtoextant.setEnabled(true);

                initCameraListener(style);
                style.addImageAsync(
                        SINGLE_EARTHQUAKE_TRIANGLE_ICON_ID,
                        BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(blueMarkers.get(selectedType))),
                        false
                );

                style.addImageAsync(
                        SELECTED_SINGLE_EARTHQUAKE_TRIANGLE_ICON_ID,
                        BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(orangeMarkers.get(selectedType))),
                        false
                );

                if (IndoreApp.db().getInt(Keys.AMENITY_SELECTED) == 2)
                    EventBus.getDefault().post(new ShowDownloadAllDialogEvent());

            });
        });
        listView = v.findViewById(R.id.listView);
        uniList = new HashMap<>();
        searches = new ArrayList<>();
        searchListAdapter = new SearchListAdapter(this.getContext(), searches);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Search selected = (Search) listView.getItemAtPosition(position);
            clickmarker(selected.cord, selected.name);
        });
        return v;
    }

    private void checkAppRelease() {
        AppUpdaterUtils appUpdaterUtils = new AppUpdaterUtils(getContext())
                .setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
                .withListener(new AppUpdaterUtils.UpdateListener() {
                    @Override
                    public void onSuccess(Update update, Boolean isUpdateAvailable) {
                        if (isUpdateAvailable)
                            if (!appUpdateDialogShown)
                                showUpdateDialog(update.getLatestVersion());
                    }

                    @Override
                    public void onFailed(AppUpdaterError error) {
                    }
                });
        appUpdaterUtils.start();
    }

    private void showUpdateDialog(String latestVersion) {
        new AppUpdater(getContext())
                .setContentOnUpdateAvailable("Version " + latestVersion + " is available. Download the latest version and you will get latest features, improvements and bug fixes")
                .setButtonUpdate("Update now?")
                .setButtonUpdateClickListener((dialog, which) ->
                        openAppStore())
                .setButtonDismiss("Maybe later")
                .setButtonDismissClickListener((dialog, which) -> {
                    IndoreApp.db().putInt(Keys.DONT_SHOW_APP_UPDATE, IndoreApp.db().getInt(Keys.DONT_SHOW_APP_UPDATE) + 1);
                    dialog.dismiss();
                })
                .setCancelable(false)
                .setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
                .setDisplay(Display.DIALOG)
                .showAppUpdated(true)
                .start();
        appUpdateDialogShown = true;
    }

    private void openAppStore() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=org.kathmandulivinglabs.exploreindore")));
    }


    private void showToastMessage(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();

    }

    private void performRouting() {
        if (btnRoute.getText().toString().equalsIgnoreCase("Route")) {
            if (mylocation != null) {
                originCoord = new LatLng(mylocation.getLatitude(), mylocation.getLongitude());
                originPosition = Point.fromLngLat(originCoord.getLongitude(), originCoord.getLatitude());
                if (isinIndore && destinationPosition != null) {
                    getRoute(originPosition, destinationPosition);
                    Toast.makeText(getContext(), "Please wait...", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(getContext(), "Sorry, You are out of Indore!", Toast.LENGTH_LONG).show();
            } else
                checkLocationPermission();
        } else {
            boolean simulateRoute = false;
            NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                    .directionsRoute(currentRoute)
                    .shouldSimulateRoute(simulateRoute)
                    .build();
            // Call this method with Context from within an Activity
            NavigationLauncher.startNavigation(getActivity(), options);
        }
    }

    public void setSnackbar(String msg) {
        new AlertDialog.Builder(getActivity())
                .setTitle("No Internet")
                .setIcon(R.drawable.ic_no_wifi)
                .setMessage(msg)
                .show();

    }

    private void getRoute(Point originPosition, Point destinationPosition) {
        NavigationRoute.builder(getActivity())
                .accessToken(Mapbox.getAccessToken())
                .origin(originPosition)
                .destination(destinationPosition)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        // You can get the generic HTTP info about the response
                        if (response.body() == null) {
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
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
                            btnRoute.setText("Navigate");
                            btnRoute.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                            Toast.makeText(getContext(), "Click the navigate button to start!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        setSnackbar("Please connect to the internet to get the route!");
                    }
                });
    }

    private void setUpNewMarkerIcons() {
        blueMarkers = new HashMap<>();
        blueMarkers.put("public_hospitals", R.drawable.blue_hospital);
        blueMarkers.put("private_hospitals", R.drawable.blue_hospital);
        blueMarkers.put("public_clinics", R.drawable.blue_ayush);
        blueMarkers.put("private_clinics", R.drawable.blue_clinic);
        blueMarkers.put("dentists", R.drawable.blue_dentist);
        blueMarkers.put("veterinaries", R.drawable.blue_veterinary);
        blueMarkers.put("patho_radio_labs", R.drawable.blue_lab);
        blueMarkers.put("anganwadi", R.drawable.blue_aaganwadi);
        blueMarkers.put("blood_banks", R.drawable.blue_bloodbank);
        blueMarkers.put("mental_health_centers", R.drawable.blue_mental_health);
        blueMarkers.put("bus_stops", R.drawable.blue_busstop);
        blueMarkers.put("atms", R.drawable.blue_atm);
        blueMarkers.put("public_washrooms", R.drawable.blue_washroom);
        blueMarkers.put("public_waste_bins", R.drawable.blue_wastebin);
        blueMarkers.put("fuel_stations", R.drawable.blue_fuel);
        blueMarkers.put("public_schools", R.drawable.blue_government_school);
        blueMarkers.put("private_schools", R.drawable.blue_private_school);
        blueMarkers.put("parks_playgrounds", R.drawable.blue_playground);
        blueMarkers.put("pharmacies", R.drawable.blue_pharmacy);

        orangeMarkers = new HashMap<>();
        orangeMarkers.put("public_hospitals", R.drawable.red_hospital);
        orangeMarkers.put("private_hospitals", R.drawable.red_hospital);
        orangeMarkers.put("public_clinics", R.drawable.red_clinic);
        orangeMarkers.put("private_clinics", R.drawable.red_clinic);
        orangeMarkers.put("dentists", R.drawable.red_dentist);
        orangeMarkers.put("veterinaries", R.drawable.red_veterinary);
        orangeMarkers.put("patho_radio_labs", R.drawable.red_lab);
        orangeMarkers.put("anganwadi", R.drawable.red_aaganwadi);
        orangeMarkers.put("blood_banks", R.drawable.red_bloodbank);
        orangeMarkers.put("mental_health_centers", R.drawable.red_mental_health);
        orangeMarkers.put("bus_stops", R.drawable.red_busstop);
        orangeMarkers.put("atms", R.drawable.red_atm);
        orangeMarkers.put("public_washrooms", R.drawable.red_washroom);
        orangeMarkers.put("public_waste_bins", R.drawable.red_wastebin);
        orangeMarkers.put("fuel_stations", R.drawable.red_fuel);
        orangeMarkers.put("public_schools", R.drawable.red_government_school);
        orangeMarkers.put("private_schools", R.drawable.red_private_school);
        orangeMarkers.put("parks_playgrounds", R.drawable.red_playground);
        orangeMarkers.put("pharmacies", R.drawable.red_pharmacy);
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

    public void detailView(boolean detailbool) {
        if (detailbool) {
            Realm realm = Realm.getDefaultInstance();


            RealmQuery<ExploreSchema> query = realm.where(ExploreSchema.class);
            ExploreSchema dbvalue = query
                    .equalTo("osm_id", poiFeatureId)
                    .findFirst();

            if (dbvalue != null && dbvalue.getTag_type() != null) {
                size = dbvalue.getTag_type().size();
            }
            realm.close();
            if (dbvalue != null) {
                detailEnglishTitle.setText(dbvalue.getName());
                detailNepaliTitle.setText(dbvalue.getNamein());
                if (dbvalue.getWeb() != null && !dbvalue.getWeb().isEmpty()) {
                    websiteLayout.setVisibility(View.VISIBLE);
                    detailWeb.setText(dbvalue.getWeb());
                    ExploreSchema finalDbvalue = dbvalue;
                    detailWeb.setOnClickListener(view -> {
                                String url = finalDbvalue.getWeb();
                                if (!url.startsWith("http://") && !url.startsWith("https://"))
                                    url = "http://" + url;
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(intent);
                            }
                    );
                } else {
                    websiteLayout.setVisibility(View.GONE);
                    detailWeb.setText("-");
                }
                if (dbvalue.getContact_email() != null && !dbvalue.getContact_email().isEmpty()) {
                    emailLayout.setVisibility(View.VISIBLE);
                    detailMail.setText(dbvalue.getContact_email());
                } else {
                    emailLayout.setVisibility(View.GONE);
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
                    String key = Utils.toTitleCase(keys.get(i).replace("_", " "));
                    if (key.contains("Mobile")) mob = label;
                    if (!key.equalsIgnoreCase("Name") && !key.equalsIgnoreCase("Name Hindi") && !key.equalsIgnoreCase("Phone Number") &&
                            !key.equalsIgnoreCase("Email Address") && !key.equalsIgnoreCase("Id") && !key.equalsIgnoreCase("Latitude") && !key.equalsIgnoreCase("Longitude")
                            && !key.equalsIgnoreCase("Precision") && !(key.equalsIgnoreCase("Mobile") || (key.equalsIgnoreCase("Mobile Number")))) {
                        view[i] = getLayoutInflater().inflate(R.layout.detailtextgenerator, containera, false);
                        detailTag[i] = view[i].findViewById(R.id.detail_type);
                        detailValue[i] = view[i].findViewById(R.id.detail_value);
                        detailTag[i].setText(key);
                        detailValue[i].setText(label);
                        containera.addView(view[i]);
                    }
                    i++;
                }
                if (mob != null && !mob.isEmpty()) {
                    mobileLayout.setVisibility(View.VISIBLE);
                    detailMobile.setText(mob);
                    detailMobile.setOnClickListener(view1 -> callIntent(detailMobile));
                } else
                    mobileLayout.setVisibility(View.GONE);

                if (dbvalue.getContact_phone() == null) {
                    detailPhone.setText("-");
                } else {
                    String pho = null;
                    if (dbvalue.getContact_phone() != null) pho = dbvalue.getContact_phone();
//                    if (mob != null) {
//                        if (pho != null)
//                            pho = pho + "," + mob;
//                        else pho = mob;
//                    }
                    detailPhone.setText(pho);
                    detailPhone.setOnClickListener(view1 -> callIntent(detailPhone));
                }
            }
        }

    }

    private void callIntent(TextView txtView) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + txtView.getText().toString().split(",")[0]));
        startActivity(intent);
    }

    public void setFilter(Boolean fp) {
        filterflag = fp;
        //mapView.refreshDrawableState();
        navigationMapRoute = null;
        FragmentTransaction ftr = getFragmentManager().beginTransaction();
        ftr.detach(MapFragment.this).attach(MapFragment.this).commit();
    }

    protected void initCameraListener(Style style) {
        try {
            new DrawIndoreGeoJson().execute();
            addItemsToClusterPlugin();
            List<LatLng> polygon = new ArrayList<>();
            Realm realm = Realm.getDefaultInstance();
            RealmResults<PokharaBoundary> wardResult = realm.where(PokharaBoundary.class).contains("tag", "all_boundary").findAll();
            realm.close();
            if (polygon.size() > 0) polygon.clear();
            polygon = new ArrayList<>();
            for (PokharaBoundary pbs : wardResult)
                polygon.add(new LatLng(pbs.getCoordinateslat(), pbs.getCoordinateslong()));

//            mapboxMap.setOnMarkerClickListener(marker -> {
//                Toast.makeText(getContext(), "Clicked", Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "initCameraListener: marker click");
//                markerclickAction(marker);
//                return true;
//            });

        } catch (Exception e) {
            Log.d(TAG, "initCameraListener: exception " + e.getMessage());
            e.printStackTrace();
        }
    }


    private class DrawIndoreGeoJson extends AsyncTask<Void, Void, List<LatLng>> {

        @Override
        protected List<LatLng> doInBackground(Void... voids) {
            ArrayList<LatLng> points = new ArrayList<>();

            try {
                InputStream inputStream = getContext().getAssets().open("Indore_City_Boundary.json");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
                StringBuilder stringBuilder = new StringBuilder();
                int cp;
                while ((cp = bufferedReader.read()) != -1) {
                    stringBuilder.append((char) cp);
                }
                inputStream.close();

                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                JSONArray features = jsonObject.getJSONArray("features");
                JSONObject feature = features.getJSONObject(0);
                JSONObject geometry = feature.getJSONObject("geometry");
                if (geometry != null) {
                    String type = geometry.getString("type");

                    if (!TextUtils.isEmpty(type) && type.equalsIgnoreCase("LineString")) {

                        JSONArray coordinates = geometry.getJSONArray("coordinates");
                        for (int i = 0; i < coordinates.length(); i++) {
                            JSONArray coordinate = coordinates.getJSONArray(i);
                            LatLng latLng = new LatLng(coordinate.getDouble(1), coordinate.getDouble(0));
                            points.add(latLng);
                        }
                    }
                }
            } catch (Exception exception) {
                Log.e(TAG, "GeoJSON: " + exception.toString());
            }

            return points;
        }

        @Override
        protected void onPostExecute(List<LatLng> points) {
            super.onPostExecute(points);
            indoreBound(points);
        }
    }

    private void indoreBound(List<LatLng> polygon) {
        List<LatLng> polygonbound = new ArrayList<>();
        polygonbound.add(new LatLng(21.647217065387817, 74.8443603515625));
        polygonbound.add(new LatLng(21.652322721683642, 76.90429687500001));
        polygonbound.add(new LatLng(23.609295317664735, 76.915283203125));
        polygonbound.add(new LatLng(23.629427267052417, 74.81689453125));
        polygonbound.add(new LatLng(21.647217065387817, 74.8443603515625));
        mapboxMap.addPolygon(new PolygonOptions()
                .addAll(polygonbound)
                .addHole(polygon)
                .alpha(0.5f)
                .fillColor(Color.parseColor("#000000")));
    }

    private void wardBound(List<LatLng> polygon) {
        List<LatLng> polygonbound = new ArrayList<>();
        //this points are boundary of indore to show wardbound in respect to indore
        polygonbound.add(new LatLng(22.8202, 76.0467));
        polygonbound.add(new LatLng(22.6248, 76.0467));
        polygonbound.add(new LatLng(22.6248, 75.7202));
        polygonbound.add(new LatLng(22.8202, 75.7202));
        polygonbound.add(new LatLng(22.8202, 76.0467));
        mapboxMap.addPolygon(new PolygonOptions()
                .addAll(polygonbound)
                .addHole(polygon)
                .alpha(0.5f)
                .fillColor(Color.parseColor("#000000")));
    }

    private void markerclickAction(Marker marker) {
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
                    for (Map.Entry<String, com.mapbox.mapboxsdk.annotations.Icon> entry : tagMp_blue.entrySet()) {
                        if (!selectedType.equals("attractions")) {
                            if (entry.getKey().equals(selectedType)) {
                                previous_selected.setIcon(entry.getValue());
                            }
                        } else if (entry.getKey().equals(MainActivity.def_type_category)) {
                            previous_selected.setIcon(entry.getValue());
                        }
                    }

                }
            }
            previous_selected = marker;
            for (Map.Entry<String, com.mapbox.mapboxsdk.annotations.Icon> entry : tagMp_orange.entrySet()) {
                if (!selectedType.equals("attractions")) {
                    if (entry.getKey().equals(selectedType)) {
                        marker.setIcon(entry.getValue());
                    }
                } else if (entry.getKey().equals(MainActivity.def_type_category)) {
                    marker.setIcon(entry.getValue());
                }
            }
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            toggleTabVisibilityListener.hideTabs();
            lm.setVisibility(View.VISIBLE);
            editName = marker.getTitle();
            editSnippet = marker.getSnippet();
            editLat = String.valueOf(marker.getPosition().getLatitude());
            editLong = String.valueOf(marker.getPosition().getLongitude());
            //swipeValue = 1;
            iff_ondown = false;
            iff_onswipe = false;
            //to keep teh color to blue/ colorPrimary
//            btnRoute.setBackgroundColor(getResources().getColor(R.color.tertiaryText));
            btnRoute.setText("Route");
            if (navigationMapRoute != null) navigationMapRoute.removeRoute();
            if (swipeValue == 1) {
                if (detailPhone != null)
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
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<ExploreSchema> query = realm.where(ExploreSchema.class).equalTo("tag", amenity);
        RealmList<ExploreSchema> querycollection = new RealmList<>();
        if (MainActivity.filter_param.size() > 0 && filterflag) {
            for (Map.Entry<String, String> filter_data : MainActivity.filter_param.entrySet()
            ) {
                try {
                    if ((filter_data.getKey().equals("wardid") && !filter_data.getValue().equals("all"))) {
                        RealmResults<Ward> wardR = realm.where(Ward.class).contains("osmID", filter_data.getValue()).findAll();
                        RealmList<PokharaBoundary> pbound = wardR.get(0).getBoundry();
                        polygon = new ArrayList<>();
                        double west = 0.0;
                        double east = 0.0;
                        double north = 0.0;
                        double south = 0.0;
                        double bbboxlat1 = 0, bbboxlat2 = 22.7225, bbboxlong1 = 0, bbboxlong2 = 75.88345;
                        double c1 = 0.0;
                        double c2 = 0.0;
                        int i = 0;

                        for (i = 0; i < pbound.size(); i++) {
                            PokharaBoundary pokharaBoundary = pbound.get(i);
                            if (i == 0) {
                                north = pokharaBoundary.getCoordinateslat();
                                south = pokharaBoundary.getCoordinateslat();
                                west = pokharaBoundary.getCoordinateslong();
                                east = pokharaBoundary.getCoordinateslong();
                            } else {
                                if (pokharaBoundary.getCoordinateslat() > north) {
                                    north = pokharaBoundary.getCoordinateslat();
                                } else if (pokharaBoundary.getCoordinateslat() < south) {
                                    south = pokharaBoundary.getCoordinateslat();
                                }
                                if (pokharaBoundary.getCoordinateslong() < west) {
                                    west = pokharaBoundary.getCoordinateslong();
                                } else if (pokharaBoundary.getCoordinateslong() > east) {
                                    east = pokharaBoundary.getCoordinateslong();
                                }
                            }


                            // Make the centroid
                            c1 = pbound.get(i).getCoordinateslat() + c1;
                            c2 = pbound.get(i).getCoordinateslong() + c2;
                            bbboxlat1 = pbound.get(i).getCoordinateslat() > bbboxlat1 ? pbound.get(i).getCoordinateslat() : bbboxlat1;
                            bbboxlong1 = pbound.get(i).getCoordinateslong() > bbboxlong1 ? pbound.get(i).getCoordinateslong() : bbboxlong1;
                            bbboxlat2 = pbound.get(i).getCoordinateslat() < bbboxlat2 ? pbound.get(i).getCoordinateslat() : bbboxlat2;
                            bbboxlong2 = pbound.get(i).getCoordinateslong() < bbboxlong2 ? pbound.get(i).getCoordinateslong() : bbboxlong2;
                            polygon.add(new LatLng(pbound.get(i).getCoordinateslat(), pbound.get(i).getCoordinateslong()));
                        }
                        c1 = c1 / i;
                        c2 = c2 / i;
                        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                                .include(new LatLng(north, east)) // Northeast
                                .include(new LatLng(south, west)) // Southwest
                                .build();
                        mapboxMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 50, 50, 50, -50));
                        zoomb.setLat(c1);
                        zoomb.setLng(c2);
                        zoomb.setZoom(13);
                        mapboxMap.setLatLngBoundsForCameraTarget(latLngBounds);
                        mapboxMap.setMinZoomPreference(10.5);
                        wardBound(polygon);
                        query.contains("ward_id", filter_data.getValue());
                    }
                    if (filter_data.getKey().endsWith("max")) {
                        String rangeMax = filter_data.getKey().split("max")[0];
                        int max = Integer.parseInt(filter_data.getValue());
                        query.beginGroup().lessThanOrEqualTo(rangeMax, max).or().isNull(rangeMax).endGroup();
                    } else if (filter_data.getKey().endsWith("min")) {
                        String rangeMin = filter_data.getKey().split("min")[0];
                        int min = Integer.parseInt(filter_data.getValue());
                        if (min > 0) query.greaterThanOrEqualTo(rangeMin, min);
                        else
                            query.beginGroup().greaterThanOrEqualTo(rangeMin, min).or().isNull(rangeMin).endGroup();
                    }

                } catch (Exception E) {
                    E.printStackTrace();
                }
            }
            querycollection.addAll(query.findAll());

            for (Map.Entry<String, String> filter_data : MainActivity.filter_param.entrySet()
            ) {
                try {
                    if (!filter_data.getKey().equals("wardid") && !filter_data.getKey().endsWith("max") && !filter_data.getKey().endsWith("min")) {
                        RealmList<ExploreSchema> qr = new RealmList<>();
                        qr.addAll(querycollection);
                        querycollection.clear();
                        for (ExploreSchema ep : qr
                        ) {
                            RealmList<String> key = ep.getTag_type();
                            RealmList<String> value = ep.getTag_lable();
                            int i = 0;

                            for (String str : key) {
                                if (str.equalsIgnoreCase("delivery_service")) {
                                    if (!value.get(i).equalsIgnoreCase("no") && str.equalsIgnoreCase(filter_data.getKey())) {
                                        querycollection.add(ep);
                                    }
                                } else if (value.get(i).equalsIgnoreCase(filter_data.getValue()) && str.equalsIgnoreCase(filter_data.getKey())) {
                                    querycollection.add(ep);
                                }
                                i++;
                            }

                        }
                    }
                } catch (Exception E) {
                    E.printStackTrace();
                }

            }
        } else {
            querycollection.addAll(query.findAll());
        }
        MainActivity.filter_param.clear();
        RealmList<ExploreSchema> results = querycollection;
        realm.close();
        setUpSearchData(results, amenity);
        //for clustering
        convertIntoPoiJSonForClustering(results);
        mapboxMap.addOnMapClickListener(point -> {
            Log.d(TAG, "onCreateView: on map click ");
            return handleClickIcon(mapboxMap.getProjection().toScreenLocation(point), point);
        });
    }

    private void setUpSearchData(RealmList<ExploreSchema> results, String amenity) {
        if (searches.size() > 0) {
            searches.clear();
            uniList.clear();
        }
        for (int i = 0; i < results.size(); i++) {
            String title;
            double lat = results.get(i).getCoordinateslong();
            double lng = results.get(i).getCoordinateslat();
            if (results.get(i).getName() != null) {
                title = results.get(i).getName();
                Search sh = new Search(null, null);
                sh.cord = new LatLng(lat, lng);
                sh.name = title;
                searches.add(sh);
                uniList.put(new LatLng(lat, lng), title);
            } else title = amenity;
        }
        listView.setAdapter(searchListAdapter);
        searchListAdapter.notifyDataSetChanged();
    }

    private void convertIntoPoiJSonForClustering(RealmList<ExploreSchema> results) {
        List<POIFeature> poiFeatures = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            ExploreSchema exploreSchema = results.get(i);
            POIFeature poiFeature = new POIFeature();
            POIFeatureProperty poiFeatureProperty = new POIFeatureProperty();
            poiFeature.id = String.valueOf(exploreSchema.getOsm_id());
            poiFeature.geometry = new POIGeometry(exploreSchema.getType(), new ArrayList<Double>() {
                {
                    add(exploreSchema.getCoordinateslat());
                    add(exploreSchema.getCoordinateslong());
                }
            });
            List<FeatureTag> tags = new ArrayList<>();
            tags.add(0, new FeatureTag("name", exploreSchema.getName()));
            poiFeatureProperty.tags = tags;
            poiFeature.type = "Feature";
            poiFeature.properties = poiFeatureProperty;
            poiFeatures.add(poiFeature);
        }
        POI poi = new POI();
        poi.type = "FeatureCollection";
        poi.features = poiFeatures;
        addClusteredGeoJsonSource(new Gson().toJson(poi));
    }

    private void addClusteredGeoJsonSource(String poi) {
        featureCollection = FeatureCollection.fromJson(poi);
        // Add a new source from the GeoJSON data and set the 'cluster' option to true.
        try {
            style.addSource(
                    new GeoJsonSource(EARTHQUAKE_SOURCE_ID,
                            FeatureCollection.fromJson(poi),
                            new GeoJsonOptions()
                                    .withCluster(true)
                                    .withClusterMaxZoom(14)
                                    .withClusterRadius(50)
                    )

            );
        } catch (NullPointerException uriSyntaxException) {
            Timber.e("Check the URL %s", uriSyntaxException.getMessage());
        }

        SymbolLayer unclusteredSymbolLayer = new SymbolLayer(UNCLUSTERED_POINTS, EARTHQUAKE_SOURCE_ID).withProperties(
                iconImage(SINGLE_EARTHQUAKE_TRIANGLE_ICON_ID),
                iconAllowOverlap(true),
                iconSize(0.8f)
        );

        //Creating a SymbolLayer icon layer for single data/icon points after cluster
        style.addLayer(unclusteredSymbolLayer);

        // Add the selected marker source and layer
        style.addSource(new GeoJsonSource(SELECTED_MARKER));
        addSelectedMarkersLayer();

        // Use the earthquakes GeoJSON source to create three layers: One layer for each cluster category.
        // Each point range gets a different fill color.
        int[][] layers = new int[][]{
                new int[]{150, ContextCompat.getColor(getActivity(), R.color.colorPrimary)},
                new int[]{20, ContextCompat.getColor(getActivity(), R.color.colorPrimary)},
                new int[]{0, ContextCompat.getColor(getActivity(), R.color.colorPrimary)}
        };

        for (int i = 0; i < layers.length; i++) {
            //Add clusters' circles
            CircleLayer circles = new CircleLayer("cluster-" + i, EARTHQUAKE_SOURCE_ID);
            circles.setProperties(
                    circleColor(layers[i][1]),
                    circleRadius(18f)
            );

            Expression pointCount = toNumber(get(POINT_COUNT));

            // Add a filter to the cluster layer that hides the circles based on "point_count"
            circles.setFilter(
                    i == 0
                            ? all(has(POINT_COUNT),
                            gte(pointCount, literal(layers[i][0]))
                    ) : all(has(POINT_COUNT),
                            gte(pointCount, literal(layers[i][0])),
                            lt(pointCount, literal(layers[i - 1][0]))
                    )
            );
            style.addLayer(circles);
        }

        //Add a SymbolLayer for the cluster data number point count
        style.addLayer(new SymbolLayer("count", EARTHQUAKE_SOURCE_ID).withProperties(
                textField(Expression.toString(get(POINT_COUNT))),
                textSize(12f),
                textColor(Color.WHITE),
                textIgnorePlacement(true),
                textAllowOverlap(true)
        ));
    }

    private void addSelectedMarkersLayer() {
        // Adding an offset so that the bottom of the blue icon gets fixed to the coordinate, rather than the
        // middle of the icon being fixed to the coordinate point.
        if (style.getLayer(SELECTED_MARKER_LAYER) == null)
            style.addLayer(new SymbolLayer(SELECTED_MARKER_LAYER, SELECTED_MARKER)
                    .withProperties(PropertyFactory
//                                    .iconImage(SELECTED_SINGLE_EARTHQUAKE_TRIANGLE_ICON_ID),
                                    .iconImage(step(zoom(), literal(SELECTED_SINGLE_EARTHQUAKE_TRIANGLE_ICON_ID),
                                            stop(zoomb.zoom < 14, SINGLE_EARTHQUAKE_TRIANGLE_ICON_ID))),
                            iconAllowOverlap(true),
                            iconSize(0.8f)));
    }


    @Override
    @SuppressWarnings({"MissingPermission"})
    public void onStart() {
        super.onStart();
        mapView.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.stopAutoManage(getActivity());
            googleApiClient.disconnect();
        }
        if (locationComponent != null)
            locationComponent.onStop();
        if (navigationMapRoute != null)
            navigationMapRoute.onStop();
        mapView.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (locationComponent != null)
            locationComponent.onDestroy();
        mapView.onDestroy();
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
            enableLocationComponent(mapboxMap.getStyle());
        } else {
            Toast.makeText(getActivity(), "user_location_permission_not_granted", Toast.LENGTH_LONG).show();
        }
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(getActivity())) {
            Toast.makeText(getContext(), "GPS is locating you!", Toast.LENGTH_SHORT).show();
            // Activate the MapboxMap LocationComponent to show user location
            // Adding in LocationComponentOptions is also an optional parameter
            locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(getActivity(), loadedMapStyle);
            locationComponent.setLocationComponentEnabled(true);
            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged: direction ");
        if (location != null) {
            mylocation = location;
            isinIndore = checkIfIsInIndore(mylocation);

            if (!firstTimeLocationAccessed) {
                firstTimeLocationAccessed = true;
                performRouting();
            }

        }
    }

    private boolean checkIfIsInIndore(Location mylocation) {
        if (mylocation.getLatitude() > 22.8202 || mylocation.getLatitude() < 22.6248
                || mylocation.getLongitude() > 76.0467 || mylocation.getLongitude() < 75.7202) {
            return false;
        } else
            return true;
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

    private boolean handleClickIcon(PointF toScreenLocation, LatLng point) {
        //this is required because we remove the layer in zoom to extent
        addSelectedMarkersLayer();
        double zoom = mapboxMap.getCameraPosition().zoom;
        Log.d(TAG, "handleClickIcon: style" + style+" marker "+markerSelected);
        if (style != null) {
            selectedMarkerSymbolLayer = (SymbolLayer) style.getLayer(SELECTED_MARKER_LAYER);
            List<Feature> features = mapboxMap.queryRenderedFeatures(toScreenLocation, UNCLUSTERED_POINTS);
            List<Feature> selectedFeature = mapboxMap.queryRenderedFeatures(
                    toScreenLocation, SELECTED_MARKER_LAYER);

            if (selectedFeature.size() > 0 && markerSelected) {
//                return false;
            }

            if (features.isEmpty()) {
                if (markerSelected) {
                    deselectMarker(selectedMarkerSymbolLayer);
                }
                return false;
            }

            GeoJsonSource source = style.getSourceAs(SELECTED_MARKER);
            if (source != null) {
                source.setGeoJson(FeatureCollection.fromFeatures(
                        new Feature[]{Feature.fromGeometry(features.get(0).geometry())}));
            }

            if (markerSelected) {
                deselectMarker(selectedMarkerSymbolLayer);
            }
            Log.d(TAG, "handleClickIcon: " + features);

            if (!features.isEmpty()) {
                if (zoom < 10)
                    mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 12), 600);
                else if (zoom < 14)
                    mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 14), 500);
                else if (zoom < 16)
                    mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, zoom + 1), 400);
                else if (zoom < 18)
                    mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, zoom + 1), 300);

                String name = features.get(0).id();
                if (features.get(0).getBooleanProperty("cluster") != null)
                    //to handle first time auto selection of marker
                    deselectMarker(selectedMarkerSymbolLayer);
                List<Feature> featureList = featureCollection.features();
                for (Feature feature : featureList) {
                    if (feature.id().equals(name)) {
                        selectMarker(selectedMarkerSymbolLayer);
                        try {
                            showDetailsOfthePoint(point, name, feature);
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
        return true;
    }

    private void selectMarker(SymbolLayer selectedMarkerSymbolLayer) {
        selectedMarkerSymbolLayer.setProperties(
                PropertyFactory.iconImage(SELECTED_SINGLE_EARTHQUAKE_TRIANGLE_ICON_ID)
        );
        markerSelected = true;
    }

    private void deselectMarker(SymbolLayer selectedMarkerSymbolLayer) {
        selectedMarkerSymbolLayer.setProperties(
                PropertyFactory.iconImage(SINGLE_EARTHQUAKE_TRIANGLE_ICON_ID)
        );
        markerSelected = false;
    }

    private void showDetailsOfthePoint(LatLng point, String name, Feature feature) {
        poiFeatureId = name;
        POIGeometry geometry = new Gson().fromJson(feature.geometry().toJson(), POIGeometry.class);
        String markerText = feature.properties().getAsJsonArray("tags").get(0).getAsJsonObject().get("value").getAsString();
        testText.setText(markerText);

        destinationPosition = Point.fromLngLat(geometry.coordinates.get(0), geometry.coordinates.get(1));
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        toggleTabVisibilityListener.hideTabs();
        lm.setVisibility(View.VISIBLE);
        editName = "name";
        editSnippet = "Snippet";
        editLat = String.valueOf(geometry.coordinates.get(1));
        editLong = String.valueOf(geometry.coordinates.get(0));
        //swipeValue = 1;
        iff_ondown = false;
        iff_onswipe = false;
        btnRoute.setText("Route");
        if (navigationMapRoute != null) navigationMapRoute.removeRoute();
        if (swipeValue == 1) {
            if (detailPhone != null)
                detailPhone.setClickable(false);
            detail_screen.removeAllViews();
            detailView(detailbool);
            detail_screen.addView(amenityInfo);
        }
    }

    private synchronized void setUpGClient() {
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected: direction");
        btnGPS.setImageResource(0);
        btnGPS.setImageResource(R.drawable.ic_action_gps_fixed);
        btnGPS.setTag("gps_fixed");
        getMyLocation();
    }

    private void getMyLocation() {
        Log.d(TAG, "getMyLocation: direction");
        if (googleApiClient != null) {
            if (googleApiClient.isConnected()) {
                int permissionLocation = checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mylocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(3000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(googleApiClient, locationRequest, this);
                    PendingResult result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(googleApiClient, builder.build());
                    result.setResultCallback(result1 -> {
                        final Status status = result1.getStatus();
                        switch (status.getStatusCode()) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Location settings are not satisfied.
                                // But could be fixed by showing the user a dialog.
                                try {
                                    // Show the dialog by calling startResolutionForResult(),
                                    status.startResolutionForResult(getActivity(),
                                            Keys.GPS_REQUEST);
                                } catch (IntentSender.SendIntentException e) {
                                    // Ignore the error.
                                }
                                break;
                            case LocationSettingsStatusCodes.SUCCESS:
                                // All location settings are satisfied.
                                // You can initialize location requests here.
                                int permissionLocation1 = checkSelfPermission(getActivity(),
                                        Manifest.permission.ACCESS_FINE_LOCATION);
                                if (permissionLocation1 == PackageManager.PERMISSION_GRANTED) {
                                    mylocation = LocationServices.FusedLocationApi
                                            .getLastLocation(googleApiClient);
                                }
                                Toast.makeText(getContext(), "Please wait...", Toast.LENGTH_LONG).show();
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                // Location settings are not satisfied. However, we have no way to fix the
                                // settings so we won't show the dialog.
                                //finish();
                                break;
                        }
                    });
                }
            } else googleApiClient.connect();
        } else setUpGClient();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: direction " + resultCode + " request " + requestCode);
        switch (requestCode) {
            case Keys.GPS_REQUEST:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getContext(), "Please wait the GPS is locating you", Toast.LENGTH_LONG).show();
                        getMyLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getActivity(), "Connection Failed", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Do whatever you need
        //You can display a message here
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getActivity(), "Connection Failed. Please try again later!", Toast.LENGTH_SHORT).show();
    }

    //edit
    private void editAmenity(String amenity) {
        Intent i = new Intent(getContext(), EditDialogActivity.class);
        String[] editInfo = {amenity, editName, editLat, editLong};
        i.putExtra("amenity", editInfo);
        startActivity(i);
    }

    public void checkLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d(TAG, "checkLocationPermission: direction marshmello");
            if (checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.ACCESS_FINE_LOCATION)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    new AlertDialog.Builder(getContext())
                            .setTitle(R.string.title_location_permission)
                            .setMessage(R.string.text_location_permission)
                            .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                                //Prompt the user once explanation has been shown
                                requestPermissions(
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            })
                            .create()
                            .show();


                } else {
                    // No explanation needed, we can request the permission.
                    requestPermissions(
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                }
            } else getMyLocation();
        } else getMyLocation();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //means location has not been accessed before so is permission requested
                    firstTimeLocationAccessed = false;
                    getMyLocation();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    @Override
    public boolean onBackPressed() {
        final boolean[] b = {true};
        if ((detail_screen != null && detail_screen.getChildCount() > 0) || (lm.getVisibility() == View.VISIBLE)) {
            swipeValue = 0;
            lm.setLayoutParams(llp);
            small_info.setVisibility(View.VISIBLE);
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
            toggleTabVisibilityListener.showTabs();
            lm.setVisibility(View.GONE);
            if (navigationMapRoute != null) navigationMapRoute.removeRoute();
            style.removeLayer(SELECTED_MARKER_LAYER); //to change red icon to blue
            detail_screen.removeAllViews();
            if (mSearchMenuItem != null)
                mSearchMenuItem.collapseActionView(); //because search view still remains there of not collapsed
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    getActivity());
            alertDialog.setTitle("");
            alertDialog.setMessage("Are you sure you want to exit?");

            alertDialog.setPositiveButton("YES",
                    (dialog, which) -> {
                        Objects.requireNonNull(getActivity()).finish();
                        getActivity().finishAffinity();
                        System.exit(0);
                        b[0] = false;
                    });

            alertDialog.setNegativeButton("NO",
                    (dialog, which) -> b[0] = true);

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

}



