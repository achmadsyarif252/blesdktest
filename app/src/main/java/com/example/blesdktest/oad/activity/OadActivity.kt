package com.example.blesdktest.oad.activity


import android.app.Activity
import android.app.AlertDialog
import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.orhanobut.logger.Logger
import com.example.blesdktest.R
import com.example.blesdktest.oad.service.DfuService
import com.veepoo.protocol.VPOperateManager
import com.veepoo.protocol.listener.oad.OnFindOadDeviceListener
import com.veepoo.protocol.listener.oad.OnUpdateCheckListener
import com.veepoo.protocol.model.enums.ECpuType
import com.veepoo.protocol.model.settings.OadSetting
import vpno.nordicsemi.android.dfu.DfuBaseService
import vpno.nordicsemi.android.dfu.DfuLogListener
import vpno.nordicsemi.android.dfu.DfuProgressListener
import vpno.nordicsemi.android.dfu.DfuProgressListenerAdapter
import vpno.nordicsemi.android.dfu.DfuServiceInitiator
import vpno.nordicsemi.android.dfu.DfuServiceListenerHelper

class OadActivity : Activity(), View.OnClickListener {
    var deviceStateTv: TextView? = null
    private var failCount = 0

    /* access modifiers changed from: private */
    var isCanEnterOadModel = false

    /* access modifiers changed from: private */
    var isFindOadDevice = false
    private var mContext: Context? = null
    private val mDfuLogListener =
        DfuLogListener { deviceAddress, level, message ->
            Logger.t(TAG).e("deviceAddress=$deviceAddress,message=$message", *arrayOfNulls(0))
        }
    private val mDfuProgressListener: DfuProgressListener = object : DfuProgressListenerAdapter() {
        override fun onDeviceConnecting(deviceAddress: String) {
            progressBar!!.isIndeterminate = true
            textPercentTv!!.text =
                this@OadActivity.resources.getString(R.string.dfu_status_connecting)
            Logger.t(TAG).e("onDeviceConnecting", *arrayOfNulls(0))
        }

        override fun onDfuProcessStarting(deviceAddress: String) {
            progressBar!!.isIndeterminate = true
            textPercentTv!!.text =
                this@OadActivity.resources.getString(R.string.dfu_status_starting)
            Logger.t(TAG).e("onDfuProcessStarting", *arrayOfNulls(0))
        }

        override fun onDfuProcessStarted(deviceAddress: String) {
            Logger.t(TAG).e("onDfuProcessStarted", *arrayOfNulls(0))
            super.onDfuProcessStarted(deviceAddress)
        }

        override fun onProgressChanged(
            deviceAddress: String,
            percent: Int,
            speed: Float,
            avgSpeed: Float,
            currentPart: Int,
            partsTotal: Int
        ) {
            Logger.t(TAG).e("onProgressChanged-$percent", *arrayOfNulls(0))
            super.onProgressChanged(
                deviceAddress,
                percent,
                speed,
                avgSpeed,
                currentPart,
                partsTotal
            )
            progressBar!!.isIndeterminate = false
            progressBar!!.progress = percent
            textPercentTv!!.text = "$percent%"
            progressBar!!.isIndeterminate = false
        }

        override fun onDeviceConnected(deviceAddress: String) {
            super.onDeviceConnected(deviceAddress)
        }

        override fun onEnablingDfuMode(deviceAddress: String) {
            super.onEnablingDfuMode(deviceAddress)
        }

        override fun onFirmwareValidating(deviceAddress: String) {
            progressBar!!.isIndeterminate = true
            textPercentTv!!.text =
                this@OadActivity.resources.getString(R.string.dfu_status_validating)
            super.onFirmwareValidating(deviceAddress)
            Logger.t(TAG).e("onFirmwareValidating", *arrayOfNulls(0))
        }

        override fun onDeviceDisconnecting(deviceAddress: String) {
            progressBar!!.isIndeterminate = true
            textPercentTv!!.text =
                this@OadActivity.resources.getString(R.string.dfu_status_disconnecting)
            super.onDeviceDisconnecting(deviceAddress)
            Logger.t(TAG).e("onDeviceDisconnecting", *arrayOfNulls(0))
        }

        override fun onDeviceDisconnected(deviceAddress: String) {
            super.onDeviceDisconnected(deviceAddress)
            Logger.t(TAG).e("onDeviceDisconnected", *arrayOfNulls(0))
        }

        override fun onDfuCompleted(deviceAddress: String) {
            super.onDfuCompleted(deviceAddress)
            Logger.t(TAG).e("onDfuCompleted", *arrayOfNulls(0))
            textPercentTv!!.text =
                this@OadActivity.resources.getString(R.string.dfu_status_completed)
            Handler().postDelayed({
                oadSuccess()
                (this@OadActivity.getSystemService(NOTIFICATION_SERVICE) as NotificationManager).cancel(
                    DfuBaseService.NOTIFICATION_ID
                )
            }, 200)
        }

        override fun onDfuAborted(deviceAddress: String) {
            Logger.t(TAG).e("onDfuAborted", *arrayOfNulls(0))
            super.onDfuAborted(deviceAddress)
        }

        override fun onError(deviceAddress: String, error: Int, errorType: Int, message: String) {
            Logger.t(TAG)
                .e("onError=$error,errorType=$errorType,message=$message", *arrayOfNulls(0))
            super.onError(deviceAddress, error, errorType, message)
            oadFail()
        }
    }

