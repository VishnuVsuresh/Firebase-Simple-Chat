package com.chatapp.di.module;


import android.content.Context;


import com.chatapp.app.ChatApp;
import com.chatapp.utils.LoginUtil;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Vishnu on 7/28/2017.
 *
 * class for getting app class objects
 */
@Module
public class AppModule {

    private ChatApp mApp;

    public AppModule(ChatApp app) {
        mApp = app;
    }


    @Provides
    @Singleton
    Context provideContext() {
        return mApp;
    }

    @Provides
    @Singleton
    LoginUtil provideAppPreferences() {
        return new LoginUtil();
    }
}
