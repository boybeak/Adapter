package com.github.boybeak.autobind

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.boybeak.autobind.databinding.ActivityCrashInfoBinding

class CrashInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCrashInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_crash_info)

        binding.crash = intent.getParcelableExtra("crash")

    }
}
