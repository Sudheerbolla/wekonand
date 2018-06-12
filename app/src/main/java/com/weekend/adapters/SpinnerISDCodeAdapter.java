package com.weekend.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.weekend.R;
import com.weekend.models.ISDCodeModel;
import com.weekend.views.CustomTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hb on 3/6/16.
 */
public class SpinnerISDCodeAdapter extends BaseAdapter {

    private Context context;
    private List<ISDCodeModel.Datum> ISDCodeList;
    private LayoutInflater inflater;

    public SpinnerISDCodeAdapter(Context context, List<ISDCodeModel.Datum> ISDCodeList) {
        this.ISDCodeList = ISDCodeList;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        int count = 0;
        if (ISDCodeList != null) {
            return ISDCodeList.size();
        } else {
            new ArrayList<>();
        }
        return 0;
    }

    public ISDCodeModel.Datum getItem(int position) {
        return ISDCodeList.get(position);
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
        if (ISDCodeList != null) {
            txtItemSpinner.setText("\u202D" + ISDCodeList.get(position).getMcIsdcode());
        }
        return txtItemSpinner;
    }
}