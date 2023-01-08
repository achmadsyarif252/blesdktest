package com.example.blesdktest.oad.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class NotificationActivity : Activity() {
    /* access modifiers changed from: protected */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isTaskRoot) {
            val intent = Intent(this, OadActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtras(getIntent().extras!!)
            startActivity(intent)
        }
        finish()
    }
}