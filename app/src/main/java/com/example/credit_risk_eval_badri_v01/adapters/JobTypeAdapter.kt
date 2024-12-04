package com.example.credit_risk_eval_badri_v01.adapters

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.credit_risk_eval_badri_v01.R
import com.example.credit_risk_eval_badri_v01.activities.HomeScreenActivity
import com.example.credit_risk_eval_badri_v01.models.JobTypeModel
import com.example.credit_risk_eval_badri_v01.fragments.ApplicantJobDetailsFragment

class JobTypeAdapter(private val jobTypeList:ArrayList<JobTypeModel>, private val activity: HomeScreenActivity):
    RecyclerView.Adapter<JobTypeAdapter.jobTypeViewHolder>() {

    class jobTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvjobType: TextView = itemView.findViewById(R.id.tvjobType)
        val ivjobType: ImageView = itemView.findViewById(R.id.ivjobType)
        val layout:LinearLayout = itemView.findViewById(R.id.layout)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): jobTypeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_job_type_item,parent,false)
        return jobTypeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return jobTypeList.size
    }

    var index = -1
    override fun onBindViewHolder(holder: jobTypeViewHolder, position: Int) {
        val jobType = jobTypeList[position]
        holder.tvjobType.text = jobType.jobType
        holder.ivjobType.setImageResource(jobType.jobIcon)
        holder.itemView.setOnClickListener (object:View.OnClickListener{
            override fun onClick(p0: View?) {
                val activity = p0!!.context as AppCompatActivity
                val applicantJobDetailsFragment = ApplicantJobDetailsFragment()

                //try
                val bundle = Bundle()
                bundle.putString("jobType", jobType.jobType)
                applicantJobDetailsFragment.arguments = bundle

//                activity.supportFragmentManager.beginTransaction()
//                    .replace(R.id.fragmentContainerView,applicantjobDetailsFragment)
//                    .addToBackStack(null)
//                    .commit()

                val fragmentManager = activity.supportFragmentManager
                fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                val transaction = fragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView, applicantJobDetailsFragment)
                transaction.commit()

                index = position
                notifyDataSetChanged()
            }

        })

        if(index == position){
            holder.tvjobType.setTextColor(Color.BLACK)
            holder.layout.setBackgroundColor(Color.parseColor("#FDD962"))

            val color = ContextCompat.getColor(holder.itemView.context, R.color.black)
            holder.ivjobType.setColorFilter(color)

        }
        else{
            holder.tvjobType.setTextColor(Color.parseColor("#C6C7C9"))
            holder.layout.setBackgroundColor(Color.parseColor("#233238"))

            val color = ContextCompat.getColor(holder.itemView.context, R.color.myWhite)
            holder.ivjobType.setColorFilter(color)

        }

    }

}