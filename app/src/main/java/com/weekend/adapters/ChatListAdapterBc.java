/*
package com.weekend.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.users.model.QBUser;
import com.weekend.R;
import com.weekend.interfaces.IOnChatDialogClick;
import com.weekend.qbutils.QbUsersHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChatListAdapterBc extends RecyclerView.Adapter<ChatListAdapterBc.ChatDialogViewHolder> {

    private static final String EMPTY_STRING = "";
    private final IOnChatDialogClick iOnChatDialogClick;
    private Context context;
    private List<QBChatDialog> dialogs = new ArrayList<>();

    public ChatListAdapterBc(Context context, List<QBChatDialog> dialogs, IOnChatDialogClick iOnChatDialogClick) {
        this.context = context;
        this.dialogs = dialogs;
        this.iOnChatDialogClick = iOnChatDialogClick;
    }

    @Override
    public ChatDialogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dialog, parent, false);
        ChatDialogViewHolder viewHolder = new ChatDialogViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ChatDialogViewHolder holder, int position) {
        final QBChatDialog dialog = dialogs.get(position);
        if (dialog.getType().equals(QBDialogType.GROUP)) {
//            holder.dialogImageView.setBackgroundDrawable(UiUtils.getGreyCircleDrawable());
            holder.dialogImageView.setImageResource(R.drawable.ic_chat_group);
        } else {
//            holder.dialogImageView.setBackgroundDrawable(UiUtils.getColorCircleDrawable(position));
            holder.dialogImageView.setImageDrawable(null);
        }

        holder.nameTextView.setText(getDialogName(dialog));
        holder.lastMessageTextView.setText(prepareTextLastMessage(dialog));

        int unreadMessagesCount = getUnreadMsgCount(dialog);
        if (unreadMessagesCount == 0) {
            holder.unreadCounterTextView.setVisibility(View.GONE);
        } else {
            holder.unreadCounterTextView.setVisibility(View.VISIBLE);
            holder.unreadCounterTextView.setText(String.valueOf(unreadMessagesCount > 99 ? "99+" : unreadMessagesCount));
        }

        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iOnChatDialogClick != null)
                    iOnChatDialogClick.onItemClick(holder.getAdapterPosition(), dialog);
            }
        });
    }

    private String getDialogName(QBChatDialog dialog) {
        if (dialog.getType().equals(QBDialogType.GROUP)) {
            return dialog.getName();
        } else {
            Integer opponentId = dialog.getRecipientId();
            QBUser user = QbUsersHolder.getInstance().getUserById(opponentId);
            if (user != null) {
                return TextUtils.isEmpty(user.getFullName()) ? user.getLogin() : user.getFullName();
            } else {
                return dialog.getName();
            }
        }
    }

    @Override
    public int getItemCount() {
        return dialogs.size();
    }

    private int getUnreadMsgCount(QBChatDialog chatDialog) {
        Integer unreadMessageCount = chatDialog.getUnreadMessageCount();
        if (unreadMessageCount == null) {
            return 0;
        } else {
            return unreadMessageCount;
        }
    }

    private boolean isLastMessageAttachment(QBChatDialog dialog) {
        String lastMessage = dialog.getLastMessage();
        Integer lastMessageSenderId = dialog.getLastMessageUserId();
        return TextUtils.isEmpty(lastMessage) && lastMessageSenderId != null;
    }

    private String prepareTextLastMessage(QBChatDialog chatDialog) {
        if (isLastMessageAttachment(chatDialog)) {
            return "Attachment";
        } else {
            return chatDialog.getLastMessage();
        }
    }

    public void addAll(List<QBChatDialog> qbChatDialogs) {
        this.dialogs = qbChatDialogs;
        notifyDataSetChanged();
    }

    public void updateList(ArrayList<QBChatDialog> qbChatDialogs) {
        this.dialogs = qbChatDialogs;
        notifyDataSetChanged();
    }

    public class ChatDialogViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.root)
        ViewGroup rootLayout;
        @Bind(R.id.image_dialog_icon)
        ImageView dialogImageView;
        @Bind(R.id.text_dialog_name)
        TextView nameTextView;
        @Bind(R.id.text_dialog_last_message)
        TextView lastMessageTextView;
        @Bind(R.id.text_dialog_unread_count)
        TextView unreadCounterTextView;

        public ChatDialogViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
*/
