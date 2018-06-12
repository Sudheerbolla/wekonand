package com.weekend.interfaces;

import android.view.View;

import com.quickblox.chat.model.QBChatMessage;

/**
 * Created by hb on 30/11/2016.
 */

public interface IChatMessageSelectListener {
    void onClick(View view, QBChatMessage qBChatMessage, int position);
}

