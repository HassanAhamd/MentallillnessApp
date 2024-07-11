package com.superior.mentallillness.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.superior.mentallillness.R
import com.superior.mentallillness.adapter.MessageAdapter
import com.superior.mentallillness.databinding.ActivityChatBinding
import com.superior.mentallillness.helpers.Helper
import com.superior.mentallillness.model.MessageModal
import com.superior.mentallillness.model.User

class ChatActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityChatBinding
    private lateinit var messageAdapter: MessageAdapter
    private val messageList = ArrayList<MessageModal>()
    private var currentQuestionIndex = 0
    private var YesCount = 0
    var USERNAME = ""
    lateinit var questions: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        var window = this.window
        window.statusBarColor = this.resources.getColor(R.color.primaryColor);
        var allQuestions = listOf(
            "Are you feeling stressed?",
            "Do you get enough sleep?",
            "Are you satisfied with your work-life balance?",
            "Do you engage in regular physical activity?",
            "Do you have a strong support system?",
            "Are you satisfied with your relationships?",
            "Do you practice mindfulness or relaxation techniques?",
            "Do you feel a sense of purpose in your life?",
            "Do you often experience negative thoughts?",
            "Do you feel optimistic about the future?",
            "Do you often feel overwhelmed by stress?",
            "Do you find it hard to focus or concentrate?",
            "Have you experienced changes in your sleep patterns?",
            "Do you often feel anxious or worried?",
            "Have you lost interest in activities you used to enjoy?",
            "Do you frequently experience mood swings?",
            "Do you struggle with feelings of sadness or hopelessness?",
            "Have you had thoughts of self-harm or suicide?",
            "Do you have difficulty in maintaining healthy relationships?",
            "Do you feel a sense of isolation or loneliness?",
            "Do you often experience feelings of guilt?",
            "Have you noticed changes in your appetite?",
            "Do you have trouble making decisions?",
            "Have you experienced physical symptoms without a clear medical cause?",
            "Do you engage in negative self-talk?",
            "Are you able to set and achieve realistic goals?",
            "Do you have a tendency to avoid social situations?",
            "Have you noticed changes in your energy levels?",
            "Do you feel a constant sense of restlessness or agitation?",
            "Do you have difficulty in expressing your emotions?",
            "Do you feel a lack of motivation?",
            "Have you experienced a significant loss recently?",
            "Do you have trouble relaxing or unwinding?",
            "Have you ever felt disconnected from reality?",
            "Do you engage in repetitive behaviors or rituals?",
            "Are you satisfied with your work or academic performance?",
            "Do you have a history of trauma or abuse?",
            "Have you experienced changes in your interest in sex?",
            "Do you have a support system that you can rely on?",
            "Are you able to cope with life's challenges effectively?",
            "Do you often feel a sense of worthlessness?",
            "Have you experienced changes in your weight without intentional changes in diet or exercise?",
            "Do you have trouble falling asleep or staying asleep?",
            "Have you lost interest in personal hygiene or self-care?",
            "Do you experience racing thoughts or a racing heartbeat?",
            "Have you ever felt paranoid or excessively suspicious of others?",
            "Do you engage in self-destructive behaviors?",
            "Have you had difficulty in trusting others?",
            "Do you feel a constant need for reassurance from others?",
            "Have you experienced a sudden and intense fear or panic attack?",
            "Do you have a history of substance abuse or addiction?",
            "Have you noticed changes in your memory or concentration?",
            "Do you often feel a sense of emptiness or numbness?",
            "Have you experienced unexplained physical pain or discomfort?",
            "Do you feel socially isolated even in the presence of others?",
            "Have you ever had hallucinations or delusions?",
            "Do you engage in excessive worrying about the future?",
            "Have you experienced changes in your ability to enjoy pleasurable activities?",
            "Do you have difficulty in setting boundaries with others?",
            "Have you ever had thoughts of harming others?",
            "Do you have a fear of social situations or being judged by others?",
            "Have you experienced changes in your motivation or interest in daily activities?",
            "Do you often feel irritable or easily angered?",
            "Have you noticed changes in your ability to handle stress?",
            "Do you experience difficulty in letting go of past traumas?",
            "Have you ever had difficulty in identifying and expressing your emotions?",
            "Do you engage in perfectionistic behaviors?",
            "Have you experienced changes in your appetite, such as overeating or undereating?",
            "Do you feel a sense of impending doom or constant worry about the future?",
            "Have you had difficulty in maintaining focus or attention?",
            "Do you have a history of obsessive-compulsive behaviors?",
            "Have you experienced a significant life transition recently?",
            "Do you have trouble trusting others with personal information?",
            "Have you ever felt a disconnection from your own body?",
            "Do you find it challenging to initiate or maintain relationships?",
            "Have you experienced changes in your self-esteem or self-worth?",
            "Do you engage in self-sabotaging behaviors?",
            "Have you ever had thoughts of escaping from reality?",
            "Do you feel a constant need for validation from others?",
            "Have you experienced changes in your level of irritability or impatience?"
        )
        questions = selectRandomQuestions(allQuestions, 10)
        loadUserFromFirebase()
        initAdapter()
        initListner()
    }

    private fun initAdapter() {
        // Set up RecyclerView
        messageAdapter = MessageAdapter(this, messageList) {
            if (it.message.contains("You are need treatment Please consult a Doctor.",ignoreCase = true)){
                startActivity(Intent(this,DoctorListActivity::class.java))
            }
        }
        mBinding.rvMessages.layoutManager = LinearLayoutManager(this)
        mBinding.rvMessages.adapter = messageAdapter
    }
    private fun displayNextQuestion() {
        if (currentQuestionIndex < questions.size) {
            var  question = "${currentQuestionIndex + 1}: ${questions[currentQuestionIndex]}"
            messageList.add(
                MessageModal(
                    question,
                    false
                )
            )
            messageAdapter.notifyItemInserted(messageList.size - 1)
            mBinding.rvMessages.smoothScrollToPosition(messageList.size)
        } else {
            if (YesCount >= 5){
                messageList.add(
                    MessageModal(
                        "${USERNAME}. You need treatment Please consult a Doctor.\nPlease Click here to book appointment with our specialist doctors",
                        false
                    )
                )
                messageAdapter.notifyItemInserted(messageList.size - 1)
                mBinding.rvMessages.smoothScrollToPosition(messageList.size)
                mBinding.editTextMessage.isEnabled = false
            }else{
                messageList.add(
                    MessageModal(
                        "${USERNAME}. You are absolutely fine Just stay HAPPY.",
                        false
                    )
                )
                messageAdapter.notifyItemInserted(messageList.size - 1)
                mBinding.rvMessages.smoothScrollToPosition(messageList.size)
                mBinding.editTextMessage.isEnabled = false
            }
        }
    }

    private fun initListner() {

        mBinding.ivBack.setOnClickListener {
            onBackPressed()
        }

        mBinding.rlSend.setOnClickListener {
            if (mBinding.editTextMessage.text.toString().trim() == "") {
                Helper.showCustomAppAlert(
                    "Please enter your message",
                    R.drawable.ic_bell_alert,
                    this
                )
                return@setOnClickListener
            }
            if (mBinding.editTextMessage.text.toString().trim() == "YES" || mBinding.editTextMessage.text.toString().trim() == "Yes" || mBinding.editTextMessage.text.toString().trim() == "yes") {
                YesCount++
                messageList.add(
                    MessageModal(
                        mBinding.editTextMessage.text.toString().trim(),
                        true
                    )
                )
                messageAdapter.notifyItemInserted(messageList.size - 1)
                currentQuestionIndex++
                displayNextQuestion()
                // Clear the EditText
                mBinding.editTextMessage.text.clear()
            } else if (mBinding.editTextMessage.text.toString().trim() == "NO" || mBinding.editTextMessage.text.toString().trim() == "No" || mBinding.editTextMessage.text.toString().trim() == "no") {
                messageList.add(
                    MessageModal(
                        mBinding.editTextMessage.text.toString().trim(),
                        true
                    )
                )
                messageAdapter.notifyItemInserted(messageList.size - 1)
                currentQuestionIndex++
                displayNextQuestion()
                // Clear the EditText
                mBinding.editTextMessage.text.clear()
            }else {
                messageList.add(
                    MessageModal(
                        mBinding.editTextMessage.text.toString().trim(),
                        true
                    )
                )
                messageAdapter.notifyItemInserted(messageList.size - 1)
                mBinding.rvMessages.smoothScrollToPosition(messageList.size)
                messageList.add(
                    MessageModal(
                        "Invalid Response. Please repose only in YES or NO",
                        false
                    )
                )
                messageAdapter.notifyItemInserted(messageList.size - 1)
                mBinding.rvMessages.smoothScrollToPosition(messageList.size)
                // Clear the EditText
                mBinding.editTextMessage.text.clear()
                return@setOnClickListener
            }
//            if (mBinding.editTextMessage.text.toString().trim() != "Yes" || mBinding.editTextMessage.text.toString().trim() != "yes") {
//
//            }else if (mBinding.editTextMessage.text.toString().trim() != "No" || mBinding.editTextMessage.text.toString().trim() != "no"){
//                messageList.add(
//                    MessageModal(
//                        "Invalid Response. Please repose only in YES orNO",
//                        false
//                    )
//                )
//                messageAdapter.notifyItemInserted(messageList.size - 1)
//                return@setOnClickListener
//            }else if(){
//
//            }
        }
    }


    private fun loadUserFromFirebase() {
        mBinding.svLoading.visibility = View.VISIBLE
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val usersRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
        userId?.let { uid ->
            // Create a query to get the user with the specified UID
            val userQuery: Query = usersRef.child(uid)

            // Add a ValueEventListener to listen for changes in the data
            userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Check if the snapshot has data
                    if (snapshot.exists()) {
                        mBinding.svLoading.visibility = View.GONE
                        // Retrieve user data
                        val user = snapshot.getValue(User::class.java)
                        user?.let {
                            USERNAME = it.username
                            messageList.add(
                                MessageModal(
                                    "Hi ${USERNAME}. Answer the following questions with YES or NO.",
                                    false
                                )
                            )
                            messageAdapter.notifyItemInserted(messageList.size - 1)
                            mBinding.rvMessages.smoothScrollToPosition(messageList.size)
                            displayNextQuestion()
                        }


                    } else {
                        // User does not exist or there's no data for the specified UID
                        messageList.add(MessageModal("Hi, How can i help you ?", false))
                        messageAdapter.notifyItemInserted(messageList.size - 1)
                        mBinding.rvMessages.smoothScrollToPosition(messageList.size)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    mBinding.svLoading.visibility = View.GONE
                }
            })
        }
    }
    fun selectRandomQuestions(allQuestions: List<String>, numberOfQuestions: Int): List<String> {
        if (numberOfQuestions > allQuestions.size) {
            throw IllegalArgumentException("Number of selected questions cannot be greater than the total number of questions.")
        }

        val shuffledQuestions = allQuestions.shuffled()
        return shuffledQuestions.subList(0, numberOfQuestions)
    }
}