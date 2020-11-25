package com.example.minimoneybox.ui.main

import android.os.Bundle
import com.example.minimoneybox.R
import com.example.minimoneybox.ui.BaseActivity
import com.example.minimoneybox.ui.main.useraccounts.UserAccountsFragment

class MainActivity: BaseActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        testFragment()
    }

    private fun testFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, UserAccountsFragment())
            .commit()
    }
}