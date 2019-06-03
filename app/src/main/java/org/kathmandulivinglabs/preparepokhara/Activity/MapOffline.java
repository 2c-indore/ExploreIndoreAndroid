package org.kathmandulivinglabs.preparepokhara.Activity;

import android.app.Notification;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineRegionError;
import com.mapbox.mapboxsdk.offline.OfflineRegionStatus;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;

import org.json.JSONObject;
import org.kathmandulivinglabs.preparepokhara.Helper.Connectivity;
import org.kathmandulivinglabs.preparepokhara.R;

/**
 * Created by Bhawak on 4/20/18.
 */

public class MapOffline extends AppCompatActivity {
    private static final String TAG = "SimpOfflineMapActivity";

    private boolean isEndNotified;
    private ProgressBar progressBar;
    //private MapView mapView;
    private OfflineManager offlineManager;
    private MapboxMap mapboxMap;
    // JSON encoding/decoding
    public static final String JSON_CHARSET = "UTF-8";
    public static final String JSON_FIELD_REGION_NAME = "FIELD_REGION_NAME";

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        //viewPager.removeAllViews();
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, "pk.eyJ1IjoiYmhhd2FrIiwiYSI6ImNpeHNrNnV2bzAwMDQzMnQ5c3lkb2l2cHYifQ.Qwso9ek7guU34YhFVnwiOw");

        // This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_offlinemap);

//        mapView = (MapView) findViewById(R.id.mapView_offline);
//        mapView.onCreate(savedInstanceState);
//        mapView.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(MapboxMap mapboxMap) {
//                Log.d("inside Offline Manager","Async_op");
        // Set up the OfflineManager
        offlineManager = OfflineManager.getInstance(MapOffline.this);
        //deletemapIfExist();
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        if (offlineManager != null) {
            offlineManager.listOfflineRegions(new OfflineManager.ListOfflineRegionsCallback() {
                @Override
                public void onList(OfflineRegion[] offlineRegions) {
                    if (offlineRegions.length == 0) {
                       createOfflineRegion();
                    }
                }

                @Override
                public void onError(String error) {
                    Log.e(TAG, "onListError: " + error);
                }
            });
        }
    }

    // Create the region asynchronously
    private void createOfflineRegion() {
        // Create a bounding box for the offline region
        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                .include(new LatLng(28.31285, 84.14949)) // Northeast
                .include(new LatLng(28.11532, 83.84905)) // Southwest
                .build();
        // Define the offline region
        OfflineTilePyramidRegionDefinition definition = new OfflineTilePyramidRegionDefinition(
                "mapbox://styles/mapbox/streets-v10",
                latLngBounds,
                11,
                16,
                MapOffline.this.getResources().getDisplayMetrics().density);

        // Set the metadata
        byte[] metadata;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(JSON_FIELD_REGION_NAME, "Pokhara");
            String json = jsonObject.toString();
            metadata = json.getBytes(JSON_CHARSET);
        } catch (Exception exception) {
            Log.e(TAG, "Failed to encode metadata: " + exception.getMessage());
            metadata = null;
        }
        offlineManager.createOfflineRegion(
                definition,
                metadata,
                new OfflineManager.CreateOfflineRegionCallback() {
                    @Override
                    public void onCreate(OfflineRegion offlineRegion) {
                        offlineRegion.setDownloadState(OfflineRegion.STATE_ACTIVE);

                        // Display the download progress bar
                        startProgress();

                        // Monitor the download progress using setObserver
                        offlineRegion.setObserver(new OfflineRegion.OfflineRegionObserver() {
                            @Override
                            public void onStatusChanged(OfflineRegionStatus status) {

                                // Calculate the download percentage and update the progress bar
                                double percentage = status.getRequiredResourceCount() >= 0
                                        ? (100.0 * status.getCompletedResourceCount() / status.getRequiredResourceCount()) :
                                        0.0;
                                Log.d("progress", String.valueOf(percentage));

                                if (status.isComplete()) {
                                    // Download complete
                                    endProgress("Download Complete");
                                } else if (status.isRequiredResourceCountPrecise()) {
                                    // Switch to determinate state
                                    setPercentage((int) Math.round(percentage));
                                }
                            }

                            @Override
                            public void onError(OfflineRegionError error) {
                                // If an error occurs, print to logcat
                                Log.e(TAG, "onError reason: " + error.getReason());
                                Log.e(TAG, "onError message: " + error.getMessage());
                            }

                            @Override
                            public void mapboxTileCountLimitExceeded(long limit) {
                                // Notify if offline region exceeds maximum tile count
                                Log.e(TAG, "Mapbox tile count limit exceeded: " + limit);
                            }
                        });
                    }

                    @Override
                    public void onError(String error) {
                        Log.e(TAG, "Error: " + error);
                    }
                });
    }
    private void deletemapIfExist() {
        if (offlineManager != null) {
            offlineManager.listOfflineRegions(new OfflineManager.ListOfflineRegionsCallback() {
                @Override
                public void onList(OfflineRegion[] offlineRegions) {
                    if (offlineRegions.length > 0) {
                        offlineRegions[(offlineRegions.length - 1)].delete(new OfflineRegion.OfflineRegionDeleteCallback() {
                            @Override
                            public void onDelete() {
                                Toast.makeText(
                                        MapOffline.this,
                                        "Map Deleted",
                                        Toast.LENGTH_LONG
                                ).show();
                            }

                            @Override
                            public void onError(String error) {
                                Log.e(TAG, "On Delete error: " + error);
                            }
                        });
                    }
                }

                @Override
                public void onError(String error) {
                    Log.e(TAG, "onListError: " + error);
                }
            });
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        //mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
       // mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        //mapView.onPause();
        if (offlineManager != null) {
            if(Connectivity.isConnected(this)) {
                if (Connectivity.isConnectedWifi(this)) {
                    moveTaskToBack(true);
                }
                else if(Connectivity.isConnectedMobile(this)){
                  deletemapIfExist();
                }
            }
//            offlineManager.listOfflineRegions(new OfflineManager.ListOfflineRegionsCallback() {
//                @Override
//                public void onList(OfflineRegion[] offlineRegions) {
//                    if (offlineRegions.length > 0) {
//                        offlineRegions[(offlineRegions.length - 1)].delete(new OfflineRegion.OfflineRegionDeleteCallback() {
//                            @Override
//                            public void onDelete() {
//                                Toast.makeText(
//                                        MapOffline.this,
//                                        "Map Deleted",
//                                        Toast.LENGTH_LONG
//                                ).show();
//                            }
//
//                            @Override
//                            public void onError(String error) {
//                                Log.e(TAG, "On Delete error: " + error);
//                            }
//                        });
//                    }
//                }
//
//                @Override
//                public void onError(String error) {
//                    Log.e(TAG, "onListError: " + error);
//                }
//            });
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
       // mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //mapView.onSaveInstanceState(outState);
    }

    // Progress bar methods
    private void startProgress() {
        // Start and show the progress bar
        isEndNotified = false;
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void setPercentage(final int percentage) {
        progressBar.setIndeterminate(false);
        progressBar.setProgress(percentage);
    }

    private void endProgress(final String message) {
        // Don't notify more than once
        if (isEndNotified) {
            return;
        }

        // Stop and hide the progress bar
        isEndNotified = true;
        progressBar.setIndeterminate(false);
        progressBar.setVisibility(View.GONE);

        // Show a toast
        Toast.makeText(MapOffline.this, message, Toast.LENGTH_LONG).show();
    }
}
