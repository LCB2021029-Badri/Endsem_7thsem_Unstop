package com.example.credit_risk_eval_badri_v01.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.credit_risk_eval_badri_v01.R
import com.example.credit_risk_eval_badri_v01.databinding.ActivitySupportScreenBinding
import com.google.ai.client.generativeai.GenerativeModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.runBlocking
import java.util.Date

class SupportScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySupportScreenBinding
    private lateinit var senderUid: String
    private lateinit var list: ArrayList<com.example.credit_risk_eval_badri_v01.activities.MessageModel>
    private lateinit var messageAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySupportScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        senderUid = FirebaseAuth.getInstance().uid.toString()
        list = ArrayList()

        messageAdapter = MessageAdapter(this, list)
        binding.rvChat.adapter = messageAdapter

        enableBottomNavView()

        binding.btnSend.setOnClickListener {
            if (binding.etMessage.text.isNotEmpty()) {
                val prompt = binding.etMessage.text.toString()
                val userMessage = MessageModel(prompt, senderUid, Date().time, "user")
                addMessageToChat(userMessage)

                val generativeModel = GenerativeModel(
                    modelName = "gemini-pro",
                    apiKey = "AIzaSyC0-M3p8S5Yky0AM_XgxBBzASDbOStYBoQ"
                )

                runBlocking {
                    val response = generativeModel.generateContent(prompt)
                    val botResponse = response.text
                    val botMessage = MessageModel(botResponse, "bot", Date().time, "bot")
                    addMessageToChat(botMessage)
                }

                binding.etMessage.text.clear()
            }
        }

        // Load chat history from Firebase
        loadChatFromDatabase()
    }

    private fun enableBottomNavView() {
        val bottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setSelectedItemId(R.id.supportScreen)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeScreen -> {
                    startActivity(Intent(applicationContext, HomeScreenActivity::class.java))
                    finish()
                    overridePendingTransition(0, 0)
                    true
                }

                R.id.supportScreen -> true
                R.id.statusScreen -> {
                    startActivity(Intent(applicationContext, StatusScreenActivity::class.java))
                    finish()
                    overridePendingTransition(0, 0)
                    true
                }

                R.id.notificationsScreen -> {
                    startActivity(
                        Intent(
                            applicationContext,
                            NotificationsScreenActivity::class.java
                        )
                    )
                    finish()
                    overridePendingTransition(0, 0)
                    true
                }

                else -> false
            }
        }
    }

    private fun loadChatFromDatabase() {
        // Assuming you still want to load historical chat data from Firebase
        val database = FirebaseDatabase.getInstance()
        val senderUidMergedReceiverUid = senderUid + "l9iU9onyrne3mGFpeCozHbJxcyr2"

        database.reference.child("chats")
            .child(senderUidMergedReceiverUid)
            .child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (snapshot1 in snapshot.children) {
                        val data =
                            snapshot1.getValue(com.example.credit_risk_eval_badri_v01.activities.MessageModel::class.java)
                        if (data != null && data.senderType == "user") {
                            list.add(data)
                        }
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun addMessageToChat(message: com.example.credit_risk_eval_badri_v01.activities.MessageModel) {
        list.add(message)
        messageAdapter.notifyItemInserted(list.size - 1)
        binding.rvChat.scrollToPosition(list.size - 1)
    }
}

data class MessageModel(
    val message: String? = "",
    val senderId: String? = "",
    val timestamp: Long? = 0L,
    val senderType: String = "user", // "user" or "bot"
)

class MessageAdapter(
    private val context: Context,
    private val messageList:
    ArrayList<com.example.credit_risk_eval_badri_v01.activities.MessageModel>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val USER_MESSAGE = 1
        private const val BOT_MESSAGE = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (messageList[position].senderId == "bot") {
            BOT_MESSAGE
        } else {
            USER_MESSAGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == USER_MESSAGE) {
            val view = LayoutInflater
                .from(context).inflate(R.layout.sender_chat_msg_layout, parent, false)
            UserMessageViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(context).inflate(R.layout.user_chat_msg_layout, parent, false)
            BotMessageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]
        if (holder is UserMessageViewHolder) {
            holder.message.text = message.message
        } else if (holder is BotMessageViewHolder) {
            holder.message.text = message.message
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    inner class UserMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val message: TextView = itemView.findViewById(R.id.tvMessage)
    }

    inner class BotMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val message: TextView = itemView.findViewById(R.id.tvMessage)
    }
}
