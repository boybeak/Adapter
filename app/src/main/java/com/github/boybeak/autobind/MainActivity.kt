package com.github.boybeak.autobind

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.github.boybeak.adapter.Converter
import com.github.boybeak.adapter.DataBindingAdapter
import com.github.boybeak.adapter.generated.CrashImpl
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: DataBindingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = DataBindingAdapter(this)
        recycler_view.adapter = adapter

        Api.service.list(0, "", "", 0, 12).enqueue(object : Callback<Result<List<Crash>>> {
            override fun onFailure(call: Call<Result<List<Crash>>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "failed", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Result<List<Crash>>>, response: Response<Result<List<Crash>>>) {
                val list = response.body()!!.data
                if (list.isEmpty()) {
                    Toast.makeText(this@MainActivity, "empty", Toast.LENGTH_SHORT).show()
                } else {
                    adapter.addAll(response.body()!!.data, object : Converter<Crash, CrashImpl> {
                        override fun convert(data: Crash?, adapter: DataBindingAdapter): CrashImpl {
                            return CrashImpl(data)
                        }

                    }).autoNotify()
                }
            }
        })
    }
}
