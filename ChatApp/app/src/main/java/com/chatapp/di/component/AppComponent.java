package com.chatapp.di.component;

import com.chatapp.app.ChatApp;
import com.chatapp.di.module.AppModule;
import com.chatapp.fcm.MyFirebaseInstanceIDService;
import com.chatapp.screen.activity.chat.ChatActivity;
import com.chatapp.screen.activity.home.HomeActivity;
import com.chatapp.screen.activity.login.LoginActivity;
import com.chatapp.screen.activity.signIn.SigInActivity;
import com.chatapp.screen.activity.splash.SplashActivity;
import com.chatapp.utils.LoginUtil;


import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Vishnu on 7/28/2017.
 * <p>
 * interface for injecting all views
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(ChatApp app);

    void inject(SplashActivity app);

    void inject(LoginActivity app);

    void inject(SigInActivity sigInActivity);

    void inject(HomeActivity homeActivity);
    void inject(ChatActivity activity);

    LoginUtil getPreferenceHelper();

    void inject(MyFirebaseInstanceIDService myFirebaseInstanceIDService);
}
