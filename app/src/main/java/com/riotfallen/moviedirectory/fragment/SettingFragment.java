package com.riotfallen.moviedirectory.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.provider.Settings;
import android.support.v4.app.Fragment;

import com.riotfallen.moviedirectory.R;
import com.riotfallen.moviedirectory.reminder.DailyAlarmReceiver;
import com.riotfallen.moviedirectory.reminder.ReleaseAlarmReceiver;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    private DailyAlarmReceiver dailyAlarmReceiver = new DailyAlarmReceiver();
    private ReleaseAlarmReceiver releaseAlarmReceiver = new ReleaseAlarmReceiver();



    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        addPreferencesFromResource(R.xml.setting_preference);

        String dailyReminder = getString(R.string.pref_daily_reminder);
        String realeaseReminder = getString(R.string.pref_release_reminder);
        String localizationPreference = getString(R.string.pref_language);


        findPreference(dailyReminder).setOnPreferenceChangeListener(this);
        findPreference(realeaseReminder).setOnPreferenceChangeListener(this);
        findPreference(localizationPreference).setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String key = preference.getKey();
        boolean isOn = (boolean) newValue;

        if (key.equals(getString(R.string.pref_daily_reminder))) {
            if (isOn) {
                dailyAlarmReceiver.setAlarm(getActivity(),
                        getString(R.string.daily_content_text));
            } else {
                dailyAlarmReceiver.cancelAlarm(getActivity());
            }
            return true;
        } else {
            if(isOn){
                releaseAlarmReceiver.setAlarm(getActivity(),
                        getString(R.string.notif_new_movie));
            } else {
                releaseAlarmReceiver.cancelAlarm(getActivity());
            }
            return true;
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
        startActivity(intent);
        return true;
    }
}
