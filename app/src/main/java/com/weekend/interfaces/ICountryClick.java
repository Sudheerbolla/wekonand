package com.weekend.interfaces;

import android.app.Dialog;

import com.weekend.models.CountryModel;

public interface ICountryClick {
    void onCountrySelect(int position, CountryModel.Datum country, Dialog dialog);
}