package com.weekend.calendar;

import com.weekend.models.ManageSchedulesModel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created on 1/8/16 at 4:51 PM.
 */
public class DateObject implements Serializable {

    public static final int STATUS_UNAVAILABLE = 0;
    public static final int STATUS_AVAILABLE = 1;
    public static final int STATUS_BOOKED = 2;
    public static final int STATUS_HOLIDAY = 3;
    public static final int STATUS_CLOSED= 4;
    public boolean isSelected;

    public Date date;
    public List<ManageSchedulesModel.Slot> bookedSlots;
    public int status = 0;
}
