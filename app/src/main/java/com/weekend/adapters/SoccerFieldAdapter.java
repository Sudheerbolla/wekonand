package com.weekend.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.weekend.R;
import com.weekend.models.PropertyDetailModel;
import com.weekend.utils.CommonUtil;
import com.weekend.utils.ImageUtil;
import com.weekend.utils.PopupUtil;
import com.weekend.utils.StaticUtil;
import com.weekend.views.CustomTextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Hymavathi.kadali on 22/7/16.
 */

public class SoccerFieldAdapter extends RecyclerView.Adapter<SoccerFieldAdapter.ViewHolder> {

    private Context mContext;
    private List<PropertyDetailModel.PropertyChaletDetail> propertyChaletDetailList;
    private OnItemClickListener listener;
    private boolean mIsSchedulingAvailable, mIsBookingAvailable;

    public interface OnItemClickListener {
        void onItemClick(String mString, int position);
    }

    public SoccerFieldAdapter(Context context, List<PropertyDetailModel.PropertyChaletDetail> propertyChaletDetailList, OnItemClickListener listener) {
        this.mContext = context;
        this.propertyChaletDetailList = propertyChaletDetailList;
        this.listener = listener;
    }

    public void setBookingAndScheduleAvailability(boolean isSchedulingAvailable, boolean isBookingAvailable){
        this.mIsSchedulingAvailable = isSchedulingAvailable;
        this.mIsBookingAvailable = isBookingAvailable;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_soccer_field_detail, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        PropertyDetailModel.PropertyChaletDetail propertyChaletDetail = propertyChaletDetailList.get(position);
        String chaletImg = null;
        if (propertyChaletDetail.getChaletImages() != null && propertyChaletDetail.getChaletImages().size() > 0) {
            chaletImg = propertyChaletDetail.getChaletImages().get(0).getImageUrl();
        }
        ImageUtil.loadImage(mContext, chaletImg, holder.ivSoccerField);
        List<PropertyDetailModel.Amenity> amenityImages = propertyChaletDetail.getAmenities();
        if (amenityImages != null && amenityImages.size() > 0) {
            holder.llAmenities.removeAllViews();
            for (int i = 0; i < amenityImages.size(); i++) {
                ImageView image = new ImageView(mContext);
                image.setLayoutParams(new ViewGroup.LayoutParams(StaticUtil.dpToPx(mContext, 20), StaticUtil.dpToPx(mContext, 20)));
                image.setPadding(5, 5, 5, 5);
                image.setScaleType(ImageView.ScaleType.FIT_XY);
                ImageUtil.loadImage(mContext, amenityImages.get(i).getImageUrl(), image);
                holder.llAmenities.addView(image);
            }
        }
        holder.tvSoccerFieldName.setText(propertyChaletDetail.getChaletTitle());
        holder.tvSize.setText("\u200f" + propertyChaletDetail.getSize());
        if (!TextUtils.isEmpty(propertyChaletDetail.getDownPayment())) {
            holder.tvSoccerFieldPrice.setText(propertyChaletDetail.getDownPayment());
            holder.llAmount.setVisibility(View.VISIBLE);
        } else {
            holder.llAmount.setVisibility(View.INVISIBLE);
        }
        String dateTime = propertyChaletDetail.getLatestAvaliablity();
        if (!dateTime.isEmpty()) {
            if (dateTime.contains(" ")) {
                holder.tvDate.setText(" " + dateTime.substring(0, dateTime.indexOf(" ")));
                String time = dateTime.substring(dateTime.indexOf(' ') + 1);
                if (!time.isEmpty()) {
                    CommonUtil.changeTimeString(mContext, holder.tvTime, "\u202D" + time, mContext.getString(R.string.to_small), false, 105, 111, 116);
                }
            } else {
                holder.tvDate.setText(" " + dateTime);
            }
        }

        if(!mIsBookingAvailable && !mIsSchedulingAvailable){
            holder.llTimeDate.setVisibility(View.INVISIBLE);
            holder.llAmount.setVisibility(View.GONE);
            holder.tvAmountHint.setVisibility(View.VISIBLE);
        }else if(!mIsBookingAvailable && mIsSchedulingAvailable){
            holder.llTimeDate.setVisibility(View.VISIBLE);
            holder.llAmount.setVisibility(View.GONE);
            holder.tvAmountHint.setVisibility(View.VISIBLE);
        }else{
            holder.llTimeDate.setVisibility(View.VISIBLE);
            holder.llAmount.setVisibility(View.VISIBLE);
            holder.tvAmountHint.setVisibility(View.GONE);
        }

        holder.bind(listener, position);
    }

    @Override
    public int getItemCount() {
        return propertyChaletDetailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.iv_soccer_field)
        ImageView ivSoccerField;
        @Bind(R.id.tv_soccer_field_name)
        CustomTextView tvSoccerFieldName;
        @Bind(R.id.tv_size)
        CustomTextView tvSize;
        @Bind(R.id.tv_soccer_field_price)
        CustomTextView tvSoccerFieldPrice;
        @Bind(R.id.tv_date)
        CustomTextView tvDate;
        @Bind(R.id.tv_time)
        CustomTextView tvTime;
        @Bind(R.id.ll_amenities)
        LinearLayout llAmenities;
        @Bind(R.id.ll_amount)
        LinearLayout llAmount;
        @Bind(R.id.ll_time_date)
        LinearLayout llTimeDate;
        @Bind(R.id.tv_amount_hint)
        CustomTextView tvAmountHint;

        String sunToWed;
        String thu;
        String fri;
        String sat;
        String eidFitr;
        String eidAdha;
        String downPayment;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.tv_show_price, R.id.ll_amenities})
        public void onClick(View view) {
            int position = getAdapterPosition();
            switch (view.getId()) {
                case R.id.tv_show_price:
                    sunToWed = propertyChaletDetailList.get(position).getPcPriceSunWed();
                    thu = propertyChaletDetailList.get(position).getPcPriceThursday();
                    fri = propertyChaletDetailList.get(position).getPcPriceFriday();
                    sat = propertyChaletDetailList.get(position).getPcPriceSaturday();
                    eidFitr = propertyChaletDetailList.get(position).getPcPriceEidAlFitr();
                    eidAdha = propertyChaletDetailList.get(position).getPcPriceEidUlAdha();
                    downPayment = propertyChaletDetailList.get(position).getDownPayment();

                    //Doing this to making downpayment availability hidden because online booking service is not available for the master property
                    if (!mIsBookingAvailable && !mIsSchedulingAvailable) {
                        downPayment = "";
                    } else if (!mIsBookingAvailable && mIsSchedulingAvailable) {
                        downPayment = "";
                    }
                    PopupUtil.showPriceRange(mContext, sunToWed, thu, fri, sat, eidFitr, eidAdha, downPayment);
                    break;
                case R.id.ll_amenities:
                    if (listener != null) {
                        listener.onItemClick("ItemClicked", position);
                    }
                    break;
                default:
                    break;
            }
        }

        public void bind(final OnItemClickListener listener, final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick("ItemClicked", position);
                }
            });
        }
    }
}
