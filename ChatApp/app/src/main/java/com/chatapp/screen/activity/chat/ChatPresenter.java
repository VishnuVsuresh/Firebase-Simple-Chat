package com.chatapp.screen.activity.chat;

import android.content.Context;
import android.util.Log;

import com.chatapp.fcm.FcmNotificationBuilder;
import com.chatapp.model.Chat;
import com.chatapp.screen.activity.home.HomeContract;
import com.chatapp.screen.base.Presenter;
import com.chatapp.utils.Constants;
import com.chatapp.utils.LoginUtil;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

/**
 * Created by vishnu on 25-01-2018.
 */

public class ChatPresenter extends Presenter<ChatContract.View> implements ChatContract.Presenter {

    private final DatabaseReference databaseReference;
    private LoginUtil mLoginUtil;
    private Context context;

    private ChildEventListener mChildEventListener=new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Chat chat = dataSnapshot.getValue(Chat.class);
            getView().getOnSuccessMessage(chat);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
           getView().getOnErrorMessage("Unable to get message: " + databaseError.getMessage());
        }
    };

    @Inject
    public ChatPresenter(Context context, LoginUtil mLoginUtil) {
        this.context=context;
        this.mLoginUtil=mLoginUtil;
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }



    @Override
    public void getMessage(String senderUid, String receiverUid) {
        final String room_type_1 = senderUid + "_" +receiverUid;
        final String room_type_2 = receiverUid + "_" + senderUid;

        databaseReference.child(Constants.ARG_CHAT_ROOMS).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(room_type_1)) {
                    Log.e("Message", "getMessageFromFirebaseUser: " + room_type_1 + " exists");
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child(Constants.ARG_CHAT_ROOMS)
                            .child(room_type_1).addChildEventListener(mChildEventListener);
                } else if (dataSnapshot.hasChild(room_type_2)) {
                    Log.e("Message", "getMessageFromFirebaseUser: " + room_type_2 + " exists");
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child(Constants.ARG_CHAT_ROOMS)
                            .child(room_type_2).addChildEventListener(mChildEventListener);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                getView().getOnErrorMessage("Unable to get message: " + databaseError.getMessage());
            }
        });

    }

    @Override
    public void sendMessage(final Chat chat, final String FBToken) {
        final String room_type_1 = chat.senderUid + "_" + chat.receiverUid;
        final String room_type_2 = chat.receiverUid + "_" + chat.senderUid;

        databaseReference.child(Constants.ARG_CHAT_ROOMS).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(room_type_1)) {
                    Log.e("Message", "sendMessageToFirebaseUser: " + room_type_1 + " exists");
                    databaseReference.child(Constants.ARG_CHAT_ROOMS).child(room_type_1).child(String.valueOf(chat.timestamp)).setValue(chat);
                } else if (dataSnapshot.hasChild(room_type_2)) {
                    Log.e("Message", "sendMessageToFirebaseUser: " + room_type_2 + " exists");
                    databaseReference.child(Constants.ARG_CHAT_ROOMS).child(room_type_2).child(String.valueOf(chat.timestamp)).setValue(chat);
                } else {
                    Log.e("Message", "sendMessageToFirebaseUser: success");
                    databaseReference.child(Constants.ARG_CHAT_ROOMS).child(room_type_1).child(String.valueOf(chat.timestamp)).setValue(chat);
                    getMessage(chat.senderUid, chat.receiverUid);
                }
                // send push notification to the receiver
                sendPushNotificationToReceiver(chat.sender,
                        chat.message,
                        chat.senderUid,
                        mLoginUtil.getFBAccessToken(context),
                        FBToken);
                getView().onSendMessageSuccess();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                getView().onSendMessageFailure("Unable to send message: " + databaseError.getMessage());
            }
        });
    }

    private void sendPushNotificationToReceiver(String sender, String message, String senderUid, String fbAccessToken, String receiverFirebaseToken) {
            FcmNotificationBuilder.initialize()
                    .title(sender)
                    .message(message)
                    .username(sender)
                    .uid(senderUid)
                    .firebaseToken(fbAccessToken)
                    .receiverFirebaseToken(receiverFirebaseToken)
                    .send();
    }
}
