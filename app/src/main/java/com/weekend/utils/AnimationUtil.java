package com.weekend.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;

import com.weekend.R;

/**
 * Created by hb on 7/15/2016.
 */
public class AnimationUtil {

    //    public static LayoutAnimationController getListBottomAnimation() {
//        AnimationSet set = new AnimationSet(true);
//        Animation animation = new AlphaAnimation(0.0f, 0.1f);
//        animation.setDuration(500);
//        set.addAnimation(animation);
//
//        animation = new TranslateAnimation(
//                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
//                Animation.RELATIVE_TO_SELF, 2.0f, Animation.RELATIVE_TO_SELF, 0.0f
//        );
//        animation.setInterpolator(new DecelerateInterpolator());
//        animation.setDuration(500);
//        set.addAnimation(animation);
//        return new LayoutAnimationController(set, 0.5f);
//    }
    public static LayoutAnimationController getListBottomAnimation() {

        Animation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0.0f
        );
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setDuration(500);
        return new LayoutAnimationController(animation, 0.5f);
    }

    public static void openSlider(final View layout, boolean openVertically) {
        ObjectAnimator mOpenMenuAnimator;
        if (openVertically) {
            mOpenMenuAnimator = ObjectAnimator.ofFloat(layout, "y", Constants.SCREEN_HEIGHT, 0);
        } else {
            mOpenMenuAnimator = ObjectAnimator.ofFloat(layout, "x", Constants.SCREEN_WIDTH, 0);
        }
        mOpenMenuAnimator.setDuration(400);
        AnimatorSet mObjAnimatorSet = new AnimatorSet();
        mObjAnimatorSet.play(mOpenMenuAnimator);
        mOpenMenuAnimator.addListener(new ObjectAnimator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d("Opening animation status", "ENd");
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.d("Opening animation status", "Repeat");
            }

        });
        mObjAnimatorSet.start();
        layout.setVisibility(View.VISIBLE);
    }

    public static void closeSlider(final View layout, boolean closeVertically) {
        ObjectAnimator mCloseMenuAnimator;
        if (closeVertically) {
            mCloseMenuAnimator = ObjectAnimator.ofFloat(layout, "y", 0, Constants.SCREEN_HEIGHT);
        } else {
            mCloseMenuAnimator = ObjectAnimator.ofFloat(layout, "x", 0, Constants.SCREEN_HEIGHT);
        }

        mCloseMenuAnimator.setDuration(400);
        AnimatorSet mObjAnimatorSet = new AnimatorSet();
        mObjAnimatorSet.play(mCloseMenuAnimator);
        mCloseMenuAnimator.addListener(new ObjectAnimator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                layout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

        });
        mObjAnimatorSet.start();
        layout.setVisibility(View.VISIBLE);
    }

    public static Animation topEntry(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.enter_from_top);
    }

    public static Animation fadeIn(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.fade_in);
    }


    public static Animation expandFromTop(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.expand_from_top);
    }

}
