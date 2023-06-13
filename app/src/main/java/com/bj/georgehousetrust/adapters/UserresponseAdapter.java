package com.bj.georgehousetrust.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bj.georgehousetrust.R;
import com.bj.georgehousetrust.models.Notification;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserresponseAdapter extends RecyclerView.Adapter<UserresponseAdapter.NotificationViewHolder> {
    private List<Notification> notificationList;
    Context context;

    public UserresponseAdapter(List<Notification> notificationList, Context context) {
        this.notificationList = notificationList;
        this.context=context;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notificationList.get(position);
        holder.name.setText(notification.getId());
        holder.email.setText(notification.getTitle());
        holder.phone.setText(notification.getMessage());

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView email;
        public TextView phone;

        public NotificationViewHolder(View itemView) {
            super(itemView);
           name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            phone=itemView.findViewById(R.id.phone);
        }
    }
}

