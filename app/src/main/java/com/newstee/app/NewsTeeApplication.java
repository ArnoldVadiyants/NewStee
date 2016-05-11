package com.newstee.app;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.vk.sdk.VKSdk;

import java.io.File;

/**
 * Created by Arnold on 29.04.2016.
 */
public class NewsTeeApplication extends Application {
  /*  VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
            if (newToken == null) {
                // VKAccessToken is invalid
            }
        }
    };*/
    @Override
    public void onCreate()
    {
        super.onCreate();

        initSingletons();
    }

    protected void initSingletons()
    {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    // System.out.println("@@@@@@@@@@@@ KeyHash " + FacebookSdk.getApplicationSignature(getApplicationContext()));

      //  vkAccessTokenTracker.startTracking();
    //    VKSdk.customInitialize(getApplicationContext(),appId, String.valueOf(VKAccessToken.tokenFromSharedPreferences(this, vkTokenKey)));
        VKSdk.initialize(getApplicationContext());
        ImageLoader imageLoader = ImageLoader.getInstance();
        File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .diskCacheExtraOptions(480, 800, null)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .diskCacheSize(100 * 1024 * 1024)
                .writeDebugLogs()
                .build();
        imageLoader.init(config);
    }

    public void customAppMethod()
    {
        // Custom application method
    }
}
