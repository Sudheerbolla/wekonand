package com.weekend.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.weekend.R;
import com.weekend.adapters.CityAdapter;
import com.weekend.adapters.CountryAdapter;
import com.weekend.adapters.MultipleSelectionAdapter;
import com.weekend.adapters.ReportAbuseReasonsAdapter;
import com.weekend.adapters.SingleSelectionAdapter;
import com.weekend.interfaces.ICancelBookingClick;
import com.weekend.interfaces.ICityClick;
import com.weekend.interfaces.ICountryClick;
import com.weekend.interfaces.IMultipleSelectionClick;
import com.weekend.interfaces.IReportClick;
import com.weekend.interfaces.ISingleSelectionClick;
import com.weekend.models.AbuseReasonListModel;
import com.weekend.models.CityModel;
import com.weekend.models.CountryModel;
import com.weekend.models.NeighbourhoodModel;
import com.weekend.models.SearchOptionModel;
import com.weekend.views.CustomEditText;
import com.weekend.views.CustomTextView;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by hb on 7/14/2016.
 */
public class PopupUtil {

    public static void showAlert(Context context, String title, String message, String positiveText, String negativeText,
                                 final View.OnClickListener onPositiveClick, final View.OnClickListener onNegativeClick) {
        StaticUtil.setWindowDimensions(context);

        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_layout);
        WindowManager.LayoutParams mWindowLayoutParams = new WindowManager.LayoutParams();
        Window mWindow = dialog.getWindow();
        mWindowLayoutParams.copyFrom(mWindow.getAttributes());
        mWindowLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.width = Constants.SCREEN_WIDTH - ((Constants.SCREEN_WIDTH) / 6);
        mWindow.setAttributes(mWindowLayoutParams);
        mWindow.setBackgroundDrawableResource(android.R.color.transparent);

        CustomTextView tvTitle = (CustomTextView) dialog.findViewById(R.id.tv_title);
        CustomTextView tvMessage = (CustomTextView) dialog.findViewById(R.id.tv_message);
        CustomTextView tvOk = (CustomTextView) dialog.findViewById(R.id.tv_ok);
        CustomTextView tvCancel = (CustomTextView) dialog.findViewById(R.id.tv_cancel);

        if (!TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        } else {
            tvTitle.setVisibility(View.GONE);
        }

        if (message != null) {
            tvMessage.setText(message);
        }

        if (!TextUtils.isEmpty(positiveText)) {
            tvOk.setText(positiveText);
        }
        if (!TextUtils.isEmpty(negativeText)) {
            tvCancel.setText(negativeText);
        }

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (onPositiveClick != null)
                    onPositiveClick.onClick(v);
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (onNegativeClick != null)
                    onNegativeClick.onClick(view);
            }
        });

        dialog.show();
    }

    public static void showAlert(Context context, String message, final View.OnClickListener onPositiveClick) {
        showAlert(context, "", message, "", "", onPositiveClick, null);
    }

    public static void showAlert(Context context, String message, String positiveText, final View.OnClickListener onPositiveClick) {
        showAlert(context, context.getString(R.string.app_name), message, positiveText, "", onPositiveClick, null);
    }

    public static void showAlert(Context context, String message, String positiveText, String negativeText, final View.OnClickListener onPositiveClick) {
        showAlert(context, context.getString(R.string.app_name), message, positiveText, negativeText, onPositiveClick, null);
    }

    public static void showAlert(Context context, String message, String positiveText, String negativeText, final View.OnClickListener onPositiveClick, final View.OnClickListener onNegativeClick) {
        showAlert(context, context.getString(R.string.app_name), message, positiveText, negativeText, onPositiveClick, onNegativeClick);
    }

    public static void showAlertMessage(Context context, String title, String message, String positiveText, final View.OnClickListener onPositiveClick) {
        StaticUtil.setWindowDimensions(context);
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_message);
        WindowManager.LayoutParams mWindowLayoutParams = new WindowManager.LayoutParams();
        Window mWindow = dialog.getWindow();
        mWindowLayoutParams.copyFrom(mWindow.getAttributes());
        mWindowLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.width = Constants.SCREEN_WIDTH - ((Constants.SCREEN_WIDTH) / 6);
        mWindow.setAttributes(mWindowLayoutParams);
        mWindow.setBackgroundDrawableResource(android.R.color.transparent);

        CustomTextView tvTitle = (CustomTextView) dialog.findViewById(R.id.tv_title);
        CustomTextView tvMessage = (CustomTextView) dialog.findViewById(R.id.tv_message);
        CustomTextView tvOk = (CustomTextView) dialog.findViewById(R.id.tv_ok);

        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
        if (message != null) {
            tvMessage.setText(message);
        }
        if (!TextUtils.isEmpty(positiveText)) {
            tvOk.setText(positiveText);
        }

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (onPositiveClick != null)
                    onPositiveClick.onClick(v);
            }
        });
        dialog.show();
    }

    public static void showCountry(Context context, String title, List<CountryModel.Datum> countryList, ICountryClick countryClick, final View.OnClickListener onCancelClick) {
        StaticUtil.setWindowDimensions(context);
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_list_view);
        WindowManager.LayoutParams mWindowLayoutParams = new WindowManager.LayoutParams();
        Window mWindow = dialog.getWindow();
        mWindowLayoutParams.copyFrom(mWindow.getAttributes());
        mWindowLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.width = Constants.SCREEN_WIDTH - ((Constants.SCREEN_WIDTH) / 5);
        mWindowLayoutParams.height = Constants.SCREEN_HEIGHT - ((Constants.SCREEN_HEIGHT) / 3);
        mWindow.setAttributes(mWindowLayoutParams);
        mWindow.setBackgroundDrawableResource(android.R.color.transparent);

        ImageView ivClose = (ImageView) dialog.findViewById(R.id.iv_close);
        CustomTextView tvTitle = (CustomTextView) dialog.findViewById(R.id.tv_title);
        RecyclerView rvCountry = (RecyclerView) dialog.findViewById(R.id.rv_list);

        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }

        CountryAdapter countryAdapter = new CountryAdapter(context, countryList, countryClick, dialog);
        rvCountry.setHasFixedSize(true);
