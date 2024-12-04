package com.example.credit_risk_eval_badri_v01.myobjects

import com.example.credit_risk_eval_badri_v01.interfaces.ResumeAnalysisApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://resume-screening-app-1.onrender.com/"

    val instance: ResumeAnalysisApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ResumeAnalysisApi::class.java)
    }
}
