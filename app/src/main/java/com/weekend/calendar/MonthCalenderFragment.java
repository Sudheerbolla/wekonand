package com.weekend.calendar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.weekend.R;
import com.weekend.models.ManageSchedulesModel;
import com.weekend.utils.Log;
import com.weekend.views.ExpandableHeightGridView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */

public class MonthCalenderFragment extends Fragment implements AdapterView.OnItemClickListener {
    public static final String TAG = MonthCalenderFragment.class.getSimpleName();
    public static Date selectedDate;
    public int position = 0;
    int DAYS_COUNT = 42;
    ArrayList<DateObject> cells;
    View rootView;
    List<ManageSchedulesModel.SlotTime> allSlots;
    private transient Calendar calendarDate;
    //first day of next month or current date of the month
    private transient Date baseDate;
    private transient CalendarAdapter calendarAdapter;
    private transient ExpandableHeightGridView gridView;
    private transient CalendarView.OnDateSelectedListener onDateSelectedListener;
    private boolean isFromAvailability, isSoccerField;

    public static MonthCalenderFragment newInstance(Calendar calendar, int position, CalendarView.OnDateSelectedListener onDateSelectedListener, boolean isFromAvailability) {
        MonthCalenderFragment fragment = new MonthCalenderFragment();
        Bundle args = new Bundle();
        args.putSerializable("calendar", calendar);
        args.putInt("position", position);
        args.putSerializable("listener", onDateSelectedListener);
        args.putSerializable("is_availability", isFromAvailability);
        fragment.setArguments(args);
        return fragment;
    }

