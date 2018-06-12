package com.weekend.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.weekend.R;
import com.weekend.views.CustomTextView;

import java.util.ArrayList;

/**
 * Created on 1/8/16 at 4:50 PM.
 */
public class CalendarAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<DateObject> dateObjectArrayList;

    public CalendarAdapter(Context context, ArrayList<DateObject> dateObjectArrayList) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.dateObjectArrayList = new ArrayList<>(dateObjectArrayList);
    }

    @Override
    public int getCount() {
        return dateObjectArrayList.size();
    }

    @Override
    public DateObject getItem(int position) {
        return dateObjectArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateData(ArrayList<DateObject> datalist) {
        this.dateObjectArrayList.clear();
        this.dateObjectArrayList = new ArrayList<>(datalist);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.inflate_calendar_cell, parent, false);
            holder = new ViewHolder();
            holder.llCell = (LinearLayout) convertView.findViewById(R.id.ll_cell);
            holder.tvDate = (CustomTextView) convertView.findViewById(R.id.tv_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DateObject dateObject = dateObjectArrayList.get(position);
        holder.tvDate.setText(String.valueOf(dateObject.date.getDate()));

        switch (dateObject.status) {
            case DateObject.STATUS_AVAILABLE:
                holder.llCell.setBackgroundResource(R.drawable.calender_state_available_selector);
                break;
            case DateObject.STATUS_UNAVAILABLE:
                holder.llCell.setBackgroundResource(R.drawable.calender_state_unavailable);
                break;
            case DateObject.STATUS_BOOKED:
                holder.llCell.setBackgroundResource(R.drawable.calender_state_booked);
                break;
            case DateObject.STATUS_CLOSED:
                //holder.llCell.setBackgroundResource(R.drawable.calender_state_closed);
                holder.llCell.setBackgroundResource(R.drawable.calender_state_booked);
                break;
            default:
                holder.llCell.setBackgroundResource(R.drawable.calender_state_booked);
                break;
        }

        if (dateObject.status == DateObject.STATUS_AVAILABLE && dateObject.isSelected) {
            holder.llCell.setBackgroundResource(R.drawable.calender_state_available_selected);
            convertView.setSelected(true);
        } else if (dateObject.status == DateObject.STATUS_AVAILABLE) {
            convertView.setBackgroundResource(R.drawable.calender_state_available);
            convertView.setSelected(false);
        } else if (dateObject.status == DateObject.STATUS_UNAVAILABLE) {
            holder.llCell.setBackgroundResource(R.drawable.calender_state_unavailable);
        } else if (dateObject.status == DateObject.STATUS_BOOKED) {
            holder.llCell.setBackgroundResource(R.drawable.calender_state_booked);
        }
        return convertView;
    }

    class ViewHolder {
        LinearLayout llCell;
        CustomTextView tvDate;
    }

    public ArrayList<DateObject> getFullList() {
        return dateObjectArrayList;
    }
}
