package com.example.credit_risk_eval_badri_v01.myobjects

import com.example.credit_risk_eval_badri_v01.interfaces.JobService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://serpapi.com/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val jobService: JobService by lazy {
        retrofit.create(JobService::class.java)
    }
}
