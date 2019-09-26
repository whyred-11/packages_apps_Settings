/*
 * Copyright (C) 2019 ion-OS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.deviceinfo.ion;

import android.content.Context;
import android.os.SystemProperties;
import android.os.UserManager;

import com.android.settings.R;

public class IonTypeDialogController {

    private static final String ION_PROPERTY = "ro.ion.build_type";
    private static final String ION_RELEASE = "ro.ion.release_type";
    private static final int ION_TYPE_VALUE_ID = R.id.ion_type_value;
    private static final int ION_TYPE_LABEL_ID = R.id.ion_type_label;

    private final IonInfoDialogFragment mDialog;
    private final Context mContext;
    private final UserManager mUserManager;

    public IonTypeDialogController(IonInfoDialogFragment dialog) {
        mDialog = dialog;
        mContext = dialog.getContext();
        mUserManager = (UserManager) mContext.getSystemService(Context.USER_SERVICE);
    }

    public void initialize() {

        String buildType = SystemProperties.get(ION_PROPERTY,
                mContext.getResources().getString(R.string.device_info_default));
        String buildRelease = SystemProperties.get(ION_RELEASE,
                mContext.getResources().getString(R.string.device_info_default));

        mDialog.setText(ION_TYPE_VALUE_ID, buildType + "-" + buildRelease);
    }
}
