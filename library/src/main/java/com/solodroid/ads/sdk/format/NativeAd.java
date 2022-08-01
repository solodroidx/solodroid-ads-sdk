package com.solodroid.ads.sdk.format;

import static com.solodroid.ads.sdk.util.Constant.ADMOB;
import static com.solodroid.ads.sdk.util.Constant.AD_STATUS_ON;
import static com.solodroid.ads.sdk.util.Constant.APPLOVIN;
import static com.solodroid.ads.sdk.util.Constant.APPLOVIN_DISCOVERY;
import static com.solodroid.ads.sdk.util.Constant.APPLOVIN_MAX;
import static com.solodroid.ads.sdk.util.Constant.GOOGLE_AD_MANAGER;
import static com.solodroid.ads.sdk.util.Constant.IRONSOURCE;
import static com.solodroid.ads.sdk.util.Constant.NONE;
import static com.solodroid.ads.sdk.util.Constant.STARTAPP;
import static com.solodroid.ads.sdk.util.Constant.UNITY;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.MediaView;
import com.solodroid.ads.sdk.R;
import com.solodroid.ads.sdk.util.AdManagerTemplateView;
import com.solodroid.ads.sdk.util.Constant;
import com.solodroid.ads.sdk.util.NativeTemplateStyle;
import com.solodroid.ads.sdk.util.TemplateView;
import com.solodroid.ads.sdk.util.Tools;
import com.startapp.sdk.ads.nativead.NativeAdDetails;
import com.startapp.sdk.ads.nativead.NativeAdPreferences;
import com.startapp.sdk.ads.nativead.StartAppNativeAd;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;

import java.util.ArrayList;

public class NativeAd {

    public static class Builder {

        private static final String TAG = "AdNetwork";
        private final Activity activity;
        LinearLayout nativeAdViewContainer;

        MediaView mediaView;
        TemplateView admobNativeAd;
        LinearLayout admobNativeBackground;

        MediaView adManagerMediaView;
        AdManagerTemplateView adManagerNativeAd;
        LinearLayout adManagerNativeBackground;

        View startappNativeAd;
        ImageView startappNativeImage;
        ImageView startappNativeIcon;
        TextView startappNativeTitle;
        TextView startappNativeDescription;
        Button startappNativeButton;
        LinearLayout startappNativeBackground;

        FrameLayout applovinNativeAd;
        MaxNativeAdLoader nativeAdLoader;
        MaxAd nativeAd;

        private String adStatus = "";
        private String adNetwork = "";
        private String backupAdNetwork = "";
        private String adMobNativeId = "";
        private String adManagerNativeId = "";
        private String appLovinNativeId = "";
        private int placementStatus = 1;
        private boolean darkTheme = false;
        private boolean legacyGDPR = false;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder build() {
            loadNativeAd();
            return this;
        }

        public Builder setPadding(int left, int top, int right, int bottom) {
            setNativeAdPadding(left, top, right, bottom);
            return this;
        }

        public Builder setAdStatus(String adStatus) {
            this.adStatus = adStatus;
            return this;
        }

        public Builder setAdNetwork(String adNetwork) {
            this.adNetwork = adNetwork;
            return this;
        }

        public Builder setBackupAdNetwork(String backupAdNetwork) {
            this.backupAdNetwork = backupAdNetwork;
            return this;
        }

        public Builder setAdMobNativeId(String adMobNativeId) {
            this.adMobNativeId = adMobNativeId;
            return this;
        }

        public Builder setAdManagerNativeId(String adManagerNativeId) {
            this.adManagerNativeId = adManagerNativeId;
            return this;
        }

        public Builder setAppLovinNativeId(String appLovinNativeId) {
            this.appLovinNativeId = appLovinNativeId;
            return this;
        }

        public Builder setPlacementStatus(int placementStatus) {
            this.placementStatus = placementStatus;
            return this;
        }

        public Builder setDarkTheme(boolean darkTheme) {
            this.darkTheme = darkTheme;
            return this;
        }

        public Builder setLegacyGDPR(boolean legacyGDPR) {
            this.legacyGDPR = legacyGDPR;
            return this;
        }

