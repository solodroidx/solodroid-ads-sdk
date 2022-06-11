package com.solodroid.ads.sdk.util;

import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Display;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.ads.mediation.facebook.FacebookAdapter;
import com.google.ads.mediation.facebook.FacebookExtras;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.solodroid.ads.sdk.gdpr.LegacyGDPR;

import java.nio.charset.StandardCharsets;

public class Tools {

    public static AdSize getAdSize(Activity activity) {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }

    public static AdRequest getAdRequest(Activity activity, Boolean legacyGDPR) {
        Bundle extras = new FacebookExtras().setNativeBanner(true).build();
        if (legacyGDPR) {
            return new AdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter.class, LegacyGDPR.getBundleAd(activity))
                    .addNetworkExtrasBundle(FacebookAdapter.class, extras)
                    .build();
        } else {
            return new AdRequest.Builder()
                    .addNetworkExtrasBundle(FacebookAdapter.class, extras)
                    .build();
        }
    }

    public static AdManagerAdRequest getGoogleAdManagerRequest() {
        return new AdManagerAdRequest.Builder()
                .build();
    }

    public static String decode(String code) {
        return decodeBase64(decodeBase64(decodeBase64(code)));
    }

    public static String decodeBase64(String code) {
        byte[] valueDecoded = Base64.decode(code.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
        return new String(valueDecoded);
    }

}
