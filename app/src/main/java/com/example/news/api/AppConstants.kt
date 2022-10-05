package com.example.news.api

import com.example.news.BuildConfig


object AppConstants {
    //gateway
    const val urlGateway: String = BuildConfig.HOST_GATEWAY
    const val apiKey: String = BuildConfig.API_KEY

    const val errorOccurred: String = "An error occurred unknown"
    const val errorNoInternet: String = "Please Check Your Internet Connection"
}