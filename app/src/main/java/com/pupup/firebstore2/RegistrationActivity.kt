package com.pupup.firebstore2

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegistrationActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        
        val reg_name : EditText = findViewById(R.id.reg_name)
        val reg_age : EditText = findViewById(R.id.reg_age)
        val reg_email : EditText = findViewById(R.id.reg_email)
        val reg_btn : Button = findViewById(R.id.reg_btn)
        
        val userId = Firebase.auth.currentUser?.uid.toString()
        val db = Firebase.firestore
        reg_btn.setOnClickListener {
            val studentData = StudentModel(
                 reg_name.text.toString(),
                 reg_email.text.toString(),
                 reg_age.text.toString().toLong(),
                userId 
            )
            db.collection("students").document(userId).set(studentData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Insert Successfully!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,MainActivity::class.java))
                }
                .addOnFailureListener {
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
            
       
        
    }
}