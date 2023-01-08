package com.example.blesdktest.ui

import android.util.Log
import com.veepoo.protocol.listener.base.IBleWriteResponse

class WriteResponse() : IBleWriteResponse {
    override fun onResponse(code: Int) {
        Log.i(TAG, "write cmd status:$code")
    }

    companion object {
        private val TAG = WriteResponse::class.java.simpleName
    }
}
