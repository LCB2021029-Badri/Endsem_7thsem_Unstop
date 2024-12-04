package com.example.credit_risk_eval_badri_v01.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.credit_risk_eval_badri_v01.activities.JobDescriptionActivity
import com.example.credit_risk_eval_badri_v01.databinding.ListItemJobBinding
import com.example.credit_risk_eval_badri_v01.models.JobsResult

class JobResultsAdapter(private val jobsList: List<JobsResult>) : RecyclerView.Adapter<JobResultsAdapter.JobViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val binding = ListItemJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = jobsList[position]
        holder.bind(job)
    }

    override fun getItemCount() = jobsList.size

    inner class JobViewHolder(private val binding: ListItemJobBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(job: JobsResult) {
            binding.companyName.text = job.company_name
            binding.description.text = job.description
            binding.jobId.text = job.job_id
            binding.location.text = job.location
            binding.title.text = job.title
            binding.via.text = job.via
            binding.link.text = job.apply_options.firstOrNull()?.link

            // Handle item click to open DescriptionActivity
            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, JobDescriptionActivity::class.java).apply {
                    putExtra("company_name", job.company_name)
                    putExtra("description", job.description)
                    putExtra("job_id", job.job_id)
                    putExtra("location", job.location)
                    putExtra("title", job.title)
                    putExtra("via", job.via)
                    putExtra("link", job.apply_options.firstOrNull()?.link)
                }
                context.startActivity(intent)
            }
        }





    }
}
