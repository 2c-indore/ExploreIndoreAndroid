package org.kathmandulivinglabs.preparepokhara.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.kathmandulivinglabs.preparepokhara.R;

public class AboutActivity extends AppCompatActivity {
    String[] amenitySelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle("About Us");
        amenitySelected = getIntent().getStringArrayExtra("amenityType");
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("about",amenitySelected);
        startActivity(i);
        finish();
    }
}
