package org.kathmandulivinglabs.preparepokhara.Activity;

import android.os.AsyncTask;

import io.realm.Realm;

public class DataManager extends AsyncTask<String,Void,String> {

    @Override
    protected String doInBackground(String... strings) {
        Realm realm =Realm.getDefaultInstance();
        try {

        } finally {
            realm.close();
        }
        return null;
    }
}
