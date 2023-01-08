package com.example.blesdktest.archive

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.blesdktest.adapter.BleScanViewAdapter
import com.example.blesdktest.adapter.CustomLogAdapter
import com.example.blesdktest.databinding.ActivityMainBinding
import com.inuker.bluetooth.library.search.SearchResult
import com.inuker.bluetooth.library.search.response.SearchResponse
import com.inuker.bluetooth.library.utils.BluetoothUtils
import com.orhanobut.logger.LogLevel
import com.orhanobut.logger.Logger
import com.veepoo.protocol.VPOperateManager
import com.veepoo.protocol.listener.base.IABleConnectStatusListener
import com.veepoo.protocol.listener.base.IABluetoothStateListener
import com.veepoo.protocol.listener.base.IConnectResponse
import com.veepoo.protocol.listener.base.INotifyResponse
import java.util.*

class MainActivity : AppCompatActivity() {
//    , SwipeRefreshLayout.OnRefreshListener
//    private lateinit var binding: ActivityMainBinding
//    val MY_PERMISSIONS_REQUEST_BLUETOOTH = 85
//
//    val TAG = MainActivity::class.java.simpleName
//    private val YOUR_APPLICATION = "BLE_SDK"
//    private val REQUEST_CODE = 1
//
//    var mListAddress: ArrayList<String> = ArrayList<String>()
//    var mListData: ArrayList<SearchResult> = ArrayList<SearchResult>()
////    val adapter = BleScanViewAdapter(mListData)
//
//
//    private lateinit var bluetoothAdapter: BluetoothAdapter
//    private lateinit var bluetoothManager: BluetoothManager
//
//    private var mBScanner: BluetoothLeScanner? = null
//
//
//    private val mBleConnectStatusListener: IABleConnectStatusListener =
//        object : IABleConnectStatusListener() {
//            override fun onConnectStatusChanged(mac: String, status: Int) {
//                if (status == 16) {
//                    Logger.t(TAG).i("STATUS_CONNECTED", *arrayOfNulls<Any>(0))
//                } else if (status == 32) {
//                    Logger.t(TAG).i("STATUS_DISCONNECTED", *arrayOfNulls<Any>(0))
//                }
//            }
//        }
//
//    private val mBluetoothStateListener: IABluetoothStateListener =
//        object : IABluetoothStateListener() {
//            override fun onBluetoothStateChanged(openOrClosed: Boolean) {
//                Logger.t(TAG).i("open=$openOrClosed", *arrayOfNulls<Any>(0))
//            }
//        }
//
//    var mContext = this as Context
//
//    /* access modifiers changed from: private */
//    var mIsOadModel = false
//
//
//    lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
//
//    var mTitleTextView: TextView? = null
////    lateinit var mVpoperateManager: VPOperateManager
    //        mVpoperateManager = VPOperateManager.getMangerInstance(context)


    lateinit var binding: ActivityMainBinding
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        mSwipeRefreshLayout = binding.mainSwipeRefreshLayout
//
//        val context: Context = this
//
//        bluetoothManager = context.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
//        bluetoothAdapter = bluetoothManager.adapter
//        mVpoperateManager = VPOperateManager.getMangerInstance(context)

