package com.weekend.interfaces;

import com.weekend.models.MyBookingsModel;

public interface IBookingClick {
    void onItemClick(int position);

    void onShareClick(int position, MyBookingsModel.Datum property);

    void onReviewClick(int position, String propertyId);
}