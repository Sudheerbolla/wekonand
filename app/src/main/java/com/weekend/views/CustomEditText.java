package com.weekend.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.EditText;

import com.weekend.R;

public class CustomEditText extends EditText {
    private int font;

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        try {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText);
            String str;
            int font = a.getInt(R.styleable.CustomEditText_font_type, 0);
            a.recycle();
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
}
