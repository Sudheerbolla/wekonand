package com.weekend.calendar;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.weekend.R;
import com.weekend.models.ManageSchedulesModel;
import com.weekend.views.CustomTextView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created on 1/8/16 at 4:21 PM.
 */

//    holiday_type
//                  0-no holiday,
//                  1-holiday1,
//                  2-holiday2

//    booked_status_id
//                  1-Cancel,
//                  2-Hold,
//                  3-Booked,
//                  4-Close"

//    availability =
//                  0 means booked
//                  1 means ready to process
public class CalendarView extends LinearLayout implements Serializable{
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat SERVER_BOOKED_DATE_FORAMT = new SimpleDateFormat("yyyy-MM-dd");
    transient SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.getDefault());
    transient SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    // internal components
    private transient  ImageView ivPrevious, ivNext;
    private transient  CustomTextView tvDate;
    private transient Calendar currentCalenderDate;
    private transient ViewPager viewPagerCalender;
    private transient CalenderPagerAdapter calenderPagerAdapter;
    private transient ViewPager.OnPageChangeListener onPageChangeListener;
    private transient ViewPager.OnPageChangeListener activityOnPageChangeListener;
    private transient ViewPager.OnPageChangeListener onPageChangeListener1 = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            activityOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            setHeaderDate(position);
            activityOnPageChangeListener.onPageSelected(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            activityOnPageChangeListener.onPageScrollStateChanged(state);
        }
    };

    public CalendarView(Context context) {
        super(context);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initControl(context);
    }


    /**
     * Load component XML layout
     */
    private void initControl(Context context) {
        if (!isInEditMode()) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.inflate_calendar_view, this);

            ivNext = (ImageView) findViewById(R.id.iv_calendar_next);
            ivPrevious = (ImageView) findViewById(R.id.iv_calendar_previous);
            tvDate = (CustomTextView) findViewById(R.id.tv_current_date);
            viewPagerCalender = (ViewPager) findViewById(R.id.vp_calender);
            currentCalenderDate = Calendar.getInstance();
            //currentCalenderDate.add(Calendar.DATE, 1);//Adding 1 day to making a restriction to not get a booking for current day
            currentCalenderDate.set(Calendar.HOUR_OF_DAY, 0);
            currentCalenderDate.set(Calendar.MINUTE, 0);
            currentCalenderDate.set(Calendar.SECOND, 0);
            currentCalenderDate.set(Calendar.MILLISECOND, 0);
            ivNext.setVisibility(View.INVISIBLE);
            ivPrevious.setVisibility(View.VISIBLE);
            ivPrevious.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewPagerCalender.getCurrentItem() - 1 >= 0)
                        viewPagerCalender.setCurrentItem(viewPagerCalender.getCurrentItem() - 1, true);
                }
            });

            ivNext.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewPagerCalender.getCurrentItem() + 1 <= viewPagerCalender.getAdapter().getCount() - 1)
                        viewPagerCalender.setCurrentItem(viewPagerCalender.getCurrentItem() + 1, true);
                }
            });

        }
    }

    /**
     * @param position Method is used to update title of pager
     */
    private void setHeaderDate(int position) {
        // update title
        Calendar tempCalender = (Calendar) this.currentCalenderDate.clone();
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

        tvDate.setText(monthFormat.format(tempCalender.getTime()) + " " + yearFormat.format(tempCalender.getTime()));
    }

    /**
     * Method is used to initiate viewpager for calender
     *
     * @param fm                     Child Fragment FragmentManager
     * @param onPageChangeListener   listener for calling WS when page is changed.
     * @param activity               for context
     * @param onDateSelectedListener callback when date is selected from calender
     */
    public void initView(FragmentManager fm, ViewPager.OnPageChangeListener onPageChangeListener, FragmentActivity activity, OnDateSelectedListener onDateSelectedListener, boolean isFromAvailability) {
        try {
            calenderPagerAdapter = new CalenderPagerAdapter(fm, activity, currentCalenderDate, onDateSelectedListener, isFromAvailability);
            viewPagerCalender.setAdapter(calenderPagerAdapter);
            viewPagerCalender.setOffscreenPageLimit(5);
            viewPagerCalender.setCurrentItem(5);
            setHeaderDate(5);
            this.activityOnPageChangeListener = onPageChangeListener;
            viewPagerCalender.addOnPageChangeListener(onPageChangeListener1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param pos
     * @return the fragment instance at pos.
     */
    public MonthCalenderFragment getFragmentAt(int pos) {
        return calenderPagerAdapter.getFragmentAt(pos);
    }

    /**
     * Update Calender View when wS response is finished
     *
     * @param scheduleArrayList shedule of current month
     * @param slotTimes         Availble slot list of current month
     * @param isSoccerField     true (Soccer) / false (Chalet)
     */
    public void setCalenderViewOnResponse(List<ManageSchedulesModel.Schdule> scheduleArrayList, List<ManageSchedulesModel.SlotTime> slotTimes, boolean isSoccerField) {
        MonthCalenderFragment monthCalenderFragment = calenderPagerAdapter.getFragmentAt(viewPagerCalender.getCurrentItem());
        monthCalenderFragment.updateCalendar(scheduleArrayList, slotTimes, isSoccerField);
    }


    public interface OnDateSelectedListener extends Serializable {
        void onDateSelected(DateObject selectedDate, List<ManageSchedulesModel.SlotTime> allSlots);
    }

    public ViewPager getViewPagerCalender() {
        return viewPagerCalender;
    }
}
