package com.example.blesdktest

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.blesdktest.Oprate.Companion.SPORT_MODE_START_INDOOR
import com.example.blesdktest.Oprate.Companion.oprateStr
import com.example.blesdktest.adapter.GridAdapter
import com.example.blesdktest.archive.OperaterActivity
import com.example.blesdktest.databinding.ActivityFiturTestBinding
import com.example.blesdktest.ui.HeartRateActivity
import com.example.blesdktest.ui.SportDataActivity
import com.example.blesdktest.ui.SuhuActivity
import com.orhanobut.logger.Logger
import com.veepoo.protocol.VPOperateManager
import com.veepoo.protocol.listener.base.IBleWriteResponse
import com.veepoo.protocol.listener.data.ISocialMsgDataListener
import com.veepoo.protocol.listener.data.ISportModelStateListener
import com.veepoo.protocol.model.datas.*
import com.veepoo.protocol.model.enums.EBPDetectModel
import com.veepoo.protocol.model.enums.EFunctionStatus.SUPPORT
import com.veepoo.protocol.model.enums.ESex
import com.veepoo.protocol.model.enums.ESportType


class FiturTest : AppCompatActivity() {
    //ui
    private lateinit var binding: ActivityFiturTestBinding
    private lateinit var deviceaddress: String
    var mGridData = ArrayList<String>()
    var mContext: Context = this@FiturTest
    var isSleepPrecision = false
    var msg: Message? = null


    var writeResponse: WriteResponse = WriteResponse()

    class WriteResponse : IBleWriteResponse {
        override fun onResponse(code: Int) {
            Log.i(FiturTest.TAG, "write cmd status:$code")
        }
    }

    /**
     * Password verification to obtain the following information
     */
    var watchDataDay = 3
    var weatherStyle = 0
    var contactMsgLength = 0
    var allMsgLenght = 4
    private var deviceNumber = -1
    private var deviceVersion: String? = null
    private var deviceTestVersion: String? = null
    var isOadModel = false
    var isNewSportCalc = false
    var isInPttModel = false
    var socialMsgDataListener: ISocialMsgDataListener = object : ISocialMsgDataListener {
        override fun onSocialMsgSupportDataChange(socailMsgData: FunctionSocailMsgData) {
            val message = "FunctionSocailMsgData:\n$socailMsgData"
            Logger.t(TAG).i(message)
            sendMsg(message, 3)
        }

        override fun onSocialMsgSupportDataChange2(socailMsgData: FunctionSocailMsgData) {
            val message = "FunctionSocailMsgData2:\n$socailMsgData"
            Log.i(TAG, message)
            sendMsg(message, 3)
        }
    }