        supportActionBar?.hide()
//        initLog()
//        initRv()
//        //cek permisson bluetooth
//        checkPermission()
//        registerBluetoothStateListener()

    }
}
//
//    private val mSearchResponse: SearchResponse = object : SearchResponse {
//
//        override fun onSearchStarted() {
//            Logger.t(TAG).i("onSearchStarted", *arrayOfNulls<Any>(0))
//        }
//
//        override fun onDeviceFounded(device: SearchResult) {
//            Toast.makeText(this@MainActivity, "onDeviceFounded", Toast.LENGTH_SHORT).show()
//
//            Logger.t(TAG).i(
//                String.format(
//                    "device for %s-%s-%d",
//                    *arrayOf<Any>(device.name, device.address, Integer.valueOf(device.rssi))
//                ), *arrayOfNulls<Any>(0)
//            )
//            runOnUiThread {
//                if (!mListAddress.contains(device.address)) {
//                    mListData.add(device)
//                    mListAddress.add(device.address)
//                }
//                Collections.sort(
//                    mListData,
//                    DeviceCompare()
//                )
////                adapter.notifyDataSetChanged()
//            }
//
//        }
//
//        override fun onSearchStopped() {
//            Toast.makeText(
//                this@MainActivity,
//                "onSearchStopped,mListData.size = ${mListData.size}",
//                Toast.LENGTH_SHORT
//            ).show()
//            refreshStop()
//            Logger.t(TAG).i("onSearchStopped ${mListData.size}", *arrayOfNulls<Any>(0))
//        }
//
//        override fun onSearchCanceled() {
//            refreshStop()
//            Toast.makeText(this@MainActivity, "onSearchCanceled", Toast.LENGTH_SHORT).show()
//            Logger.t(TAG).i("onSearchCanceled", *arrayOfNulls<Any>(0))
//        }
//    }
//
//    fun refreshStop() {
//        Logger.t(TAG).i("refreshComlete", *arrayOfNulls<Any>(0))
//        if (mSwipeRefreshLayout.isRefreshing) {
//            mSwipeRefreshLayout.isRefreshing = false
//        }
//    }
//
//    private fun initLog() {
//        Logger.init(YOUR_APPLICATION).methodCount(0).methodOffset(0).hideThreadInfo()
//            .logLevel(
//                LogLevel.FULL
//            ).logAdapter(CustomLogAdapter())
//    }
//
//    override fun onRefresh() {
//        if (checkBLE()) {
//            scanDevice()
//        }
//    }
//
//
//    private fun checkPermission() {
//        Log.i(TAG, "Build.VERSION.SDK_INT =" + Build.VERSION.SDK_INT)
//        if (Build.VERSION.SDK_INT <= 22) {
//            initBLE()
//            return
//        }
//        val permissionCheck: Int =
//            ContextCompat.checkSelfPermission(mContext, "android.permission.ACCESS_COARSE_LOCATION")
//        if (permissionCheck == 0) {
//            Log.i(TAG, "checkPermission,PERMISSION_GRANTED")
//            initBLE()
//        } else if (permissionCheck == -1) {
//            requestPermission()
//            Log.i(TAG, "checkPermission,PERMISSION_DENIED")
//        }
//    }
//
//    private fun initBLE() {
//        bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
//        Toast.makeText(this@MainActivity, "INIT BLE RUN", Toast.LENGTH_SHORT).show()
//        if (bluetoothManager != null) {
//            Toast.makeText(this@MainActivity, "INIT BLE RUN", Toast.LENGTH_SHORT).show()
//
//        }
//        mBScanner = bluetoothAdapter?.bluetoothLeScanner
//        Toast.makeText(this@MainActivity, mBScanner.toString(), Toast.LENGTH_SHORT).show()
//        checkBLE()
//    }
//
//    fun onClick(view: View) {
//        when (view.id) {
//            R.id.scan -> {
//                scanDevice()
//                return
//            }
//            else -> return
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode != 1) {
//            return
//        }
//        if (BluetoothUtils.isBluetoothEnabled()) {
//            scanDevice()
//        } else {
//            refreshStop()
//        }
//
//    }
//
//    @SuppressLint("MissingPermission")
//    private fun scanDevice(): Boolean {
//        Toast.makeText(this@MainActivity, "Start Scann..", Toast.LENGTH_SHORT).show()
//        if (mListAddress.isNotEmpty()) {
//            Toast.makeText(this@MainActivity, "Clear List Address", Toast.LENGTH_SHORT).show()
//            mListAddress.clear()
//        }
//        if (mListData.isNotEmpty()) {
//            Toast.makeText(this@MainActivity, "Clear List Data", Toast.LENGTH_SHORT).show()
//            mListData.clear()
//        }
//
//        if (!BluetoothUtils.isBluetoothEnabled()) {
//            Toast.makeText(mContext, "bluetooth is not turned on", Toast.LENGTH_SHORT).show()
//            return true
//        }
//
//
//        val scanCallBack = object : ScanCallback() {
//            override fun onScanResult(callbackType: Int, result: ScanResult?) {
//                super.onScanResult(callbackType, result)
//                Log.d(TAG, "onScanResult: ${result?.device?.address}")
//                Toast.makeText(this@MainActivity, "${result?.device?.address}", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        }
//        bluetoothAdapter.bluetoothLeScanner?.startScan(scanCallBack)
//        return false
//
//    }
//
//    private fun checkBLE(): Boolean {
//        return if (!BluetoothUtils.isBluetoothEnabled()) {
//            val i = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//            startActivityForResult(i ?: Intent(), REQUEST_CODE)
//            false
//        } else {
//            true
//        }
//    }
//
//    private fun requestPermission() {
//        if (ContextCompat.checkSelfPermission(
//                mContext,
//                "android.permission.ACCESS_COARSE_LOCATION"
//            ) === 0
//        ) {
//            Log.i(
//                TAG,
//                "requestPermission,shouldShowRequestPermissionRationale hehe"
//            )
//        } else if (ActivityCompat.shouldShowRequestPermissionRationale(
//                this,
//                "android.permission.ACCESS_COARSE_LOCATION"
//            )
//        ) {
//            Log.i(
//                TAG,
//                "requestPermission,shouldShowRequestPermissionRationale"
//            )
//        } else {
//            Log.i(
//                TAG,
//                "requestPermission,shouldShowRequestPermissionRationale else",
//
//                )
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf("android.permission.ACCESS_COARSE_LOCATION"),
//                85
//            )
//        }
//    }
//
//    private fun initRv() {
//        val layoutManager = LinearLayoutManager(this)
//        binding.mainRecylerlist.layoutManager = layoutManager
////        binding.mainRecylerlist.adapter = adapter
//        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
//        binding.mainRecylerlist.addItemDecoration(itemDecoration)
//
//        binding.mainSwipeRefreshLayout.setOnRefreshListener(this)
////        adapter.setOnItemClickCallback(object : BleScanViewAdapter.OnRecycleViewClickCallback {
////            override fun onItemClicked(sRslt: SearchResult) {
////                runOnUiThread {
////                    Toast.makeText(
////                        mContext,
////                        "Connecting, please wait...",
////                        Toast.LENGTH_SHORT
////                    ).show()
////                }
////                val searchResult: SearchResult = sRslt
////                connectDevice(searchResult.address, searchResult.name)
////            }
////        })
//
//    }
//
//    //saat recycleview diklik salah satu itemnya
//    private fun connectDevice(mac: String, deviceName: String) {
//        mVpoperateManager!!.registerConnectStatusListener(mac, mBleConnectStatusListener)
//        mVpoperateManager!!.connectDevice(mac, deviceName,
//            IConnectResponse { code, profile, isoadModel ->
//                if (code == 0) {
//                    Log.i(TAG, "connection succeeded")
//                    Log.i(
//                        TAG,
//                        "Whether it is firmware upgrade mode=$isoadModel"
//                    )
//                    mIsOadModel = isoadModel
//                    val unused = mIsOadModel
//                    return@IConnectResponse
//                }
//                Log.i(TAG, "Connection failed")
//            }, INotifyResponse { state ->
//                if (state == 0) {
//                    Log.i(TAG, "The monitoring is successful - other operations can be performed")
//                    val intent = Intent(mContext, OperaterActivity::class.java)
//                    intent.putExtra("isoadmodel", mIsOadModel)
//                    intent.putExtra("deviceaddress", mac)
//                    this@MainActivity.startActivity(intent)
//                    return@INotifyResponse
//                }
//                Log.i(TAG, "Listening failed, reconnect")
//            })
//    }
//
//    private fun registerBluetoothStateListener() {
//        mVpoperateManager?.registerBluetoothStateListener(mBluetoothStateListener)
//    }
//
//}