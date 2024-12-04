package com.example.credit_risk_eval_badri_v01.models

data class DetectedExtensions(
    val posted_at: String,
    val qualifications: String,
    val salary: String,
    val schedule_type: String,
    val work_from_home: Boolean
)