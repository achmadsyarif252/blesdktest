package com.example.blesdktest.archive//package com.example.blesdktest.archive
//
//import com.example.blesdktest.FiturTest
//
//package com.example.blesdktest
//
//import android.Manifest
//import android.annotation.SuppressLint
//import android.bluetooth.BluetoothAdapter
//import android.bluetooth.BluetoothManager
//import android.bluetooth.le.ScanCallback
//import android.bluetooth.le.ScanResult
//import android.content.Context
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.os.Bundle
//import android.util.Log
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import androidx.recyclerview.widget.DividerItemDecoration
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
//import com.example.blesdktest.adapter.BleScanViewAdapter
//import com.example.blesdktest.databinding.ActivityMain2Binding
//import com.inuker.bluetooth.library.Code
//import com.inuker.bluetooth.library.Constants
//import com.inuker.bluetooth.library.search.SearchResult
//import com.veepoo.protocol.VPOperateManager
//import com.veepoo.protocol.VPOperateManager.mContext
//import com.veepoo.protocol.listener.base.IABleConnectStatusListener
//
//
//class MainActivity2 : AppCompatActivity() {
//    private val CODE_REQUEST_ACCESS_COARSE_LOCATION = 101
//    lateinit var bluetoothAdapter: BluetoothAdapter
//    lateinit var bluetoothManager: BluetoothManager
//    lateinit var binding: ActivityMain2Binding
//    val data = ArrayList<ScanResult>()
//
//    val deviceList = ArrayList<SearchResult>()
//    lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
//    private var mIsOadModel = false
//    private var isStartConnecting = false
//
//
//    var mVpoperateManager: VPOperateManager? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMain2Binding.inflate(layoutInflater)
//        setContentView(binding.root)
//        supportActionBar?.title = "Demo BLE_SDK"
//        val context: Context = this
//        mVpoperateManager = VPOperateManager.getMangerInstance(context.applicationContext);
//
//        bluetoothManager = context.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
//        bluetoothAdapter = bluetoothManager.adapter
//
//        scanDevice()
//        checkPermission()
//        binding.refreshBtn.setOnClickListener {
//            scanDevice()
//        }
//    }
//
//    private fun initRv() {
//
//        binding.rvBluetooth.layoutManager = LinearLayoutManager(this)
//        val adapter = BleScanViewAdapter(deviceList)
//        adapter.setOnItemClickCallback(object : BleScanViewAdapter.OnItemClickCallback {
//            override fun onItemClicked(searchResult: SearchResult) {
//                Log.d(TAG, "onItemClicked: ${searchResult.address} Diklik")
//                Toast.makeText(this@MainActivity2, "Connecting,please wait..", Toast.LENGTH_SHORT)
//                    .show()
//                connectDevice(searchResult.address, searchResult.name);
//            }
//        })
//
//        binding.rvBluetooth.adapter = adapter
//    }
//
//    private val mBleConnectStatusListener: IABleConnectStatusListener =
//        object : IABleConnectStatusListener() {
//            override fun onConnectStatusChanged(mac: String, status: Int) {
//                if (status == Constants.STATUS_CONNECTED) {
//                    Log.i(TAG, "STATUS_CONNECTED")
//                } else if (status == Constants.STATUS_DISCONNECTED) {
//                    Log.i(TAG, "STATUS_DISCONNECTED")
//                }
//            }
//        }
//
//    private fun connectDevice(mac: String, deviceName: String) {
//        mVpoperateManager!!.registerConnectStatusListener(mac, mBleConnectStatusListener)
//        mVpoperateManager!!.connectDevice(mac, deviceName,
//            { code, _, isoadModel ->
//                if (code == Code.REQUEST_SUCCESS) {
//                    //Bluetooth connection status with the device
//                    Log.i(TAG, "connection succeeded")
//                    Toast.makeText(this@MainActivity2, "connection succeeded", Toast.LENGTH_SHORT)
//                        .show()
//                    Log.i(TAG, "Whether it is firmware upgrade mode=$isoadModel")
//                    mIsOadModel = isoadModel
//                    isStartConnecting = true
//                } else {
//                    Toast.makeText(this@MainActivity2, "Connection failed", Toast.LENGTH_SHORT)
//                        .show()
//                    Log.i(TAG, "")
//                    isStartConnecting = false
//                }
//            }) { state ->
//            if (state == Code.REQUEST_SUCCESS) {
//                //Bluetooth connection status with the device
//                Log.i(TAG, "The monitoring is successful - other operations can be performed")
//                isStartConnecting = true
//
//                val intent = Intent(mContext, FiturTest::class.java)
//                intent.putExtra("isoadmodel", mIsOadModel)
//                intent.putExtra("deviceaddress", mac)
//                startActivity(intent)
//                //                    }
//            } else {
//                Log.i(TAG, "Listening failed, reconnect")
//                Toast.makeText(
//                    this@MainActivity2,
//                    "Listening failed, reconnect",
//                    Toast.LENGTH_SHORT
//                )
//                    .show()
//                isStartConnecting = false
//            }
//        }
//    }
//
//    //    @RequiresApi(Build.VERSION_CODES.S)
//    private fun checkPermission() {
//        //Check whether you have fuzzy positioning permission
//        val permissionList = arrayOf(
//            Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.BLUETOOTH,
//            Manifest.permission.BLUETOOTH_ADMIN,
//            Manifest.permission.BLUETOOTH_SCAN,
//            Manifest.permission.BLUETOOTH_CONNECT,
//        )
//
//        val permissionRequest = arrayListOf<String>()
//
//        permissionList.forEach {
//            if (ContextCompat.checkSelfPermission(
//                    this@MainActivity2,
//                    it,
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                permissionRequest.add(it)
//            }
//        }
//
//        if (permissionRequest.isNotEmpty()) {
//            ActivityCompat.requestPermissions(
//                this@MainActivity2,
//                permissionRequest.toTypedArray(),
//                CODE_REQUEST_ACCESS_COARSE_LOCATION
//            )
//        }
//    }
//
//    fun refreshStop() {
//        if (mSwipeRefreshLayout.isRefreshing) {
//            mSwipeRefreshLayout.isRefreshing = false
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    fun scanDevice() {
//        val scanCallBack = object : ScanCallback() {
//            override fun onScanResult(callbackType: Int, result: ScanResult) {
//                super.onScanResult(callbackType, result)
//                val result2 = SearchResult(result.device, result.rssi, result.scanRecord?.bytes)
//                if (result2 !in deviceList) {
//                    deviceList.add(
//                        result2
//                    );
//
//                    Log.d(TAG, "Hasil device:${result.device.name} - ${result.device.address}")
////                    Toast.makeText(
////                        this@MainActivity2,
////                        result.device.name,
////                        Toast.LENGTH_SHORT
////                    )
////                    Toast.makeText(this@MainActivity2, "${deviceList.size}", Toast.LENGTH_SHORT)
////                        .show()
//
//
////                        .show()
////                    Log.e("", "${result.device.address}, ${result.device.name}")
////                    if (result.device.name != null) {
////                        bluetoothAdapter.bluetoothLeScanner.stopScan(this)
////                        if (mSwipeRefreshLayout.isRefreshing) mSwipeRefreshLayout.isRefreshing =
////                            false
//////                        val device = BleDevice(result.device, result.rssi)
//////                        BleManager.getInstance().connect(device)
//                }
//            }
//        }
////        }
//        initRv()
//        bluetoothAdapter.bluetoothLeScanner.startScan(scanCallBack)
//    }
//
//    companion object {
//        private val TAG = MainActivity2::class.java.simpleName
//    }
//}