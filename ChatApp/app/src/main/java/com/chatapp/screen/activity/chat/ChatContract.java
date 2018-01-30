package com.chatapp.screen.activity.chat;

import com.chatapp.model.Chat;
import com.chatapp.screen.base.BaseView;

/**
 * Created by vishnu on 25-01-2018.
 */

public class ChatContract {

    public interface View extends BaseView {
        void getOnSuccessMessage(Chat chat);
        void getOnErrorMessage(String message);
        void onSendMessageFailure(String message);
        void onSendMessageSuccess();
    }


    interface Presenter {
        void getMessage(String senderUid,String receiverUid);
        void sendMessage(Chat chat, String FBToken);
    }
}
