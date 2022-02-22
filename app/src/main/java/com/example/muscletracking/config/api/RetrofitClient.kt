package com.example.muscletracking.config.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private var apiService: ApiService
    // TODO:今後設定ファイルに移管
    private const val BASE_URL: String = "http://10.0.2.2:8080/api/"

    init {
        // moshiの初期設定
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        // retrofitの初期設定
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(getClient())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    fun getApiService():ApiService {
        return apiService
    }

    // TODO: okhttpclient定義の見直し
    private fun getClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30,TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(Interceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("Accept", "application/json")
                    .method(original.method, original.body)
                    .build()
                return@Interceptor chain.proceed(request)
            })
            .build()
    }
}