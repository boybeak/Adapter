package com.github.boybeak.autobind.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import com.github.boybeak.adapter.AbsDataBindingHolder
import com.github.boybeak.adapter.annotation.HolderInfo
import com.github.boybeak.adapter.annotation.LayoutInfo
import com.github.boybeak.autobind.Crash
import com.github.boybeak.autobind.CrashInfoActivity
import com.github.boybeak.autobind.R
import com.github.boybeak.autobind.databinding.LayoutCrashBinding
import com.github.boybeak.autobind.startActivity

@HolderInfo(layoutId = R.layout.layout_crash,
        layoutInfo = LayoutInfo(name = "CrashImpl", source = Crash::class))
class CrashHolder(b: LayoutCrashBinding) : AbsDataBindingHolder<CrashImpl, LayoutCrashBinding>(b) {
    override fun onBindData(context: Context, layout: CrashImpl, position: Int, adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        binding().crash = layout.source
        binding().root.setOnClickListener {
            Intent(context, CrashInfoActivity::class.java)
                    .putExtra("crash", layout.source)
                    .startActivity(context)
        }
    }
}