package com.chatapp.screen.activity.home;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.chatapp.R;
import com.chatapp.adpapter.UserAdapter;
import com.chatapp.app.ChatApp;
import com.chatapp.model.User;
import com.chatapp.screen.activity.chat.ChatActivity;
import com.chatapp.screen.activity.login.LoginActivity;
import com.chatapp.screen.base.BaseActivity;
import com.chatapp.utils.Constants;
import com.chatapp.utils.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity implements HomeContract.View, UserAdapter.OnItemClick {

    @Inject
    HomePresenter mPresenter;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private UserAdapter mAdapter;

    @Override
    public void onSuccess(Object response, int apiCode) {
        switch (apiCode) {
            case HomePresenter.LOG_OUT:
                startActivity(LoginActivity.class, null, null, true);
                break;
            case HomePresenter.GET_USERS:
                List<User> users = (List<User>) response;
                if (users == null)
                    return;
                mAdapter.addUsers(users);
                break;
        }
    }

    @Override
    public void onError(String message, int apiCode) {
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
        initToolBar(false, "Chat");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this));
        mAdapter = new UserAdapter(new ArrayList<User>());
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mPresenter.getUserList(HomePresenter.GET_USERS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out: {
                mPresenter.doSignOut();
                break;
            }
            // case blocks for other MenuItems (if any)
        }
        return false;
    }

    @Override
    protected void bindView() {
        setUnBinder(ButterKnife.bind(this));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onItemClicked(User user, int position) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(Constants.ARG_RECEIVER, user.email);
        intent.putExtra(Constants.ARG_RECEIVER_UID, user.uid);
        intent.putExtra(Constants.ARG_FIREBASE_TOKEN, user.fireBaseToken);
        startActivity(intent);
    }
}