        public void loadNativeAd() {

            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {

                nativeAdViewContainer = activity.findViewById(R.id.native_ad_view_container);

                admobNativeAd = activity.findViewById(R.id.admob_native_ad_container);
                mediaView = activity.findViewById(R.id.media_view);
                admobNativeBackground = activity.findViewById(R.id.background);

                adManagerNativeAd = activity.findViewById(R.id.google_ad_manager_native_ad_container);
                adManagerMediaView = activity.findViewById(R.id.ad_manager_media_view);
                adManagerNativeBackground = activity.findViewById(R.id.ad_manager_background);

                startappNativeAd = activity.findViewById(R.id.startapp_native_ad_container);
                startappNativeImage = activity.findViewById(R.id.startapp_native_image);
                startappNativeIcon = activity.findViewById(R.id.startapp_native_icon);
                startappNativeTitle = activity.findViewById(R.id.startapp_native_title);
                startappNativeDescription = activity.findViewById(R.id.startapp_native_description);
                startappNativeButton = activity.findViewById(R.id.startapp_native_button);
                startappNativeButton.setOnClickListener(v -> startappNativeAd.performClick());
                startappNativeBackground = activity.findViewById(R.id.startapp_native_background);
                applovinNativeAd = activity.findViewById(R.id.applovin_native_ad_container);

                switch (adNetwork) {
                    case ADMOB:
                        if (admobNativeAd.getVisibility() != View.VISIBLE) {
                            AdLoader adLoader = new AdLoader.Builder(activity, adMobNativeId)
                                    .forNativeAd(NativeAd -> {
                                        if (darkTheme) {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(activity, R.color.colorBackgroundDark));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            admobNativeAd.setStyles(styles);
                                            admobNativeBackground.setBackgroundResource(R.color.colorBackgroundDark);
                                        } else {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(activity, R.color.colorBackgroundLight));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            admobNativeAd.setStyles(styles);
                                            admobNativeBackground.setBackgroundResource(R.color.colorBackgroundLight);
                                        }
                                        mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                        admobNativeAd.setNativeAd(NativeAd);
                                        admobNativeAd.setVisibility(View.VISIBLE);
                                        nativeAdViewContainer.setVisibility(View.VISIBLE);
                                    })
                                    .withAdListener(new AdListener() {
                                        @Override
                                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                            loadBackupNativeAd();
                                        }
                                    })
                                    .build();
                            adLoader.loadAd(Tools.getAdRequest(activity, legacyGDPR));
                        } else {
                            Log.d(TAG, "AdMob Native Ad has been loaded");
                        }
                        break;

