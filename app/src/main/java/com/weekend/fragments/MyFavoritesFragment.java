package com.weekend.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.weekend.R;
import com.weekend.adapters.MyFavoritesAdapter;
import com.weekend.base.BaseFragment;
import com.weekend.base.WeekendApplication;
import com.weekend.interfaces.IPropertyClicks;
import com.weekend.interfaces.IReportClick;
import com.weekend.models.AbuseReasonListModel;
import com.weekend.models.FavoriteModel;
import com.weekend.models.RemoveFavoriteModel;
import com.weekend.models.SaveAbuseReasonModel;
import com.weekend.server.IParser;
import com.weekend.server.WSFactory;
import com.weekend.server.WSResponse;
import com.weekend.server.factory.WSUtils;
import com.weekend.storage.LocalStorage;
import com.weekend.utils.CommonUtil;
import com.weekend.utils.Constants;
import com.weekend.utils.PopupUtil;
import com.weekend.utils.StaticUtil;
import com.weekend.views.CustomRecyclerView;
import com.weekend.views.CustomTextView;
import com.weekend.views.Paginate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class MyFavoritesFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener,
        Paginate.Callbacks, IParser<WSResponse>, IPropertyClicks, IReportClick {
    public static final String TAG = MyFavoritesFragment.class.getSimpleName();
    @Bind(R.id.rg_soccer_chalet)
    RadioGroup rgSoccerChalet;
    @Bind(R.id.rb_chalet)
    RadioButton rbChalet;
    @Bind(R.id.v_chalets)
    View vChalets;
    @Bind(R.id.v_soccer_fields)
    View vSoccerFields;
    @Bind(R.id.rv_favorite_list)
    CustomRecyclerView rvFavoriteList;
    @Bind(R.id.tv_no_result)
    CustomTextView tvNoResult;
    private View rootView;
    private LocalStorage localStorage;
    private WSFactory wsFactory = new WSFactory();
    private String propertyTypeId = "1";//Chalet=1, Soccer field=2
    private String propertyId;

    private List<FavoriteModel.Datum> favoriteList;
    private MyFavoritesAdapter favoritesAdapter;
    private boolean isNextPageAvailable = false;
    private boolean isLoading = false;
    private int pageIndex = 1;
    private int selectedFavoritePosition;
    private int itemPos;
    private List<AbuseReasonListModel.Datum> abuseReasonList;

    public MyFavoritesFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MyFavoritesFragment.
     */
    public static MyFavoritesFragment newInstance() {
        MyFavoritesFragment fragment = new MyFavoritesFragment();
        Bundle bundle = new Bundle();
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
                rootView = inflater.inflate(R.layout.fragment_favorite_list, null);
            }
            ButterKnife.bind(this, rootView);
            activity.setTopbar(activity.getString(R.string.my_favorites), true, false, true, false, false, true, true,
                    false, false, false, false,true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initialize();
                }
            }, Constants.FRAGMENT_TRANSACTION_DELAY);
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

    private void initialize() {
        localStorage = LocalStorage.getInstance(activity);
        favoriteList = new ArrayList<>();
        LinearLayoutManager mLinearLayoutManger = new LinearLayoutManager(activity);
        rvFavoriteList.setLayoutManager(mLinearLayoutManger);
        setTheAdapter();
        pageIndex = 1;
        rvFavoriteList.setListPagination(this);
        if (isAdded()) {
            requestAbuseReasonList();
            requestFavoriteList(true);
        }
    }

    private void getBundleData() {
    }

    @Override
    @OnCheckedChanged({R.id.rb_chalet, R.id.rb_soccer_fields})
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.rb_chalet:
                if (isChecked) {
                    if (favoriteList != null && favoriteList.size() > 0) {
                        rvFavoriteList.scrollToPosition(0);
                    }
                    vChalets.setVisibility(View.VISIBLE);
                    vSoccerFields.setVisibility(View.INVISIBLE);
                    tvNoResult.setText(R.string.you_have_no_favorite_chalets);
                    pageIndex = 1;
                    propertyTypeId = "1";
                    favoriteList.clear();
                    requestFavoriteList(true);
                }
                break;
            case R.id.rb_soccer_fields:
                if (isChecked) {
                    if (favoriteList != null && favoriteList.size() > 0) {
                        rvFavoriteList.scrollToPosition(0);
                    }
                    vChalets.setVisibility(View.INVISIBLE);
                    vSoccerFields.setVisibility(View.VISIBLE);
                    tvNoResult.setText(R.string.you_have_no_favorite_soccer_fields);
                    pageIndex = 1;
                    propertyTypeId = "2";
                    favoriteList.clear();
                    requestFavoriteList(true);
                }
                break;
        }
    }


    private void setTheAdapter() {
        favoritesAdapter = new MyFavoritesAdapter(activity, favoriteList, (IPropertyClicks) this);
        rvFavoriteList.setAdapter(favoritesAdapter);
    }

    @Override
    public void onItemClick(int position) {
        itemPos = position;
        if (rgSoccerChalet.getCheckedRadioButtonId() == R.id.rb_chalet) {
            activity.replaceFragment(ChaletParentDetailsFragment.newInstance(favoriteList.get(position).getPropertyId(), ""), true, true, false, false);
        } else {
            activity.replaceFragment(SoccerFieldParentDetailsFragment.newInstance(favoriteList.get(position).getPropertyId(), ""), true, true, false, false);
        }
    }

    @Override
    public void onFavoriteClick(int position) {
        selectedFavoritePosition = position;
        StaticUtil.hideSoftKeyboard(activity);
        requestRemoveFavorite(position);
    }

    @Override
    public void onReportClick(int position) {
        propertyId = favoriteList.get(position).getPropertyId();
        String name = String.format("%s %s", localStorage.getString(Constants.SP_KEY_FIRST_NAME, ""), localStorage.getString(Constants.SP_KEY_LAST_NAME, ""));
        PopupUtil.showReport(activity, name, abuseReasonList, (IReportClick) this);
    }

    @Override
    public void onReportSubmitClick(String comment, String name, String reasonId) {
        requestSaveAbuseReason(comment, name, reasonId);
    }

    //Recycler view pagination
    @Override
    public void onLoadMore() {
        isLoading = true;
        requestFavoriteList(false);
    }

    @Override
    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public boolean hasLoadedAllItems() {
        if (isNextPageAvailable) {
            return false;
        }
        /*if next page not available means we loaded all items so return true */
        return true;
    }

    private void requestFavoriteList(boolean showDefaultProgress) {
        if (showDefaultProgress) {
            activity.showHideProgress(true);
        }
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_USER_ID, localStorage.getString(Constants.SP_KEY_USER_ID, ""));
        params.put(WSUtils.KEY_PROPERTY_TYPE_ID, propertyTypeId);
        params.put(WSUtils.KEY_PAGE_INDEX, pageIndex + "");
        params.put(WSUtils.KEY_TOKEN, localStorage.getString(Constants.SP_KEY_TOKEN, ""));
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_FAVORITE_LIST);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_FAVORITE_LIST, this);
    }

    private void requestRemoveFavorite(int position) {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_USERID, localStorage.getString(Constants.SP_KEY_USER_ID, ""));
        params.put(WSUtils.KEY_PROPERTYID, favoriteList.get(position).getPropertyId());
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        params.put(WSUtils.KEY_TOKEN, localStorage.getString(Constants.SP_KEY_TOKEN, ""));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_REMOVE_FAVORITE);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_REMOVE_FAVORITE, this);
    }

    private void requestAbuseReasonList() {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_ABUSE_REASON_LIST);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_ABUSE_REASON_LIST, this);
    }

    private void requestSaveAbuseReason(String comment, String name, String reasonId) {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_PROPERTY_ID, propertyId);
        params.put(WSUtils.KEY_USER_ID, localStorage.getString(Constants.SP_KEY_USER_ID, ""));
        params.put(WSUtils.KEY_POSTEDBY_NAME, name);
        params.put(WSUtils.KEY_ABUSE_REASON_ID, reasonId);
        params.put(WSUtils.KEY_COMMENT, comment);
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_SAVE_ABUSE_REASON);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_SAVE_ABUSE_REASON, this);
    }

    @Override
    public void successResponse(int requestCode, WSResponse response) {
        if (activity != null && isAdded()) {
            switch (requestCode) {
                case WSUtils.REQ_FAVORITE_LIST:
                    parseFavoriteList((FavoriteModel) response);
                    break;
                case WSUtils.REQ_REMOVE_FAVORITE:
                    parseRemoveFavorite((RemoveFavoriteModel) response);
                    break;
                case WSUtils.REQ_ABUSE_REASON_LIST:
                    parseAbuseReasonList((AbuseReasonListModel) response);
                    break;
                case WSUtils.REQ_SAVE_ABUSE_REASON:
                    parseSaveAbuseReason((SaveAbuseReasonModel) response);
                    break;
            }
        }
    }

    @Override
    public void errorResponse(int requestCode, Throwable t) {
        activity.showHideProgress(false);
    }

    @Override
    public void noInternetConnection(int requestCode) {
        CommonUtil.showSnackbar(getView(), getString(R.string.no_internet_connection));
        activity.showHideProgress(false);
    }

    private void parseFavoriteList(FavoriteModel response) {
        activity.showHideProgress(false);
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                if (response.getSettings().getNextPage().equalsIgnoreCase("1")) {
                    if (response.getData() != null) {
                        isNextPageAvailable = true;
                        pageIndex = pageIndex + 1;
                    }
                } else {
                    isNextPageAvailable = false;
                }

                if (pageIndex == 1 && favoriteList != null) {
                    favoriteList.clear();
                }

                isLoading = false;

                if (response.getData() != null && response.getData().size() > 0) {
                    favoriteList.addAll(response.getData());
                    tvNoResult.setVisibility(View.GONE);
                    rvFavoriteList.setVisibility(View.VISIBLE);
                } else {
                    tvNoResult.setVisibility(View.VISIBLE);
                    rvFavoriteList.setVisibility(View.GONE);
                }
            } else if (response.getSettings().getSuccess().equalsIgnoreCase("2")) {
                activity.manageTokenExpire();
            } else {
                tvNoResult.setVisibility(View.VISIBLE);
                rvFavoriteList.setVisibility(View.GONE);
            }

            if (favoritesAdapter != null) {
                favoritesAdapter.notifyDataSetChanged();
            }
        }
    }


    private void parseRemoveFavorite(RemoveFavoriteModel response) {
        activity.showHideProgress(false);
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage());
                favoriteList.remove(selectedFavoritePosition);
                favoritesAdapter.notifyDataSetChanged();
                if (favoriteList.size() == 0) {
                    tvNoResult.setVisibility(View.VISIBLE);
                    rvFavoriteList.setVisibility(View.GONE);
                }
            } else if (response.getSettings().getSuccess().equalsIgnoreCase("2")) {
                activity.manageTokenExpire();
            } else {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
            }
        }
    }

    private void parseAbuseReasonList(AbuseReasonListModel response) {
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                abuseReasonList = response.getData();
            } else {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage());
            }
        }
        addBlankSelectionAbuseReason();
        activity.showHideProgress(false);
    }

    private void parseSaveAbuseReason(SaveAbuseReasonModel response) {
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage());
            } else {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage());
            }
        }
        activity.showHideProgress(false);
    }

    private void addBlankSelectionAbuseReason() {
        AbuseReasonListModel.Datum tempModel = new AbuseReasonListModel.Datum();
        tempModel.setAbuseReasonMasterId("");
        tempModel.setLangCode(getString(R.string.lang_id));
        tempModel.setReasonTitle(getString(R.string.please_select_reason));
        if (abuseReasonList == null) {
            abuseReasonList = new ArrayList<>();
        }

        abuseReasonList.add(0, tempModel);
    }

}
