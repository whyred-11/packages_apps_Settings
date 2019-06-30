/*
 * Copyright (C) 2019 ion
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
package com.android.settings.battery;

import android.app.Activity;
import android.content.Context;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.SwitchPreference;
import android.provider.Settings;

import com.android.settings.R;
import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.util.ion.IonUtils;
import com.android.settings.SettingsPreferenceFragment;

import java.util.Arrays;
import java.util.HashSet;

public class AdvancedBatteryOptions extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    private static final String BATTERY_ESTIMATE_POSITION_TYPE = "battery_estimate_position";
    private ListPreference mEstimatePositionType;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.advanced_battery_options);

        ContentResolver resolver = getActivity().getContentResolver();

        // battery estimate position
        mEstimatePositionType = (ListPreference) findPreference(BATTERY_ESTIMATE_POSITION_TYPE);
        int type = Settings.System.getInt(resolver,
                Settings.System.BATTERY_ESTIMATE_POSITION, 0);
        mEstimatePositionType.setValue(String.valueOf(type));
        mEstimatePositionType.setSummary(mEstimatePositionType.getEntry());
        mEstimatePositionType.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (preference == mEstimatePositionType) {
            int type = Integer.valueOf((String) objValue);
            int index = mEstimatePositionType.findIndexOfValue((String) objValue);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.BATTERY_ESTIMATE_POSITION, type);
            mEstimatePositionType.setSummary(mEstimatePositionType.getEntries()[index]);
            IonUtils.showSystemUiRestartDialog(getContext());
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.ION_IONIZER;
    }
}
