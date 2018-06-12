package com.weekend.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.animation.Animation;

import com.weekend.HomeActivity;
import com.weekend.utils.FragmentUtils;

public class BaseFragment extends Fragment {
    public static final String TAG = BaseFragment.class.getSimpleName();
    public HomeActivity activity;
    public WeekendApplication application;
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (HomeActivity) getActivity();
        application = (WeekendApplication) activity.getApplication();
    }

    @Override
    public void onPause() {
        super.onPause();
        activity.showHideProgress(false);
    }

    /**
     * Method to show loading dialog
     *
     * @param title        Title of loading dialog
     * @param isCancelable isCancelable
     */
    public void showProgressDialog(final String title, final boolean isCancelable) {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            mProgressDialog = new ProgressDialog(activity);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage(title);
            mProgressDialog.setCancelable(isCancelable);
            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to hide Loading dialog
     */
    public void hideProgressDialog() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            mProgressDialog = null;
        } catch (Exception e) {
            mProgressDialog = null;
        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (FragmentUtils.sDisableFragmentAnimations) {
            Animation a = new Animation() {
            };
            a.setDuration(0);
            return a;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }
}
