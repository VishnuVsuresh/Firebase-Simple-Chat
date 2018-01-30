package com.chatapp.screen.activity.signIn;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.chatapp.R;
import com.chatapp.model.User;
import com.chatapp.screen.base.Presenter;
import com.chatapp.utils.Constants;
import com.chatapp.utils.LoginUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

/**
 * Created by vishnu on 24-01-2018.
 */

public class SignInPresenter extends Presenter<SignInContract.View> implements SignInContract.Presenter {
    private static final String TAG = "SignInPresenter";
    private Context mContext;
    private FirebaseAuth mAuth;
    private LoginUtil mLoginUtil;

    @Inject
    public SignInPresenter(Context mContext, LoginUtil loginUtil) {
        this.mContext = mContext;
        this.mLoginUtil = loginUtil;
        mAuth = FirebaseAuth.getInstance();
    }


    @Override
    public void doSignIn(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    addUser(user);

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    getView().onError(task.getException() == null ? "createUserWithEmail:failure" : task.getException().getMessage(), 0);
                }
            }
        });


    }

    @Override
    public void addUser(final FirebaseUser firebaseUser) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final User user = new User(firebaseUser.getUid(),
                firebaseUser.getEmail(),
                mLoginUtil.getFBAccessToken(mContext));
        database.child(Constants.ARG_USERS)
                .child(firebaseUser.getUid())
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            getView().onSuccess(firebaseUser, 0);
                        } else {
                            getView().onError(mContext.getString(R.string.user_unable_to_add), 0);
                        }
                    }
                });
    }
}
