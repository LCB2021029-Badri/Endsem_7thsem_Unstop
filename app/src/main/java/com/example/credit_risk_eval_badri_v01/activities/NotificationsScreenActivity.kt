package com.example.credit_risk_eval_badri_v01.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.credit_risk_eval_badri_v01.R
import com.example.credit_risk_eval_badri_v01.adapters.JobResultsAdapter
import com.example.credit_risk_eval_badri_v01.databinding.ActivityNotificationsScreenBinding
import com.example.credit_risk_eval_badri_v01.models.AssessmentScoreModel
import com.example.credit_risk_eval_badri_v01.models.JobsResult
import com.example.credit_risk_eval_badri_v01.myobjects.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class NotificationsScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationsScreenBinding
    private lateinit var jobResultsAdapter: JobResultsAdapter
    private val jobResultsList = mutableListOf<JobsResult>()

    lateinit var score:String
    lateinit var MLPrediction:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationsScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        score = sharedPreferences.getString("testScore","80").toString()
        MLPrediction = sharedPreferences.getString("MLPrediction", "Python Developer").toString()

        enableBottomNavView()

        setupRecyclerView()
        fetchJobResults()


    }

    private fun setupRecyclerView() {
        jobResultsAdapter = JobResultsAdapter(jobResultsList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = jobResultsAdapter
    }

    private fun fetchJobResults() {
        // Coroutine to fetch data from API
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.jobService.getJobResults(
                    engine = "google_jobs",
                    location = "India",
                    query = "Python Developer",
                    apiKey = "e7e72a16e5ab8bb55b3c55767601c37990e077ad952eb4225d7336f7ba543d7a"
                )

                // Update the RecyclerView on the main thread
                withContext(Dispatchers.Main) {
                    jobResultsList.clear()
                    jobResultsList.addAll(response.jobs_results)
                    jobResultsAdapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                // Handle error (e.g., show an error message)
                e.printStackTrace()
            }
        }
    }

    private fun enableBottomNavView(){
        val bottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setSelectedItemId(R.id.notificationsScreen)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.statusScreen -> {
                    startActivity(Intent(applicationContext, StatusScreenActivity::class.java))
                    finish()
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.notificationsScreen -> true
                R.id.supportScreen -> {
                    startActivity(Intent(applicationContext, SupportScreenActivity::class.java))
                    finish()
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.homeScreen -> {
                    startActivity(Intent(applicationContext, HomeScreenActivity::class.java))
                    finish()
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }
    }


}