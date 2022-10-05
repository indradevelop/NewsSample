package com.example.news.api

import com.google.gson.annotations.SerializedName

data class ErrorResult (
    @SerializedName("status")
    var status: String? = null,
    @SerializedName("code")
    var code: String? = null,
    @SerializedName("message")
    var message: String? = null,
)