package com.chatapp.screen.activity.splash;

import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.widget.ImageView;

import com.chatapp.R;
import com.chatapp.app.ChatApp;
import com.chatapp.screen.activity.home.HomeActivity;
import com.chatapp.screen.activity.login.LoginActivity;
import com.chatapp.screen.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vishnu on 23-01-2018.
 */

public class SplashActivity extends BaseActivity implements SplashContract.View {

    @Inject
    SplashPresenter mPresenter;

    @BindView(R.id.ivLogo)
    ImageView mIvLogo;

    /*For setting delay for splash animation*/
    private Handler handler;
    private SplashHandler splashThread;

    @Override
    public void onSuccess(Object response, int apiCode) {
startActivity(HomeActivity.class,null,null,true);
    }

    @Override
    public void onError(String message, int apiCode) {
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, mIvLogo, "logo");
        startActivity(LoginActivity.class, null, options, true);

    }

    @Override
    protected void initializeDagger() {
        ChatApp app = ChatApp.getAppInstance(this);
        app.getAppComponent().inject(this);
    }

    @Override
    protected void initializePresenter() {
        super.mPresenter = mPresenter;
        mPresenter.setView(this);
    }

    @Override
    protected void initComponent() {
        mIvLogo.setAlpha(0.0f);
        mIvLogo.animate().alpha(1).setDuration(1500).start();
    }

    @Override
    protected void bindView() {
        setUnBinder(ButterKnife.bind(this));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_spalsh;
    }


    @Override
    protected void onResume() {
        super.onResume();
        /*handler initialization*/
        splashHandler();
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*if app is minimized or destroyed this will invoke*/
        if (handler != null)
            handler.removeCallbacks(splashThread);
        handler = null;
        splashThread = null;
    }

    private void splashHandler() {
        handler = new Handler();
        splashThread = new SplashHandler();
        handler.postDelayed(splashThread,
                getResources().getInteger(R.integer.splash_timeout));
    }

    private class SplashHandler implements Runnable {

        @Override
        public void run() {
            if (!isFinishing()) {
                mPresenter.takeToNextLevel();
            }
        }
    }
}
