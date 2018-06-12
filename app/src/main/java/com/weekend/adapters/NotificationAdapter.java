package com.weekend.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weekend.R;
import com.weekend.interfaces.INotificationDeleteClick;
import com.weekend.models.CustomerNotificationListModel;
import com.weekend.views.CustomTextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<CustomerNotificationListModel.Datum> notificationsList;
    private INotificationDeleteClick notificationDeleteClick;

    public NotificationAdapter(List<CustomerNotificationListModel.Datum> notificationList, INotificationDeleteClick notificationDeleteClick) {
        this.notificationsList = notificationList;
        this.notificationDeleteClick = notificationDeleteClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notifications, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvDate.setText(notificationsList.get(position).getNotificationDateTime());
        holder.tvNotification.setText(notificationsList.get(position).getNotificationText());
    }


    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_date)
        CustomTextView tvDate;
        @Bind(R.id.tv_notification)
        CustomTextView tvNotification;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.rl_row_notification, R.id.iv_delete_notification})
        public void onClick(View view) {
            int position = getAdapterPosition();
            switch (view.getId()) {
                case R.id.iv_delete_notification:
                    if (notificationDeleteClick != null) {
                        notificationDeleteClick.onDeleteClick(position);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
