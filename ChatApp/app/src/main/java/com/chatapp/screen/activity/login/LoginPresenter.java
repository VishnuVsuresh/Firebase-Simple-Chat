package com.chatapp.screen.activity.login;

import android.content.Context;
import android.support.annotation.NonNull;

import com.chatapp.screen.base.Presenter;
import com.chatapp.utils.Constants;
import com.chatapp.utils.LoginUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

/**
 * Created by vishnu on 24-01-2018.
 */

public class LoginPresenter extends Presenter<LoginContract.View> implements LoginContract.Presenter {

    private Context mContext;
    private LoginUtil mLoginUtil;
    private FirebaseAuth mAuth;

    @Inject
    public LoginPresenter(Context context, LoginUtil loginUtil) {
        this.mContext = context;
        this.mLoginUtil = loginUtil;
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void doLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information

                    FirebaseUser user = mAuth.getCurrentUser();
                    updateFirebaseToken(user.getUid(),
                            mLoginUtil.getFBAccessToken(mContext));

                    getView().onSuccess(user, 0);
                } else {
                    getView().onError(task.getException() == null ? "Authentication failed." : task.getException().getMessage(), 0);
                }
            }
        });
    }

    private void updateFirebaseToken(String uid, String fbAccessToken) {

        FirebaseDatabase.getInstance()
                .getReference()
                .child(Constants.ARG_USERS)
                .child(uid)
                .child(Constants.ARG_FIREBASE_TOKEN)
                .setValue(fbAccessToken);
    }
}
