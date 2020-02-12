package org.kathmandulivinglabs.exploreindore.Activity;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import org.kathmandulivinglabs.exploreindore.R;

public class AboutActivity extends AppCompatActivity {
    String amenitySelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle("About Us");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        amenitySelected = getIntent().getStringExtra("amenityType");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("about", amenitySelected);
        Log.wtf("amenityAbout", amenitySelected);
        startActivity(i);
        finish();
    }
}
