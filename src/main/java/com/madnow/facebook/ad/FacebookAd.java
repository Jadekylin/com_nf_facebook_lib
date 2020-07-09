package com.wogame.ad;

import android.os.Handler;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.RewardData;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.RewardedVideoAdListener;
import com.wogame.common.AppMacros;
import com.wogame.common.Common;
import com.wogame.service.PushJniService;
import com.wogame.util.AppInfoUtil;
import com.wogame.util.DeviceUtil;

import org.cocos2dx.lib.Cocos2dxActivity;

/**
 * Created by Administrator on 2018/3/10 0010.
 */

public class FacebookAd implements AdInterface, RewardedVideoAdListener {

//    private AdView adView;
    private InterstitialAd interstitialAd;
    private RewardedVideoAd rewardedVideoAd;
    private Cocos2dxActivity appActivity;
    private static String bannerPlaceId = "";
    private static String interstitialPlaceId = "";
    private static String videoPlaceId = "";
    public boolean isInterstitial = false;
    public boolean isShowVideo = false;
    private int interstitialPlaceCount = 0;
    private int videoPlaceCount = 0;
    private  ADCallback _adCallback = null;
    private boolean mIsRewardedVideoComplete = false;

    @Override
    public void initActivity(Cocos2dxActivity _appActivity,ADCallback callback) {
        appActivity = _appActivity;
        _adCallback = callback;
    }
    @Override
    public void initAd(Cocos2dxActivity activity, final String appId) {
        this.appActivity = activity;
    }

    public void setAdId(final String bannerTopId,final String bannerBottomId,final String interstitialId,final String rewardedId,final String rewardedId2){
        if(!bannerTopId.isEmpty()){
//            initBannerTop(bannerTopId);
        }
        if(!bannerBottomId.isEmpty()){
//            initBannerBottom(bannerBottomId);
        }
        if(!interstitialId.isEmpty()){
            initInterstitialAd(interstitialId);
        }
        if(!rewardedId.isEmpty()){
            initRewardedVideoAd(rewardedId);
        }
        if(!rewardedId2.isEmpty()){
//            m_rewardedId2 = rewardedId2;
        }
    }

    @Override
    public void playBanner(String placeId,int type) {
        this.bannerPlaceId = placeId;
//        adView.setVisibility(View.VISIBLE);
//        appActivity.runOnGLThread(new Runnable() {
//            @Override
//            public void run() {
//                JniService.onVideoAdReward(1,1,bannerPlaceId);
//            }
//        });
    }

