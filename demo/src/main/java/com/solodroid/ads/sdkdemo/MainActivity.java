package com.solodroid.ads.sdkdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.solodroid.ads.sdk.format.AdNetwork;
import com.solodroid.ads.sdk.format.AppOpenAdManager;
import com.solodroid.ads.sdk.format.BannerAd;
import com.solodroid.ads.sdk.format.InterstitialAd;
import com.solodroid.ads.sdk.format.NativeAd;

public class MainActivity extends AppCompatActivity {

    public static final String AD_STATUS = "1";
    public static final String AD_NETWORK = "admob";
    public static final String BACKUP_AD_NETWORK = "none";

    public static final String ADMOB_BANNER_ID = "ca-app-pub-3940256099942544/6300978111";
    public static final String ADMOB_INTERSTITIAL_ID = "ca-app-pub-3940256099942544/1033173712";
    public static final String ADMOB_NATIVE_ID = "ca-app-pub-3940256099942544/2247696110";
    public static final String ADMOB_APP_OPEN_AD_ID = "ca-app-pub-3940256099942544/3419835294";

    public static final String STARTAPP_APP_ID = "0";

    public static final String UNITY_GAME_ID = "4089993";
    public static final String UNITY_BANNER_ID = "banner";
    public static final String UNITY_INTERSTITIAL_ID = "video";

    public static final String APPLOVIN_BANNER_ID = "da17eff31ae69f15";
    public static final String APPLOVIN_INTERSTITIAL_ID = "98f6a586ed642919";
    public static final String APPLOVIN_NATIVE_MANUAL_ID = "87343269587e8998";

    public static final String APPLOVIN_BANNER_ZONE_ID = "afb7122672e86340";
    public static final String APPLOVIN_INTERSTITIAL_ZONE_ID = "b6eba8b976279ea5";

    public static final String MOPUB_BANNER_ID = "b195f8dd8ded45fe847ad89ed1d016da";
    public static final String MOPUB_INTERSTITIAL_ID = "24534e1901884e398f1253216226017e";

    Toolbar toolbar;
    AdNetwork.Initialize adNetwork;
    BannerAd.Builder bannerAd;
    InterstitialAd.Builder interstitialAd;
    NativeAd.Builder nativeAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        adNetwork = new AdNetwork.Initialize(this)
                .setAdStatus(AD_STATUS)
                .setAdNetwork(AD_NETWORK)
                .setBackupAdNetwork(BACKUP_AD_NETWORK)
                .setAdMobAppId(null)
                .setStartappAppId(STARTAPP_APP_ID)
                .setUnityGameId(UNITY_GAME_ID)
                .setAppLovinSdkKey(getResources().getString(R.string.applovin_sdk_key))
                .setMopubBannerId(MOPUB_BANNER_ID)
                .setDebug(BuildConfig.DEBUG)
                .build();

        bannerAd = new BannerAd.Builder(this)
                .setAdStatus(AD_STATUS)
                .setAdNetwork(AD_NETWORK)
                .setBackupAdNetwork(BACKUP_AD_NETWORK)
                .setAdMobBannerId(ADMOB_BANNER_ID)
                .setUnityBannerId(UNITY_BANNER_ID)
                .setAppLovinBannerId(APPLOVIN_BANNER_ID)
                .setAppLovinBannerZoneId(APPLOVIN_BANNER_ZONE_ID)
                .setMopubBannerId(MOPUB_BANNER_ID)
                .setDarkTheme(false)
                .build();

        interstitialAd = new InterstitialAd.Builder(this)
                .setAdStatus(AD_STATUS)
                .setAdNetwork(AD_NETWORK)
                .setBackupAdNetwork(BACKUP_AD_NETWORK)
                .setAdMobInterstitialId(ADMOB_INTERSTITIAL_ID)
                .setUnityInterstitialId(UNITY_INTERSTITIAL_ID)
                .setAppLovinInterstitialId(APPLOVIN_INTERSTITIAL_ID)
                .setAppLovinInterstitialZoneId(APPLOVIN_INTERSTITIAL_ZONE_ID)
                .setMopubInterstitialId(MOPUB_INTERSTITIAL_ID)
                .setInterval(1)
                .build();

        findViewById(R.id.btn_interstitial).setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), SecondActivity.class));
            interstitialAd.show();
        });

        nativeAd = new NativeAd.Builder(this)
                .setAdStatus(AD_STATUS)
                .setAdNetwork(AD_NETWORK)
                .setBackupAdNetwork(BACKUP_AD_NETWORK)
                .setAdMobNativeId(ADMOB_NATIVE_ID)
                .setAppLovinNativeId(APPLOVIN_NATIVE_MANUAL_ID)
                .setDarkTheme(false)
                .build();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}