    public Calendar getCurrentCalender() {
        return calendarDate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.inflate_calender_grid, null);
        initComponents();
        return rootView;
    }

    private void getBundleData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("calendar"))
                calendarDate = (Calendar) bundle.getSerializable("calendar");
            if (bundle.containsKey("position")) {
                position = bundle.getInt("position");
                position = bundle.getInt("pos");
                Log.e("pos:", String.valueOf(position));
            }
            if (bundle.containsKey("listener")) {
                onDateSelectedListener = (CalendarView.OnDateSelectedListener) bundle.getSerializable("listener");
            }
            isFromAvailability = bundle.getBoolean("is_availability");
        }
    }

    private void initComponents() {
        getBundleData();
        gridView = (ExpandableHeightGridView) rootView.findViewById(R.id.gv_calender);
        gridView.setExpanded(true);
        cells = new ArrayList<>();
        calendarAdapter = new CalendarAdapter(getContext(), cells);
        gridView.setAdapter(calendarAdapter);
        gridView.setOnItemClickListener(this);
        initializeDate();
        Log.e(TAG, "valid stating date of month:" + baseDate.toString());
    }

    /**
     * if current month date then take as it is or set next month first date of that month
     */
    private void initializeDate() {
        try {

            String dateString = CalendarView.dateFormat.format(calendarDate.getTime());
            baseDate = CalendarView.dateFormat.parse(dateString);

            String dateString1 = CalendarView.dateFormat.format(Calendar.getInstance().getTime());
            Date tempCurrentDate = CalendarView.dateFormat.parse(dateString1);

            if (baseDate.getMonth() > tempCurrentDate.getMonth()) {
                // determine the cell for current month's beginning
                Calendar calendar = (Calendar) calendarDate.clone();
                calendar.set(Calendar.DAY_OF_MONTH, 1);

                String dateString2 = CalendarView.dateFormat.format(calendar.getTime());
                baseDate = CalendarView.dateFormat.parse(dateString2);
            } else if (baseDate.getYear() > tempCurrentDate.getYear()) {
                Calendar calendar = (Calendar) calendarDate.clone();
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                String dateString2 = CalendarView.dateFormat.format(calendar.getTime());
                baseDate = CalendarView.dateFormat.parse(dateString2);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to update Whole calender UI
     *
     * @param schduleList   Current Month schedule
     * @param slotTime      Current Month Available Slots
     * @param isSoccerField true/false
     */
    public void updateCalendar(List<ManageSchedulesModel.Schdule> schduleList, List<ManageSchedulesModel.SlotTime> slotTime, boolean isSoccerField) {
        Log.e("updateCalendar :", String.valueOf(schduleList));
        this.allSlots = slotTime;
        this.isSoccerField = isSoccerField;
        cells.clear();

        Calendar calendar = (Calendar) calendarDate.clone();

        // determine the cell for current month's beginning
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        // move calendar backwards to the beginning of the week
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

        // fill cells (42 days calendar as per our business logic)

        while (cells.size() < DAYS_COUNT) {
            DateObject dateObject = new DateObject();
            dateObject.date = calendar.getTime();
            if (validDate(calendar.getTime())) {
                boolean isValueSet = false;
                for (ManageSchedulesModel.Schdule schedule : schduleList) {
                    try {
                        Date compareDate = CalendarView.SERVER_BOOKED_DATE_FORAMT.parse(schedule.getBookedDate());
                        if (compareDate.equals(calendar.getTime())) {
                            if (schedule.getBookedStatusId().equalsIgnoreCase("2")) {
                                if (isSoccerField) {
                                    //Soccer
                                    if (schedule.getAvailability().equalsIgnoreCase("0")) {
                                        isValueSet = true;
                                        dateObject.status = DateObject.STATUS_CLOSED;
                                    }
                                } else {
                                    //Chalet
                                    isValueSet = true;
                                    dateObject.status = DateObject.STATUS_CLOSED;
                                }
                                break;
                            }
                            if (schedule.getBookedStatusId().equalsIgnoreCase("3")) {
                                if (isSoccerField) {
                                    dateObject.bookedSlots = schedule.getSlot();
                                    if (allSlots.size() == schedule.getSlot().size() && isAllBookedSlot(schedule.getSlot())) {
                                        isValueSet = true;
                                        dateObject.status = DateObject.STATUS_BOOKED;
                                    }
                                } else {
                                    isValueSet = true;
                                    dateObject.status = DateObject.STATUS_BOOKED;
                                }
                                break;
                            }
                            if (schedule.getBookedStatusId().equalsIgnoreCase("4")) {
                                if (isSoccerField) {
                                    //Soccer
                                    if (schedule.getAvailability().equalsIgnoreCase("0")) {
                                        isValueSet = true;
                                        dateObject.status = DateObject.STATUS_CLOSED;
                                    }
                                } else {
                                    //Chalet
                                    isValueSet = true;
                                    dateObject.status = DateObject.STATUS_CLOSED;
                                }
                                break;
                            }
                            if (schedule.getAvailability().equalsIgnoreCase("0")) {
                                if (isSoccerField) {
                                    //Soccer
                                    if (schedule.getAvailability().equalsIgnoreCase("0")) {
                                        isValueSet = true;
                                        dateObject.status = DateObject.STATUS_UNAVAILABLE;
                                    }
                                } else {
                                    //Chalet
                                    isValueSet = true;
                                    dateObject.status = DateObject.STATUS_UNAVAILABLE;
                                }
                                break;
                            }
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (!isValueSet) {
                    //from response date is not in schedule means booking schedule is available
                    dateObject.status = DateObject.STATUS_AVAILABLE;
                }
            } else {
                dateObject.status = DateObject.STATUS_UNAVAILABLE;
            }

            cells.add(dateObject);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        /*} else {
            while (cells.size() < DAYS_COUNT) {
                DateObject dateObject = new DateObject();
                dateObject.date = calendar.getTime();

                if (validDate(calendar.getTime())) {
                    if (checkSoccerBooked(schduleList, dateObject).status == DateObject.STATUS_BOOKED) {
                        dateObject = checkSoccerBooked(schduleList, dateObject);
                    } else {
                        dateObject.status = DateObject.STATUS_AVAILABLE;
                    }
                } else {
                    dateObject.status = DateObject.STATUS_UNAVAILABLE;
                }

                cells.add(dateObject);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
        }*/
        // update grid

        calendarAdapter.updateData(cells);
    }

    private boolean isAllBookedSlot(List<ManageSchedulesModel.Slot> slot) {
        for (int i = 0; i < slot.size(); i++) {
            if (!slot.get(i).getBookedStatusId().equalsIgnoreCase("3")) {
                return false;
            }
        }
        return true;
    }


    /**
     * check date is greater or equal to current and this month date
     *
     * @param date
     * @return
     */
    private boolean validDate(Date date) {
        boolean status = false;
        String date2String = CalendarView.dateFormat.format(date);
        try {

            Date date2 = CalendarView.dateFormat.parse(date2String);
            if (baseDate.equals(date2)) {
                status = true;
            } else if (date.after(baseDate) && baseDate.getMonth() == date.getMonth()) {
                status = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return status;
    }


    public int compare(Date d1, Date d2) {
        if (d1.getYear() != d2.getYear())
            return d1.getYear() - d2.getYear();
        if (d1.getMonth() != d2.getMonth())
            return d1.getMonth() - d2.getMonth();
        return d1.getDate() - d2.getDate();
    }


    /**
     * Method to check if current soccer field is booked or not Based on Booking dare (Slots are not considered).
     *
     * @param schduleList
     * @param dateObject
     * @return
     */
    private DateObject checkChaletBooked(List<ManageSchedulesModel.Schdule> schduleList, DateObject dateObject) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        if (schduleList != null && schduleList.size() > 0)
            for (int i = 0; i < schduleList.size(); i++) {
                String dateStr = schduleList.get(i).getBookedDate();
                Date date = new Date();
                try {
                    date = simpleDateFormat.parse(dateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (compare(date, dateObject.date) == 0) {
                    dateObject.status = DateObject.STATUS_BOOKED;
                    dateObject.bookedSlots = schduleList.get(i).getSlot();
                    break;
                }
            }
        return dateObject;
    }

    /**
     * Method to check if current soccer field is booked or not Based on slots booked.
     *
     * @param schduleList
     * @param dateObject
     * @return
     */
    private DateObject checkSoccerBooked(List<ManageSchedulesModel.Schdule> schduleList, DateObject dateObject) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        if (schduleList != null && schduleList.size() > 0)
            for (int i = 0; i < schduleList.size(); i++) {
                String dateStr = schduleList.get(i).getBookedDate();
                Date date = new Date();
                try {
                    date = simpleDateFormat.parse(dateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (compare(date, dateObject.date) == 0) {
                    dateObject.bookedSlots = schduleList.get(i).getSlot();
                    if (schduleList.get(i).getSlot().size() == allSlots.size()) {
                        dateObject.status = DateObject.STATUS_BOOKED;
                    }
                }

            }
        return dateObject;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (isSoccerField || !isFromAvailability) {
            selectDate(position);
        }
    }

    private void selectDate(int position) {
        ArrayList<DateObject> fullList = ((CalendarAdapter) gridView.getAdapter()).getFullList();
        if (fullList.get(position).status == DateObject.STATUS_AVAILABLE) {
            for (int i = 0; i < gridView.getAdapter().getCount(); i++) {
                fullList.get(i).isSelected = false;
            }
            if (fullList.get(position).status == DateObject.STATUS_AVAILABLE) {
                fullList.get(position).isSelected = true;
                selectedDate = fullList.get(position).date;
                if (onDateSelectedListener != null)
                    onDateSelectedListener.onDateSelected(fullList.get(position), allSlots);
            }

            calendarAdapter.notifyDataSetChanged();
        }
    }


}
