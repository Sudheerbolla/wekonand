package com.weekend.qbutils.dbUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.weekend.base.WeekendApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class DbHelper {

    private Context context;
    private DatabaseHandler databaseHandler;

    public DbHelper(Context context) {
        this.context = context;
        databaseHandler = DatabaseHandler.getInstance(context);
    }

    public synchronized long addChatMessage(QBChatMessage qbChatMessage) {
        Log.e("sudheer", "DbHelper addChatMessage");
        databaseHandler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableMessages.ID, qbChatMessage.getId());
        values.put(TableMessages.DIALOG_ID, qbChatMessage.getDialogId());
        values.put(TableMessages.BODY, qbChatMessage.getBody());
        values.put(TableMessages.SENDER_ID, qbChatMessage.getSenderId());
        values.put(TableMessages.RECEIVER_ID, qbChatMessage.getRecipientId());
        if (qbChatMessage.getAttachments() != null && qbChatMessage.getAttachments().size() > 0) {
            values.put(TableMessages.ATTACHMENT_URL, ((QBAttachment) qbChatMessage.getAttachments().toArray()[0]).getUrl());
            values.put(TableMessages.ATTACHMENT_ID, ((QBAttachment) qbChatMessage.getAttachments().toArray()[0]).getId());
        }
        values.put(TableMessages.TIMESTAMP, qbChatMessage.getDateSent());
        try {
            long affected = databaseHandler.updateData(TableMessages.TABLENAME, values, TableMessages.ID + "= ?",
                    new String[]{values.getAsString(TableMessages.ID)});
            if (affected == 0) {
                return databaseHandler.insertData(TableMessages.TABLENAME, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public synchronized void addChatMessages(ArrayList<QBChatMessage> arrayList) {
        Log.e("sudheer", "DbHelper addChatMessages");
        for (QBChatMessage qbChatMessage : arrayList) {
            addChatMessage(qbChatMessage);
        }
    }

    public synchronized ArrayList<QBChatMessage> getChatMessagesArrayList(QBChatDialog dialog) {
        Log.e("sudheer", "DbHelper getChatMessagesArrayList");
        ArrayList<QBChatMessage> arrayList = new ArrayList<>();
        String selectQuery = "select * FROM " + TableMessages.TABLENAME + " WHERE " + TableMessages.DIALOG_ID + " = '" +
                dialog.getDialogId() + "' ORDER BY " + TableMessages.TIMESTAMP + " ASC";
        databaseHandler.getReadableDatabase();
        Cursor cursor = databaseHandler.selectData(selectQuery, true);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                QBChatMessage qbChatMessage = new QBChatMessage();
                qbChatMessage.setProperty(TableMessages.z_PK, String.valueOf(cursor.getInt(cursor.getColumnIndex(TableMessages.z_PK))));
                qbChatMessage.setId(cursor.getString(cursor.getColumnIndex(TableMessages.ID)));
                qbChatMessage.setDialogId(cursor.getString(cursor.getColumnIndex(TableMessages.DIALOG_ID)));
                qbChatMessage.setBody(cursor.getString(cursor.getColumnIndex(TableMessages.BODY)));
                qbChatMessage.setSenderId(cursor.getInt(cursor.getColumnIndex(TableMessages.SENDER_ID)));
//                qbChatMessage.setRecipientId(cursor.getInt(cursor.getColumnIndex(TableMessages.RECEIVER_ID)));

                if (!TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex(TableMessages.ATTACHMENT_URL)))) {
                    QBAttachment qbAttachment = new QBAttachment(QBAttachment.PHOTO_TYPE);
                    qbAttachment.setId(cursor.getString(cursor.getColumnIndex(TableMessages.ATTACHMENT_ID)));
                    qbAttachment.setUrl(cursor.getString(cursor.getColumnIndex(TableMessages.ATTACHMENT_URL)));
                    HashMap<File, QBAttachment> fileQBAttachmentHashMap = new HashMap<>();
                    fileQBAttachmentHashMap.put(null, qbAttachment);
                    qbChatMessage.setAttachments(new HashSet<>(fileQBAttachmentHashMap.values()));
                }

                qbChatMessage.setDateSent(cursor.getInt(cursor.getColumnIndex(TableMessages.TIMESTAMP)));
                arrayList.add(qbChatMessage);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return arrayList;
    }

    public synchronized QBChatMessage getLastChatMessage(String dialogId) {
        QBChatMessage qbChatMessage = new QBChatMessage();
        String selectQuery = "select * FROM " + TableMessages.TABLENAME + " WHERE " + TableMessages.DIALOG_ID + " = '" + dialogId + "' ORDER BY " + TableMessages.TIMESTAMP + " DESC LIMIT 1";
        databaseHandler.getReadableDatabase();
        Cursor cursor = databaseHandler.selectData(selectQuery, true);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                qbChatMessage.setProperty(TableMessages.z_PK, String.valueOf(cursor.getInt(cursor.getColumnIndex(TableMessages.z_PK))));
                qbChatMessage.setId(cursor.getString(cursor.getColumnIndex(TableMessages.ID)));
                qbChatMessage.setDialogId(cursor.getString(cursor.getColumnIndex(TableMessages.DIALOG_ID)));
                qbChatMessage.setBody(cursor.getString(cursor.getColumnIndex(TableMessages.BODY)));
                qbChatMessage.setSenderId(cursor.getInt(cursor.getColumnIndex(TableMessages.SENDER_ID)));

                if (!TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex(TableMessages.ATTACHMENT_URL)))) {
                    QBAttachment qbAttachment = new QBAttachment(QBAttachment.PHOTO_TYPE);
                    qbAttachment.setId(cursor.getString(cursor.getColumnIndex(TableMessages.ATTACHMENT_ID)));
                    qbAttachment.setUrl(cursor.getString(cursor.getColumnIndex(TableMessages.ATTACHMENT_URL)));
                    HashMap<File, QBAttachment> fileQBAttachmentHashMap = new HashMap<>();
                    fileQBAttachmentHashMap.put(null, qbAttachment);
                    qbChatMessage.setAttachments(new HashSet<>(fileQBAttachmentHashMap.values()));
                }

                qbChatMessage.setDateSent(cursor.getInt(cursor.getColumnIndex(TableMessages.TIMESTAMP)));
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return qbChatMessage;
    }

    public ArrayList<QBChatMessage> getChatMessagesArrayListUnSync(QBChatDialog dialog) {
        Log.e("sudheer", "DbHelper getChatMessagesArrayList");
        ArrayList<QBChatMessage> arrayList = new ArrayList<>();
        String selectQuery = "select * FROM " + TableMessages.TABLENAME + " WHERE " + TableMessages.DIALOG_ID + " = '" +
                dialog.getDialogId() + "' ORDER BY " + TableMessages.TIMESTAMP + " ASC";
        databaseHandler.getReadableDatabase();
        Cursor cursor = databaseHandler.selectData(selectQuery, true);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                QBChatMessage qbChatMessage = new QBChatMessage();
                qbChatMessage.setProperty(TableMessages.z_PK, String.valueOf(cursor.getInt(cursor.getColumnIndex(TableMessages.z_PK))));
                qbChatMessage.setId(cursor.getString(cursor.getColumnIndex(TableMessages.ID)));
                qbChatMessage.setDialogId(cursor.getString(cursor.getColumnIndex(TableMessages.DIALOG_ID)));
                qbChatMessage.setBody(cursor.getString(cursor.getColumnIndex(TableMessages.BODY)));
                qbChatMessage.setSenderId(cursor.getInt(cursor.getColumnIndex(TableMessages.SENDER_ID)));
                qbChatMessage.setRecipientId(cursor.getInt(cursor.getColumnIndex(TableMessages.RECEIVER_ID)));

                if (!TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex(TableMessages.ATTACHMENT_URL)))) {
                    QBAttachment qbAttachment = new QBAttachment(QBAttachment.PHOTO_TYPE);
                    qbAttachment.setId(cursor.getString(cursor.getColumnIndex(TableMessages.ATTACHMENT_ID)));
                    qbAttachment.setUrl(cursor.getString(cursor.getColumnIndex(TableMessages.ATTACHMENT_URL)));
                    HashMap<File, QBAttachment> fileQBAttachmentHashMap = new HashMap<>();
                    fileQBAttachmentHashMap.put(null, qbAttachment);
                    qbChatMessage.setAttachments(new HashSet<>(fileQBAttachmentHashMap.values()));
                }

                qbChatMessage.setDateSent(cursor.getInt(cursor.getColumnIndex(TableMessages.TIMESTAMP)));
                arrayList.add(qbChatMessage);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return arrayList;
    }

    /**
     * return message having z_PK>lastMessagePK
     *
     * @param dialogID
     * @param lastMessagePK
     * @return
     */
    public synchronized ArrayList<QBChatMessage> getChatMessagesArrayList(String dialogID, int lastMessagePK) {
        Log.e("sudheer", "DbHelper getChatMessagesArrayList");
        ArrayList<QBChatMessage> arrayList = new ArrayList<>();
        String selectQuery = "select * FROM " + TableMessages.TABLENAME + " WHERE " + TableMessages.DIALOG_ID + " = '" +
                dialogID + "' AND " + TableMessages.z_PK + ">" + lastMessagePK + " ORDER BY " + TableMessages.TIMESTAMP + " ASC";
        databaseHandler.getReadableDatabase();
        Cursor cursor = databaseHandler.selectData(selectQuery, true);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                QBChatMessage qbChatMessage = new QBChatMessage();
                qbChatMessage.setProperty(TableMessages.z_PK, String.valueOf(cursor.getInt(cursor.getColumnIndex(TableMessages.z_PK))));
                qbChatMessage.setId(cursor.getString(cursor.getColumnIndex(TableMessages.ID)));
                qbChatMessage.setDialogId(cursor.getString(cursor.getColumnIndex(TableMessages.DIALOG_ID)));
                qbChatMessage.setBody(cursor.getString(cursor.getColumnIndex(TableMessages.BODY)));
                qbChatMessage.setSenderId(cursor.getInt(cursor.getColumnIndex(TableMessages.SENDER_ID)));
                qbChatMessage.setRecipientId(cursor.getInt(cursor.getColumnIndex(TableMessages.RECEIVER_ID)));

                if (!TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex(TableMessages.ATTACHMENT_URL)))) {
                    QBAttachment qbAttachment = new QBAttachment(QBAttachment.PHOTO_TYPE);
                    qbAttachment.setId(cursor.getString(cursor.getColumnIndex(TableMessages.ATTACHMENT_ID)));
                    qbAttachment.setUrl(cursor.getString(cursor.getColumnIndex(TableMessages.ATTACHMENT_URL)));
                    HashMap<File, QBAttachment> fileQBAttachmentHashMap = new HashMap<>();
                    fileQBAttachmentHashMap.put(null, qbAttachment);
                    qbChatMessage.setAttachments(new HashSet<>(fileQBAttachmentHashMap.values()));
                }

                qbChatMessage.setDateSent(cursor.getInt(cursor.getColumnIndex(TableMessages.TIMESTAMP)));
                arrayList.add(qbChatMessage);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return arrayList;
    }

    public synchronized void updateChatDialog(QBChatDialog qbChatDialog) {
        Log.e("sudheer", "DbHelper updateChatDialog");
        ContentValues values = new ContentValues();
        values.put(TableChatList.DIALOG_ID, qbChatDialog.getDialogId());
        values.put(TableChatList.NAME, qbChatDialog.getName());
        values.put(TableChatList.LAST_MESSAGE, qbChatDialog.getLastMessage());
        values.put(TableChatList.LAST_MESSAGE_DATE_SENT, qbChatDialog.getLastMessageDateSent());
        values.put(TableChatList.UNREAD_MESSAGE_COUNT, qbChatDialog.getUnreadMessageCount());
        values.put(TableChatList.PHOTO, qbChatDialog.getPhoto());

        databaseHandler.updateData(TableChatList.TABLENAME, values, TableChatList.DIALOG_ID + "= ?", new String[]{qbChatDialog.getDialogId()});
    }

    public synchronized void updateLastMessageInDialog(String dialogId, String message, long datetime) {
        ContentValues valuess = new ContentValues();
        valuess.put(TableChatList.LAST_MESSAGE, message);
        valuess.put(TableChatList.LAST_MESSAGE_DATE_SENT, datetime);
        databaseHandler.updateData(TableChatList.TABLENAME, valuess, TableChatList.DIALOG_ID + "=?", new String[]{dialogId});
    }

    public synchronized void updateUnreadMessageCount(String dialogId, int count) {
        ContentValues values = new ContentValues();
        values.put(TableChatList.UNREAD_MESSAGE_COUNT, count);
        databaseHandler.updateData(TableChatList.TABLENAME, values, TableChatList.DIALOG_ID + "=?", new String[]{dialogId});
    }

    public synchronized void updateUnreadMessageCount(QBChatDialog qbChatDialog, int count) {
        databaseHandler.getWritableDatabase();
        String where = TableChatList.DIALOG_ID + "=?";
        String[] args = new String[]{qbChatDialog.getDialogId()};
        ContentValues values = new ContentValues();
        values.put(TableChatList.UNREAD_MESSAGE_COUNT, count);
        values.put(TableChatList.LAST_MESSAGE_DATE_SENT, qbChatDialog.getLastMessageDateSent());
        values.put(TableChatList.LAST_MESSAGE, qbChatDialog.getLastMessage());
        databaseHandler.updateData(TableChatList.TABLENAME, values, where, args);
    }

    public synchronized void addChatDialog(QBChatDialog qbChatDialog) {
        Log.e("sudheer", "DbHelper addChatDialog");
        databaseHandler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableChatList.DIALOG_ID, qbChatDialog.getDialogId());
        values.put(TableChatList.NAME, qbChatDialog.getName());
        values.put(TableChatList.LAST_MESSAGE, qbChatDialog.getLastMessage());
        values.put(TableChatList.LAST_MESSAGE_DATE_SENT, qbChatDialog.getLastMessageDateSent());
        values.put(TableChatList.UNREAD_MESSAGE_COUNT, qbChatDialog.getUnreadMessageCount());
        values.put(TableChatList.PHOTO, qbChatDialog.getPhoto());
        if (qbChatDialog.getOccupants() != null && !qbChatDialog.getOccupants().isEmpty()) {
            JSONObject occupants = new JSONObject();
            if (qbChatDialog.getOccupants().contains(WeekendApplication.loggedInQBUser.getId()))
                qbChatDialog.getOccupants().remove(WeekendApplication.loggedInQBUser.getId());
            try {
                occupants.put(TableChatList.OCCUPANTS, new JSONArray(qbChatDialog.getOccupants()));
                values.put(TableChatList.OCCUPANTS, occupants.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            int affected = databaseHandler.updateData(TableChatList.TABLENAME, values, TableChatList.DIALOG_ID + "= ?", new String[]{values.getAsString(TableChatList.DIALOG_ID)});
            if (affected == 0) {
                databaseHandler.insertData(TableChatList.TABLENAME, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void addChatDialogs(ArrayList<QBChatDialog> chatDialogArrayList) {
        Log.e("sudheer", "DbHelper addChatDialogs");
        for (QBChatDialog qbChatDialog : chatDialogArrayList) {
            addChatDialog(qbChatDialog);
        }
    }

    public synchronized ArrayList<QBChatDialog> getChatDialogs() {
        String selectQuery = "select * FROM " + TableChatList.TABLENAME + " ORDER BY " + TableChatList.LAST_MESSAGE_DATE_SENT + " DESC";
        databaseHandler.getReadableDatabase();
        ArrayList<QBChatDialog> arrayList = new ArrayList<>();
        Cursor cursor = databaseHandler.selectData(selectQuery, true);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                QBChatDialog qbChatDialog = new QBChatDialog();
                qbChatDialog.setDialogId(cursor.getString(cursor.getColumnIndex(TableChatList.DIALOG_ID)));
                qbChatDialog.setName(cursor.getString(cursor.getColumnIndex(TableChatList.NAME)));
                qbChatDialog.setLastMessage(cursor.getString(cursor.getColumnIndex(TableChatList.LAST_MESSAGE)));
                qbChatDialog.setLastMessageDateSent(cursor.getInt(cursor.getColumnIndex(TableChatList.LAST_MESSAGE_DATE_SENT)));
                qbChatDialog.setUnreadMessageCount(cursor.getInt(cursor.getColumnIndex(TableChatList.UNREAD_MESSAGE_COUNT)));
                qbChatDialog.setPhoto(cursor.getString(cursor.getColumnIndex(TableChatList.PHOTO)));
                String occupants = cursor.getString(cursor.getColumnIndex(TableChatList.OCCUPANTS));
                if (!TextUtils.isEmpty(occupants)) {
                    try {
                        JSONArray items = new JSONObject(cursor.getString(cursor.getColumnIndex(TableChatList.OCCUPANTS))).optJSONArray(TableChatList.OCCUPANTS);
                        ArrayList<Integer> occupantIds = new ArrayList<>();
                        for (int i = 0; i < items.length(); i++) {
                            occupantIds.add(items.optInt(i));
                        }
                        qbChatDialog.setOccupantsIds(occupantIds);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                arrayList.add(qbChatDialog);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return arrayList;
    }

    public synchronized QBChatDialog getChatDialog(String dialogId) {
        Log.e("sudheer", "DbHelper getChatDialog");
        String selectQuery = "select * FROM " + TableChatList.TABLENAME + " WHERE " + TableChatList.DIALOG_ID + " = '" + dialogId + "'";
        QBChatDialog qbChatDialog = null;
        databaseHandler.getReadableDatabase();
        Cursor cursor = databaseHandler.selectData(selectQuery, true);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                qbChatDialog = new QBChatDialog();
                qbChatDialog.setDialogId(cursor.getString(cursor.getColumnIndex(TableChatList.DIALOG_ID)));
                qbChatDialog.setName(cursor.getString(cursor.getColumnIndex(TableChatList.NAME)));
                qbChatDialog.setLastMessage(cursor.getString(cursor.getColumnIndex(TableChatList.LAST_MESSAGE)));
                qbChatDialog.setLastMessageDateSent(cursor.getInt(cursor.getColumnIndex(TableChatList.LAST_MESSAGE_DATE_SENT)));
                qbChatDialog.setUnreadMessageCount(cursor.getInt(cursor.getColumnIndex(TableChatList.UNREAD_MESSAGE_COUNT)));
                qbChatDialog.setPhoto(cursor.getString(cursor.getColumnIndex(TableChatList.PHOTO)));
                try {
                    JSONArray items = new JSONObject(cursor.getString(cursor.getColumnIndex(TableChatList.OCCUPANTS))).optJSONArray(TableChatList.OCCUPANTS);
                    ArrayList<Integer> occupantIds = new ArrayList<>();
                    for (int i = 0; i < items.length(); i++) {
                        occupantIds.add(items.getInt(i));
                    }
                    qbChatDialog.setOccupantsIds(occupantIds);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return qbChatDialog;
    }

    public synchronized void deleteChatHistory(String dialogId) {
        String where = TableMessages.DIALOG_ID + "=?";
        String[] args = new String[]{dialogId};
        databaseHandler.getWritableDatabase();
        int affected = databaseHandler.deleteData(TableMessages.TABLENAME, where, args);
    }

    public synchronized void deleteChatDialog(String dialogId) {
        databaseHandler.deleteData(TableChatList.TABLENAME, TableChatList.DIALOG_ID + "=?", new String[]{dialogId});
    }

    public synchronized void deleteAllChatDialogs() {
        databaseHandler.clearSingleTable(TableChatList.TABLENAME);
        databaseHandler.close();
    }

    public synchronized void deleteChatHistory(ArrayList<QBChatMessage> arrayList) {
        Log.e("sudheer", "DbHelper addChatMessages");
        for (QBChatMessage qbChatMessage : arrayList) {
//            addChatMessage(qbChatMessage);
        }
    }

    public void closeDb() {
        databaseHandler.clearDB();
        databaseHandler.close();
    }

}
