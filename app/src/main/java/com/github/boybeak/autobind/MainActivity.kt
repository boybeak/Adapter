package com.github.boybeak.autobind

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.boybeak.adapter.Converter
import com.github.boybeak.adapter.DataBindingAdapter
import com.github.boybeak.adapter.annotation.AdapterConfig
import com.github.boybeak.adapter.annotation.CodeType
import com.github.boybeak.adapter.generated.ColorImpl
import com.github.boybeak.adapter.generated.IntImpl
import com.github.boybeak.adapter.generated.TextImpl
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

@AdapterConfig(
        packageName = "com.github.boybeak.autobind",
        codeType = CodeType.JAVA
)
class MainActivity : AppCompatActivity() {

    private lateinit var adapter: DataBindingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = DataBindingAdapter(this)
        recycler_view.adapter = adapter
        val textList = List(20) {
            "text-$it"
        }
        adapter.addAll(textList, Converter<String, TextImpl> { data, adapter -> TextImpl(data) }).autoNotify()

        val intList = List(20) {
            it
        }
        adapter.addAll(intList, Converter<Int, IntImpl> { data, adapter -> IntImpl(data!!) }).autoNotify()

        val colorList = List(20) {
            Color.GREEN
        }
        adapter.addAll(colorList, Converter<Int, ColorImpl> { data, adapter -> ColorImpl(data) }).autoNotify()
    }
}
