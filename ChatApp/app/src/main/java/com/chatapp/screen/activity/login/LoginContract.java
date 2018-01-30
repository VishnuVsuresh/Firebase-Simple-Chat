package com.chatapp.screen.activity.login;

import com.chatapp.screen.base.BaseView;

/**
 * Created by vishnu on 24-01-2018.
 */

public class LoginContract  {

    public interface View extends BaseView {
    }


    interface Presenter {
        void doLogin(String email,String password);
    }
}
