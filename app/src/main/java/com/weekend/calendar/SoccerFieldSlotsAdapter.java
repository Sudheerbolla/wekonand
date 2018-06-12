package com.weekend.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.weekend.R;
import com.weekend.fragments.SoccerFieldChildDetailsFragment;
import com.weekend.models.ManageSchedulesModel;
import com.weekend.utils.CommonUtil;
import com.weekend.utils.DateUtil;

import java.util.List;

/**
 * Created by hb on 12/8/16.
 */
public class SoccerFieldSlotsAdapter extends BaseAdapter {

    private final List<ManageSchedulesModel.SlotTime> slotTimes;
    private boolean isAvailability;
    SoccerFieldChildDetailsFragment.OnSlotSelectedListener onSlotSelectedListener;
    private LayoutInflater layoutInflater;
    private Context context;

    public SoccerFieldSlotsAdapter(Context context, List<ManageSchedulesModel.SlotTime> slotTimes, SoccerFieldChildDetailsFragment.OnSlotSelectedListener onSlotSelectedListener, boolean isAvailability) {
        this.context = context;
        this.slotTimes = slotTimes;
        this.onSlotSelectedListener = onSlotSelectedListener;
        this.isAvailability = isAvailability;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return slotTimes.size();
    }

    @Override
    public Object getItem(int position) {
        return slotTimes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        TextView tvSlot;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_soccer_slot, null);
            tvSlot = (TextView) convertView.findViewById(R.id.tv_time_slot);
            convertView.setTag(tvSlot);
        } else {
            tvSlot = (TextView) convertView.getTag();
        }

        String toTime = DateUtil.convertToAppTime(slotTimes.get(position).getSlotToTime());
        String fromtime = DateUtil.convertToAppTime(slotTimes.get(position).getSlotFromTime());
        tvSlot.setText(fromtime + " - " + toTime);
        if (slotTimes.get(position).status==DateObject.STATUS_BOOKED) {
            tvSlot.setTextColor(context.getResources().getColor(R.color.color_224_140_98));
        }else if(slotTimes.get(position).status==DateObject.STATUS_CLOSED){
            tvSlot.setTextColor(context.getResources().getColor(R.color.color_63_67_72));
        } else {
            if (slotTimes.get(position).isSelected) {
                tvSlot.setTextColor(context.getResources().getColor(R.color.color_255_255_255));
                tvSlot.setBackgroundResource(R.drawable.bg_green_corner_10);
            } else {
                tvSlot.setTextColor(context.getResources().getColor(R.color.color_146_186_72));
                tvSlot.setBackgroundResource(R.drawable.bg_white_corner_10);
            }
            tvSlot.setTag(position);
            tvSlot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isAvailability) {
                        if (MonthCalenderFragment.selectedDate != null) {
                            int pos = (int) v.getTag();
                            if (slotTimes.get(pos).isSelected) {
                                slotTimes.get(pos).isSelected = false;
                                onSlotSelectedListener.onSlotSelected(slotTimes.get(pos));
                            } else {
                                for (int j = 0; j < slotTimes.size(); j++) {
                                    slotTimes.get(j).isSelected = false;
                                }
                                slotTimes.get(pos).isSelected = true;
                                notifyDataSetChanged();
                                onSlotSelectedListener.onSlotSelected(slotTimes.get(pos));
                            }
                        } else {
                            CommonUtil.showSnackbar(parent.getRootView(), context.getString(R.string.please_select_date_to_select_time_slot));
                        }
                    }
                }
            });
        }

        return convertView;
    }
}
