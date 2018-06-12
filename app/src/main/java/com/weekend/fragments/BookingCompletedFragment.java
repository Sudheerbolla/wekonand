package com.weekend.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weekend.R;
import com.weekend.base.BaseFragment;
import com.weekend.base.WeekendApplication;
import com.weekend.utils.Constants;
import com.weekend.views.CustomTextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Hymavathi.kadali on 19/7/16.
 */
public class BookingCompletedFragment extends BaseFragment implements View.OnClickListener {

    public static final String TAG = BookingCompletedFragment.class.getSimpleName();
    private static final String BUNDLE_BOOKING_ID = "booking_id";
    @Bind(R.id.tv_booking_id)
    CustomTextView tvBookingId;
    private View rootView;
    private String bookingId;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param bookingId
     * @return A new instance of fragment BookingCompletedFragment.
     */
    public static BookingCompletedFragment newInstance(String bookingId) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_BOOKING_ID, bookingId);
        BookingCompletedFragment fragment = new BookingCompletedFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBundleData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try {
            if (rootView == null) {
                rootView = inflater.inflate(R.layout.fragment_booking_completed, null);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initialize();
                    }
                }, Constants.FRAGMENT_TRANSACTION_DELAY);
            }
            ButterKnife.bind(this, rootView);
            activity.setTopbar(getString(R.string.booking_completed), true, false, true, false, false, false, true,
                    false, false, false, false, true);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rootView;
    }

    @Override
    public void onResume() {
        WeekendApplication.selectedFragment = TAG;
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initialize() {
        tvBookingId.setText(bookingId);
    }

    private void getBundleData() {
        if (getArguments() != null) {
            bookingId = getArguments().getString(BUNDLE_BOOKING_ID);
        }
    }

    @Override
    @OnClick({R.id.tv_ok_thanks})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ok_thanks:
                onOkEvent();
                break;
            default:
                break;
        }
    }

    private void onOkEvent() {
        activity.replaceFragment(MyBookingsFragment.newInstance(), false, true, false, true);
    }
}
