package com.madnow.facebook.ad;

import android.app.Activity;
import android.os.Handler;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.RewardData;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.RewardedVideoAdListener;
import com.wogame.cinterface.AdCallBack;
import com.wogame.common.AppMacros;
import com.wogame.common.Common;
import com.wogame.util.DeviceUtil;

public class AdRewardedVideo implements RewardedVideoAdListener {
    private RewardedVideoAd mRewardedVideoAd;
    private Activity mActivity;
    private int mVideoCount = 0;
    private String mVideoPlaceId = "";
    private boolean mIsShowVideo;

    public void initActivity(Activity activity, AdCallBack callback) {
        mActivity = activity;

    }
    /////////////////////////////////////////////////////////////////////////////////////////////////
    //视频广告
    private void initRewardedVideoAd(final String rewardedId) {
        if (mRewardedVideoAd != null) {
            mRewardedVideoAd.destroy();
            mRewardedVideoAd = null;
        }
        mRewardedVideoAd = new RewardedVideoAd(mActivity,rewardedId);
        mRewardedVideoAd.setAdListener(this);
        mRewardedVideoAd.setRewardData(new RewardData(DeviceUtil.m_deviceId, "100"));
        loadRewardedVideoAd();
    }

    private void loadRewardedVideoAd(){
        if(mRewardedVideoAd != null){
            mRewardedVideoAd.loadAd();
            mVideoCount++;
        }
    }


    public void playVideo(String placeId) {
        this.mVideoPlaceId = placeId;
        mRewardedVideoAd.show();
        mIsShowVideo = true;
    }

    public boolean canPlayVideo(){
        if (mRewardedVideoAd == null || !mRewardedVideoAd.isAdLoaded()) {
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void onRewardedVideoCompleted() {
//        showToast("Rewarded Video View Complete");
//        AdConfig.onAdShow(AppMacros.AT_RewardVideo);
//        if(_adCallback!=null) _adCallback.onAdShow(AdConfig.TYPE_FAN, AppMacros.AT_RewardVideo);
//        mIsRewardedVideoComplete = true;
//        appActivity.runOnGLThread(new Runnable() {
//            @Override
//            public void run() {
//                PushJniService.onVideoAdReward(AppMacros.CALL_SUCCESS,AppMacros.AT_RewardVideo,videoPlaceId);
//            }
//        });
    }

    @Override
    public void onError(Ad ad, AdError adError) {
        if (ad == mRewardedVideoAd) {
            if(mVideoCount <= 6){
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

    }

    @Override
    public void onAdClicked(Ad ad) {

    }

    @Override
    public void onLoggingImpression(Ad ad) {

    }

    @Override
    public void onRewardedVideoClosed() {
        loadRewardedVideoAd();
    }

    public void onDestroy() {
        if (mRewardedVideoAd != null) {
            mRewardedVideoAd.destroy();
            mRewardedVideoAd = null;
        }
    }
}
