package com.chatapp.screen.activity.signIn;

import com.chatapp.screen.base.BaseView;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by vishnu on 24-01-2018.
 */

public class SignInContract {
    public interface View extends BaseView {
    }


    interface Presenter {
        void doSignIn(String email,String password);
        void addUser(FirebaseUser user);
    }
}
