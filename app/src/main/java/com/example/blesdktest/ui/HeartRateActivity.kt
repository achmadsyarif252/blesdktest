package com.example.blesdktest.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.blesdktest.IBleSdkCallback
import com.example.blesdktest.viewModel.SmartWViewModel
import com.example.blesdktest.databinding.ActivityHeartRateBinding
import com.veepoo.protocol.VPOperateManager
import com.veepoo.protocol.VPOperateManager.mContext
import com.veepoo.protocol.model.datas.HeartData
import com.veepoo.protocol.model.datas.SportData
import com.veepoo.protocol.model.datas.TemptureDetectData

class HeartRateActivity() :
    AppCompatActivity() {
    private lateinit var binding: ActivityHeartRateBinding
    private lateinit var smartWViewModel: SmartWViewModel

    var writeResponse: WriteResponse = WriteResponse()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHeartRateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpViewModel()
    }


    private fun setUpViewModel() {
        smartWViewModel = ViewModelProvider(this)[SmartWViewModel::class.java]
        smartWViewModel.heartRate.observe(this) {
            showHeartRate(it)
        }
        smartWViewModel.checkHr()
    }

    private fun showHeartRate(heartData: HeartData?) {
        binding.HR.text = heartData?.data.toString()
        binding.status.text = heartData?.heartStatus.toString()
    }


}