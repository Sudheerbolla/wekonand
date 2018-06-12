package com.weekend.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.weekend.R;
import com.weekend.models.PropertyDetailModel;
import com.weekend.utils.ImageUtil;
import com.weekend.views.CustomTextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AmenitiesAdapter extends RecyclerView.Adapter<AmenitiesAdapter.ViewHolder> {

    private List<PropertyDetailModel.Amenity> amenityImageList;
    private List<PropertyDetailModel.Suitable> suitableForImagesList;
    private Context context;
    private boolean isNameReq;

    public AmenitiesAdapter(Context context, List<PropertyDetailModel.Amenity> amenityImageList, List<PropertyDetailModel.Suitable> suitableForImagesList, boolean isNameReq) {
        this.context = context;
        this.amenityImageList = amenityImageList;
        this.suitableForImagesList = suitableForImagesList;
        this.isNameReq = isNameReq;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_amenities, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (amenityImageList != null && amenityImageList.size() > 0) {
            PropertyDetailModel.Amenity amenityImage = amenityImageList.get(position);
            if (isNameReq) {
                holder.tvAmenityName.setText(amenityImage.getVAmenityName());
            } else {
                holder.tvAmenityName.setVisibility(View.GONE);
            }
            ImageUtil.loadImage(context, amenityImage.getImageUrl(), holder.ivAmenity);
        }
        if (suitableForImagesList != null && suitableForImagesList.size() > 0) {
            PropertyDetailModel.Suitable suitableImage = suitableForImagesList.get(position);
            if (isNameReq) {
                holder.tvAmenityName.setText(suitableImage.getVAmenityName());
            } else {
                holder.tvAmenityName.setVisibility(View.GONE);
            }
            ImageUtil.loadImage(context, suitableImage.getImageUrl(), holder.ivAmenity);
        }
    }

    @Override
    public int getItemCount() {
        if (amenityImageList != null) {
            return amenityImageList.size();
        } else if (suitableForImagesList != null) {
            return suitableForImagesList.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_amenity_name)
        CustomTextView tvAmenityName;
        @Bind(R.id.iv_amenity)
        ImageView ivAmenity;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
