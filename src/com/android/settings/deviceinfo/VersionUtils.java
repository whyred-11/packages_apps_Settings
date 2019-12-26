
package com.android.settings.deviceinfo;

import android.os.SystemProperties;

public class VersionUtils {
    public static String getIonVersion(){
        String buildDate = SystemProperties.get("ro.ion.build_date","");
        String buildIon = SystemProperties.get("ro.ionizer","");
        String buildDev = SystemProperties.get("ro.ion.device","");
        String buildRel = SystemProperties.get("ro.ion.release_type","");
        String buildType = SystemProperties.get("ro.ion.build_type","unofficial")/*.toUpperCase()*/;
        return buildDate.equals("") ? "" : "ion-" + buildIon + "-" + buildDev + "-" + buildDate + "-" + buildType + "-" + buildRel;
    }
}
