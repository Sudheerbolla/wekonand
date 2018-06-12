package com.weekend.interfaces;

public interface ISingleSelectionClick {
    void onItemSelect(int position, String id, String title, String from);
    void onNoneSelect(String from);
}