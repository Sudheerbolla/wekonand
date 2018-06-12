package com.weekend.calendar;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.weekend.models.ManageSchedulesModel;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by hb on 26/7/16.
 */
public class CalenderPagerAdapter extends FragmentStatePagerAdapter {
    private boolean isFromAvailability;
    public ManageSchedulesModel.SchduleDetail scheduleDetailList;
    public Context mContext;
    CalendarView.OnDateSelectedListener onDateSelectedListener;
    private Calendar calendar;
    private MonthCalenderFragment calenderFragment;
    private HashMap<Integer, MonthCalenderFragment> monthCalenderFragmentHashMap;

    public CalenderPagerAdapter(FragmentManager fm, Context context, Calendar calendar, CalendarView.OnDateSelectedListener onDateSelectedListener, boolean isFromAvailability) {
        super(fm);
        this.mContext = context;
        this.calendar = calendar;
        this.onDateSelectedListener = onDateSelectedListener;
        this.isFromAvailability = isFromAvailability;
        monthCalenderFragmentHashMap = new HashMap<>();
    }

    @Override
    public Fragment getItem(int position) {
        Calendar tempCalender = (Calendar) this.calendar.clone();

        switch (position) {
            case 0:
                tempCalender.add(Calendar.MONTH, 5);
                break;
            case 1:
                tempCalender.add(Calendar.MONTH, 4);
                break;
            case 2:
                tempCalender.add(Calendar.MONTH, 3);
                break;
            case 3:
                tempCalender.add(Calendar.MONTH, 2);
                break;
            case 4:
                tempCalender.add(Calendar.MONTH, 1);
                break;
            case 5:
                tempCalender.add(Calendar.MONTH, 0);
                break;
        }
        MonthCalenderFragment monthCalenderFragment = MonthCalenderFragment.newInstance(tempCalender, position, onDateSelectedListener, isFromAvailability);
        monthCalenderFragmentHashMap.put(position, monthCalenderFragment);
        return monthCalenderFragment;
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        monthCalenderFragmentHashMap.remove(position);
    }

/*

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            calenderFragment = ((MonthCalenderFragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }
*/


    public MonthCalenderFragment getFragmentAt(int position) {
        return monthCalenderFragmentHashMap.get(position);
    }
}
