package com.dancy.graphdemo.pref;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import com.dancy.graphdemo.Graph;
import com.dancy.graphdemo.R;

/**
 * Created by dancy on 6/12/2015.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        PreferenceScreen ps = getPreferenceScreen();
        for (int i = 0; i < ps.getPreferenceCount(); i++) {
            Preference p = ps.getPreference(i);
            initSummary(p);
        }

        Preference p = ps.findPreference(getResources().getString(R.string.pref_graphnodecount));
        if (p != null) {
            p.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String sVal = (String)newValue;
                    StringBuilder sb = new StringBuilder();
                    try {
                        int val = Integer.parseInt(sVal);
                        if (val < Graph.MINNODENUM || val > Graph.MAXNODENUM) {
                            sb.append("The value should be in the scope of ").append(Graph.MINNODENUM).append(" and ").append(Graph.MAXNODENUM);
                        }
                    } catch (NumberFormatException e) {
                        sb.append(e.getMessage()).append("\n");
                    }
                    if (sb.length() > 0) {
                        new AlertDialog.Builder(getActivity()).setTitle("Error").setMessage(sb.toString()).setPositiveButton(android.R.string.ok, null).show();
                        return false;
                    }
                    return true;
                }
            });
        }
    }

    private void initSummary(Preference p) {
        //TODO: handle preferenceCategory
        updatePreference(p);
    }

    private void updatePreference(Preference p) {
        if (p instanceof EditTextPreference) {
            ((EditTextPreference)p).setSummary(((EditTextPreference) p).getText());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference p = findPreference(key);
        if (p instanceof EditTextPreference) {
            ((EditTextPreference)p).setSummary(((EditTextPreference) p).getText());
        }
    }
}
