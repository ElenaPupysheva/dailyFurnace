package com.alonsonya.dailyfurnace.di

import com.alonsonya.dailyfurnace.BuildConfig
import com.alonsonya.dailyfurnace.data.api.TokenApi
import com.alonsonya.dailyfurnace.data.api.ApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "http://149.154.71.181:6060/api/"
val networkModule = module {
    single {
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }
    single {
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.IS_DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        }
        OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .build()
    }
    single<ApiService> { get<Retrofit>().create(ApiService::class.java) }
    single<TokenApi>   { get<Retrofit>().create(TokenApi::class.java) }
}