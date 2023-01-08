package com.example.blesdktest.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blesdktest.IBleSdkCallback
import com.example.blesdktest.ui.SmartWImp
import com.example.blesdktest.ui.WriteResponse
import com.veepoo.protocol.VPOperateManager
import com.veepoo.protocol.model.datas.*

class SmartWViewModel : ViewModel() {
    private var _sportData = MutableLiveData<SportData>()
    val sportData: LiveData<SportData> = _sportData

    private var _heartRate = MutableLiveData<HeartData>()
    val heartRate: LiveData<HeartData> = _heartRate

    private var _tempData = MutableLiveData<Float>()
    val tmpData: LiveData<Float> = _tempData

    fun cekTmp() {
        swi.checkTemp()
    }

    fun checkHr() {
        swi.checkHearRate()
    }

    fun cekStep() {
        swi.readStep()
    }

    private val swi = SmartWImp(object : IBleSdkCallback {
        override fun onHeartDataChange(heartData: HeartData) {
            _heartRate.value = heartData
        }

        override fun onSportDataChange(sportData: SportData) {
            _sportData.value = sportData
        }

        override fun onTmpDataChange(tmpDetectData: TemptureDetectData) {
            _tempData.value = tmpDetectData.tempture
        }

    })


}