//        rvCountry.setLayoutAnimation(AnimationUtil.getListBottomAnimation());
        LinearLayoutManager weekdayLayout = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rvCountry.setLayoutManager(weekdayLayout);
        rvCountry.setAdapter(countryAdapter);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (onCancelClick != null)
                    onCancelClick.onClick(view);
            }
        });
        dialog.show();
    }

    public static void showCity(Context context, String title, List<CityModel.Datum> cityList, ICityClick cityClick, final View.OnClickListener onCancelClick) {
        StaticUtil.setWindowDimensions(context);
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_list_view);
        WindowManager.LayoutParams mWindowLayoutParams = new WindowManager.LayoutParams();
        Window mWindow = dialog.getWindow();
        mWindowLayoutParams.copyFrom(mWindow.getAttributes());
        mWindowLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.width = Constants.SCREEN_WIDTH - ((Constants.SCREEN_WIDTH) / 5);
        mWindowLayoutParams.height = Constants.SCREEN_HEIGHT - ((Constants.SCREEN_HEIGHT) / 3);
        mWindow.setAttributes(mWindowLayoutParams);
        mWindow.setBackgroundDrawableResource(android.R.color.transparent);

        ImageView ivClose = (ImageView) dialog.findViewById(R.id.iv_close);
        CustomTextView tvTitle = (CustomTextView) dialog.findViewById(R.id.tv_title);
        RecyclerView rvCity = (RecyclerView) dialog.findViewById(R.id.rv_list);

        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }

        CityAdapter cityAdapter = new CityAdapter(context, cityList, cityClick, dialog);
        rvCity.setHasFixedSize(true);
