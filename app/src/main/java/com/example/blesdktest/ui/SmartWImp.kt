package com.example.blesdktest.ui

import com.example.blesdktest.IBleSdkCallback
import com.veepoo.protocol.VPOperateManager

class SmartWImp(private val callback: IBleSdkCallback) {
    private val writeResponse: WriteResponse = WriteResponse()

    fun checkHearRate() {
        VPOperateManager.getMangerInstance(VPOperateManager.mContext).startDetectHeart(
            writeResponse
        ) {
            callback.onHeartDataChange(it)
        }
    }

    fun checkTemp() {
        VPOperateManager.getMangerInstance(VPOperateManager.mContext).startDetectTempture(
            writeResponse
        ) { temptureDetectData ->
            callback.onTmpDataChange(temptureDetectData)
        }
    }

    fun readStep() {
        VPOperateManager.getMangerInstance(VPOperateManager.mContext).readSportStep(
            writeResponse
        ) { sportData ->
            callback.onSportDataChange(sportData)
        }
    }


}