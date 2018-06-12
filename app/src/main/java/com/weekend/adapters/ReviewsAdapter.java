package com.weekend.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.weekend.R;
import com.weekend.models.PropertyReviewListModel;
import com.weekend.views.CustomTextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {


    private List<PropertyReviewListModel.PropertyReview> propertyReviewList;
    private Context context;

    public ReviewsAdapter(Context context, List<PropertyReviewListModel.PropertyReview> propertyReviewList) {
        this.propertyReviewList = propertyReviewList;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reviews, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        PropertyReviewListModel.PropertyReview reviews = propertyReviewList.get(position);
        holder.tvUserName.setText(reviews.getCustomerName());
        holder.tvUserDate.setText(reviews.getReviewDate());
        holder.tvUserReview.setText(reviews.getReview());
        holder.tvRating.setText(reviews.getRating());
        if (reviews.getOwnerReply().equalsIgnoreCase("Yes")) {
            holder.llOwner.setVisibility(View.VISIBLE);
            holder.llOwner.setSelected(true);
            holder.tvOwnerDate.setText(reviews.getReplyDate());
            holder.tvOwnerReview.setText(reviews.getReplyText());
        }
    }

    @Override
    public int getItemCount() {
        return propertyReviewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_rating)
        CustomTextView tvRating;
        @Bind(R.id.tv_user_name)
        CustomTextView tvUserName;
        @Bind(R.id.tv_user_date)
        CustomTextView tvUserDate;
        @Bind(R.id.tv_user_review)
        CustomTextView tvUserReview;
        @Bind(R.id.tv_owner_date)
        CustomTextView tvOwnerDate;
        @Bind(R.id.tv_owner_review)
        CustomTextView tvOwnerReview;
        @Bind(R.id.ll_owner)
        LinearLayout llOwner;
        @Bind(R.id.ll_row_review)
        LinearLayout llRowReview;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
