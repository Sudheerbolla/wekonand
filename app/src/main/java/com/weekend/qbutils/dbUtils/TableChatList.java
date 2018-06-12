package com.weekend.qbutils.dbUtils;

class TableChatList {

    static String
            Z_PK,
            NAME,
            DIALOG_ID,
            LAST_MESSAGE,
            LAST_MESSAGE_DATE_SENT,
            PHOTO,
            UNREAD_MESSAGE_COUNT,
            OCCUPANTS;

    static final String TABLENAME = "ChatList";
    static String CREATE_TABLE;

    static {

        Z_PK = "_id";
        NAME = "zName";
        DIALOG_ID = "zDialogId";
        LAST_MESSAGE = "zLastMessage";
        LAST_MESSAGE_DATE_SENT = "zLastMessageDateSent";
        PHOTO = "zPhoto";
        UNREAD_MESSAGE_COUNT = "zUnreadMessageCount";
        OCCUPANTS = "zOccupants";
        CREATE_TABLE = "create table if not exists "
                + TABLENAME + " ( "
                + Z_PK + " INTEGER PRIMARY KEY NOT NULL, "
                + NAME + " TEXT, "
                + DIALOG_ID + " TEXT, "
                + LAST_MESSAGE + " TEXT, "
                + OCCUPANTS + " TEXT, "
                + LAST_MESSAGE_DATE_SENT + " INTEGER, "
                + PHOTO + " TEXT, "
                + UNREAD_MESSAGE_COUNT + " INTEGER,"
                + "UNIQUE (" + LAST_MESSAGE_DATE_SENT + ") ON CONFLICT REPLACE);";

    }

}
