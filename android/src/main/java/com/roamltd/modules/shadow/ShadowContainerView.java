package com.roamltd.modules.shadow;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.facebook.react.views.view.ReactViewGroup;

public class ShadowContainerView extends FrameLayout {

    private ReactViewGroup viewGroup;
    private CellShadowDrawable shadowDrawable;

    public ShadowContainerView(Context context) {
        super(context);

         shadowDrawable = new CellShadowDrawable(context.getResources(), CellShadowDrawable.Type.LargeCorner);
        setBackground(shadowDrawable);

        viewGroup = new ReactViewGroup(context);
        addView(viewGroup, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public CellShadowDrawable getShadowDrawable() {
        return shadowDrawable;
    }

    public ReactViewGroup getViewGroup() {
        return viewGroup;
    }
}
