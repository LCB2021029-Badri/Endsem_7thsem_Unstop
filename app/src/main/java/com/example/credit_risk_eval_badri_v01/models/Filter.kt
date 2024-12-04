package com.example.credit_risk_eval_badri_v01.models

data class Filter(
    val link: String,
    val name: String,
    val options: List<Option>,
    val parameters: ParametersX,
    val serpapi_link: String
)