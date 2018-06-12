package com.weekend.interfaces;

import android.view.View;

import com.quickblox.users.model.QBUser;

/**
 * Created by hb on 30/11/2016.
 */

public interface IUserSelectListener {
    void onClick(View view, QBUser qbUser, int position);
}
