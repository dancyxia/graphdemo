package com.dancy.graphdemo.pref;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;

import com.dancy.graphdemo.Graph;
import com.dancy.graphdemo.R;

/**
 * Created by dancy on 6/12/2015.
 */
public class SettingActivity extends Activity  {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().add(android.R.id.content, new SettingsFragment()).commit();
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }

}
