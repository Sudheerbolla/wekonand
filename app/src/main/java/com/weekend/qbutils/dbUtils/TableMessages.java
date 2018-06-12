package com.weekend.qbutils.dbUtils;

public class TableMessages {
    public static String DIALOG_ID, SENDER_ID, RECEIVER_ID, ATTACHMENT_URL, ATTACHMENT_ID, TIMESTAMP, ID, BODY, PROFILE_PIC, z_PK;
    public static final String TABLENAME = "Messages";
    public static String CREATE_TABLE;

    static {

        z_PK = "_id";
        ID = "id";
        DIALOG_ID = "dialog_id";
        BODY = "body";
        SENDER_ID = "sender_id";
        RECEIVER_ID = "receiver_id";
        ATTACHMENT_URL = "attachment_url";
        ATTACHMENT_ID = "attachment_id";
        TIMESTAMP = "timestamp";

        CREATE_TABLE = "create table if not exists " + TABLENAME + " ( "
                + z_PK + " INTEGER PRIMARY KEY NOT NULL, "
                + ID + " TEXT, "
                + DIALOG_ID + " TEXT, "
                + BODY + " TEXT, "
                + SENDER_ID + " INTEGER, "
                + RECEIVER_ID + " INTEGER, "
                + ATTACHMENT_URL + " TEXT, "
                + ATTACHMENT_ID + " TEXT, "
                + TIMESTAMP + " INTEGER,"
                + "UNIQUE (" + TIMESTAMP + ") ON CONFLICT REPLACE);";
    }

}
