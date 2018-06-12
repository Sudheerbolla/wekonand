package com.weekend.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.weekend.R;
import com.weekend.interfaces.IBookingClick;
import com.weekend.models.MyBookingsModel;
import com.weekend.utils.CommonUtil;
import com.weekend.utils.ImageUtil;
import com.weekend.views.CustomTextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyBookingsAdapter extends RecyclerView.Adapter<MyBookingsAdapter.ViewHolder> {


    private Context context;
    private IBookingClick iBookingClick;
    private List<MyBookingsModel.Datum> myBookingsList;

    public MyBookingsAdapter(Context context, List<MyBookingsModel.Datum> myBookingsList, IBookingClick iBookingClick) {
        this.context = context;
        this.myBookingsList = myBookingsList;
        this.iBookingClick = iBookingClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_bookings, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (myBookingsList != null && myBookingsList.size() > 0) {
            MyBookingsModel.Datum mybookings = myBookingsList.get(position);
            List<MyBookingsModel.GetSoccerImage> images = mybookings.getGetSoccerImage();
            String image = "";
            //chalet = 1, soccer field = 2
            if (mybookings.getPropertyType().equalsIgnoreCase("1"/*context.getResources().getString(R.string.chalet)*/)) {
                image = images.get(0).getChaletImage();
            } else {
                image = images.get(0).getSoccerImage();
            }
            ImageUtil.loadImage(context, image, holder.ivChalet);
            holder.tvTitle.setText(mybookings.getPropertyTitle());
            holder.tvChaletName.setText(mybookings.getChaletName());
            holder.tvAddress.setText(CommonUtil.insertComma(mybookings.getCity(), mybookings.getNeighbourhood()));
            holder.tvBookingId.setText(" " + mybookings.getBookingId());
            holder.tvBookedOn.setText(" " + mybookings.getBookedDate());
            holder.tvBookingName.setText(" " + mybookings.getBookingName());
            holder.tvBookingDate.setText(" " + mybookings.getBookingDate());
            holder.tvStatus.setText(" " + mybookings.getBookingStatus());
            holder.tvChaletPrice.setText(context.getResources().getString(R.string.paid) + " " + mybookings.getAmount() + " " + context.getResources().getString(R.string.currency));
            if (mybookings.getAllowRating().equalsIgnoreCase("Yes")) {
                holder.ivReview.setVisibility(View.VISIBLE);
            } else {
                holder.ivReview.setVisibility(View.GONE);
            }
            holder.tvChaletPrice.setSelected(true);
        }
    }

    @Override
    public int getItemCount() {
        return myBookingsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_title)
        CustomTextView tvTitle;
        @Bind(R.id.iv_review)
        ImageView ivReview;
        @Bind(R.id.iv_chalet)
        ImageView ivChalet;
        @Bind(R.id.tv_chalet_price)
        CustomTextView tvChaletPrice;
        @Bind(R.id.tv_chalet_name)
        CustomTextView tvChaletName;
        @Bind(R.id.tv_address)
        CustomTextView tvAddress;
        @Bind(R.id.tv_booking_id)
        CustomTextView tvBookingId;
        @Bind(R.id.tv_booked_on)
        CustomTextView tvBookedOn;
        @Bind(R.id.tv_booking_name)
        CustomTextView tvBookingName;
        @Bind(R.id.tv_booking_date)
        CustomTextView tvBookingDate;
        @Bind(R.id.tv_status)
        CustomTextView tvStatus;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.iv_share, R.id.iv_review, R.id.ll_row_my_bookings})
        public void onClick(View view) {
            int position = getAdapterPosition();
            switch (view.getId()) {
                case R.id.ll_row_my_bookings:
                    if (iBookingClick != null) {
                        iBookingClick.onItemClick(position);
                    }
                    break;
                case R.id.iv_review:
                    if (iBookingClick != null) {
                        iBookingClick.onReviewClick(position, myBookingsList.get(position).getPropertyId());
                    }
                    break;
                case R.id.iv_share:
                    if (iBookingClick != null) {
                        iBookingClick.onShareClick(position, myBookingsList.get(position));
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
