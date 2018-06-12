package com.weekend.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.weekend.R;
import com.weekend.models.AbuseReasonListModel;
import com.weekend.views.CustomTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hb on 3/6/16.
 */
public class ReportAbuseReasonsAdapter extends BaseAdapter {

    private Context context;
    private List<AbuseReasonListModel.Datum> abuseReasonList;
    private LayoutInflater inflater;

    public ReportAbuseReasonsAdapter(Context context, List<AbuseReasonListModel.Datum> abuseReasonList) {
        this.context = context;
        this.abuseReasonList = abuseReasonList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        int count = 0;
        if (abuseReasonList != null) {
            count = abuseReasonList.size();
            return count > 0 ? count - 1 : count;
        } else {
            new ArrayList<>();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return abuseReasonList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        CustomTextView txtItemSpinner = (CustomTextView) inflater.inflate(R.layout.item_single_text, parent, false);
        if (abuseReasonList != null) {
            txtItemSpinner.setText(abuseReasonList.get(position).getReasonTitle());
        }
        return txtItemSpinner;
    }
}