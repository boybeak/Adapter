package com.github.boybeak.autobind.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import com.github.boybeak.adapter.AbsDataBindingHolder
import com.github.boybeak.adapter.annotation.HolderInfo
import com.github.boybeak.adapter.annotation.LayoutInfo
import com.github.boybeak.adapter.generated.ColorImpl
import com.github.boybeak.autobind.R
import com.github.boybeak.autobind.databinding.LayoutColorBinding

@HolderInfo(layoutId = R.layout.layout_color, viewBindingClass = LayoutColorBinding::class,
        layoutInfo = LayoutInfo(name = "ColorImpl", source = Integer::class))
class ColorHolder(b: LayoutColorBinding) : AbsDataBindingHolder<ColorImpl, LayoutColorBinding>(b) {
    override fun onBindData(context: Context, layout: ColorImpl, position: Int, adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        binding().color = layout.source
    }
}