package com.github.boybeak.autobind.adapter;

import android.databinding.BindingAdapter;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

public class Bindings {
    @BindingAdapter("color")
    public static void showColor(AppCompatTextView view, int color) {
        view.setBackgroundColor(color);
        view.setText(Integer.toHexString(color));
        view.setTextColor(0xffffff & color);
    }
}
