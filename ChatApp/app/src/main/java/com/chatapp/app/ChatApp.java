package com.chatapp.app;

import android.app.Application;
import android.content.Context;



import com.chatapp.di.component.AppComponent;
import com.chatapp.di.component.DaggerAppComponent;
import com.chatapp.di.module.AppModule;

import java.io.File;


/**
 * Created by Vishnu on 11/14/2017.
 * <p>
 * Application class for ChatApp app
 */

public class ChatApp extends Application {

    private AppComponent appComponent;
    private static boolean sIsChatActivityOpen = false;

    public static ChatApp getAppInstance(Context context) {
        return (ChatApp) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        File cacheFile = new File(getCacheDir(), "responses");
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        appComponent.inject(this);
    }




    public static boolean isChatActivityOpen() {
        return sIsChatActivityOpen;
    }

    public static void setChatActivityOpen(boolean isChatActivityOpen) {
        ChatApp.sIsChatActivityOpen = isChatActivityOpen;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

}
