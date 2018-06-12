package com.weekend.adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weekend.R;
import com.weekend.interfaces.ISingleSelectionClick;
import com.weekend.models.SearchOptionModel;
import com.weekend.views.CustomTextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SingleSelectionAdapter extends RecyclerView.Adapter<SingleSelectionAdapter.ViewHolder> {


    private Context context;
    private ISingleSelectionClick singleSelectionClick;
    private Dialog dialog;
    private List<SearchOptionModel.CityLocation> cityLocationList;
    private List<SearchOptionModel.GetSectionList> sectionLists;
    private List<SearchOptionModel.ChaletLeasingTypeList> leasingTypeLists;
    private List<SearchOptionModel.ChaletSize> chaletSizeList;

    public SingleSelectionAdapter(Context context, List<SearchOptionModel.CityLocation> cityLocationList,
                                  List<SearchOptionModel.ChaletLeasingTypeList> leasingTypeLists,
                                  List<SearchOptionModel.GetSectionList> sectionLists,
                                  List<SearchOptionModel.ChaletSize> chaletSizeList,
                                  ISingleSelectionClick singleSelectionClick, Dialog dialog) {
        this.context = context;
        this.cityLocationList = cityLocationList;
        this.sectionLists = sectionLists;
        this.leasingTypeLists = leasingTypeLists;
        this.chaletSizeList = chaletSizeList;
        this.singleSelectionClick = singleSelectionClick;
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
        if (cityLocationList != null && cityLocationList.size() > 0) {
            SearchOptionModel.CityLocation location = cityLocationList.get(position);
            holder.tvName.setText(location.getLocationName());
            holder.tvName.setSelected(cityLocationList.get(position).isSelected());
            if (position == cityLocationList.size() - 1) {
                holder.vSeparator.setVisibility(View.GONE);
            } else {
                holder.vSeparator.setVisibility(View.VISIBLE);
            }
        }
        if (sectionLists != null && sectionLists.size() > 0) {
            SearchOptionModel.GetSectionList section = sectionLists.get(position);
            holder.tvName.setText(section.getSectionTitle());
            holder.tvName.setSelected(sectionLists.get(position).isSelected());
            if (position == sectionLists.size() - 1) {
                holder.vSeparator.setVisibility(View.GONE);
            } else {
                holder.vSeparator.setVisibility(View.VISIBLE);
            }
        }
        if (leasingTypeLists != null && leasingTypeLists.size() > 0) {
            SearchOptionModel.ChaletLeasingTypeList leasing = leasingTypeLists.get(position);
            holder.tvName.setText(leasing.getChaletLeasingTypeTitle());
            holder.tvName.setSelected(leasingTypeLists.get(position).isSelected());
            if (position == leasingTypeLists.size() - 1) {
                holder.vSeparator.setVisibility(View.GONE);
            } else {
                holder.vSeparator.setVisibility(View.VISIBLE);
            }
        }
        if (chaletSizeList != null && chaletSizeList.size() > 0) {
            SearchOptionModel.ChaletSize leasing = chaletSizeList.get(position);
            holder.tvName.setText(leasing.getText());
            holder.tvName.setSelected(chaletSizeList.get(position).isSelected());
            if (position == chaletSizeList.size() - 1) {
                holder.vSeparator.setVisibility(View.GONE);
            } else {
                holder.vSeparator.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (cityLocationList != null) {
            return cityLocationList.size();
        } else if (sectionLists != null) {
            return sectionLists.size();
        } else if (leasingTypeLists != null) {
            return leasingTypeLists.size();
        } else if (chaletSizeList != null) {
            return chaletSizeList.size();
        } else {
            return 0;
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
                    dialog.dismiss();
                    if (singleSelectionClick != null && cityLocationList != null) {
                        singleSelectionClick.onItemSelect(position, cityLocationList.get(position).getLocationId(), cityLocationList.get(position).getLocationName(), "city");
                        if (!cityLocationList.get(position).isSelected()) {
                            cityLocationList.get(position).setSelected(!cityLocationList.get(position).isSelected());
                            for (int i = 0; i < cityLocationList.size(); i++) {
                                if (i != position) {
                                    cityLocationList.get(i).setSelected(false);
                                }
                            }
                            tvName.setSelected(cityLocationList.get(position).isSelected());
                        }
                    } else if (singleSelectionClick != null && sectionLists != null) {
                        singleSelectionClick.onItemSelect(position, sectionLists.get(position).getSectionId(), sectionLists.get(position).getSectionTitle(), "section");
                        if (!sectionLists.get(position).isSelected()) {
                            sectionLists.get(position).setSelected(!sectionLists.get(position).isSelected());
                            for (int i = 0; i < sectionLists.size(); i++) {
                                if (i != position) {
                                    sectionLists.get(i).setSelected(false);
                                }
                            }
                            tvName.setSelected(sectionLists.get(position).isSelected());
                        }
                    } else if (singleSelectionClick != null && leasingTypeLists != null) {
                        singleSelectionClick.onItemSelect(position, leasingTypeLists.get(position).getChaletLeasingTypeId(), leasingTypeLists.get(position).getChaletLeasingTypeTitle(), "leasing");
                        if (!leasingTypeLists.get(position).isSelected()) {
                            leasingTypeLists.get(position).setSelected(!leasingTypeLists.get(position).isSelected());
                            for (int i = 0; i < leasingTypeLists.size(); i++) {
                                if (i != position) {
                                    leasingTypeLists.get(i).setSelected(false);
                                }
                            }
                            tvName.setSelected(leasingTypeLists.get(position).isSelected());
                        }
                    } else if (singleSelectionClick != null && chaletSizeList != null) {
                        singleSelectionClick.onItemSelect(position, chaletSizeList.get(position).getValue(), chaletSizeList.get(position).getText(), "size");
                        if (!chaletSizeList.get(position).isSelected()) {
                            chaletSizeList.get(position).setSelected(!chaletSizeList.get(position).isSelected());
                            for (int i = 0; i < chaletSizeList.size(); i++) {
                                if (i != position) {
                                    chaletSizeList.get(i).setSelected(false);
                                }
                            }
                            tvName.setSelected(chaletSizeList.get(position).isSelected());
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
