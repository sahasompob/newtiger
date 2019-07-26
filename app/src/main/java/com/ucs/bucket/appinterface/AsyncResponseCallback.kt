package com.ucs.bucket.appinterface

interface AsyncResponseCallback {
    fun onResponse(isSuccess: Boolean, call: String)

}