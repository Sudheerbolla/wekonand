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
import com.weekend.models.FavoriteModel;
import com.weekend.utils.CommonUtil;
import com.weekend.utils.ImageUtil;
import com.weekend.views.CustomTextView;
import com.weekend.views.customimageview.RoundedImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyFavoritesAdapter extends RecyclerView.Adapter<MyFavoritesAdapter.ViewHolder> {

    private IPropertyClicks iPropertyClick;
    private Context mContext;
    private List<FavoriteModel.Datum> favoriteList;

    public MyFavoritesAdapter(Context context, List<FavoriteModel.Datum> list, IPropertyClicks iPropertyClick) {
        this.mContext = context;
        this.favoriteList = list;
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
        FavoriteModel.Datum favorite = favoriteList.get(position);
        if (!TextUtils.isEmpty(favorite.getImage())) {
            ImageUtil.loadPropertyImage(mContext, favorite.getImage(), holder.ivLogo);
        } else {
            holder.ivLogo.setImageResource(R.mipmap.loading_img);
        }
        if (!TextUtils.isEmpty(favorite.getAvgRating()) && Math.round(Float.valueOf(favorite.getAvgRating())) > 0) {
            holder.tvRating.setText(favorite.getAvgRating());
            holder.tvRating.setVisibility(View.VISIBLE);
        } else {
            holder.tvRating.setVisibility(View.GONE);
        }
        holder.tvPrice.setText(String.format("%s %s", favorite.getMinPrize(), mContext.getResources().getString(R.string.currency)));
        holder.tvTitle.setText(favorite.getPropertyTitle());
        holder.tvAddress.setText(CommonUtil.insertComma(favorite.getCountry(), favorite.getCity()));
        holder.ivVerified.setVisibility(favorite.getMarkAsVerified().equalsIgnoreCase("Yes") ? (View.VISIBLE) : (View.GONE));
        holder.ivRecommend.setVisibility(favorite.getMarkAsSuggested().equalsIgnoreCase("Yes") ? (View.VISIBLE) : (View.GONE));
        holder.ivReport.setVisibility(View.VISIBLE/*favorite.getMarkAsFeatured().equalsIgnoreCase("Yes") ? (View.VISIBLE) : (View.GONE)*/);
        holder.ivFavorite.setSelected(true);
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public interface OnFavoriteClickListener {
        void onFavoriteClick(int position);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
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
}
