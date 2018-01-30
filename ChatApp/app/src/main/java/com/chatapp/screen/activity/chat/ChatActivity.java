package com.chatapp.screen.activity.chat;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.chatapp.R;
import com.chatapp.adpapter.ChatAdapter;
import com.chatapp.app.ChatApp;
import com.chatapp.model.Chat;
import com.chatapp.model.PushNotification;
import com.chatapp.screen.base.BaseActivity;
import com.chatapp.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vishnu on 25-01-2018.
 */

public class ChatActivity extends BaseActivity implements ChatContract.View {

    @Inject
    ChatPresenter mPresenter;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.etMessage)
    EditText mEditText;
    @BindView(R.id.sendButton)
    ImageView mSendbtn;
    @BindView(R.id.bottomLayout)
    View mBottomLayout;

    private String mReceiverMail, mReceiverId, mReceiverFBToken;
    private ChatAdapter mAdapter;

    @Override
    public void onSuccess(Object response, int apiCode) {

    }

    @Override
    public void onError(String message, int apiCode) {

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

        mReceiverMail = getIntent().getStringExtra(Constants.ARG_RECEIVER);
        mReceiverId = getIntent().getStringExtra(Constants.ARG_RECEIVER_UID);
        mReceiverFBToken = getIntent().getStringExtra(Constants.ARG_FIREBASE_TOKEN);
        mBottomLayout.setVisibility(View.VISIBLE);
        initToolBar(true, mReceiverMail);
        mAdapter = new ChatAdapter(new ArrayList<Chat>());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mSendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        mPresenter.getMessage(FirebaseAuth.getInstance().getCurrentUser().getUid(),mReceiverId);
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
    public void getOnSuccessMessage(Chat chat) {
        if (mAdapter!=null&&mRecyclerView!=null) {
            mAdapter.add(chat);
            mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
        }
    }

    @Override
    public void getOnErrorMessage(String message) {
        showToast(message);
    }

    @Override
    public void onSendMessageFailure(String message) {
        showToast(message);
    }

    @Override
    public void onSendMessageSuccess() {
        mEditText.setText("");
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ChatApp.setChatActivityOpen(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ChatApp.setChatActivityOpen(false);
    }

    private void sendMessage() {
        String message = mEditText.getText().toString();
        String receiver = mReceiverMail;
        String receiverUid = mReceiverId;
        String sender = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Chat chat = new Chat(sender,
                receiver,
                senderUid,
                receiverUid,
                message, "",
                System.currentTimeMillis());
        mPresenter.sendMessage(chat, mReceiverFBToken);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotification(PushNotification pushNotification) {
        if (mAdapter == null || mAdapter.getItemCount() == 0) {
            mPresenter.getMessage(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                    pushNotification.getUid());
        }
    }
}
