package org.kathmandulivinglabs.exploreindore.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.kathmandulivinglabs.exploreindore.R;

public class AboutActivity extends AppCompatActivity {
    String amenitySelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle("About Us");
        amenitySelected = getIntent().getStringExtra("amenityType");
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("about",amenitySelected);
        Log.wtf("amenityAbout", amenitySelected);
        startActivity(i);
        finish();
    }
}
