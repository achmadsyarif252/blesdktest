package com.example.blesdktest.archive

import com.inuker.bluetooth.library.search.SearchResult

class DeviceCompare : Comparator<SearchResult?> {
    override fun compare(p0: SearchResult?, p1: SearchResult?): Int {
        return p1?.rssi?.minus(p0?.rssi!!)!!
    }
}
