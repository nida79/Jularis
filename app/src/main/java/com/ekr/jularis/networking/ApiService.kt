package com.ekr.jularis.networking

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {

    private var BASE_URL: String = "http://103.55.36.171:8001/v1/"
    val endpoint: ApiEndpoint
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val httpClient =
                OkHttpClient.Builder().addInterceptor(interceptor).addInterceptor { chain ->
                    val request: Request =
                        chain.request().newBuilder().addHeader("Accept", "application/json").build()
                    chain.proceed(request)
                }.build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiEndpoint::class.java)

        }
}