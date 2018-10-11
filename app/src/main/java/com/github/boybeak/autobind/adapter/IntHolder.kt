package com.github.boybeak.autobind.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import com.github.boybeak.adapter.AbsDataBindingHolder
import com.github.boybeak.adapter.annotation.HolderInfo
import com.github.boybeak.adapter.annotation.LayoutInfo
import com.github.boybeak.adapter.generated.IntImpl
import com.github.boybeak.autobind.R
import com.github.boybeak.autobind.databinding.LayoutIntBinding

@HolderInfo(layoutId = R.layout.layout_int, viewBindingClass = LayoutIntBinding::class,
        layoutInfo = LayoutInfo(name = "IntImpl", source = Integer::class))
class IntHolder(b: LayoutIntBinding) : AbsDataBindingHolder<IntImpl, LayoutIntBinding>(b) {
    override fun onBindData(context: Context, layout: IntImpl, position: Int, adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        binding().text = layout.source.toString()
    }
}