    public void removeBannerAtADType(int type){
//        adView.setVisibility(View.INVISIBLE);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////
    //插屏广告
    private void initInterstitialAd(final String interstitialId){
        // Instantiate an InterstitialAd object
        interstitialAd = new InterstitialAd(appActivity, interstitialId);
        loadInterstitialAd();

        // Set listeners for the Interstitial Ad
        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                loadInterstitialAd();
//                AdConfig.onAdShow(AppMacros.AT_Interstitial);
                if(_adCallback!=null) _adCallback.onAdShow(AdConfig.TYPE_FAN,AppMacros.AT_Interstitial);
                appActivity.runOnGLThread(new Runnable() {
                    @Override
                    public void run() {
                        PushJniService.onVideoAdReward(AppMacros.CALL_SUCCESS, AppMacros.AT_Interstitial,interstitialPlaceId);
                    }
                });
            }
            @Override
            public void onInterstitialDismissed(Ad ad) {
//                AdManager.getInstance().isInterstitial = false;
                isInterstitial = false;
                appActivity.runOnGLThread(new Runnable() {
                    @Override
                    public void run() {
                        PushJniService.onVideoAdReward(AppMacros.CALL_CANCEL,AppMacros.AT_Interstitial,interstitialPlaceId);
                    }
                });
            }
            @Override
            public void onError(Ad ad, AdError adError) {
                if(ad == interstitialAd) {
                    if(interstitialPlaceCount <= 6){
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
                if(_adCallback!=null) _adCallback.onAdClick(appActivity,AdConfig.TYPE_FAN,AppMacros.AT_Interstitial, interstitialPlaceId);
            }
            @Override
            public void onLoggingImpression(Ad ad) {
                Toast.makeText(appActivity, "Impression logged!", Toast.LENGTH_LONG).show();
            }
        });

    }
    private void  loadInterstitialAd(){
        if(interstitialAd != null){
            interstitialAd.loadAd();
            interstitialPlaceCount++;
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    //视频广告
    private void initRewardedVideoAd(final String rewardedId) {
        if (rewardedVideoAd != null) {
            rewardedVideoAd.destroy();
            rewardedVideoAd = null;
        }
        rewardedVideoAd = new RewardedVideoAd(appActivity,rewardedId);
        rewardedVideoAd.setAdListener(this);
        rewardedVideoAd.setRewardData(new RewardData(DeviceUtil.m_deviceId, "100"));
        loadRewardedVideoAd();
    }

    private void loadRewardedVideoAd(){
        if(interstitialAd != null){
            rewardedVideoAd.loadAd();
            videoPlaceCount++;
        }
    }

    @Override
    public void playVideo(String placeId) {
        this.videoPlaceId = placeId;
        rewardedVideoAd.show();
        isShowVideo = true;
    }

    public boolean canPlayVideo(){
        if (rewardedVideoAd == null || !rewardedVideoAd.isAdLoaded()) {
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void playInterstitialAd(String placeId) {
        if (interstitialAd != null && interstitialAd.isAdLoaded()){
            isInterstitial = true;
            this.interstitialPlaceId = placeId;
            interstitialAd.show();
        }
    }

    public boolean canPlayInterstitialAd(){
        if(interstitialAd != null){
            return interstitialAd.isAdLoaded();
        }
        return false;
    }

    @Override
    public void onRewardedVideoCompleted() {
        showToast("Rewarded Video View Complete");
//        AdConfig.onAdShow(AppMacros.AT_RewardVideo);
        if(_adCallback!=null) _adCallback.onAdShow(AdConfig.TYPE_FAN,AppMacros.AT_RewardVideo);
        mIsRewardedVideoComplete = true;
//        appActivity.runOnGLThread(new Runnable() {
//            @Override
//            public void run() {
//                PushJniService.onVideoAdReward(AppMacros.CALL_SUCCESS,AppMacros.AT_RewardVideo,videoPlaceId);
//            }
//        });
    }

    @Override
    public void onError(Ad ad, AdError adError) {
        if (ad == rewardedVideoAd) {
            if(videoPlaceCount <= 6){
                if (Common.getInstance().isNetworkConnected()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadRewardedVideoAd();
                        }
                    }, 35000);
                }
            }

        }
    }

    @Override
    public void onAdLoaded(Ad ad) {
        if (ad == rewardedVideoAd) {
        }
    }

    @Override
    public void onAdClicked(Ad ad) {
        showToast("Rewarded Video Clicked");
//        AdConfig.onAdClick(appActivity, AppMacros.AT_RewardVideo, videoPlaceId);
        if(_adCallback!=null) _adCallback.onAdClick(appActivity,AdConfig.TYPE_FAN,AppMacros.AT_RewardVideo, videoPlaceId);
    }

    @Override
    public void onLoggingImpression(Ad ad) {
        showToast("Rewarded Video Impression");
    }

    @Override
    public void onRewardedVideoClosed() {
        showToast("Rewarded Video Closed");
//        rewardedVideoAd.loadAd();
        loadRewardedVideoAd();
        appActivity.runOnGLThread(new Runnable() {
            @Override
            public void run() {
                if(mIsRewardedVideoComplete){
                    PushJniService.onVideoAdReward(AppMacros.CALL_SUCCESS,AppMacros.AT_RewardVideo,videoPlaceId);
                }
                else {
                    PushJniService.onVideoAdReward(AppMacros.CALL_CANCEL,AppMacros.AT_RewardVideo,videoPlaceId);
                }
                mIsRewardedVideoComplete = false;
                isShowVideo = false;
            }
        });
    }

    private void showToast(String message) {
//        Toast.makeText(appActivity, message, Toast.LENGTH_SHORT).show();
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {
//        if (adView!=null) {
//            adView.destroy();
//        }
        if (rewardedVideoAd != null) {
            rewardedVideoAd.destroy();
            rewardedVideoAd = null;
        }
    }

}
