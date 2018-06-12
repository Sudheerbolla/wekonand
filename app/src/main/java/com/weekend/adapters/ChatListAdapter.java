package com.weekend.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.quickblox.chat.model.QBChatDialog;
import com.weekend.R;
import com.weekend.interfaces.IOnChatDialogClick;
import com.weekend.utils.StaticUtil;
import com.weekend.views.CircleImageView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatDialogViewHolder> {

    private final IOnChatDialogClick iOnChatDialogClick;
    private ArrayList<QBChatDialog> chatDialogsArrayList;
    private Context context;

    public ChatListAdapter(Context context, ArrayList<QBChatDialog> dialogs, IOnChatDialogClick iOnChatDialogClick) {
        this.context = context;
        this.chatDialogsArrayList = dialogs;
        this.iOnChatDialogClick = iOnChatDialogClick;
    }

    @Override
    public ChatDialogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChatDialogViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dialog, parent, false));
    }

    @Override
    public void onBindViewHolder(final ChatDialogViewHolder holder, int position) {
        final QBChatDialog qbChatDialog = chatDialogsArrayList.get(position);
        holder.nameTextView.setText(qbChatDialog.getName());
        holder.lastMessageTextView.setText(prepareTextLastMessage(qbChatDialog));
        holder.txtTime.setText(StaticUtil.getDisplayTimeInChatList(qbChatDialog.getLastMessageDateSent() * 1000));
        int unreadMessagesCount = getUnreadMsgCount(qbChatDialog);
        if (unreadMessagesCount == 0) {
            holder.unreadCounterTextView.setVisibility(View.GONE);
        } else {
            holder.unreadCounterTextView.setVisibility(View.VISIBLE);
            holder.unreadCounterTextView.setText(String.valueOf(unreadMessagesCount > 99 ? "99+" : unreadMessagesCount));
        }
        String path = qbChatDialog.getPhoto();

        if (!TextUtils.isEmpty(path) && !path.equalsIgnoreCase("null"))
            Glide.with(context).load(path).into(holder.dialogImageView);
        else
            holder.dialogImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_chat_group));


        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iOnChatDialogClick != null)
                    iOnChatDialogClick.onItemClick(holder.getAdapterPosition(), qbChatDialog);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatDialogsArrayList.size();
    }

    private int getUnreadMsgCount(QBChatDialog chatDialog) {
        Integer unreadMessageCount = chatDialog.getUnreadMessageCount();
        return unreadMessageCount == null ? 0 : unreadMessageCount;
    }

    private boolean isLastMessageAttachment(QBChatDialog dialog) {
        String lastMessage = dialog.getLastMessage();
        Integer lastMessageSenderId = dialog.getLastMessageUserId();
        return TextUtils.isEmpty(lastMessage) && lastMessageSenderId != null;
    }

    private String prepareTextLastMessage(QBChatDialog chatDialog) {
        return isLastMessageAttachment(chatDialog) ? "Attachment" : chatDialog.getLastMessage();
    }

    public class ChatDialogViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.root)
        ViewGroup rootLayout;
        @Bind(R.id.image_dialog_icon)
        CircleImageView dialogImageView;
        @Bind(R.id.text_dialog_name)
        TextView nameTextView;
        @Bind(R.id.text_dialog_last_message)
        TextView lastMessageTextView;
        @Bind(R.id.text_dialog_unread_count)
        TextView unreadCounterTextView;
        @Bind(R.id.txtTime)
        TextView txtTime;

        public ChatDialogViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
