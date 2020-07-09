package com.madnow.facebook.service;

import android.app.Activity;
import android.os.Bundle;

/**
 * Open your /app/res/values/strings.xml file and add the following lines
 * <string name="facebook_app_id">[APP_ID]</string>
 * <string name="fb_login_protocol_scheme">fb[APP_ID]</string>
 *
 *
 */
import com.facebook.appevents.AppEventsLogger;

public class FBEventService {
    public static FBEventService instance;

    private static AppEventsLogger logger ;//Facebook 统计;

    public static FBEventService getInstance() {
        if (instance == null) {
            instance = new FBEventService();
        }
        return instance;
    }

    public void initActivity(Activity activity){
        logger = AppEventsLogger.newLogger(activity);
    }

    /**
     * 禁止事件发送
     * @param isEnabled
     */
    public void setAutoLogAppEventsEnabled(boolean isEnabled){
        //FacebookSDK.setAutoLogAppEventsEnabled(isEnabled);
    }

    /**
     * 统计
     * @param eventId
     * @param params
     */
    public void logEvent(final String eventId, Bundle params){
        if(logger != null){
            logger.logEvent(eventId,params);
        }
    }

    /**
     * 统计
     * @param eventId
     * @param params
     * @param count
     */
    public void logEvent(final String eventId, Bundle params, final int count){
        if(logger != null){
            logger.logEvent(eventId,count,params);
        }
    }
}
