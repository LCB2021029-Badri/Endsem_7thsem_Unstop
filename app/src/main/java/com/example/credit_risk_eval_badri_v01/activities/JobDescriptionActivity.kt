package com.example.credit_risk_eval_badri_v01.activities

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.credit_risk_eval_badri_v01.R

class JobDescriptionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_job_description)

        // Get the data from the Intent
        val companyName = intent.getStringExtra("companyName") ?: "Company Name"
        val title = intent.getStringExtra("title") ?: "Job Title"
        val location = intent.getStringExtra("location") ?: "Location"
        val link = intent.getStringExtra("link") ?: "https://example.com"
        val description = intent.getStringExtra("description") ?: "Job Description"

        // Find the TextViews by ID
        val companyNameTextView: TextView = findViewById(R.id.companyName)
        val titleTextView: TextView = findViewById(R.id.title)
        val locationTextView: TextView = findViewById(R.id.location)
        val linkTextView: TextView = findViewById(R.id.link)
        val descriptionTextView: TextView = findViewById(R.id.description)

        // Set the data to the TextViews
        companyNameTextView.text = companyName
        titleTextView.text = title
        locationTextView.text = location
        linkTextView.text = link
        descriptionTextView.text = description
    }
}