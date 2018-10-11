package com.github.boybeak.autobind.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import com.github.boybeak.adapter.AbsDataBindingHolder
import com.github.boybeak.adapter.annotation.HolderInfo
import com.github.boybeak.adapter.annotation.LayoutInfo
import com.github.boybeak.adapter.generated.CrashImpl
import com.github.boybeak.autobind.Crash
import com.github.boybeak.autobind.R
import com.github.boybeak.autobind.databinding.LayoutCrashBinding

@HolderInfo(layoutId = R.layout.layout_crash, viewBindingClass = Void::class,
        layoutInfo = LayoutInfo(name = "CrashImpl", source = Crash::class))
class CrashHolder(b: LayoutCrashBinding) : AbsDataBindingHolder<CrashImpl, LayoutCrashBinding>(b) {
    override fun onBindData(context: Context, layout: CrashImpl, position: Int, adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        binding().crash = layout.source
    }
}