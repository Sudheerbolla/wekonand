package com.weekend.adapters;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.core.helper.CollectionsUtil;
import com.weekend.R;
import com.weekend.base.WeekendApplication;
import com.weekend.interfaces.IChatMessageSelectListener;
import com.weekend.models.AdminUsersModel;
import com.weekend.qbutils.dbUtils.DbHelper;
import com.weekend.utils.StaticUtil;
import com.weekend.views.CircleImageView;
import com.weekend.views.CustomTextView;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChatMessagesListAdapter extends RecyclerView.Adapter<ChatMessagesListAdapter.ChatViewHolder> {

    private static final String TAG = ChatMessagesListAdapter.class.getSimpleName();
    private QBChatDialog qbChatDialog;
    private int previousGetCount = 0;
    private Context context;
    private IChatMessageSelectListener iChatMessageSelectListener;
    private List<QBChatMessage> qBChatMessageArrayList;
    private static int OUTGOING = 0, INCOMING = 1;
    private ArrayList<AdminUsersModel> adminUsersModelArrayList;

    public ChatMessagesListAdapter(Context context, QBChatDialog chatDialog, List<QBChatMessage> chatMessages, IChatMessageSelectListener iChatMessageSelectListener) {
        this.context = context;
        this.qbChatDialog = chatDialog;
        this.qBChatMessageArrayList = chatMessages;
        this.iChatMessageSelectListener = iChatMessageSelectListener;
        this.adminUsersModelArrayList = new ArrayList<>();
        adminUsersModelArrayList.addAll(WeekendApplication.adminUsersModelArrayList);
    }

    @Override
    public int getItemViewType(int position) {
        QBChatMessage chatMessage = qBChatMessageArrayList.get(position);
        return (chatMessage.getSenderId() != null && !chatMessage.getSenderId().equals(WeekendApplication.loggedInQBUser.getId())) ? INCOMING : OUTGOING;
    }

    private boolean isRead(QBChatMessage chatMessage) {
        Integer currentUserId = WeekendApplication.loggedInQBUser.getId();
        return !CollectionsUtil.isEmpty(chatMessage.getReadIds()) && chatMessage.getReadIds().contains(currentUserId);
    }

    public void updateChatDialog(QBChatDialog dialog) {
        qbChatDialog = dialog;
    }

    private void readMessage(QBChatMessage chatMessage) {
        try {
            if (qbChatDialog != null) {
                qbChatDialog.readMessage(chatMessage);
                new DbHelper(context).updateUnreadMessageCount(qbChatDialog.getDialogId(), 0);
            }
        } catch (XMPPException | SmackException.NotConnectedException e) {
            Log.w(TAG, e);
        }
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chat_message, parent, false));
    }

    @Override
    public void onBindViewHolder(final ChatViewHolder holder, final int position) {
        final QBChatMessage chatMessage = qBChatMessageArrayList.get(position);
        holder.qBChatMessage = chatMessage;
        holder.setPosition(position);
        setVisbilitiesGone(holder);
        int messageType = getItemViewType(position);
        if (messageType == OUTGOING) {
            setOutGoingMessageLayout(holder, chatMessage);
        } else {
            setIncomingMessageLayout(holder, chatMessage);
            String path = "";
            if (chatMessage.getSenderId() != null && !chatMessage.getSenderId().equals(WeekendApplication.loggedInQBUser.getId())) {
                path = StaticUtil.getProfilePic(chatMessage.getSenderId(), adminUsersModelArrayList);
            }
            if (!TextUtils.isEmpty(path))
                Glide.with(context).load(path).into(holder.imageViewProfilePic);
            else
                holder.imageViewProfilePic.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_chat_group));
            clearNotifications(chatMessage.getId());
        }

        holder.relMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StaticUtil.hasAttachments(chatMessage)) {
                    iChatMessageSelectListener.onClick(v, chatMessage, position);
                }
            }
        });

        if (messageType == INCOMING && !isRead(chatMessage)) {
            readMessage(chatMessage);
        }
    }

    private void clearNotifications(String id) {
        try {
            int nid = Integer.parseInt(id);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(nid);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void setVisbilitiesGone(ChatViewHolder holder) {
        holder.txtUserMessageOutGoing.setVisibility(View.GONE);
        holder.txtUserMessageIncoming.setVisibility(View.GONE);

        holder.txtIncomingTimeImage.setVisibility(View.GONE);
        holder.txtIncomingTimeText.setVisibility(View.GONE);
        holder.txtOutGoingTimeImage.setVisibility(View.GONE);
        holder.txtOutGoingTimeText.setVisibility(View.GONE);

        holder.imageViewAttachmentIncoming.setVisibility(View.GONE);
        holder.imageViewAttachmentOutGoing.setVisibility(View.GONE);

        holder.imageViewProfilePic.setVisibility(View.GONE);

        holder.relIncoming.setVisibility(View.GONE);
        holder.relOutGoing.setVisibility(View.GONE);
    }

    private void setIncomingMessageLayout(final ChatViewHolder holder, final QBChatMessage qBChatMessage) {
        holder.relIncoming.setVisibility(View.VISIBLE);
        holder.imageViewProfilePic.setVisibility(View.VISIBLE);
        if (StaticUtil.hasAttachments(qBChatMessage)) {
            if (qBChatMessage.getAttachments() != null && qBChatMessage.getAttachments().size() > 0) {
                Collection<QBAttachment> attachments = qBChatMessage.getAttachments();
                for (QBAttachment attachment : attachments) {
                    holder.relProgressIncoming.setVisibility(View.VISIBLE);
                    Glide.with(context).load(attachment.getUrl()).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            holder.relProgressIncoming.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.relProgressIncoming.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(holder.imageViewAttachmentIncoming);
                }
                holder.relProgressIncoming.setVisibility(View.GONE);
                holder.imageViewAttachmentIncoming.setVisibility(View.VISIBLE);
                holder.imageViewAttachmentIncoming.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (iChatMessageSelectListener != null)
                            iChatMessageSelectListener.onClick(holder.imageViewAttachmentIncoming, qBChatMessage, holder.getAdapterPosition());
                    }
                });
                holder.txtIncomingTimeImage.setVisibility(View.VISIBLE);
                holder.txtIncomingTimeImage.setText(StaticUtil.getTime(qBChatMessage.getDateSent()));
            } else {
                holder.relProgressIncoming.setVisibility(View.GONE);
                holder.imageViewAttachmentIncoming.setVisibility(View.GONE);
            }
        } else {
            holder.txtUserMessageIncoming.setVisibility(View.VISIBLE);
            holder.relProgressIncoming.setVisibility(View.GONE);
            holder.imageViewAttachmentIncoming.setVisibility(View.GONE);
            holder.txtUserMessageIncoming.setText(qBChatMessage.getBody());
            holder.txtIncomingTimeText.setVisibility(View.VISIBLE);
            holder.txtIncomingTimeText.setText(StaticUtil.getTime(qBChatMessage.getDateSent()));
        }
    }

    private void setOutGoingMessageLayout(final ChatViewHolder holder, final QBChatMessage qBChatMessage) {
        holder.relOutGoing.setVisibility(View.VISIBLE);
        if (StaticUtil.hasAttachments(qBChatMessage)) {
            if (!TextUtils.isEmpty(qBChatMessage.getAttachments().toString())) {
                Collection<QBAttachment> attachments = qBChatMessage.getAttachments();
                for (QBAttachment attachment : attachments) {
                    holder.relProgressOutGoing.setVisibility(View.VISIBLE);
                    Glide.with(context).load(attachment.getUrl()).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            holder.relProgressOutGoing.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.relProgressOutGoing.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(holder.imageViewAttachmentOutGoing);
                }
                holder.imageViewAttachmentOutGoing.setVisibility(View.VISIBLE);
                holder.imageViewAttachmentOutGoing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (iChatMessageSelectListener != null)
                            iChatMessageSelectListener.onClick(holder.imageViewAttachmentOutGoing, qBChatMessage, holder.getAdapterPosition());
                    }
                });
                holder.txtOutGoingTimeImage.setVisibility(View.VISIBLE);
                holder.txtOutGoingTimeImage.setText(StaticUtil.getTime(qBChatMessage.getDateSent()));
            } else {
                holder.relProgressOutGoing.setVisibility(View.GONE);
                holder.imageViewAttachmentOutGoing.setVisibility(View.GONE);
            }
            holder.txtUserMessageOutGoing.setVisibility(View.GONE);
        } else {
            holder.imageViewAttachmentOutGoing.setVisibility(View.GONE);
            holder.txtUserMessageOutGoing.setText(qBChatMessage.getBody());
            holder.relProgressOutGoing.setVisibility(View.GONE);
            holder.txtUserMessageOutGoing.setVisibility(View.VISIBLE);
            holder.txtOutGoingTimeText.setVisibility(View.VISIBLE);
            holder.txtOutGoingTimeText.setText(StaticUtil.getTime(qBChatMessage.getDateSent()));
        }
    }

    @Override
    public int getItemCount() {
        return qBChatMessageArrayList.size();
    }

    public List<QBChatMessage> getList() {
        return qBChatMessageArrayList;
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.imageViewAttachmentIncoming)
        ImageView imageViewAttachmentIncoming;
        @Bind(R.id.imageViewProfilePic)
        CircleImageView imageViewProfilePic;
        @Bind(R.id.relProgressIncoming)
        RelativeLayout relProgressIncoming;
        @Bind(R.id.txtUserMessageIncoming)
        CustomTextView txtUserMessageIncoming;
        @Bind(R.id.relIncoming)
        RelativeLayout relIncoming;
        @Bind(R.id.imageViewAttachmentOutGoing)
        ImageView imageViewAttachmentOutGoing;
        @Bind(R.id.relProgressOutGoing)
        RelativeLayout relProgressOutGoing;
        @Bind(R.id.txtUserMessageOutGoing)
        CustomTextView txtUserMessageOutGoing;
        @Bind(R.id.txtIncomingTimeImage)
        CustomTextView txtIncomingTimeImage;
        @Bind(R.id.txtIncomingTimeText)
        CustomTextView txtIncomingTimeText;
        @Bind(R.id.txtOutGoingTimeImage)
        CustomTextView txtOutGoingTimeImage;
        @Bind(R.id.txtOutGoingTimeText)
        CustomTextView txtOutGoingTimeText;
        @Bind(R.id.relOutGoing)
        RelativeLayout relOutGoing;
        @Bind(R.id.relMessage)
        RelativeLayout relMessage;

        private int position = -1;
        private QBChatMessage qBChatMessage;

        private void setPosition(int position) {
            this.position = position;
        }

        public ChatViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
