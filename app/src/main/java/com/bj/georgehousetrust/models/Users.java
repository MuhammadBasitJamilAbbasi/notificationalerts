package com.bj.georgehousetrust.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Users implements Parcelable {
    public Users(String name, String email, String password, String phonenumber, String skills, String intrest) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phonenumber = phonenumber;
        this.skills = skills;
        this.intrest = intrest;
    }

    String name,email,password,phonenumber,skills,intrest;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getIntrest() {
        return intrest;
    }

    public void setIntrest(String intrest) {
        this.intrest = intrest;
    }

    protected Users(Parcel in) {
        name = in.readString();
        phonenumber = in.readString();
        email = in.readString();
        password = in.readString();
        skills = in.readString();
        intrest = in.readString();
    }

    public static final Creator<Users> CREATOR = new Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phonenumber);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(skills);
        dest.writeString(intrest);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
