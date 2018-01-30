package com.chatapp.screen.activity.splash;

import com.chatapp.screen.base.BaseView;

/**
 * Created by vishnu on 23-01-2018.
 */

public class SplashContract {

    public interface View extends BaseView {
    }


    interface Presenter {
        void takeToNextLevel();
    }
}
