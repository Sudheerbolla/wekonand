package com.weekend.interfaces;

import android.app.Dialog;

import com.weekend.models.CityModel;

public interface ICityClick {
    void onCitySelect(int position, CityModel.Datum city, Dialog dialog);
}