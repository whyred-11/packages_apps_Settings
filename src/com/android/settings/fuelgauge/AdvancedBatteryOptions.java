/*
 * Copyright (C) 2019-2020 ion
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.settings.fuelgauge;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchIndexableResource;
import android.provider.Settings;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.SwitchPreference;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;
import com.android.settingslib.search.SearchIndexable;

import com.android.settings.R;
import com.ion.ionizer.preferences.SystemSettingMasterSwitchPreference;

import java.util.ArrayList;
import java.util.List;

@SearchIndexable
public class AdvancedBatteryOptions extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener, Indexable {

    public static final String TAG = "AdvancedBatteryOptions";

    private static final String SMART_PIXELS_ENABLED = "smart_pixels_enable";
    private static final String SMART_CHARGING = "smart_charging";

    private SystemSettingMasterSwitchPreference mSmartPixelsEnabled;
    private SystemSettingMasterSwitchPreference mSmartChargeEnabled;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.advanced_battery_options);
        ContentResolver resolver = getActivity().getContentResolver();

        mSmartPixelsEnabled = (SystemSettingMasterSwitchPreference) findPreference(SMART_PIXELS_ENABLED);
        mSmartPixelsEnabled.setOnPreferenceChangeListener(this);
        mSmartChargeEnabled = (SystemSettingMasterSwitchPreference) findPreference(SMART_CHARGING);
        mSmartChargeEnabled.setOnPreferenceChangeListener(this);
        updatePreferences();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mSmartPixelsEnabled) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getContentResolver(),
		            SMART_PIXELS_ENABLED, value ? 1 : 0);
            return true;
        } else if (preference == mSmartChargeEnabled) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getContentResolver(),
		            SMART_CHARGING, value ? 1 : 0);
            return true;
        }
        return false;
    }

    private void updatePreferences() {
        int smartPixelsEnabled = Settings.System.getInt(getContentResolver(),
                SMART_PIXELS_ENABLED, 0);
        mSmartPixelsEnabled.setChecked(smartPixelsEnabled != 0);
        if (!getResources().getBoolean(com.android.internal.R.bool.config_enableSmartPixels)) {
            getPreferenceScreen().removePreference(mSmartPixelsEnabled);
        }
        int smartChargeEnabled = Settings.System.getInt(getContentResolver(),
                SMART_CHARGING, 0);
        mSmartChargeEnabled.setChecked(smartChargeEnabled != 0);
        if (!getResources().getBoolean(com.android.internal.R.bool.config_deviceSupportSmartCharging)) {
            getPreferenceScreen().removePreference(mSmartChargeEnabled);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        updatePreferences();
    }

    @Override
    public void onResume() {
        super.onResume();

        updatePreferences();
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.ION_IONIZER;
    }

    public static final Indexable.SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {
                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                        boolean enabled) {
                    ArrayList<SearchIndexableResource> result =
                            new ArrayList<SearchIndexableResource>();
                    SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.advanced_battery_options;
                    result.add(sir);
                    return result;
                }
                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    ArrayList<String> result = new ArrayList<String>();
                    return result;
                }
    };
}
