package com.example.news.api

import android.util.Base64
import com.google.gson.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.UnsupportedEncodingException
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit
import com.example.news.BuildConfig


//class RetrofitFactory(private val sessionManager: SessionManager){
class RetrofitFactory{

    private val authInterceptor = Interceptor {
        val original = it.request()
        val newRequest = original.newBuilder()
            .method(original.method, original.body)

        newRequest.addHeader("Authorization", AppConstants.apiKey)

        /*val token = sessionManager.userToken
        if(!token.isNullOrEmpty()){
            newRequest.addHeader("Authorization", "Bearer $token")
        }*/

        it.proceed(newRequest.build())
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    //Not logging the authkey if not debug
    private fun client(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .readTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)

        builder.addInterceptor(authInterceptor)

        if (BuildConfig.DEBUG) {
            builder.addNetworkInterceptor(loggingInterceptor)
        }

        return builder.build()
    }

    class DoubleGsonTypeAdapter : JsonDeserializer<Double?> {
        @Throws(JsonParseException::class)
        override fun deserialize(
            jsonElement: JsonElement,
            type: Type?,
            jsonDeserializationContext: JsonDeserializationContext?
        ): Double? {
            var result: Double? = null
            result = try {
                jsonElement.asDouble
            } catch (e: NumberFormatException) {
                return result
            }
            return result
        }
    }

    class IntGsonTypeAdapter : JsonDeserializer<Int?> {
        @Throws(JsonParseException::class)
        override fun deserialize(
            jsonElement: JsonElement,
            type: Type?,
            jsonDeserializationContext: JsonDeserializationContext?
        ): Int? {
            var result: Int? = null
            result = try {
                jsonElement.asInt
            } catch (e: NumberFormatException) {
                return result
            }
            return result
        }
    }

    private fun gsonConfig(): Gson {

        return GsonBuilder()
            .registerTypeAdapter(java.lang.Double::class.java, DoubleGsonTypeAdapter())
            .registerTypeAdapter(Double::class.java, DoubleGsonTypeAdapter())
            .registerTypeAdapter(java.lang.Integer::class.java, IntGsonTypeAdapter())
            .registerTypeAdapter(Int::class.java, IntGsonTypeAdapter())
            .create()
    }

    fun retrofit(url: String): Retrofit {
        return Retrofit.Builder()
            .client(client())
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(gsonConfig()))
            .build()
    }

    private fun encodeBase64(key: String): String? {
        try {
            val keyData = key.toByteArray(charset("UTF-8"))
            return Base64.encodeToString(keyData, Base64.NO_WRAP)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return null
    }

}