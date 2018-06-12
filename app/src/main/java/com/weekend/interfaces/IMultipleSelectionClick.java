package com.weekend.interfaces;

import com.weekend.models.NeighbourhoodModel;
import com.weekend.models.SearchOptionModel;

import java.util.List;

public interface IMultipleSelectionClick {

    void OnNeighbourItemSelected(List<NeighbourhoodModel.Datum> selectedList);

    void OnAmenityItemSelected(List<SearchOptionModel.AmenitiesList> selectedAmenitiesList);

    void OnSuitableForItemSelected(List<SearchOptionModel.SuitableForList> selectedSuitableForLists);

    void OnSoccerSizeItemSelected(List<SearchOptionModel.SoccerSize> selectedSoccerSizeLists);
}