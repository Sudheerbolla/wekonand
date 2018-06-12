package com.weekend.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.weekend.R;
import com.weekend.models.AdminUsersModel;
import com.weekend.utils.StaticUtil;
import com.weekend.views.CircleImageView;
import com.weekend.views.CustomTextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AdminsAdapter extends RecyclerView.Adapter<AdminsAdapter.ViewHolder> {

    private List<AdminUsersModel> adminUsersModelList;
    private Context context;

    public AdminsAdapter(Context context, List<AdminUsersModel> adminUsersModelList) {
        this.adminUsersModelList = adminUsersModelList;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_users, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        AdminUsersModel adminUsersModel = adminUsersModelList.get(position);
        String url = StaticUtil.getImageUrl(adminUsersModel.getQbUser().getCustomData());
        if (!TextUtils.isEmpty(url)) {
            Glide.with(context.getApplicationContext()).load(url).into(holder.imgProfilePic);
        } else
            holder.imgProfilePic.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_chat_group));

        holder.txtAdminName.setText(TextUtils.isEmpty(adminUsersModel.getQbUser().getFullName()) ? adminUsersModel.getQbUser().getEmail() : adminUsersModel.getQbUser().getFullName());
    }

    @Override
    public int getItemCount() {
        return adminUsersModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.txtAdminName)
        CustomTextView txtAdminName;
        @Bind(R.id.imgProfilePic)
        CircleImageView imgProfilePic;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
