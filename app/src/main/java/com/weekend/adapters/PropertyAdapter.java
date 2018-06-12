package com.weekend.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.weekend.R;
import com.weekend.interfaces.IPropertyClicks;
import com.weekend.models.PropertyListModel;
import com.weekend.utils.CommonUtil;
import com.weekend.utils.ImageUtil;
import com.weekend.views.CustomTextView;
import com.weekend.views.customimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.ViewHolder> {

    private Context mContext;
    private List<PropertyListModel.GetPropertyListing> propertyList, copyList;
    private IPropertyClicks iPropertyClick;


    public PropertyAdapter(Context context, List<PropertyListModel.GetPropertyListing> propertyList, IPropertyClicks iPropertyClick) {
        this.mContext = context;
        this.propertyList = propertyList;
        this.iPropertyClick = iPropertyClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        PropertyListModel.GetPropertyListing property = propertyList.get(position);
        if (!TextUtils.isEmpty(property.getPiImage())) {
            ImageUtil.loadPropertyImage(mContext, property.getPiImage(), holder.ivLogo);
        } else {
            holder.ivLogo.setImageResource(R.mipmap.loading_img);
        }
        if (!TextUtils.isEmpty(property.getAvgRating()) && Math.round(Float.valueOf(property.getAvgRating())) > 0) {
            holder.tvRating.setText(property.getAvgRating());
            holder.tvRating.setVisibility(View.VISIBLE);
        } else {
            holder.tvRating.setVisibility(View.GONE);
        }
        holder.tvAddress.setText(CommonUtil.insertComma(property.getNeighbourhood(), property.getCity()));
        holder.tvTitle.setText(property.getPropertyTitle());
        holder.tvPrice.setText(String.format("%s %s", property.getPrice(), mContext.getResources().getString(R.string.currency)));
        holder.ivRecommend.setVisibility(property.getMarkAsRecommended().equalsIgnoreCase("Yes") ? (View.VISIBLE) : (View.GONE));
        holder.ivVerified.setVisibility(property.getMarkAsVerified().equalsIgnoreCase("Yes") ? (View.VISIBLE) : (View.GONE));
        holder.ivReport.setVisibility(View.VISIBLE/*property.getMarkAsFeatured().equalsIgnoreCase("Yes") ? (View.VISIBLE) : (View.GONE)*/);
        holder.ivFavorite.setSelected(property.getIsFavorite().equalsIgnoreCase("Yes"));
    }

    @Override
    public int getItemCount() {
        return propertyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.tv_price)
        CustomTextView tvPrice;
        @Bind(R.id.tv_rating)
        CustomTextView tvRating;
        @Bind(R.id.tv_title)
        CustomTextView tvTitle;
        @Bind(R.id.tv_address)
        CustomTextView tvAddress;
        @Bind(R.id.iv_logo)
        RoundedImageView ivLogo;
        @Bind(R.id.iv_favorite)
        ImageView ivFavorite;
        @Bind(R.id.iv_report)
        ImageView ivReport;
        @Bind(R.id.iv_verified)
        ImageView ivVerified;
        @Bind(R.id.iv_recommend)
        ImageView ivRecommend;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.iv_favorite, R.id.iv_report, R.id.iv_logo})
        public void onClick(View view) {
            int position = getAdapterPosition();
            switch (view.getId()) {
                case R.id.iv_favorite:
                    if (iPropertyClick != null) {
                        iPropertyClick.onFavoriteClick(position);
                    }
                    break;
                case R.id.iv_report:
                    if (iPropertyClick != null) {
                        iPropertyClick.onReportClick(position);
                    }
                    break;
                case R.id.iv_logo:
                    if (iPropertyClick != null) {
                        iPropertyClick.onItemClick(position);
                    }
                    break;
                default:
                    break;
            }
        }


    }

    //search...
    public void addItems(List<PropertyListModel.GetPropertyListing> arrayList) {
        this.propertyList = new ArrayList<PropertyListModel.GetPropertyListing>(arrayList);
        copyList = new ArrayList<PropertyListModel.GetPropertyListing>();
        for (int i = 0; i < arrayList.size(); i++) {
            copyList.add(arrayList.get(i));
        }
        notifyDataSetChanged();
    }


    public List<PropertyListModel.GetPropertyListing> onTextChange(String text) {
        propertyList.clear();
        if (copyList == null)
            return null;
        if (text.isEmpty()) {
            propertyList.addAll(copyList);
        } else {
            for (PropertyListModel.GetPropertyListing property : copyList) {
                if (property.getPropertyTitle().toLowerCase(new Locale(text.trim())).contains(text.toLowerCase(new Locale(text.trim())))) {
                    propertyList.add(property);
                }
            }
        }
        notifyDataSetChanged();
        return propertyList;
    }

}