    /* access modifiers changed from: private */
    var mOadAddress: String? = null

    /* access modifiers changed from: private */
    var mOadFileName: String? = null
    var oadButton: Button? = null
    var oadSetting: OadSetting? = null
    var progressBar: ProgressBar? = null
    var textPercentTv: TextView? = null
    var upLoadingTv: TextView? = null

    /* access modifiers changed from: protected */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oad_custom)
        mContext = applicationContext
        oadSetting = intData
        initView()
        registerListener()
    }

    private fun registerListener() {
        DfuServiceListenerHelper.registerProgressListener(this, mDfuProgressListener)
        DfuServiceListenerHelper.registerLogListener(this, mDfuLogListener)
    }

    private val intData: OadSetting
        private get() {
            val isOadModel = intent.getBooleanExtra("isoadmodel", false)
            return OadSetting(
                intent.getStringExtra("deviceaddress"),
                intent.getStringExtra("deviceversion"),
                intent.getStringExtra("devicetestversion"),
                intent.getIntExtra("devicenumber", 0),
                isOadModel
            )
        }

    private fun initView() {
        deviceStateTv = findViewById<View>(R.id.oad_devicestate) as TextView
        upLoadingTv = findViewById<View>(R.id.oad_uploading) as TextView
        textPercentTv = findViewById<View>(R.id.oad_upload_progress_gv) as TextView
        oadButton = findViewById<View>(R.id.oad_update) as Button
        progressBar = findViewById<View>(R.id.oad_upload_progress) as ProgressBar
        oadButton!!.setOnClickListener(this)
        initDeviceStateView()
    }

    private fun initDeviceStateView() {
        val sb = StringBuffer()
        sb.append("设备地址：" + oadSetting!!.deviceAddress)
        sb.append("\n")
        sb.append("设备编号：" + oadSetting!!.deviceNumber)
        sb.append("\n")
        sb.append("正式版本号：" + oadSetting!!.deviceVersion)
        sb.append("\n")
        sb.append("测试版本号：" + oadSetting!!.deviceTestVersion)
        sb.append("\n")
        sb.append("是否DFU模式：" + oadSetting!!.isOadModel)
        deviceStateTv!!.text = sb.toString()
    }

    override fun onClick(v: View) {
        if (!isCanEnterOadModel) {
            checkVersionAndFile()
        } else if (isFindOadDevice) {
            startOad()
        } else {
            findOadModelDevice()
        }
    }

    private fun checkVersionAndFile() {
        oadSetting!!.deviceVersion = "00.23.00"
        oadSetting!!.deviceTestVersion = "00.23.00.00"
        Logger.t(TAG).i("升级前：版本验证->文件验证->查找目标设备", *arrayOfNulls(0))
        VPOperateManager.getMangerInstance(mContext)
            .checkVersionAndFile(oadSetting, object : OnUpdateCheckListener {
                override fun findOadDevice(p0: String?, p1: ECpuType?) {
                    TODO("Not yet implemented")
                }

                override fun unKnowCpu() {
                    TODO("Not yet implemented")
                }

                override fun onNetVersionInfo(
                    deviceNumber: Int,
                    deviceVersion: String,
                    des: String
                ) {
                    Logger.t(TAG).i(
                        "服务器版本信息,设备号=$deviceNumber,最新版本=$deviceVersion,升级描述=$des",
                        *arrayOfNulls(0)
                    )
                }

                override fun onDownLoadOadFile(progress: Float) {
                    Logger.t(TAG).i("从服务器下载文件,进度=$progress", *arrayOfNulls(0))
                }

                override fun onCheckFail(endState: Int) {
                    when (endState) {
                        11 -> {
                            Logger.t(TAG).i("网络出错", *arrayOfNulls(0))
                            return
                        }
                        12 -> {
                            Logger.t(TAG).i("服务器连接不上", *arrayOfNulls(0))
                            return
                        }
                        13 -> {
                            Logger.t(TAG).i("服务器无此版本", *arrayOfNulls(0))
                            return
                        }
                        14 -> {
                            Logger.t(TAG).i("设备是最新版本", *arrayOfNulls(0))
                            return
                        }
                        15 -> {
                            Logger.t(TAG).i("文件不存在", *arrayOfNulls(0))
                            return
                        }
                        16 -> {
                            Logger.t(TAG).i("文件md5不一致", *arrayOfNulls(0))
                            return
                        }
                        else -> return
                    }
                }

                override fun onCheckSuccess(oadFileName: String) {
                    Logger.t(TAG).i("版本确认无误，文件确认无误", *arrayOfNulls(0))
                    mOadFileName = oadFileName
                    val unused = mOadFileName
                    if (!TextUtils.isEmpty(mOadFileName)) {
                        isCanEnterOadModel = true
                        val unused2 = isCanEnterOadModel
                    }
                }

                fun findOadDevice(oadAddress: String?) {
                    Logger.t(TAG).i("找到OAD模式下的设备了", *arrayOfNulls(0))
                    mOadAddress = oadAddress
                    val unused = mOadAddress
                    if (!TextUtils.isEmpty(mOadAddress)) {
                        isFindOadDevice = true
                        val unused2 = isFindOadDevice
                        runOnUiThread { startOad() }
                    }
                }
            })
    }

    private fun findOadModelDevice() {
        VPOperateManager.getMangerInstance(mContext)
            .findOadModelDevice(oadSetting, object : OnFindOadDeviceListener {
                fun findOadDevice(oadAddress: String?) {
                    startOad()
                }

                override fun findOadDevice(p0: String?, p1: ECpuType?) {
                    TODO("Not yet implemented")
                }

                override fun unKnowCpu() {
                    TODO("Not yet implemented")
                }
            })
    }

    /* access modifiers changed from: private */
    fun startOad() {
        Logger.t(TAG).i("执行升级程序，最多尝试5次", *arrayOfNulls(0))
        showProgressBar()
        val isBinder = false
        val dfuServiceInitiator = DfuServiceInitiator(mOadAddress).setDeviceName("veepoo")
            .setKeepBond(isBinder)
        dfuServiceInitiator.setZip(null as Uri?, mOadFileName)
        dfuServiceInitiator.start(this, DfuService::class.java)
    }

    /* access modifiers changed from: protected */
    public override fun onDestroy() {
        super.onDestroy()
        DfuServiceListenerHelper.unregisterProgressListener(this, mDfuProgressListener)
        DfuServiceListenerHelper.unregisterLogListener(this, mDfuLogListener)
        finish()
    }

    private fun showProgressBar() {
        oadButton!!.isEnabled = false
        progressBar!!.isIndeterminate = true
        progressBar!!.visibility = View.VISIBLE
        textPercentTv!!.visibility = View.VISIBLE
        upLoadingTv!!.visibility = View.GONE
    }

    private fun disMissProgressBar() {
        progressBar!!.visibility = View.INVISIBLE
        textPercentTv!!.visibility = View.INVISIBLE
        upLoadingTv!!.visibility = View.GONE
        oadButton!!.isEnabled = true
    }

    /* access modifiers changed from: private */
    fun oadSuccess() {
        isFindOadDevice = false
        disMissProgressBar()
        Toast.makeText(mContext, "升级成功", Toast.LENGTH_SHORT).show()
        Logger.t(TAG).e("升级成功", *arrayOfNulls(0))
        finish()
    }

    /* access modifiers changed from: private */
    fun oadFail() {
        failCount++
        disMissProgressBar()
        Logger.t(TAG).e("showErrorMessage", *arrayOfNulls(0))
        try {
            Thread.currentThread()
            Thread.sleep(300)
        } catch (e: Exception) {
        }
        if (failCount < 5) {
            Logger.t(TAG).e("再试一次=" + failCount, *arrayOfNulls(0))
            showProgressBar()
            startOad()
            return
        }
        showOadFailDialog()
    }

    private fun showOadFailDialog() {
        isFindOadDevice = false
        val oadFailDialog = AlertDialog.Builder(mContext).setTitle("提示")
            .setCancelable(false).setMessage("升级失败，设备名字会变成DfuLang").setPositiveButton(
                "知道了"
            ) { dialog, which -> finish() }.create()
        oadFailDialog.setCanceledOnTouchOutside(false)
        oadFailDialog.show()
        //ada ini setelah set title .setIconAttribute(16843605)
    }

    companion object {
        private const val MAX_ALLOW_FAIL_COUNT = 5

        /* access modifiers changed from: private */
        val TAG = OadActivity::class.java.simpleName
    }
}