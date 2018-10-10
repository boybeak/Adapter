package com.github.boybeak.autobind.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView

import com.github.boybeak.adapter.AbsDataBindingHolder
import com.github.boybeak.adapter.annotation.LayoutInfo
import com.github.boybeak.adapter.annotation.HolderInfo
import com.github.boybeak.adapter.generated.TextImpl
import com.github.boybeak.autobind.R
import com.github.boybeak.autobind.databinding.LayoutTextBinding

@HolderInfo(
        layoutId = R.layout.layout_text,
        viewBindingClass = LayoutTextBinding::class,
        layoutInfo = LayoutInfo(
                name = "TextImpl",
                source = String::class
        )
)
class TextHolder(binding: LayoutTextBinding) : AbsDataBindingHolder<TextImpl, LayoutTextBinding>(binding) {

    override fun onBindData(context: Context, layout: TextImpl, position: Int, adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        binding().text = layout.source
    }
}