                    case GOOGLE_AD_MANAGER:
                        if (adManagerNativeAd.getVisibility() != View.VISIBLE) {
                            AdLoader adLoader = new AdLoader.Builder(activity, adManagerNativeId)
                                    .forNativeAd(NativeAd -> {
                                        if (darkTheme) {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(activity, R.color.colorBackgroundDark));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            adManagerNativeAd.setStyles(styles);
                                            adManagerNativeBackground.setBackgroundResource(R.color.colorBackgroundDark);
                                        } else {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(activity, R.color.colorBackgroundLight));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            adManagerNativeAd.setStyles(styles);
                                            adManagerNativeBackground.setBackgroundResource(R.color.colorBackgroundLight);
                                        }
                                        adManagerMediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                        adManagerNativeAd.setNativeAd(NativeAd);
                                        adManagerNativeAd.setVisibility(View.VISIBLE);
                                        nativeAdViewContainer.setVisibility(View.VISIBLE);
                                    })
                                    .withAdListener(new AdListener() {
                                        @Override
                                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                            loadBackupNativeAd();
                                        }
                                    })
                                    .build();
                            adLoader.loadAd(Tools.getGoogleAdManagerRequest());
                        } else {
                            Log.d(TAG, "Ad Manager Native Ad has been loaded");
                        }
                        break;

                    case STARTAPP:
                        if (startappNativeAd.getVisibility() != View.VISIBLE) {
                            StartAppNativeAd startAppNativeAd = new StartAppNativeAd(activity);
                            NativeAdPreferences nativePrefs = new NativeAdPreferences()
                                    .setAdsNumber(3)
                                    .setAutoBitmapDownload(true)
                                    .setPrimaryImageSize(Constant.STARTAPP_IMAGE_MEDIUM);
                            AdEventListener adListener = new AdEventListener() {
                                @Override
                                public void onReceiveAd(@NonNull com.startapp.sdk.adsbase.Ad arg0) {
                                    Log.d(TAG, "StartApp Native Ad loaded");
                                    startappNativeAd.setVisibility(View.VISIBLE);
                                    nativeAdViewContainer.setVisibility(View.VISIBLE);
                                    //noinspection rawtypes
                                    ArrayList ads = startAppNativeAd.getNativeAds(); // get NativeAds list

                                    // Print all ads details to log
                                    for (Object ad : ads) {
                                        Log.d(TAG, "StartApp Native Ad " + ad.toString());
                                    }

                                    NativeAdDetails ad = (NativeAdDetails) ads.get(0);
                                    if (ad != null) {
                                        startappNativeImage.setImageBitmap(ad.getImageBitmap());
                                        startappNativeIcon.setImageBitmap(ad.getSecondaryImageBitmap());
                                        startappNativeTitle.setText(ad.getTitle());
                                        startappNativeDescription.setText(ad.getDescription());
                                        startappNativeButton.setText(ad.isApp() ? "Install" : "Open");
                                        ad.registerViewForInteraction(startappNativeAd);
                                    }

                                    if (darkTheme) {
                                        startappNativeBackground.setBackgroundResource(R.color.colorBackgroundDark);
                                    } else {
                                        startappNativeBackground.setBackgroundResource(R.color.colorBackgroundLight);
                                    }

                                }

                                @Override
                                public void onFailedToReceiveAd(com.startapp.sdk.adsbase.Ad arg0) {
                                    loadBackupNativeAd();
                                    Log.d(TAG, "StartApp Native Ad failed loaded");
                                }
                            };
                            startAppNativeAd.loadAd(nativePrefs, adListener);
                        } else {
                            Log.d(TAG, "StartApp Native Ad has been loaded");
                        }
                        break;
                    case IRONSOURCE:
                    case APPLOVIN_DISCOVERY:
                    case APPLOVIN_MAX:
                    case APPLOVIN:
                        if (applovinNativeAd.getVisibility() != View.VISIBLE) {
                            nativeAdLoader = new MaxNativeAdLoader(appLovinNativeId, activity);
                            nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                                @Override
                                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                                    // Clean up any pre-existing native ad to prevent memory leaks.
                                    if (nativeAd != null) {
                                        nativeAdLoader.destroy(nativeAd);
                                    }

                                    // Save ad for cleanup.
                                    nativeAd = ad;

                                    // Add ad view to view.
                                    applovinNativeAd.removeAllViews();
                                    applovinNativeAd.addView(nativeAdView);
                                    applovinNativeAd.setVisibility(View.VISIBLE);
                                    nativeAdViewContainer.setVisibility(View.VISIBLE);

                                    Log.d(TAG, "Max Native Ad loaded successfully");
                                }

                                @Override
                                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                                    // We recommend retrying with exponentially higher delays up to a maximum delay
                                    loadBackupNativeAd();
                                    Log.d(TAG, "failed to load Max Native Ad with message : " + error.getMessage() + " and error code : " + error.getCode());
                                }

                                @Override
                                public void onNativeAdClicked(final MaxAd ad) {
                                    // Optional click callback
                                }
                            });
                            if (darkTheme) {
                                nativeAdLoader.loadAd(createNativeAdViewDark());
                            } else {
                                nativeAdLoader.loadAd(createNativeAdView());
                            }
                        } else {
                            Log.d(TAG, "AppLovin Native Ad has been loaded");
                        }
                        break;

                    case UNITY:
                        //do nothing
                        break;
                }

            }

        }

        public void loadBackupNativeAd() {

            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {

                nativeAdViewContainer = activity.findViewById(R.id.native_ad_view_container);

                admobNativeAd = activity.findViewById(R.id.admob_native_ad_container);
                mediaView = activity.findViewById(R.id.media_view);
                admobNativeBackground = activity.findViewById(R.id.background);

                adManagerNativeAd = activity.findViewById(R.id.google_ad_manager_native_ad_container);
                adManagerMediaView = activity.findViewById(R.id.ad_manager_media_view);
                adManagerNativeBackground = activity.findViewById(R.id.ad_manager_background);

                startappNativeAd = activity.findViewById(R.id.startapp_native_ad_container);
                startappNativeImage = activity.findViewById(R.id.startapp_native_image);
                startappNativeIcon = activity.findViewById(R.id.startapp_native_icon);
                startappNativeTitle = activity.findViewById(R.id.startapp_native_title);
                startappNativeDescription = activity.findViewById(R.id.startapp_native_description);
                startappNativeButton = activity.findViewById(R.id.startapp_native_button);
                startappNativeButton.setOnClickListener(v -> startappNativeAd.performClick());
                startappNativeBackground = activity.findViewById(R.id.startapp_native_background);
                applovinNativeAd = activity.findViewById(R.id.applovin_native_ad_container);

                switch (backupAdNetwork) {
                    case ADMOB:
                        if (admobNativeAd.getVisibility() != View.VISIBLE) {
                            AdLoader adLoader = new AdLoader.Builder(activity, adMobNativeId)
                                    .forNativeAd(NativeAd -> {
                                        if (darkTheme) {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(activity, R.color.colorBackgroundDark));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            admobNativeAd.setStyles(styles);
                                            admobNativeBackground.setBackgroundResource(R.color.colorBackgroundDark);
                                        } else {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(activity, R.color.colorBackgroundLight));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            admobNativeAd.setStyles(styles);
                                            admobNativeBackground.setBackgroundResource(R.color.colorBackgroundLight);
                                        }
                                        mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                        admobNativeAd.setNativeAd(NativeAd);
                                        admobNativeAd.setVisibility(View.VISIBLE);
                                        nativeAdViewContainer.setVisibility(View.VISIBLE);
                                    })
                                    .withAdListener(new AdListener() {
                                        @Override
                                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                            admobNativeAd.setVisibility(View.GONE);
                                            nativeAdViewContainer.setVisibility(View.GONE);
                                        }
                                    })
                                    .build();
                            adLoader.loadAd(Tools.getAdRequest(activity, legacyGDPR));
                        } else {
                            Log.d(TAG, "AdMob Native Ad has been loaded");
                        }
                        break;

                    case GOOGLE_AD_MANAGER:
                        if (adManagerNativeAd.getVisibility() != View.VISIBLE) {
                            AdLoader adLoader = new AdLoader.Builder(activity, adManagerNativeId)
                                    .forNativeAd(NativeAd -> {
                                        if (darkTheme) {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(activity, R.color.colorBackgroundDark));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            adManagerNativeAd.setStyles(styles);
                                            adManagerNativeBackground.setBackgroundResource(R.color.colorBackgroundDark);
                                        } else {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(activity, R.color.colorBackgroundLight));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            adManagerNativeAd.setStyles(styles);
                                            adManagerNativeBackground.setBackgroundResource(R.color.colorBackgroundLight);
                                        }
                                        adManagerMediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                        adManagerNativeAd.setNativeAd(NativeAd);
                                        adManagerNativeAd.setVisibility(View.VISIBLE);
                                        nativeAdViewContainer.setVisibility(View.VISIBLE);
                                    })
                                    .withAdListener(new AdListener() {
                                        @Override
                                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                            adManagerNativeAd.setVisibility(View.GONE);
                                            nativeAdViewContainer.setVisibility(View.GONE);
                                        }
                                    })
                                    .build();
                            adLoader.loadAd(Tools.getGoogleAdManagerRequest());
                        } else {
                            Log.d(TAG, "Ad Manager Native Ad has been loaded");
                        }
                        break;

                    case STARTAPP:
                        if (startappNativeAd.getVisibility() != View.VISIBLE) {
                            StartAppNativeAd startAppNativeAd = new StartAppNativeAd(activity);
                            NativeAdPreferences nativePrefs = new NativeAdPreferences()
                                    .setAdsNumber(3)
                                    .setAutoBitmapDownload(true)
                                    .setPrimaryImageSize(Constant.STARTAPP_IMAGE_MEDIUM);
                            AdEventListener adListener = new AdEventListener() {
                                @Override
                                public void onReceiveAd(@NonNull com.startapp.sdk.adsbase.Ad arg0) {
                                    Log.d(TAG, "StartApp Native Ad loaded");
                                    startappNativeAd.setVisibility(View.VISIBLE);
                                    nativeAdViewContainer.setVisibility(View.VISIBLE);
                                    //noinspection rawtypes
                                    ArrayList ads = startAppNativeAd.getNativeAds(); // get NativeAds list

                                    // Print all ads details to log
                                    for (Object ad : ads) {
                                        Log.d(TAG, "StartApp Native Ad " + ad.toString());
                                    }

                                    NativeAdDetails ad = (NativeAdDetails) ads.get(0);
                                    if (ad != null) {
                                        startappNativeImage.setImageBitmap(ad.getImageBitmap());
                                        startappNativeIcon.setImageBitmap(ad.getSecondaryImageBitmap());
                                        startappNativeTitle.setText(ad.getTitle());
                                        startappNativeDescription.setText(ad.getDescription());
                                        startappNativeButton.setText(ad.isApp() ? "Install" : "Open");
                                        ad.registerViewForInteraction(startappNativeAd);
                                    }

                                    if (darkTheme) {
                                        startappNativeBackground.setBackgroundResource(R.color.colorBackgroundDark);
                                    } else {
                                        startappNativeBackground.setBackgroundResource(R.color.colorBackgroundLight);
                                    }

                                }

                                @Override
                                public void onFailedToReceiveAd(com.startapp.sdk.adsbase.Ad arg0) {
                                    startappNativeAd.setVisibility(View.GONE);
                                    nativeAdViewContainer.setVisibility(View.GONE);
                                    Log.d(TAG, "StartApp Native Ad failed loaded");
                                }
                            };
                            startAppNativeAd.loadAd(nativePrefs, adListener);
                        } else {
                            Log.d(TAG, "StartApp Native Ad has been loaded");
                        }
                        break;

                    case APPLOVIN_MAX:
                    case APPLOVIN:
                        if (applovinNativeAd.getVisibility() != View.VISIBLE) {
                            nativeAdLoader = new MaxNativeAdLoader(appLovinNativeId, activity);
                            nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                                @Override
                                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                                    // Clean up any pre-existing native ad to prevent memory leaks.
                                    if (nativeAd != null) {
                                        nativeAdLoader.destroy(nativeAd);
                                    }

                                    // Save ad for cleanup.
                                    nativeAd = ad;

                                    // Add ad view to view.
                                    applovinNativeAd.removeAllViews();
                                    applovinNativeAd.addView(nativeAdView);
                                    applovinNativeAd.setVisibility(View.VISIBLE);
                                    nativeAdViewContainer.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                                    // We recommend retrying with exponentially higher delays up to a maximum delay
                                }

                                @Override
                                public void onNativeAdClicked(final MaxAd ad) {
                                    // Optional click callback
                                }
                            });
                            if (darkTheme) {
                                nativeAdLoader.loadAd(createNativeAdViewDark());
                            } else {
                                nativeAdLoader.loadAd(createNativeAdView());
                            }
                        } else {
                            Log.d(TAG, "AppLovin Native Ad has been loaded");
                        }
                        break;

                    case UNITY:

                    case NONE:
                        nativeAdViewContainer.setVisibility(View.GONE);
                        break;
                }

            }

        }

        public void setNativeAdPadding(int left, int top, int right, int bottom) {
            nativeAdViewContainer = activity.findViewById(R.id.native_ad_view_container);
            nativeAdViewContainer.setPadding(left, top, right, bottom);
        }

        public MaxNativeAdView createNativeAdView() {
            MaxNativeAdViewBinder binder = new MaxNativeAdViewBinder.Builder(R.layout.gnt_applovin_medium_template_view)
                    .setTitleTextViewId(R.id.title_text_view)
                    .setBodyTextViewId(R.id.body_text_view)
                    .setAdvertiserTextViewId(R.id.advertiser_textView)
                    .setIconImageViewId(R.id.icon_image_view)
                    .setMediaContentViewGroupId(R.id.media_view_container)
                    .setOptionsContentViewGroupId(R.id.ad_options_view)
                    .setCallToActionButtonId(R.id.cta_button)
                    .build();
            return new MaxNativeAdView(binder, activity);
        }

        public MaxNativeAdView createNativeAdViewDark() {
            MaxNativeAdViewBinder binder = new MaxNativeAdViewBinder.Builder(R.layout.gnt_applovin_dark_medium_template_view)
                    .setTitleTextViewId(R.id.title_text_view)
                    .setBodyTextViewId(R.id.body_text_view)
                    .setAdvertiserTextViewId(R.id.advertiser_textView)
                    .setIconImageViewId(R.id.icon_image_view)
                    .setMediaContentViewGroupId(R.id.media_view_container)
                    .setOptionsContentViewGroupId(R.id.ad_options_view)
                    .setCallToActionButtonId(R.id.cta_button)
                    .build();
            return new MaxNativeAdView(binder, activity);
        }

    }

}
