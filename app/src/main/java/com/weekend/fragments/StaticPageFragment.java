package com.weekend.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.weekend.R;
import com.weekend.base.BaseFragment;
import com.weekend.base.WeekendApplication;
import com.weekend.server.factory.WSUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StaticPageFragment extends BaseFragment {
    public static final String TAG = StaticPageFragment.class.getSimpleName();
    private static final String ARGS_PAGE_TITLE = "page_title";
    private static final String ARGS_FROM = "from";
    @Bind(R.id.wv_page)
    WebView wvPage;
    private View rootView;
    private String pageTitle = "";
    private boolean isFromSignUp;

    public StaticPageFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StaticPageFragment.
     */
    public static StaticPageFragment newInstance(String pageTitle, boolean fromSignUp) {
        StaticPageFragment fragment = new StaticPageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGS_PAGE_TITLE, pageTitle);
        bundle.putBoolean(ARGS_FROM, fromSignUp);
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
                rootView = inflater.inflate(R.layout.fragment_app_info, null);
            }
            ButterKnife.bind(this, rootView);
            if (isFromSignUp) {
                activity.setTopbar(pageTitle, true, false, true, false, false, false, true, false, false, false, false);
            } else {
                activity.setTopbar(pageTitle, true, false, false, false, false, true, true, false, false, false, false);
            }
            initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }

    @Override
    public void onResume() {
        WeekendApplication.selectedFragment = TAG + "-" + pageTitle;
        super.onResume();
    }

    /**
     * Make a call in onCreate() only
     */
    private void getBundleData() {
        if (getArguments() != null) {
            pageTitle = getArguments().getString(ARGS_PAGE_TITLE);
            isFromSignUp = getArguments().getBoolean(ARGS_FROM);
        }
    }

    private void initialize() {
        loadPage();
    }

    private void loadPage() {
        activity.showHideProgress(true);
        wvPage.getSettings().setJavaScriptEnabled(true);
        wvPage.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                activity.showHideProgress(false);
            }
        });
        if (pageTitle.equalsIgnoreCase(getString(R.string.about_us))) {
            wvPage.loadUrl(WSUtils.ABOUT_US_URL);
        } else if (pageTitle.equalsIgnoreCase(getString(R.string.terms_conditions))) {
            wvPage.loadUrl(WSUtils.TERMS_CONDITIONS_URL);
        } else if (pageTitle.equalsIgnoreCase(getString(R.string.privacy_policy))) {
            wvPage.loadUrl(WSUtils.PRIVACY_POLICY_URL);
        } else if (pageTitle.equalsIgnoreCase(getString(R.string.faqs))) {
            wvPage.loadUrl(WSUtils.FAQ_URL);
        }else if (pageTitle.equalsIgnoreCase(getString(R.string.credit))) {
            wvPage.loadUrl(WSUtils.CREDITS_URL); //TODO: need to change with live url
        }
    }

}
