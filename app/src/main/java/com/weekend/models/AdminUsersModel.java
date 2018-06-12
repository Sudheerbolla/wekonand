package com.weekend.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.quickblox.users.model.QBUser;

/**
 * Created by Alphabit Tech on 15-03-2018.
 */

public class AdminUsersModel implements Parcelable {
    private QBUser qbUser;

    public AdminUsersModel(QBUser qbUser) {
        this.qbUser = qbUser;
    }

    protected AdminUsersModel(Parcel in) {
        this.qbUser= (QBUser) in.readSerializable();
    }

    public void setQbUser(QBUser qbUser) {
        this.qbUser = qbUser;
    }

    public QBUser getQbUser() {
        return qbUser;
    }

    public static final Creator<AdminUsersModel> CREATOR = new Creator<AdminUsersModel>() {
        @Override
        public AdminUsersModel createFromParcel(Parcel in) {
            return new AdminUsersModel(in);
        }

        @Override
        public AdminUsersModel[] newArray(int size) {
            return new AdminUsersModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.qbUser);
    }
}
