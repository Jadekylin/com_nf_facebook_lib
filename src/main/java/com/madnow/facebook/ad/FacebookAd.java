package com.madnow.facebook.ad;

import android.app.Activity;
import android.os.Handler;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.RewardData;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.RewardedVideoAdListener;
import com.facebook.appevents.AppEventsLogger;
import com.madnow.facebook.service.FBEventService;
import com.wogame.cinterface.AdCallBack;
import com.wogame.cinterface.AdInterface;
import com.wogame.common.AppMacros;
import com.wogame.common.Common;
import com.wogame.util.DeviceUtil;

/**
 * Created by Administrator on 2018/3/10 0010.
 */

public class FacebookAd {

    public static FacebookAd mInstance;
    private Activity mActivity;

    private AdCallBack mAdCallback = null;

    private AdBanner mAdBanner;
    private AdInterstitial mInterstitial;
    private AdRewardedVideo mRewardedVideo;


    public static FacebookAd getInstance() {
        if (mInstance == null) {
            mInstance = new FacebookAd();
        }
        return mInstance;
    }

    public void initActivity(Activity activity,AdCallBack callback) {
        mActivity = activity;
        mAdCallback = callback;
    }

    public void setAdId(final String bannerTopId,final String bannerBottomId,final String interstitialId,final String rewardedId){

        AudienceNetworkAds.initialize(mActivity);

        if(!bannerTopId.isEmpty() || !bannerBottomId.isEmpty()){
            mAdBanner = new AdBanner(mActivity);

            if(!bannerTopId.isEmpty()){
                mAdBanner.loadTopAd(bannerTopId);
            }

            if(!bannerBottomId.isEmpty()){
                mAdBanner.loadBottomAd(bannerBottomId);
            }
        }

        if(!interstitialId.isEmpty()){
            mInterstitial = new AdInterstitial(mActivity,interstitialId);
        }

        if(!rewardedId.isEmpty()){
            mRewardedVideo = new AdRewardedVideo(mActivity,rewardedId);
        }

    }

    /**
     * 显示广告
     * @param type
     * @param cpPlaceId
     * @param px
     * @param py
     */
    public void showAd(final int type, final String cpPlaceId, final int px, final int py){
        if(type == AppMacros.AT_RewardVideo){

        }
        else  if(type == AppMacros.AT_FullScreenVideo){


        }
    }

    /**
     * 关闭广告
     * @param type
     */
    public void closeAd(final int type) {

    }


    public void onDestroy() {
        if (mRewardedVideo!=null) {
            mRewardedVideo.onDestroy();
        }

        if(mAdBanner!= null){
            mAdBanner.onDestroy();
        }
    }

}
