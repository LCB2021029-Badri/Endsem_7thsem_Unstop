package com.example.credit_risk_eval_badri_v01.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.credit_risk_eval_badri_v01.R
import com.example.credit_risk_eval_badri_v01.databinding.ActivityClientUpdateStatusBinding
import com.example.credit_risk_eval_badri_v01.interfaces.MyBlockchainApi
import com.example.credit_risk_eval_badri_v01.models.JobDataModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ClientUpdateStatusActivity : AppCompatActivity() {

    private lateinit var binding:ActivityClientUpdateStatusBinding
    private lateinit var database: FirebaseDatabase

    val USERNAME = "u0tzzoe182"
    val PASSWORD = "joCMnDubl5xBq5UAHM9NFdvSr8zZu6GDHC_karKsrGE"
    val BASE2_URL = "https://u0ddvaf03p-u0wjcd0t94-connect.us0-aws.kaleido.io/gateways/u0g2ygmpsm/0xcb9a050d5611bf88d5409057fc1059582d726565/"
    val kldFromValue2 = "0x099bfedb558eb1deb3ad94aa5eaf367d83c94823"
    private lateinit var myApi: MyBlockchainApi


    private lateinit var name:String
    private lateinit var uid:String
    private lateinit var email:String
    private lateinit var jobType:String
    private lateinit var noOfDependents:String
    private lateinit var education:String
    private lateinit var sritscore:String
    private lateinit var etCgpa:String
    private lateinit var etField:String
    private lateinit var etExperience:String
    private lateinit var etpreviousctc:String
    private lateinit var etcurrentctc:String
    private lateinit var etskills:String
    private lateinit var etworkinghours:String
    private lateinit var etadditionalinfo:String
    private lateinit var mlOutput:String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientUpdateStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        displayCurrentResultFromDatabase()
        binding.btnUpdate.setOnClickListener {
            if(binding.etUpdateResult.text.isNullOrEmpty()){

            }
            else{
                mlOutput = binding.etUpdateResult.text.toString()
                updateLjobDataInDatabase()
                postData()
            }
        }

    }

    private fun displayCurrentResultFromDatabase(){
        database = FirebaseDatabase.getInstance()
        uid = intent.getStringExtra("uid")!!
        database.reference.child("jobs")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(snapshot1 in snapshot.children){
                        val data = snapshot1.getValue(JobDataModel::class.java)
                        if(data!!.uid == uid){
                            name = data.name!!
                            email = data.email!!
                            jobType = data.jobType!!
                            noOfDependents = data.noOfDependents!!
                            education = data.education!!
                            sritscore = data.sritscore!!
                            etCgpa = data.etCgpa!!
                            etpreviousctc = data.etpreviousctc!!
                            etadditionalinfo = data.etadditionalinfo!!
                            etExperience = data.etExperience!!
                            etcurrentctc = data.etcurrentctc!!
                            etworkinghours = data.etworkinghours!!
                            etField = data.etField!!
                            etskills = data.etskills!!
                            mlOutput = data.mlOutput!!

                            binding.tvCurrentResult.text =mlOutput
                            if(mlOutput=="Accepted"){
                                binding.tvCurrentResult.setBackgroundColor(ContextCompat.getColor(this@ClientUpdateStatusActivity, R.color.myGreen))
                            }
                            else if(mlOutput == "Declined"){
                                binding.tvCurrentResult.setBackgroundColor(ContextCompat.getColor(this@ClientUpdateStatusActivity, R.color.myRed))
                            }
                            else{
                                binding.tvCurrentResult.setBackgroundColor(ContextCompat.getColor(this@ClientUpdateStatusActivity, R.color.myGrey))
                            }
                            break
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun updateLjobDataInDatabase(){
        if(binding.etUpdateResult.text.isNullOrEmpty()){
            //do nothing
        }
        else{
            var jobData = JobDataModel(
                name,
                uid,
                email,
                jobType,
                noOfDependents,
                education,
                sritscore,
                etCgpa,
                etField,
                etExperience,
                etpreviousctc,
                etcurrentctc,
                etskills,
                etworkinghours,
                etadditionalinfo,
                binding.etUpdateResult.text.toString()
            )
//            dialogBox("Updating data in Database","Please wait...")
            database.reference.child("jobs")
                .child(uid)
                .setValue(jobData)
                .addOnSuccessListener {
//                                startActivity(Intent(this@SignupActivity, MainActivity::class.java))
//                                finish()
//                    dialog.dismiss()
                }
                .addOnFailureListener {
//                createSnackBar(binding.root,"failed to upload data to Realtime DB","Try Again")
//                    dialog.dismiss()
                }

        }

    }

    private fun RetrofitCreate() {
        val credentials = Credentials.basic(
            USERNAME,
            PASSWORD
        )
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE2_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                //-----------------------
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                    .addInterceptor { chain ->
                        val newRequest = chain.request().newBuilder()
                            .header("Authorization", credentials)
                            .build()
                        chain.proceed(newRequest)
                    }
                    .build()
                //------------------------
            )
            .build()

        myApi = retrofit.create(MyBlockchainApi::class.java)
    }

    private fun postData() {
        RetrofitCreate()
        getOutputFromML()
        val inputData:List<String> = listOf(
            jobType,
            etpreviousctc,
            sritscore,
            mlOutput,
            uid
        )
        val requestData = MyBlockchainApi.RequestData(inputData)
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = myApi.PostData(kldFromValue2, requestData).execute()
//                dialogBox("Adding updated data to the blockchain","Please wait...")
                if (response.isSuccessful) {
                    GlobalScope.launch(Dispatchers.Main) {
//                        dialog.dismiss()
                        Toast.makeText(this@ClientUpdateStatusActivity, "new data added to blockchain", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    GlobalScope.launch(Dispatchers.Main) {
//                        dialog.dismiss()
                        Toast.makeText(this@ClientUpdateStatusActivity, "Failed to add data to blockchain", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } catch (e: Exception) {
                GlobalScope.launch(Dispatchers.Main) {
//                    dialog.dismiss()
                    Toast.makeText(this@ClientUpdateStatusActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getOutputFromML(){
        mlOutput = binding.etUpdateResult.text.toString()
    }


}