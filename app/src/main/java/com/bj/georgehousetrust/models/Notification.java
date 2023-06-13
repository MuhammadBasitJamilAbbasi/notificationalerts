package com.bj.georgehousetrust.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Notification implements Parcelable {
    private String id;
    private String title;
    private String message;

    public Notification() {

    }
    public Notification(String id,String title, String message) {
        this.title = title;
        this.message = message;
        this.id=id;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected Notification(Parcel in) {
        title = in.readString();
        message = in.readString();
        id=in.readString();

    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(message);
        dest.writeString(id);

    }
}
