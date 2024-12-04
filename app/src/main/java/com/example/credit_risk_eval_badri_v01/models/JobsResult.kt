package com.example.credit_risk_eval_badri_v01.models

data class JobsResult(
    val apply_options: List<ApplyOption>,
    val company_name: String,
    val description: String,
    val detected_extensions: DetectedExtensions,
    val extensions: List<String>,
    val job_id: String,
    val location: String,
    val share_link: String,
    val thumbnail: String,
    val title: String,
    val via: String
)