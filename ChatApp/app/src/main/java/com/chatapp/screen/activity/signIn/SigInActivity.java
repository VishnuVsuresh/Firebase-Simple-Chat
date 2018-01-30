package com.chatapp.screen.activity.signIn;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.chatapp.R;
import com.chatapp.app.ChatApp;
import com.chatapp.screen.base.BaseActivity;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vishnu on 24-01-2018.
 */

public class SigInActivity extends BaseActivity implements SignInContract.View, View.OnClickListener {

    @Inject
    SignInPresenter mPresenter;

    @BindView(R.id.btn_sign_in)
    Button mSignIn;
    @BindView(R.id.email)
    EditText mEmail;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.tilEmail)
    TextInputLayout mTLEmail;
    @BindView(R.id.tilPassword)
    TextInputLayout mTLPassword;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @Override
    public void onSuccess(Object response, int apiCode) {
        mProgressBar.setVisibility(View.GONE);
        showToast(((FirebaseUser) response).getEmail() + " is created");
        supportFinishAfterTransition();
    }

    @Override
    public void onError(String message, int apiCode) {
        mProgressBar.setVisibility(View.GONE);
        showToast(message);
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
        mSignIn.setOnClickListener(this);
    }

    @Override
    protected void bindView() {
        setUnBinder(ButterKnife.bind(this));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_sign_in;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                if (TextUtils.isEmpty(getEmail())) {
                    mTLEmail.setError("enter your mail");
                } else if (TextUtils.isEmpty(getPassword())) {
                    mTLPassword.setError("enter your password");
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mPresenter.doSignIn(getEmail(), getPassword());
                }
                break;
        }
    }

    @NonNull
    private String getPassword() {
        return mPassword.getText().toString();
    }

    @NonNull
    private String getEmail() {
        return mEmail.getText().toString();
    }
}
