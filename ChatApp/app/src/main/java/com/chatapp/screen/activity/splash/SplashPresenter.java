package com.chatapp.screen.activity.splash;

import android.content.Context;

import com.chatapp.screen.base.Presenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

/**
 * Created by vishnu on 23-01-2018.
 */

public class SplashPresenter extends Presenter<SplashContract.View> implements SplashContract.Presenter {
    private Context mContext;

    private FirebaseAuth mAuth;


    @Inject
    public SplashPresenter(Context context) {
        this.mContext = context;
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void takeToNextLevel() {

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
            getView().onSuccess(null, 0);
        else
            getView().onError(null, 0);
    }
}
