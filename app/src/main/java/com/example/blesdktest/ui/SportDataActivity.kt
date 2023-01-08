package com.example.blesdktest.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.blesdktest.IBleSdkCallback
import com.example.blesdktest.viewModel.SmartWViewModel
import com.example.blesdktest.databinding.ActivitySportDataBinding
import com.veepoo.protocol.VPOperateManager
import com.veepoo.protocol.model.datas.HeartData
import com.veepoo.protocol.model.datas.SportData
import com.veepoo.protocol.model.datas.TemptureDetectData

class SportDataActivity : AppCompatActivity() {
    private lateinit var smartWViewModel: SmartWViewModel
    private lateinit var binding: ActivitySportDataBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySportDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.refresh.setOnClickListener {

        }
        setUpViewModel()
    }


    private fun setUpViewModel() {
        smartWViewModel = ViewModelProvider(this)[SmartWViewModel::class.java]
        smartWViewModel.sportData.observe(this) {
            showSportData(it)
        }
        smartWViewModel.cekStep()


    }

    private fun showSportData(sportData: SportData) {
        binding.data1.text = "Total langkah : " + sportData.step.toString()
        binding.data2.text = "Total kalori terbakar :" + sportData.kcal.toString() + "Kcal"
        binding.data3.text = "Jarak : " + sportData.dis.toString()
        binding.data4.text = sportData.calcType.toString()
        binding.data5.text = sportData.triaxialX.toString()
        binding.data6.text = sportData.triaxialY.toString()
        binding.data7.text = sportData.triaxialZ.toString()
    }

    companion object {
        private val TAG = SportDataActivity::class.java.simpleName
    }
}