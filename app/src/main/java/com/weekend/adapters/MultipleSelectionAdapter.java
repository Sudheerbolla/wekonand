package com.weekend.adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weekend.R;
import com.weekend.interfaces.IMultipleSelectionClick;
import com.weekend.models.NeighbourhoodModel;
import com.weekend.models.SearchOptionModel;
import com.weekend.views.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MultipleSelectionAdapter extends RecyclerView.Adapter<MultipleSelectionAdapter.ViewHolder> {


    private Context context;
    private IMultipleSelectionClick multipleSelectionClick;
    private Dialog dialog;
    private List<NeighbourhoodModel.Datum> neighbourhoodList, selectedNeighbourItemsList, tempNeighbourList;
    private List<SearchOptionModel.AmenitiesList> amenitiesLists, selectedAmenitiesList, tempAmenityList;
    private List<SearchOptionModel.SuitableForList> suitableForLists, selectedsuitableForLists, tempSuitableForList;
    private List<SearchOptionModel.SoccerSize> soccerSizeList, selectedSoccerSizeList, tempSoccerSizeList;


    public MultipleSelectionAdapter(Context context, List<NeighbourhoodModel.Datum> neighbourhoodList,
                                    List<SearchOptionModel.AmenitiesList> amenitiesLists,
                                    List<SearchOptionModel.SuitableForList> suitableForLists,
                                    List<SearchOptionModel.SoccerSize> soccerSizeList,
                                    IMultipleSelectionClick multipleSelectionClick, Dialog dialog) {

        this.context = context;
        this.neighbourhoodList = neighbourhoodList;
        this.selectedNeighbourItemsList = new ArrayList<>();
        this.tempNeighbourList = new ArrayList<>();
        this.amenitiesLists = amenitiesLists;
        this.selectedAmenitiesList = new ArrayList<>();
        this.tempAmenityList = new ArrayList<>();
        this.suitableForLists = suitableForLists;
        this.selectedsuitableForLists = new ArrayList<>();
        this.tempSuitableForList = new ArrayList<>();
        this.soccerSizeList = soccerSizeList;
        this.selectedSoccerSizeList = new ArrayList<>();
        this.tempSoccerSizeList = new ArrayList<>();
        this.multipleSelectionClick = multipleSelectionClick;
        this.dialog = dialog;
    }


    public List<NeighbourhoodModel.Datum> getSelectedNeighbourList() {
        selectedNeighbourItemsList.clear();
        for (int i = 0; i < neighbourhoodList.size(); i++) {
            if (neighbourhoodList.get(i).isSelected()) {
                selectedNeighbourItemsList.add(neighbourhoodList.get(i));
            }
        }
        return selectedNeighbourItemsList;
    }

    public List<SearchOptionModel.AmenitiesList> getSelectedAmenityList() {
        selectedAmenitiesList.clear();
        for (int i = 0; i < amenitiesLists.size(); i++) {
            if (amenitiesLists.get(i).isSelected()) {
                selectedAmenitiesList.add(amenitiesLists.get(i));
            }
        }
        return selectedAmenitiesList;
    }

    public List<SearchOptionModel.SuitableForList> getSelectedSuitableForList() {
        selectedsuitableForLists.clear();
        for (int i = 0; i < suitableForLists.size(); i++) {
            if (suitableForLists.get(i).isSelected()) {
                selectedsuitableForLists.add(suitableForLists.get(i));
            }
        }
        return selectedsuitableForLists;
    }

    public List<SearchOptionModel.SoccerSize> getSelectedSoccerSizeList() {
        selectedSoccerSizeList.clear();
        for (int i = 0; i < soccerSizeList.size(); i++) {
            if (soccerSizeList.get(i).isSelected()) {
                selectedSoccerSizeList.add(soccerSizeList.get(i));
            }
        }
        return selectedSoccerSizeList;
    }

    public void setNeighbourItemSelected(int position) {
        boolean isSelected = !neighbourhoodList.get(position).isSelected();
        neighbourhoodList.get(position).setSelected(isSelected);
        tempNeighbourList.add(neighbourhoodList.get(position));
        notifyDataSetChanged();
    }

    public void setAmenityItemSelected(int position) {
        boolean isSelected = !amenitiesLists.get(position).isSelected();
        amenitiesLists.get(position).setSelected(isSelected);
        tempAmenityList.add(amenitiesLists.get(position));
        notifyDataSetChanged();
    }

    public void setSuitableForItemSelected(int position) {
        boolean isSelected = !suitableForLists.get(position).isSelected();
        suitableForLists.get(position).setSelected(isSelected);
        tempSuitableForList.add(suitableForLists.get(position));
        notifyDataSetChanged();
    }

    public void setSoccerSizeItemSelected(int position) {
        boolean isSelected = !soccerSizeList.get(position).isSelected();
        soccerSizeList.get(position).setSelected(isSelected);
        tempSoccerSizeList.add(soccerSizeList.get(position));
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alert_multiple_select_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (neighbourhoodList != null && neighbourhoodList.size() > 0) {
            holder.tvName.setText(neighbourhoodList.get(position).getName());
            holder.tvName.setSelected(neighbourhoodList.get(position).isSelected());
            if (position == neighbourhoodList.size() - 1) {
                holder.vSeparator.setVisibility(View.GONE);
            } else {
                holder.vSeparator.setVisibility(View.VISIBLE);
            }
        } else if (amenitiesLists != null && amenitiesLists.size() > 0) {
            holder.tvName.setText(amenitiesLists.get(position).getAmenityName());
            holder.tvName.setSelected(amenitiesLists.get(position).isSelected());
            if (position == amenitiesLists.size() - 1) {
                holder.vSeparator.setVisibility(View.GONE);
            } else {
                holder.vSeparator.setVisibility(View.VISIBLE);
            }
        } else if (suitableForLists != null && suitableForLists.size() > 0) {
            holder.tvName.setText(suitableForLists.get(position).getSuitableAmenityName());
            holder.tvName.setSelected(suitableForLists.get(position).isSelected());
            if (position == suitableForLists.size() - 1) {
                holder.vSeparator.setVisibility(View.GONE);
            } else {
                holder.vSeparator.setVisibility(View.VISIBLE);
            }
        } else if (soccerSizeList != null && soccerSizeList.size() > 0) {
            holder.tvName.setText(soccerSizeList.get(position).getName());
            holder.tvName.setSelected(soccerSizeList.get(position).isSelected());
            if (position == soccerSizeList.size() - 1) {
                holder.vSeparator.setVisibility(View.GONE);
            } else {
                holder.vSeparator.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (neighbourhoodList != null) {
            return neighbourhoodList.size();
        } else if (amenitiesLists != null) {
            return amenitiesLists.size();
        } else if (suitableForLists != null) {
            return suitableForLists.size();
        } else if (soccerSizeList != null) {
            return soccerSizeList.size();
        } else {
            return 0;
        }
    }

    public void makeUnselectedItemsClear() {
        if (tempNeighbourList != null && tempNeighbourList.size() > 0) {
            for (int i = 0; i < neighbourhoodList.size(); i++) {
                NeighbourhoodModel.Datum amenity = neighbourhoodList.get(i);
                if (tempNeighbourList.contains(amenity)) {
                    neighbourhoodList.get(i).setSelected(!neighbourhoodList.get(i).isSelected());
                }
            }
        } else if (tempAmenityList != null && tempAmenityList.size() > 0) {
            for (int i = 0; i < amenitiesLists.size(); i++) {
                SearchOptionModel.AmenitiesList amenity = amenitiesLists.get(i);
                if (tempAmenityList.contains(amenity)) {
                    amenitiesLists.get(i).setSelected(!amenitiesLists.get(i).isSelected());
                }
            }
        } else if (tempSuitableForList != null && tempSuitableForList.size() > 0) {
            for (int i = 0; i < suitableForLists.size(); i++) {
                SearchOptionModel.SuitableForList suitableFor = suitableForLists.get(i);
                if (tempSuitableForList.contains(suitableFor)) {
                    suitableForLists.get(i).setSelected(!suitableForLists.get(i).isSelected());
                }
            }
        } else if (tempSoccerSizeList != null && tempSoccerSizeList.size() > 0) {
            for (int i = 0; i < soccerSizeList.size(); i++) {
                SearchOptionModel.SoccerSize soccerSize = soccerSizeList.get(i);
                if (tempSoccerSizeList.contains(soccerSize)) {
                    soccerSizeList.get(i).setSelected(!soccerSizeList.get(i).isSelected());
                }
            }
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
                    if (multipleSelectionClick != null && neighbourhoodList != null) {
                        setNeighbourItemSelected(position);
                        tvName.setSelected(neighbourhoodList.get(position).isSelected());
                    } else if (multipleSelectionClick != null && amenitiesLists != null) {
                        setAmenityItemSelected(position);
                        tvName.setSelected(amenitiesLists.get(position).isSelected());
                    } else if (multipleSelectionClick != null && suitableForLists != null) {
                        setSuitableForItemSelected(position);
                        tvName.setSelected(suitableForLists.get(position).isSelected());
                    } else if (multipleSelectionClick != null && soccerSizeList != null) {
                        setSoccerSizeItemSelected(position);
                        tvName.setSelected(soccerSizeList.get(position).isSelected());
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
