package com.roamltd.modules.shadow;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.view.View;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

public class ShadowViewManager extends ViewGroupManager<ShadowContainerView> {

    public static final String REACT_CLASS = "RCTShadowView";

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected ShadowContainerView createViewInstance(ThemedReactContext reactContext) {
        return new ShadowContainerView(reactContext);
    }

    @ReactProp(name = "insets")
    public void setInsets(ShadowContainerView view, ReadableMap insets) {
        CellShadowDrawable shadow = view.getShadowDrawable();

        shadow.setInsetTop(insets.hasKey("top") ? insets.getInt("top") : 0);
        shadow.setInsetBottom(insets.hasKey("bottom") ? insets.getInt("bottom") : 0);
        shadow.setInsetLeft(insets.hasKey("left") ? insets.getInt("left") : 0);
        shadow.setInsetRight( insets.hasKey("right") ? insets.getInt("right") : 0);
    }

    @ReactProp(name = "color")
    public void setColor(ShadowContainerView view, String color) {
        if(color == null) {
            view.getShadowDrawable().setColorFilter(null);
        } else {
            try {
                view.getShadowDrawable().setColorFilter(new LightingColorFilter(Color.TRANSPARENT, Color.parseColor(color)));
            } catch(IllegalArgumentException e) {
                view.getShadowDrawable().setColorFilter(null);
            }
        }
    }

    @Override
    public void addView(ShadowContainerView parent, View child, int index) {
        parent.getViewGroup().addView(child, index);
    }

    @Override
    public int getChildCount(ShadowContainerView parent) {
        return parent.getViewGroup().getChildCount();
    }

    @Override
    public View getChildAt(ShadowContainerView parent, int index) {
        return parent.getViewGroup().getChildAt(index);
    }

    @Override
    public void removeViewAt(ShadowContainerView parent, int index) {
        parent.getViewGroup().removeViewAt(index);
    }
}
