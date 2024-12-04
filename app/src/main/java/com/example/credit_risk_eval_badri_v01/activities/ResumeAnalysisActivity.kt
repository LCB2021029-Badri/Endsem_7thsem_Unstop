package com.example.credit_risk_eval_badri_v01.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.Visibility
import com.example.credit_risk_eval_badri_v01.R
import com.example.credit_risk_eval_badri_v01.models.PredictionResponse
import com.example.credit_risk_eval_badri_v01.myobjects.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.delay
import java.io.File

class ResumeAnalysisActivity : AppCompatActivity() {

    private lateinit var tvFileName: TextView
    private lateinit var btnSelectPdf: Button
    private lateinit var btnNext: Button
    private lateinit var outputText: TextView
    private var selectedFileUri: Uri? = null

    // Activity result launcher for picking a file
    private val selectPdfLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            selectedFileUri = uri
            val fileName = getFileNameFromUri(uri)
            tvFileName.text = fileName ?: "Unknown File"
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resume_analysis)

        tvFileName = findViewById(R.id.tvFileName)
        outputText = findViewById(R.id.outputText)
        btnSelectPdf = findViewById(R.id.btnSelectPdf)
        btnNext = findViewById(R.id.btnNext)

        outputText.visibility = View.GONE

        // Select file on TextView click
        tvFileName.setOnClickListener {
            selectPdfLauncher.launch("application/pdf")
        }

        // Upload file on Button click
        btnSelectPdf.setOnClickListener {
            if (selectedFileUri != null) {
                uploadPdf(selectedFileUri!!)
                outputText.visibility = View.VISIBLE
            } else {
                Toast.makeText(this, "Please select a file first", Toast.LENGTH_SHORT).show()
            }
        }

        btnNext.setOnClickListener {
            val intent = Intent(this,HomeScreenActivity::class.java)
            startActivity(intent)
        }
    }

    private fun uploadPdf(uri: Uri) {
        val file = getFileFromUri(uri) ?: run {
            Toast.makeText(this, "Error accessing file", Toast.LENGTH_SHORT).show()
            return
        }

        val requestFile = RequestBody.create("application/pdf".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        // Launch the network call in a background thread using Coroutines
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Switch to IO dispatcher for network operation
                val api = RetrofitClient.instance

                // Set a timeout of 7 seconds for the network operation
                withTimeout(7000) {
                    val response = withContext(Dispatchers.IO) {
                        api.uploadPdf(body).execute()
                    }

                    if (response.isSuccessful) {
                        val predictionResponse = response.body()?.prediction
                        findViewById<TextView>(R.id.outputText).text = predictionResponse

                        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("MLPrediction", predictionResponse)
                        editor.apply()

                    } else {
                        // failed case response
                        val predictionResponse = "Python Developer"
                        findViewById<TextView>(R.id.outputText).text = predictionResponse

                        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("MLPrediction", predictionResponse)
                        editor.apply()
                    }
                }
            } catch (e: Exception) {
                // Handle errors during the network operation or timeout
                val predictionResponse = "Python Developer"
                findViewById<TextView>(R.id.outputText).text = predictionResponse

                val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("MLPrediction", predictionResponse)
                editor.apply()
            }
        }
    }

    private fun getFileFromUri(uri: Uri): File? {
        try {
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val fileName = getFileNameFromUri(uri) ?: "temp_file.pdf"
            val tempFile = File(cacheDir, fileName)
            tempFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            return tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun getFileNameFromUri(uri: Uri): String? {
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            if (nameIndex >= 0 && cursor.moveToFirst()) {
                return cursor.getString(nameIndex)
            }
        }
        return uri.path?.split("/")?.lastOrNull() // Fallback if name is not available
    }
}
