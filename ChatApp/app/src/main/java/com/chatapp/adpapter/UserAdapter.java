package com.chatapp.adpapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chatapp.R;
import com.chatapp.model.User;

import java.util.List;

/**
 * Created by vishnu on 25-01-2018.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<User> mUsers;
    private OnItemClick onItemClick;

    public UserAdapter(List<User> users) {
        this.mUsers = users;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        User user = mUsers.get(position);

        String alphabet = user.email.substring(0, 1);

        holder.txtUsername.setText(user.email);
        holder.txtUserAlphabet.setText(alphabet);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClick != null)
                    onItemClick.onItemClicked(mUsers.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mUsers != null) {
            return mUsers.size();
        }
        return 0;
    }

    public void add(User user) {
        mUsers.add(user);
        notifyItemInserted(mUsers.size() - 1);
    }

    public void setOnItemClickListener(OnItemClick itemClickListener) {
        this.onItemClick = itemClickListener;
    }

    public void addUsers(List<User> user) {
        mUsers.clear();
        mUsers.addAll(user);
        notifyItemRangeChanged(mUsers.size() - 1, user.size());
    }

    public interface OnItemClick {
        void onItemClicked(User user, int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtUserAlphabet, txtUsername;

        ViewHolder(View itemView) {
            super(itemView);
            txtUserAlphabet = (TextView) itemView.findViewById(R.id.tv_user_alphabet);
            txtUsername = (TextView) itemView.findViewById(R.id.tv_username);

        }
    }
}
