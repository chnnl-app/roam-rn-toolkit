package com.roamltd.modules.shadow;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.roamltd.R;

// Ported from the Roam Android Toolkit
public class CellShadowDrawable extends Drawable {
    public enum Type{
        DEBUG(-1),
        Square(0),
        SmallCorner(R.drawable.shadow_corner_small),
        LargeCorner(R.drawable.shadow_corner_large);

        private final int res;

        Type(@DrawableRes int res){
            this.res = res;
        }
    }

    private final Rect insets = new Rect();
    private final Rect drawRect = new Rect();
    private final Drawable childDrawable;

    public CellShadowDrawable(@NonNull Resources resources, @NonNull Type type){
        this(resources, type.res);
    }

    public CellShadowDrawable(@NonNull Resources resources, @DrawableRes int shadowRes) {
        this(shadowRes == -1? new ColorDrawable(Color.RED) : resources.getDrawable(shadowRes));
    }

    public CellShadowDrawable(@NonNull Drawable shadowDrawable) {
        super();

        this.childDrawable = shadowDrawable;
    }

    public CellShadowDrawable setInsetX(int dpOrDimenRes){
        insets.left = insets.right = dpOrDimenRes;
        invalidateSelf();
        return this;
    }

    public CellShadowDrawable setInsetRight(int dpOrDimenRes){
        insets.right = dpOrDimenRes;
        invalidateSelf();
        return this;
    }

    public CellShadowDrawable setInsetLeft(int dpOrDimenRes){
        insets.left = dpOrDimenRes;
        invalidateSelf();
        return this;
    }

    public CellShadowDrawable setInsetY(int dpOrDimenRes){
        insets.top = insets.bottom = dpOrDimenRes;
        invalidateSelf();
        return this;
    }

    public CellShadowDrawable setInset(int dpOrDimenRes){
        insets.left = insets.bottom = insets.top = insets.right = dpOrDimenRes;
        invalidateSelf();
        return this;
    }

    public CellShadowDrawable setInsetTop(int dpOrDimenRes){
        insets.top = dpOrDimenRes;
        invalidateSelf();
        return this;
    }

    public CellShadowDrawable setInsetBottom(int dpOrDimenRes){
        insets.bottom = dpOrDimenRes;
        invalidateSelf();
        return this;
    }

    @Override
    public void setAlpha(int alpha) {
        childDrawable.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        childDrawable.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return childDrawable.getOpacity();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        drawRect.set(getBounds());
        drawRect.left += insets.left;
        drawRect.right -= insets.right;
        drawRect.top += insets.top;
        drawRect.bottom -= insets.bottom;

        childDrawable.setBounds(drawRect);
        childDrawable.draw(canvas);
    }
}
