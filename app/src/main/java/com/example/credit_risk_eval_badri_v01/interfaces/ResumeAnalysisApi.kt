package com.example.credit_risk_eval_badri_v01.interfaces

import com.example.credit_risk_eval_badri_v01.models.PredictionResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ResumeAnalysisApi {
    @Multipart
    @POST("predict")
    fun uploadPdf(@Part file: MultipartBody.Part): Call<PredictionResponse>
}