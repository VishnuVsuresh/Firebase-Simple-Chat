package com.chatapp.screen.activity.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.chatapp.model.User;
import com.chatapp.screen.base.Presenter;
import com.chatapp.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by vishnu on 24-01-2018.
 */

public class HomePresenter extends Presenter<HomeContract.View> implements HomeContract.Presenter {

    public static final int GET_USERS = 1;
    public static final int LOG_OUT = 10;
    private Context mContext;
    private FirebaseAuth mAuth;
    private ValueEventListener mValueEventListener;

    @Inject
    public HomePresenter(Context context) {
        this.mContext=context;
        mAuth=FirebaseAuth.getInstance();
    }

    @Override
    public void getUserList(final int apiCode) {
        mValueEventListener= FirebaseDatabase.getInstance().getReference().child(Constants.ARG_USERS).addValueEventListener(getValueEventListener(apiCode));
    }

    @NonNull
    private ValueEventListener getValueEventListener(final int apiCode) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
                List<User> users = new ArrayList<>();
                while (dataSnapshots.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshots.next();
                    User user = dataSnapshotChild.getValue(User.class);
                    if (!TextUtils.equals(user.uid, FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        users.add(user);
                    }
                }
                getView().onSuccess(users,apiCode);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                getView().onError(databaseError.getMessage(),apiCode);
            }
        };
    }

    @Override
    public void doSignOut() {
        FirebaseDatabase.getInstance().getReference().child(Constants.ARG_USERS).removeEventListener(mValueEventListener);
        FirebaseAuth.getInstance().signOut();
        getView().onSuccess(null,LOG_OUT);
    }
}
