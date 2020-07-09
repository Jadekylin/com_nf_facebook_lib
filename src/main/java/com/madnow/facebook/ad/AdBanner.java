package com.madnow.facebook.ad;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.madnow.facebook.R;
import com.wogame.common.AppMacros;
import com.wogame.util.GMDebug;

public class AdBanner {
    private Activity mActivity;
    private AdView mBottomAdView;
    private AdView mTopAdView;
    View mViewRoot;

    private LinearLayout mBottomContainer;
    private LinearLayout mTopContainer;

    public AdBanner(Activity activity){
        mActivity = activity;

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        @SuppressLint("WrongViewCast") LinearLayout fragment_bannerad = (LinearLayout) mActivity.findViewById(R.id.banner_container);
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        mViewRoot = inflater.inflate(R.layout.banner_ad, fragment_bannerad, false);
        mActivity.addContentView(mViewRoot, params);

    }

    //
    public void loadBottomAd(final String bannerId){
        mBottomAdView = new AdView(mActivity, "IMG_16_9_APP_INSTALL#"+bannerId, AdSize.BANNER_HEIGHT_50);
        mBottomContainer= (LinearLayout) mViewRoot.findViewById(R.id.banner_container);
        // Add the ad view to your activity layout
        mBottomContainer.removeAllViews();
        mBottomContainer.addView(mBottomAdView);

        mBottomAdView.setAdListener(new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                GMDebug.LogD("Error: " + adError.getErrorMessage());
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
        });

        mBottomAdView.loadAd();
    }

    public void loadTopAd(final String bannerId){
        mTopAdView = new AdView(mActivity, "IMG_16_9_APP_INSTALL#"+bannerId, AdSize.BANNER_HEIGHT_50);
        mTopContainer = (LinearLayout) mViewRoot.findViewById(R.id.banner_top_container);
        // Add the ad view to your activity layout
        mTopContainer.removeAllViews();
        mTopContainer.addView(mTopAdView);

        mTopAdView.setAdListener(new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                GMDebug.LogD("Error: " + adError.getErrorMessage());
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
        });

        mTopAdView.loadAd();
    }

    public void loadAd(int type){
        if(type == AppMacros.AT_Banner_Top){
            mTopContainer.removeAllViews();
            mTopContainer.addView(mTopAdView);
        }
        else if(type == AppMacros.AT_Banner_Bottom) {
            mBottomContainer.removeAllViews();
            mBottomContainer.addView(mBottomAdView);
        }
    }

    public void hideBanner(int type){
        if(type == AppMacros.AT_Banner_Top){
            mTopContainer.removeAllViews();
        }
        else if(type == AppMacros.AT_Banner_Bottom) {
            mBottomContainer.removeAllViews();
        }
    }

    public void onDestroy() {
        if (mBottomAdView != null) {
            mBottomAdView.destroy();
        }

        if (mTopAdView != null) {
            mTopAdView.destroy();
        }
    }
}
