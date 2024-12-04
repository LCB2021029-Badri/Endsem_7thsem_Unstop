package com.example.credit_risk_eval_badri_v01.interfaces

import com.example.credit_risk_eval_badri_v01.models.JobResultsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface JobService {
    @GET("search")
    suspend fun getJobResults(
        @Query("engine") engine: String,
        @Query("location") location: String,
        @Query("q") query: String,
        @Query("api_key") apiKey: String
    ): JobResultsResponse
}
