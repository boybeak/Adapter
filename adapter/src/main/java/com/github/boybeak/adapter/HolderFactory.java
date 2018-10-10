package com.github.boybeak.adapter;

import android.databinding.ViewDataBinding;

public interface HolderFactory {
    AbsDataBindingHolder getHolder(int viewType, ViewDataBinding binding);
}
