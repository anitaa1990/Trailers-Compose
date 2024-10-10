package com.an.trailers_compose.data.remote.interceptor

import com.an.trailers_compose.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .header("Authorization", BuildConfig.api_key)
            .build()
        return chain.proceed(request)
    }
}