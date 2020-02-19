package org.kathmandulivinglabs.exploreindore;

import android.app.Application;
import android.util.Log;

import org.kathmandulivinglabs.exploreindore.Application.TinyDB;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class IndoreApp extends Application {
    private static TinyDB tinyDB;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().name("pokhara.realm").
                schemaVersion(0).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfiguration);

        //tiny db config
        tinyDB = new TinyDB(getApplicationContext());
    }

    public static TinyDB db() {
        return tinyDB;
    }
}
