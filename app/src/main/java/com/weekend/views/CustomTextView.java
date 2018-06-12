package com.weekend.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.weekend.R;

public class CustomTextView extends TextView  {

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        try {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
            int font = a.getInt(R.styleable.CustomTextView_font_type, 0);
            a.recycle();
            String str;
            switch (font) {
                case 1:
                    str = "fonts/AvenirNextLTPro_Bold_0.otf";
                    break;
                case 2:
                    str = "fonts/AvenirNextLTPro_Demi_0.otf";
                    break;
                case 3:
                    str = "fonts/AvenirNextLTPro_DemiIt_0.otf";
                    break;
                case 4:
                    str = "fonts/AvenirNextLTPro_It_0.otf";
                    break;
                case 5:
                    str = "fonts/AvenirNextLTPro_Medium.otf";
                    break;
                case 6:
                    str = "fonts/AvenirNextLTPro_Regular_0.otf";
                    break;
                default:
                    str = "fonts/AvenirNextLTPro_Regular_0.otf";
                    break;
            }

            setTypeface(FontManager.getInstance(getContext()).loadFont(str));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    private void internalInit(Context context, AttributeSet attrs) {

    }
}
