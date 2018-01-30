package com.chatapp.screen.activity.home;

import com.chatapp.screen.base.BaseView;

/**
 * Created by vishnu on 24-01-2018.
 */

public class HomeContract {
    public interface View extends BaseView {
    }


    interface Presenter {
        void getUserList(int apiCode);
        void doSignOut();
    }
}
