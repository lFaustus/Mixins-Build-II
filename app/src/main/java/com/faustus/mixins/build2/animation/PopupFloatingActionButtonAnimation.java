package com.faustus.mixins.build2.animation;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;

/**
 * Created by flux on 6/22/15.
 */
public class PopupFloatingActionButtonAnimation {

    private int AnimationDuration;
    private int StartDelay = 500;
    private int Alpha = 1;
    private Interpolator interpolator;
    private View v;
    private ObjectAnimator animator;
    private PropertyValuesHolder pvhTranslationX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X,0);
    private PropertyValuesHolder pvhTranslationY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y,0);
    private PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofFloat(View.SCALE_X,1);
    private PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y,1);
    private PropertyValuesHolder pvhAlpha = PropertyValuesHolder.ofFloat(View.ALPHA,1);

    public PopupFloatingActionButtonAnimation(View v) {
        this.v = v;
    }

    public PopupFloatingActionButtonAnimation setAnimationDuration(int AnimationDuration)
    {
        this.AnimationDuration = AnimationDuration;
        return this;
    }

    public PopupFloatingActionButtonAnimation setInterpolator(Interpolator interpolator)
    {
        this.interpolator = interpolator;
        return this;
    }

    public PopupFloatingActionButtonAnimation setViewScaleX(int ScaleX)
    {
        this.pvhScaleX = PropertyValuesHolder.ofFloat(View.SCALE_X,ScaleX);
        return this;
    }

    public PopupFloatingActionButtonAnimation setViewScaleY(int ScaleY)
    {
        this.pvhScaleX = PropertyValuesHolder.ofFloat(View.SCALE_Y,ScaleY);
        return this;
    }

    public PopupFloatingActionButtonAnimation setTranslationX(int TranslationX)
    {
        this.pvhTranslationX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X,TranslationX);
        return this;
    }

    public PopupFloatingActionButtonAnimation setTranslationY(int TranslationY)
    {
        this.pvhTranslationY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y,TranslationY);
        return this;
    }

    public PopupFloatingActionButtonAnimation setStartDelay(int StartDelay)
    {
        this.StartDelay = StartDelay;
        return this;
    }

    public PopupFloatingActionButtonAnimation setAlpha(int Alpha)
    {
        this.pvhAlpha = PropertyValuesHolder.ofFloat(View.ALPHA,Alpha);
        this.Alpha = Alpha;
        return this;
    }

    public PopupFloatingActionButtonAnimation startAnimation()
    {
        this.animator = ObjectAnimator.ofPropertyValuesHolder(this.v,this.pvhTranslationX,this.pvhTranslationY,this.pvhScaleX,this.pvhScaleY,this.pvhAlpha);
        this.animator.setInterpolator(this.interpolator);
        this.animator.setDuration(this.AnimationDuration);
        this.animator.setStartDelay(this.StartDelay);
        this.animator.start();



        if(this.Alpha == 0)
            this.v.setVisibility(View.GONE);
        else
            this.v.setVisibility(View.VISIBLE);
        return this;
    }

}
