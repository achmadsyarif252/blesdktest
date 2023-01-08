package com.example.blesdktest.oad.service

import android.app.Activity
import com.example.blesdktest.oad.activity.NotificationActivity
import vpno.nordicsemi.android.dfu.DfuBaseService

class DfuService : DfuBaseService() {
    /* access modifiers changed from: protected */
    public override fun getNotificationTarget(): Class<out Activity?> {
        return NotificationActivity::class.java
    }
}