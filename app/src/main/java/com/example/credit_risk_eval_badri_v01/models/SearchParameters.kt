package com.example.credit_risk_eval_badri_v01.models

data class SearchParameters(
    val engine: String,
    val google_domain: String,
    val location_requested: String,
    val location_used: String,
    val q: String
)