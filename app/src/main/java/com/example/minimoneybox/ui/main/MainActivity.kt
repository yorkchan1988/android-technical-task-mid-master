package com.example.minimoneybox.ui.main

import android.os.Bundle
import com.example.minimoneybox.R
import com.example.minimoneybox.ui.BaseActivity

class MainActivity: BaseActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
    }
}