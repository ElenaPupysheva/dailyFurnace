package com.alonsonya.dailyfurnace.push

import androidx.lifecycle.ViewModel
import com.alonsonya.dailyfurnace.data.api.FcmApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

class ChatViewModel: ViewModel() {

    private val api: FcmApi = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create()

}