//        rvCity.setLayoutAnimation(AnimationUtil.getListBottomAnimation());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rvCity.setLayoutManager(layoutManager);
        rvCity.setAdapter(cityAdapter);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (onCancelClick != null)
                    onCancelClick.onClick(view);
            }
        });
        dialog.show();
    }

    public static void showStaticPage(Context context, String title, String content) {
        StaticUtil.setWindowDimensions(context);
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_static_page);
        WindowManager.LayoutParams mWindowLayoutParams = new WindowManager.LayoutParams();
        Window mWindow = dialog.getWindow();
        mWindowLayoutParams.copyFrom(mWindow.getAttributes());
        mWindowLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mWindowLayoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        mWindow.setAttributes(mWindowLayoutParams);
        mWindow.setBackgroundDrawableResource(android.R.color.white);

        ImageView ivBack = (ImageView) dialog.findViewById(R.id.iv_back);
        CustomTextView tvTitle = (CustomTextView) dialog.findViewById(R.id.tv_title);
        CustomTextView tvContent = (CustomTextView) dialog.findViewById(R.id.tv_content);
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
        if (!TextUtils.isEmpty(content)) {
            tvContent.setText(content);
        }
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void showSingleButtonAlert(Context context, String message) {
        showSingleButtonAlert(context, context.getString(R.string.app_name), message, context.getString(R.string.ok), null);
    }

    public static void showSingleButtonAlert(Context context, String title, String message, String positiveText, final View.OnClickListener onPositiveClick) {
        StaticUtil.setWindowDimensions(context);

        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_layout);
        WindowManager.LayoutParams mWindowLayoutParams = new WindowManager.LayoutParams();
        Window mWindow = dialog.getWindow();
        mWindowLayoutParams.copyFrom(mWindow.getAttributes());
        mWindowLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.width = Constants.SCREEN_WIDTH - ((Constants.SCREEN_WIDTH) / 6);
        mWindow.setAttributes(mWindowLayoutParams);
        mWindow.setBackgroundDrawableResource(android.R.color.transparent);

        CustomTextView tvTitle = (CustomTextView) dialog.findViewById(R.id.tv_title);
        CustomTextView tvMessage = (CustomTextView) dialog.findViewById(R.id.tv_message);
        CustomTextView tvOk = (CustomTextView) dialog.findViewById(R.id.tv_ok);
        CustomTextView tvCancel = (CustomTextView) dialog.findViewById(R.id.tv_cancel);
        tvCancel.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        } else {
            tvTitle.setVisibility(View.GONE);
        }

        if (message != null) {
            tvMessage.setText(message);
        }

        if (!TextUtils.isEmpty(positiveText)) {
            tvOk.setText(positiveText);
        }

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (onPositiveClick != null)
                    onPositiveClick.onClick(v);
            }
        });

        dialog.show();
    }

    public static void showSingleSelection(Context context, String title, final List<SearchOptionModel.CityLocation> cityLocationList, final List<SearchOptionModel.ChaletLeasingTypeList> leasingTypeLists, final List<SearchOptionModel.GetSectionList> sectionLists, final List<SearchOptionModel.ChaletSize> chaletSizeList, final ISingleSelectionClick singleSelectionClick) {
        StaticUtil.setWindowDimensions(context);
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_list_view_single_selection);
        WindowManager.LayoutParams mWindowLayoutParams = new WindowManager.LayoutParams();
        Window mWindow = dialog.getWindow();
        mWindowLayoutParams.copyFrom(mWindow.getAttributes());
        mWindowLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.width = Constants.SCREEN_WIDTH - ((Constants.SCREEN_WIDTH) / 5);
        mWindowLayoutParams.height = Constants.SCREEN_HEIGHT - ((Constants.SCREEN_HEIGHT) / 3);
        mWindow.setAttributes(mWindowLayoutParams);
        mWindow.setBackgroundDrawableResource(android.R.color.transparent);

        final ImageView ivClose = (ImageView) dialog.findViewById(R.id.iv_close);
        CustomTextView tvTitle = (CustomTextView) dialog.findViewById(R.id.tv_title);
        CustomTextView tvNone = (CustomTextView) dialog.findViewById(R.id.tv_none);
        RecyclerView rvCity = (RecyclerView) dialog.findViewById(R.id.rv_list);

        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }

        SingleSelectionAdapter searchOptionsAdapter = new SingleSelectionAdapter(context, cityLocationList, leasingTypeLists, sectionLists, chaletSizeList, singleSelectionClick, dialog);
        rvCity.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rvCity.setLayoutManager(layoutManager);
        rvCity.setAdapter(searchOptionsAdapter);
        tvNone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (singleSelectionClick != null) {
                    if (cityLocationList != null && cityLocationList.size() > 0) {
                        singleSelectionClick.onNoneSelect("city");
                    } else if (leasingTypeLists != null && leasingTypeLists.size() > 0) {
                        singleSelectionClick.onNoneSelect("leasing");
                    } else if (sectionLists != null && sectionLists.size() > 0) {
                        singleSelectionClick.onNoneSelect("section");
                    } else if (chaletSizeList != null && chaletSizeList.size() > 0) {
                        singleSelectionClick.onNoneSelect("size");
                    }
                }
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private static MultipleSelectionAdapter adapterDialogList;

    public static void showMultipleSelection(final Context context, String title,
                                             final List<NeighbourhoodModel.Datum> neighbourHoodList,
                                             final List<SearchOptionModel.AmenitiesList> amenitiesLists,
                                             final List<SearchOptionModel.SuitableForList> suitableForLists,
                                             final List<SearchOptionModel.SoccerSize> soccerSizeList,
                                             final IMultipleSelectionClick multipleSelectionClick) {
        StaticUtil.setWindowDimensions(context);
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_list_view);
        WindowManager.LayoutParams mWindowLayoutParams = new WindowManager.LayoutParams();
        Window mWindow = dialog.getWindow();
        mWindowLayoutParams.copyFrom(mWindow.getAttributes());
        mWindowLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.width = Constants.SCREEN_WIDTH - ((Constants.SCREEN_WIDTH) / 5);
        mWindowLayoutParams.height = Constants.SCREEN_HEIGHT - ((Constants.SCREEN_HEIGHT) / 3);
        mWindow.setAttributes(mWindowLayoutParams);
        mWindow.setBackgroundDrawableResource(android.R.color.transparent);

        ImageView ivClose = (ImageView) dialog.findViewById(R.id.iv_close);
        CustomTextView tvDone = (CustomTextView) dialog.findViewById(R.id.tv_done);
        tvDone.setVisibility(View.VISIBLE);
        CustomTextView tvTitle = (CustomTextView) dialog.findViewById(R.id.tv_title);
        RecyclerView rvCity = (RecyclerView) dialog.findViewById(R.id.rv_list);

        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
        LinearLayoutManager mLinearLayoutManger = new LinearLayoutManager(context);
        rvCity.setLayoutManager(mLinearLayoutManger);
        adapterDialogList = new MultipleSelectionAdapter(context, neighbourHoodList, amenitiesLists, suitableForLists, soccerSizeList, multipleSelectionClick, dialog);
        rvCity.setAdapter(adapterDialogList);

        tvDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (multipleSelectionClick != null && neighbourHoodList != null) {
                    multipleSelectionClick.OnNeighbourItemSelected(adapterDialogList.getSelectedNeighbourList());
                } else if (multipleSelectionClick != null && amenitiesLists != null) {
                    multipleSelectionClick.OnAmenityItemSelected(adapterDialogList.getSelectedAmenityList());
                } else if (multipleSelectionClick != null && suitableForLists != null) {
                    multipleSelectionClick.OnSuitableForItemSelected(adapterDialogList.getSelectedSuitableForList());
                } else if (multipleSelectionClick != null && soccerSizeList != null) {
                    multipleSelectionClick.OnSoccerSizeItemSelected(adapterDialogList.getSelectedSoccerSizeList());
                }
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                adapterDialogList.makeUnselectedItemsClear();
            }
        });
        dialog.show();
    }

    public static void showPriceRange(Context context, String sunToWed, String thu, String fri, String sat, String eidFitr, String eidAdha, String downPayment) {
        StaticUtil.setWindowDimensions(context);
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_price_range);
        WindowManager.LayoutParams mWindowLayoutParams = new WindowManager.LayoutParams();
        Window mWindow = dialog.getWindow();
        mWindowLayoutParams.copyFrom(mWindow.getAttributes());
        mWindowLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.width = Constants.SCREEN_WIDTH - ((Constants.SCREEN_WIDTH) / 6);
        mWindow.setAttributes(mWindowLayoutParams);
        mWindow.setBackgroundDrawableResource(android.R.color.transparent);

        ImageView ivClose = (ImageView) dialog.findViewById(R.id.iv_close);
        CustomTextView tvSunWedPrice = (CustomTextView) dialog.findViewById(R.id.tv_sun_wed_price);
        CustomTextView tvthuPrice = (CustomTextView) dialog.findViewById(R.id.tv_thu_price);
        CustomTextView tvFriPrice = (CustomTextView) dialog.findViewById(R.id.tv_fri_price);
        CustomTextView tvSatPrice = (CustomTextView) dialog.findViewById(R.id.tv_sat_price);
        CustomTextView tvEidFitrPrice = (CustomTextView) dialog.findViewById(R.id.tv_eid_al_fitr_price);
        CustomTextView tvEidAdhaPrice = (CustomTextView) dialog.findViewById(R.id.tv_eid_al_adha_price);
        CustomTextView tvDownPaymentPrice = (CustomTextView) dialog.findViewById(R.id.tv_down_price);
        RelativeLayout rlDownpayment = (RelativeLayout) dialog.findViewById(R.id.rl_downpayment);
        CustomTextView tvDownPaymentHint = (CustomTextView) dialog.findViewById(R.id.tv_downpayment_hint);

        if (!TextUtils.isEmpty(sunToWed)) {
            tvSunWedPrice.setText(sunToWed);
        }
        if (!TextUtils.isEmpty(thu)) {
            tvthuPrice.setText(thu);
        }
        if (!TextUtils.isEmpty(fri)) {
            tvFriPrice.setText(fri);
        }
        if (!TextUtils.isEmpty(sat)) {
            tvSatPrice.setText(sat);
        }
        if (!TextUtils.isEmpty(eidFitr)) {
            tvEidFitrPrice.setText(eidFitr);
        }
        if (!TextUtils.isEmpty(eidAdha)) {
            tvEidAdhaPrice.setText(eidAdha);
        }
        if (!TextUtils.isEmpty(downPayment)) {
            tvDownPaymentPrice.setText(downPayment);
            rlDownpayment.setVisibility(View.VISIBLE);
            tvDownPaymentHint.setVisibility(View.GONE);
        } else {
            rlDownpayment.setVisibility(View.GONE);
            tvDownPaymentHint.setVisibility(View.VISIBLE);
        }

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void showBookingCancellation(final Context context, final ICancelBookingClick cancelBookingClick) {
        StaticUtil.setWindowDimensions(context);
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_booking_cancellation);
        WindowManager.LayoutParams mWindowLayoutParams = new WindowManager.LayoutParams();
        Window mWindow = dialog.getWindow();
        mWindowLayoutParams.copyFrom(mWindow.getAttributes());
        mWindowLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.width = Constants.SCREEN_WIDTH - ((Constants.SCREEN_WIDTH) / 6);
        mWindow.setAttributes(mWindowLayoutParams);
        mWindow.setBackgroundDrawableResource(android.R.color.transparent);

        ImageView ivClose = (ImageView) dialog.findViewById(R.id.iv_close);
        CustomTextView tvSendRequest = (CustomTextView) dialog.findViewById(R.id.tv_send_request);
        CustomEditText edtEnterReason = (CustomEditText) dialog.findViewById(R.id.edt_enter_reason);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tvSendRequest.setTag(edtEnterReason);
        tvSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomEditText edtEnterReason = (CustomEditText) v.getTag();
                if (TextUtils.isEmpty(edtEnterReason.getText().toString())) {
                    showAlertMessage(context, context.getString(R.string.app_name), context.getString(R.string.please_enter_reason), context.getString(R.string.ok), null);
                } else {
                    dialog.dismiss();
                    if (cancelBookingClick != null)
                        cancelBookingClick.onCancelBooking(edtEnterReason.getText().toString());
                }
            }
        });

        dialog.show();
    }

    public static void showRefundPolicy(Context context, String title, String policyType, String PolicyDescription, String cancelDescription, String refundDescription, boolean isRefund, final View.OnClickListener onClick) {
        StaticUtil.setWindowDimensions(context);
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_refund_policy);
        WindowManager.LayoutParams mWindowLayoutParams = new WindowManager.LayoutParams();
        Window mWindow = dialog.getWindow();
        mWindowLayoutParams.copyFrom(mWindow.getAttributes());
        mWindowLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.width = Constants.SCREEN_WIDTH - ((Constants.SCREEN_WIDTH) / 6);
        mWindow.setAttributes(mWindowLayoutParams);
        mWindow.setBackgroundDrawableResource(android.R.color.transparent);

        ImageView ivClose = (ImageView) dialog.findViewById(R.id.iv_close);
        CustomTextView tvTitle = (CustomTextView) dialog.findViewById(R.id.tv_title);
        CustomTextView tvPolicyType = (CustomTextView) dialog.findViewById(R.id.tv_policy_type);
        CustomTextView tvPolicyDescription = (CustomTextView) dialog.findViewById(R.id.tv_policy_description);
        CustomTextView tvCancellationDesc = (CustomTextView) dialog.findViewById(R.id.tv_cancellation_desc);
        CustomTextView tvRefundDesc = (CustomTextView) dialog.findViewById(R.id.tv_refund_desc);

        if (isRefund) {
            tvPolicyType.setVisibility(View.VISIBLE);
            tvCancellationDesc.setVisibility(View.VISIBLE);
        } else {
            tvPolicyDescription.setVisibility(View.GONE);
            tvCancellationDesc.setVisibility(View.GONE);
            tvRefundDesc.setVisibility(View.GONE);
            tvPolicyType.setText(cancelDescription);
        }
        if (!TextUtils.isEmpty(PolicyDescription)) {
            tvPolicyDescription.setText(PolicyDescription);
        }
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
        if (!TextUtils.isEmpty(policyType)) {
            tvPolicyType.setText(policyType);
        }
        if (!TextUtils.isEmpty(cancelDescription)) {
            tvCancellationDesc.setText(cancelDescription);
        }
        if (!TextUtils.isEmpty(refundDescription)) {
            tvRefundDesc.setText(refundDescription);
        }

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (onClick != null)
                    onClick.onClick(v);
            }
        });

        tvCancellationDesc.setVisibility(View.GONE);
        dialog.show();
    }

    public static void showAlertActivatedNow(Context context, final View.OnClickListener onClick) {
        StaticUtil.setWindowDimensions(context);
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_activated_now);
        WindowManager.LayoutParams mWindowLayoutParams = new WindowManager.LayoutParams();
        Window mWindow = dialog.getWindow();
        mWindowLayoutParams.copyFrom(mWindow.getAttributes());
        mWindowLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.width = Constants.SCREEN_WIDTH - ((Constants.SCREEN_WIDTH) / 6);
        mWindow.setAttributes(mWindowLayoutParams);
        mWindow.setBackgroundDrawableResource(android.R.color.transparent);

        CustomTextView tvOk = (CustomTextView) dialog.findViewById(R.id.tv_ok);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (onClick != null)
                    onClick.onClick(v);
            }
        });
        dialog.show();
    }

    public static void showEditProfileMobileUpdateMenu(Context context, final View.OnClickListener onMenu1Click, final View.OnClickListener onMenu2Click, final View.OnClickListener onCancelClick) {
        StaticUtil.setWindowDimensions(context);
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.inflate_menu_option_selection);
        WindowManager.LayoutParams mWindowLayoutParams = new WindowManager.LayoutParams();
        Window mWindow = dialog.getWindow();
        mWindowLayoutParams.copyFrom(mWindow.getAttributes());
        mWindowLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mWindowLayoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        mWindow.setAttributes(mWindowLayoutParams);
        //mWindow.setBackgroundDrawableResource(android.R.color.transparent);

        CustomTextView tvMenu1 = (CustomTextView) dialog.findViewById(R.id.tv_menu1);
        CustomTextView tvMenu2 = (CustomTextView) dialog.findViewById(R.id.tv_menu2);
        CustomTextView tvCancel = (CustomTextView) dialog.findViewById(R.id.tv_cancel);

        tvMenu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (onMenu1Click != null)
                    onMenu1Click.onClick(v);
            }
        });
        tvMenu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (onMenu2Click != null)
                    onMenu2Click.onClick(v);
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (onCancelClick != null)
                    onCancelClick.onClick(v);
            }
        });
        dialog.show();
    }

    private static TextView selectedfilter, selectedsort;

    public static PopupWindow showFilterPopup(Context context, View anchorView, String filterAllCount, String filterAvailableCount,
                                              String filterUnavailableCount, boolean isShowWithScheduleAvailable,
                                              final View.OnClickListener onFilterAllClick,
                                              final View.OnClickListener onFilterAvailableClick,
                                              final View.OnClickListener onFilterUnAvailableClick,
                                              final View.OnClickListener onFilterScheduleAvailableClick) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        CardView cvFilterMenu = (CardView) layoutInflater.inflate(R.layout.inflate_filter_layout, null, false);
        int OFFSET_X = 0;
        int OFFSET_Y = StaticUtil.dpToPx(context, 85);
        int[] location = new int[2];
        // Get the x, y location and store it in the location[] array
        // location[0] = x, location[1] = y.
        anchorView.getLocationOnScreen(location);
        //Initialize the Point with x, and y positions
        Point point = new Point();
        point.x = 0;//location[0];
        point.y = 0;//location[1];
        final PopupWindow popupWindow = new PopupWindow(cvFilterMenu, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, point.x + OFFSET_X, point.y + OFFSET_Y);
        //PopupWindowCompat.showAsDropDown(popupWindow, anchorView, -anchorView.getMeasuredWidth(), -anchorView.getMeasuredHeight(), Gravity.TOP | Gravity.RIGHT);
        ImageView ivFilterClose = (ImageView) cvFilterMenu.findViewById(R.id.iv_filter_close);
        final CustomTextView tvFilterAll = (CustomTextView) cvFilterMenu.findViewById(R.id.tv_filter_all);
        final CustomTextView tvFilterAvailable = (CustomTextView) cvFilterMenu.findViewById(R.id.tv_filter_available);
        final CustomTextView tvFilterUnavailable = (CustomTextView) cvFilterMenu.findViewById(R.id.tv_filter_unavailable);
        final CustomTextView tvFilterScheduleAvailable = (CustomTextView) cvFilterMenu.findViewById(R.id.tv_filter_schedule_available);
        if (selectedfilter != null) {
            cvFilterMenu.findViewById(selectedfilter.getId()).setSelected(true);
            if (selectedfilter.getId() == tvFilterAvailable.getId()) {
                tvFilterScheduleAvailable.setSelected(true);
                tvFilterScheduleAvailable.setEnabled(false);
                tvFilterScheduleAvailable.setAlpha(0.7f);
            } else {

            }
        } else {
            tvFilterAll.setSelected(true);
            tvFilterScheduleAvailable.setEnabled(true);
            tvFilterScheduleAvailable.setAlpha(1f);
        }
        if (!TextUtils.isEmpty(filterAllCount)) {
            tvFilterAll.setText(context.getString(R.string.all) + " (" + filterAllCount + ")");
        }
        if (!TextUtils.isEmpty(filterAvailableCount)) {
            tvFilterAvailable.setText(context.getString(R.string.available) + " (" + filterAvailableCount + ")");
        }
        if (!TextUtils.isEmpty(filterUnavailableCount)) {
            tvFilterUnavailable.setText(context.getString(R.string.unavailable) + " (" + filterUnavailableCount + ")");
        }

        tvFilterScheduleAvailable.setSelected(isShowWithScheduleAvailable);

        ivFilterClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        tvFilterAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedfilter = tvFilterAll;
                tvFilterScheduleAvailable.setEnabled(true);
                tvFilterScheduleAvailable.setAlpha(1f);
                popupWindow.dismiss();
                if (onFilterAllClick != null) {
                    onFilterAllClick.onClick(v);
                }
            }
        });
        tvFilterAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedfilter = tvFilterAvailable;
                popupWindow.dismiss();
                if (onFilterAvailableClick != null) {
                    tvFilterScheduleAvailable.setSelected(true);
                    tvFilterScheduleAvailable.setEnabled(false);
                    tvFilterScheduleAvailable.setAlpha(0.7f);
                    onFilterAvailableClick.onClick(v);
                }
            }
        });
        tvFilterUnavailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedfilter = tvFilterUnavailable;
                popupWindow.dismiss();
                tvFilterScheduleAvailable.setEnabled(true);
                tvFilterScheduleAvailable.setAlpha(1f);
                if (onFilterUnAvailableClick != null) {
                    onFilterUnAvailableClick.onClick(v);
                }
            }
        });
        tvFilterScheduleAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (onFilterScheduleAvailableClick != null) {
                    onFilterScheduleAvailableClick.onClick(v);
                }
            }
        });
        return popupWindow;
    }

    public static PopupWindow showSortPopup(Context context, View anchorView,
                                            final View.OnClickListener onSortRelevanceClick,
                                            final View.OnClickListener onSortPriceLowToHighClick,
                                            final View.OnClickListener onSortPriceHighToLowClick,
                                            final View.OnClickListener onSortNearestFirstClick) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        CardView cvFilterMenu = (CardView) layoutInflater.inflate(R.layout.inflate_sorting_layout, null, false);
        int OFFSET_X = 0;
        int OFFSET_Y = StaticUtil.dpToPx(context, 85);
        int[] location = new int[2];
        // Get the x, y location and store it in the location[] array
        // location[0] = x, location[1] = y.
        anchorView.getLocationOnScreen(location);
        //Initialize the Point with x, and y positions
        Point point = new Point();
        point.x = 0;//location[0];
        point.y = 0;//location[1];
        final PopupWindow popupWindow = new PopupWindow(cvFilterMenu, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, point.x + OFFSET_X, point.y + OFFSET_Y);
        //PopupWindowCompat.showAsDropDown(popupWindow, anchorView, -anchorView.getMeasuredWidth(), -anchorView.getMeasuredHeight(), Gravity.TOP | Gravity.RIGHT);
        ImageView ivFilterClose = (ImageView) cvFilterMenu.findViewById(R.id.iv_sort_close);
        final CustomTextView tvSortRelevance = (CustomTextView) cvFilterMenu.findViewById(R.id.tv_sort_relevance);
        final CustomTextView tvSortPriceLowToHigh = (CustomTextView) cvFilterMenu.findViewById(R.id.tv_sort_price_low_to_high);
        final CustomTextView tvSortPriceHighToLow = (CustomTextView) cvFilterMenu.findViewById(R.id.tv_sort_price_high_to_low);
        final CustomTextView tvSortNearestFirst = (CustomTextView) cvFilterMenu.findViewById(R.id.tv_sort_nearest_first);
        if (selectedsort != null) {
            cvFilterMenu.findViewById(selectedsort.getId()).setSelected(true);
        } else {
            tvSortRelevance.setSelected(true);
        }
        ivFilterClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        tvSortRelevance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                selectedsort = tvSortRelevance;
                if (onSortRelevanceClick != null) {
                    onSortRelevanceClick.onClick(v);
                }
            }
        });
        tvSortPriceLowToHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                selectedsort = tvSortPriceLowToHigh;
                if (onSortPriceLowToHighClick != null) {
                    onSortPriceLowToHighClick.onClick(v);
                }
            }
        });
        tvSortPriceHighToLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                selectedsort = tvSortPriceHighToLow;
                if (onSortPriceHighToLowClick != null) {
                    onSortPriceHighToLowClick.onClick(v);
                }
            }
        });
        tvSortNearestFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                selectedsort = tvSortNearestFirst;
                if (onSortNearestFirstClick != null) {
                    onSortNearestFirstClick.onClick(v);
                }
            }
        });
        return popupWindow;
    }

    public static void showReport(final Context context, String name, final List<AbuseReasonListModel.Datum> abuseReasonList, final IReportClick reportClick) {
        StaticUtil.setWindowDimensions(context);
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_report_property);
        WindowManager.LayoutParams mWindowLayoutParams = new WindowManager.LayoutParams();
        Window mWindow = dialog.getWindow();
        mWindowLayoutParams.copyFrom(mWindow.getAttributes());
        mWindowLayoutParams.width = Constants.SCREEN_WIDTH - ((Constants.SCREEN_WIDTH) / 5);
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindow.setAttributes(mWindowLayoutParams);
        mWindow.setBackgroundDrawableResource(android.R.color.transparent);

        ImageView ivCancel = (ImageView) dialog.findViewById(R.id.iv_cancel);
        final CustomEditText edtName = (CustomEditText) dialog.findViewById(R.id.edt_name);
        final CustomEditText edtComment = (CustomEditText) dialog.findViewById(R.id.edt_comment);
        final Spinner spinnerReport = (Spinner) dialog.findViewById(R.id.spinner_report);
        CustomTextView tvSubmit = (CustomTextView) dialog.findViewById(R.id.tv_submit);

        if (!TextUtils.isEmpty(name)) {
            edtName.setText(name);
        }

        ReportAbuseReasonsAdapter reportAbuseReasonsAdapter = new ReportAbuseReasonsAdapter(context, abuseReasonList);
        spinnerReport.setAdapter(reportAbuseReasonsAdapter);
        spinnerReport.setSelection(0);//To keep selection for hint
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinnerReport);

            // Set popupWindow height to 500px
            popupWindow.setHeight(500);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String reasonId = abuseReasonList.get(spinnerReport.getSelectedItemPosition()).getAbuseReasonMasterId();
                String comment = edtComment.getText().toString();
                String name = edtName.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    showAlertMessage(context, context.getString(R.string.app_name), context.getString(R.string.please_enter_name), context.getString(R.string.ok), null);
                } else if (TextUtils.isEmpty(reasonId)) {
                    showAlertMessage(context, context.getString(R.string.app_name), context.getString(R.string.please_select_reason), context.getString(R.string.ok), null);
                } else if (TextUtils.isEmpty(comment)) {
                    showAlertMessage(context, context.getString(R.string.app_name), context.getString(R.string.please_enter_comment), context.getString(R.string.ok), null);
                } else {
                    dialog.dismiss();
                    reportClick.onReportSubmitClick(comment, name, reasonId);
                }
            }
        });

        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void showAlertText(Context context, String title, String text) {
        StaticUtil.setWindowDimensions(context);
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_text);
        WindowManager.LayoutParams mWindowLayoutParams = new WindowManager.LayoutParams();
        Window mWindow = dialog.getWindow();
        mWindowLayoutParams.copyFrom(mWindow.getAttributes());
        mWindowLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.width = Constants.SCREEN_WIDTH - ((Constants.SCREEN_WIDTH) / 6);
        mWindow.setAttributes(mWindowLayoutParams);
        mWindow.setBackgroundDrawableResource(android.R.color.transparent);

        ImageView ivClose = (ImageView) dialog.findViewById(R.id.iv_close);
        CustomTextView tvText = (CustomTextView) dialog.findViewById(R.id.tv_text);
        CustomTextView tvTitle = (CustomTextView) dialog.findViewById(R.id.tv_title);

        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
        if (!TextUtils.isEmpty(text)) {
            tvText.setText(text);
        }

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
