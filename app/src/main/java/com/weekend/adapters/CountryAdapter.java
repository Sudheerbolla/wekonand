package com.weekend.adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weekend.R;
import com.weekend.interfaces.ICountryClick;
import com.weekend.models.CountryModel;
import com.weekend.views.CustomTextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {

    private List<CountryModel.Datum> countryList;
    private Context context;
    private ICountryClick countryClick;
    private Dialog dialog;

    public CountryAdapter( Context context, List<CountryModel.Datum> list, ICountryClick countryClick, Dialog dialog) {
        this.countryList = list;
        this.context = context;
        this.countryClick = countryClick;
        this.dialog = dialog;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alert_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        CountryModel.Datum country = countryList.get(position);
        holder.tvName.setText(country.getCountry());
        if (position == countryList.size() - 1) {
            holder.vSeparator.setVisibility(View.GONE);
        } else {
            holder.vSeparator.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.tv_name)
        CustomTextView tvName;
        @Bind(R.id.v_separator)
        View vSeparator;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.tv_name})
        public void onClick(View view) {
            int position = getAdapterPosition();
            switch (view.getId()) {
                case R.id.tv_name:
                    if (countryClick != null) {
                        countryClick.onCountrySelect(position, countryList.get(position), dialog);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
