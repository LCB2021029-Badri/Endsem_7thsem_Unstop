package com.example.credit_risk_eval_badri_v01.models

data class JobResultsResponse(
    val filters: List<Filter>,
    val jobs_results: List<JobsResult>,
    val search_metadata: SearchMetadata,
    val search_parameters: SearchParameters,
    val serpapi_pagination: SerpapiPagination
)