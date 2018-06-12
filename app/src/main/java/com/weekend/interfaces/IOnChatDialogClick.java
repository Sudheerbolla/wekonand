package com.weekend.interfaces;

import com.quickblox.chat.model.QBChatDialog;

/**
 * Created by ALPHABITTECH on 3/12/2018.
 */

public interface IOnChatDialogClick {
    void onItemClick(int position, QBChatDialog qbChatDialog);

    void onItemLongPres(int position, QBChatDialog qbChatDialog);
}
