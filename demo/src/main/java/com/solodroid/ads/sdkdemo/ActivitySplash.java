package com.solodroid.ads.sdkdemo;

import static com.solodroid.ads.sdk.util.Constant.ADMOB;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.appcompat.app.AppCompatActivity;

import com.solodroid.ads.sdk.format.AdNetwork;

public class ActivitySplash extends AppCompatActivity {

    private static final long COUNTER_TIME = 2000;
    long secondsRemaining;
    Application application;
    AdNetwork.Initialize adNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initAds();

        if (Constant.AD_NETWORK.equals(ADMOB)) {
            application = getApplication();
            ((MyApplication) application).showAdIfAvailable(ActivitySplash.this, this::createTimer);
        } else {
            startMainActivity();
        }

    }

    private void initAds() {
        adNetwork = new AdNetwork.Initialize(this)
                .setAdStatus(Constant.AD_STATUS)
                .setAdNetwork(Constant.AD_NETWORK)
                .setBackupAdNetwork(Constant.BACKUP_AD_NETWORK)
                .setAdMobAppId(null)
                .setStartappAppId(Constant.STARTAPP_APP_ID)
                .setUnityGameId(Constant.UNITY_GAME_ID)
                .setAppLovinSdkKey(getResources().getString(R.string.applovin_sdk_key))
                .setIronSourceAppKey(Constant.IRONSOURCE_APP_KEY)
                .setDebug(BuildConfig.DEBUG)
                .build();
    }

    private void createTimer() {

        CountDownTimer countDownTimer = new CountDownTimer(COUNTER_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                secondsRemaining = ((millisUntilFinished / 1000) + 1);
            }

            @Override
            public void onFinish() {
                secondsRemaining = 0;
                startMainActivity();
            }
        };
        countDownTimer.start();
    }

    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }

}
