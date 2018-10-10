package com.github.boybeak.autobind

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.boybeak.adapter.Converter
import com.github.boybeak.adapter.DataBindingAdapter
import com.github.boybeak.adapter.annotation.AdapterConfig
import com.github.boybeak.adapter.annotation.CodeType
import com.github.boybeak.adapter.generated.TextImpl
import kotlinx.android.synthetic.main.activity_main.*

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
        val list = List(100) {
            "$it"
        }
        adapter.addAll(list, object : Converter<String, TextImpl> {
            override fun convert(data: String?, adapter: DataBindingAdapter): TextImpl {
                return TextImpl(data)
            }
        }).autoNotify()

    }
}
