package com.tallesperozzo.agileprocesschallenge.view.components;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.tallesperozzo.agileprocesschallenge.R;

/*
 * SimpleDividerItemDecoration
 *
 * This component was created to separate the list itens following the material design patterns.
 *
 * Created by Talles Perozzo
 */

public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
    private final Drawable divider;
    private final Context context;

    public SimpleDividerItemDecoration(Context context) {
        this.context = context;
        divider = context.getResources().getDrawable(R.drawable.line_divider, context.getTheme());
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int left = ((2 * parent.getWidth()) / 7);
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + divider.getIntrinsicHeight();

            Resources r = context.getResources();
            int px  = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    16,
                    r.getDisplayMetrics()
            );

            if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                //divider.setBounds(90 + 5 * px, top, right - px, bottom);
                divider.setBounds(left, top, right - px, bottom);
                divider.draw(c);
            }
            else{
                divider.setBounds(left, top, right - px, bottom);
                divider.draw(c);
            }
        }
    }
}