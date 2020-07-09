package com.madnow.facebook.ad;

import android.app.Activity;
import android.os.Handler;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.wogame.cinterface.AdCallBack;
import com.wogame.common.AppMacros;
import com.wogame.common.Common;
import com.wogame.util.DeviceUtil;

public class AdInterstitial {
    private InterstitialAd interstitialAd;
    private Activity mActivity;
    private int mInterstitialPlaceCount = 0;
    private String mVideoPlaceId = "";
    public boolean mIsInterstitial = false;
    private String mInterstitialPlaceId = "";


    public AdInterstitial(Activity activity,final String interstitialId){
        mActivity = activity;
        initInterstitialAd(interstitialId);
    }

    //插屏广告
    private void initInterstitialAd(final String interstitialId){
        // Instantiate an InterstitialAd object
        interstitialAd = new InterstitialAd(mActivity, interstitialId);
        loadInterstitialAd();

        // Set listeners for the Interstitial Ad
        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                loadInterstitialAd();
//                AdConfig.onAdShow(AppMacros.AT_Interstitial);
//                if(_adCallback!=null) _adCallback.onAdShow(AdConfig.TYPE_FAN, AppMacros.AT_Interstitial);
//                appActivity.runOnGLThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        PushJniService.onVideoAdReward(AppMacros.CALL_SUCCESS, AppMacros.AT_Interstitial,interstitialPlaceId);
//                    }
//                });
            }
            @Override
            public void onInterstitialDismissed(Ad ad) {
//                AdManager.getInstance().isInterstitial = false;
//                isInterstitial = false;
//                appActivity.runOnGLThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        PushJniService.onVideoAdReward(AppMacros.CALL_CANCEL,AppMacros.AT_Interstitial,interstitialPlaceId);
//                    }
//                });
            }
            @Override
            public void onError(Ad ad, AdError adError) {
                if(ad == interstitialAd) {
                    if(mInterstitialPlaceCount <= 6){
                        if (Common.getInstance().isNetworkConnected()) {
                            //delay to load again
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loadInterstitialAd();
                                }
                            }, 35000);
                        }
                    }
                }
            }
            @Override
            public void onAdLoaded(Ad ad) {
            }
            @Override
            public void onAdClicked(Ad ad) {
//                AdConfig.onAdClick(appActivity, AppMacros.AT_Interstitial, interstitialPlaceId);
               // if(_adCallback!=null) _adCallback.onAdClick(appActivity,AdConfig.TYPE_FAN,AppMacros.AT_Interstitial, interstitialPlaceId);
            }
            @Override
            public void onLoggingImpression(Ad ad) {
               // Toast.makeText(appActivity, "Impression logged!", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void  loadInterstitialAd(){
        if(interstitialAd != null){
            interstitialAd.loadAd();
            mInterstitialPlaceCount++;
        }
    }


    public void playInterstitialAd(String placeId) {
        if (interstitialAd != null && interstitialAd.isAdLoaded()){
            mIsInterstitial = true;
            this.mInterstitialPlaceId = placeId;
            interstitialAd.show();
        }
    }


    public void onDestroy() {

    }
}