    private fun sendMsg(message: String, what: Int) {
        AlertDialog.Builder(this).apply {
            setTitle(what.toString())
            setMessage(message)
            setPositiveButton("OK") { dialog, _ ->
                dialog.cancel()
            }
            create()
            show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFiturTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        mContext = applicationContext
        deviceaddress = intent.getStringExtra("deviceaddress").toString()
        initGridView()


    }

    private fun initGridView() {
        var i = 0
        while (i < oprateStr.size) {
            val s: String = oprateStr[i]
            mGridData.add(s)
            i++
        }
        val layoutManager = GridLayoutManager(this, 3)
        binding.mainGridview.layoutManager = layoutManager

        val gridAdapter = GridAdapter(mGridData)
        gridAdapter.setOnItemClickCallback(object : GridAdapter.OnItemCliCkCallback {
            override fun onItemClicked(i: Int) {
                onClicked(i)
            }
        })
        binding.mainGridview.adapter = gridAdapter

    }


    private fun onClicked(i: Int) {
        when (i) {
            0 -> {
                val is24Hourmodel = false
                VPOperateManager.getMangerInstance(mContext).confirmDevicePwd(writeResponse,
                    { pwdData ->
                        val message = "PwdData:\n$pwdData"
                        Logger.t(TAG).i(message)
                        //                    sendMsg(message, 1);
                        deviceNumber = pwdData.deviceNumber
                        deviceVersion = pwdData.deviceVersion
                        deviceTestVersion = pwdData.deviceTestVersion
                        Toast.makeText(
                            this@FiturTest,
                            "Device No：$deviceNumber,version number：$deviceVersion,\ntest version number：$deviceTestVersion",
                            Toast.LENGTH_SHORT
                        ).show()
                    }, { functionSupport ->
                        val message = "FunctionDeviceSupportData:\n$functionSupport"
                        Logger.t(TAG).i(message)
                        //                    sendMsg(message, 2);
                        val newCalcSport = functionSupport.newCalcSport
                        isNewSportCalc = newCalcSport != null && newCalcSport == SUPPORT
                        watchDataDay = functionSupport.wathcDay
                        weatherStyle = functionSupport.weatherStyle
                        contactMsgLength = functionSupport.contactMsgLength
                        allMsgLenght = functionSupport.allMsgLength
                        isSleepPrecision = functionSupport.precisionSleep == SUPPORT
                    }, socialMsgDataListener,
                    { customSettingData ->
                        val message = "CustomSettingData:\n$customSettingData"
                        Logger.t(TAG).i(message)
                        //                    sendMsg(message, 4);
                    }, "0000", is24Hourmodel
                )
            }
            1 -> {
                VPOperateManager.getMangerInstance(mContext).syncPersonInfo(
                    writeResponse,
                    { EOprateStauts ->
                        val message = "Synchronize personal information:\n$EOprateStauts"
                        Logger.t(TAG).i(message)
                        sendMsg(message, 1)
                    }, PersonInfoData(ESex.MAN, 178, 60, 20, 8000)
                )
            }
            7 -> {
                startActivity(Intent(this@FiturTest, HeartRateActivity::class.java))
            }
            9 -> {
                startActivity(Intent(this@FiturTest, SuhuActivity::class.java))
            }
            11 -> {
                VPOperateManager.getMangerInstance(mContext).stopDetectTempture(
                    writeResponse
                ) { temptureDetectData ->
                    val message = "stopDetectTempture temptureDetectData:\n$temptureDetectData"
                    Toast.makeText(
                        this@FiturTest,
                        "Temperature stop :$message",
                        Toast.LENGTH_SHORT
                    ).show()
                    Logger.t(TAG).i(message)
                    sendMsg(message, 1)
                }
            }
            12 -> {
                VPOperateManager.getMangerInstance(mContext).startDetectBP(
                    writeResponse,
                    { bpData ->
                        val message = "BpData date statues:\n$bpData"
                        Logger.t(TAG).i(message)
                        sendMsg(message, 1)
                        Toast.makeText(
                            this@FiturTest,
                            "BpData date statues:\n $bpData",
                            Toast.LENGTH_SHORT
                        ).show()
                    }, EBPDetectModel.DETECT_MODEL_PUBLIC
                )
            }
            18 -> {
                startActivity(Intent(this@FiturTest, SportDataActivity::class.java))
            }
            89 -> {
                VPOperateManager.getMangerInstance(mContext)
                    .startMultSportModel(writeResponse, object : ISportModelStateListener {
                        override fun onSportModelStateChange(sportModelStateData: SportModelStateData) {
                            val message = "indoor walk$sportModelStateData"
                            Toast.makeText(this@FiturTest, "$message", Toast.LENGTH_SHORT).show()
                            Logger.t(TAG).i(message)
                        }

                        override fun onSportStopped() {
                            Logger.t(TAG)
                                .i(SPORT_MODE_START_INDOOR + "================================sports end @_@")
                        }
                    }, ESportType.INDOOR_WALK)
            }
        }
    }


    //mulai 


    companion object {
        private val TAG = OperaterActivity::class.java.simpleName

    }
}