package com.weekend.adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weekend.R;
import com.weekend.interfaces.ICityClick;
import com.weekend.interfaces.IPopularCityClick;
import com.weekend.models.CityModel;
import com.weekend.models.PopularCityListModel;
import com.weekend.views.CustomTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {

    private List<CityModel.Datum> cityList;
    private List<PopularCityListModel.Datum> popularCityList, copyList;
    private Context context;
    private ICityClick cityClick;
    private IPopularCityClick popularCityClick;
    private Dialog dialog;

    public CityAdapter(Context context, List<PopularCityListModel.Datum> popularCityList, IPopularCityClick popularCityClick) {
        this.context = context;
        this.popularCityList = popularCityList;
        this.popularCityClick = popularCityClick;
    }

    public CityAdapter(Context context, List<CityModel.Datum> list, ICityClick cityClick, Dialog dialog) {
        this.cityList = list;
        this.context = context;
        this.cityClick = cityClick;
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
        if (cityList != null && cityList.size() > 0) {
            CityModel.Datum city = cityList.get(position);
            holder.tvName.setText(city.getName());
            if (position == cityList.size() - 1) {
                holder.vSeparator.setVisibility(View.GONE);
            } else {
                holder.vSeparator.setVisibility(View.VISIBLE);
            }
        } else if (popularCityList != null && popularCityList.size() > 0) {
            PopularCityListModel.Datum popularCity = popularCityList.get(position);
            holder.tvName.setText(popularCity.getCity());
            holder.tvName.setSelected(popularCity.isSelected());
            if (position == popularCityList.size() - 1) {
                holder.vSeparator.setVisibility(View.GONE);
            } else {
                holder.vSeparator.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (cityList != null) {
            return cityList.size();
        } else if (popularCityClick != null) {
            return popularCityList.size();
        } else {
            return new ArrayList<>().size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
                    if (cityClick != null) {
                        cityClick.onCitySelect(position, cityList.get(position), dialog);
                    } else if (popularCityClick != null) {
                        popularCityClick.onCitySelect(position, popularCityList.get(position).getCityId());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void addItems(List<PopularCityListModel.Datum> arrayList) {
        this.popularCityList = new ArrayList<PopularCityListModel.Datum>(arrayList);
        copyList = new ArrayList<PopularCityListModel.Datum>();
        for (int i = 0; i < arrayList.size(); i++) {
            copyList.add(arrayList.get(i));
        }
        notifyDataSetChanged();
    }

    public List<PopularCityListModel.Datum> onTextChange(String text) {
        popularCityList.clear();
        if (copyList == null)
            return null;
        if (text.isEmpty()) {
            popularCityList.addAll(copyList);
        } else {
            for (PopularCityListModel.Datum city : copyList) {
                if (city.getCity().toLowerCase(new Locale(text.trim())).contains(text.toLowerCase(new Locale(text.trim())))) {
                    popularCityList.add(city);
                }
            }
        }
        notifyDataSetChanged();
        return popularCityList;
    }
}
