package com.pupup.firebstore2

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.auth.PhoneAuthProvider.verifyPhoneNumber
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit
import javax.xml.transform.Transformer

class OtpActivity : AppCompatActivity() {
    var auth = FirebaseAuth.getInstance()
    lateinit var callbacks: OnVerificationStateChangedCallbacks
    lateinit var otp : EditText
    var verificationId = " "
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        val phoneNum : EditText = findViewById(R.id.phoneNum)
        val send_btn : Button = findViewById(R.id.sendOtp)

        send_btn.setOnClickListener {
            if(phoneNum.text.toString().isEmpty()){
                phoneNum.error="Enter  phone number"
            }
            else {
                sendOtp(phoneNum.text.toString())

                val inflater = layoutInflater.inflate(R.layout.verify_otp_layout, null)
                 otp = inflater.findViewById(R.id.otp)
                val verify_btn: Button = inflater.findViewById(R.id.verifyOtp)

                val close: ImageView = inflater.findViewById(R.id.close)
                val dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setView(inflater)
                val dialog = dialogBuilder.create()
                dialog.setCancelable(false)
                dialog.show()
                close.setOnClickListener {
                    dialog.hide()
                    Toast.makeText(this, "Close!", Toast.LENGTH_SHORT).show()
                }
                verify_btn.setOnClickListener {
                    val credential =PhoneAuthProvider.getCredential(verificationId,otp.text.toString())
                    verifyOtp(credential)
                }
            }
        }
        callbacks = object :OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                verifyOtp(p0)
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(this@OtpActivity, "Failed", Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                verificationId=p0
            }

        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun sendOtp(mobile : String){
        val phoneAtuhOption = PhoneAuthOptions.newBuilder()
            .setPhoneNumber("+91$mobile")
            .setActivity(this)
            .setCallbacks(callbacks)
            .setTimeout(60L,TimeUnit.SECONDS)
            .build()
            verifyPhoneNumber(phoneAtuhOption)
    }
    fun verifyOtp(credential: PhoneAuthCredential){
        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                Toast.makeText(this, "Successfull Verify", Toast.LENGTH_SHORT).show()
//                startActivity(Intent(this,RegistrationActivity::class.java))
                checkDetails()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
    }

    fun checkDetails(){
        var db = Firebase.firestore.collection("students").whereEqualTo("userid",auth.currentUser?.uid.toString()).get()
            .addOnSuccessListener {
                if(it.isEmpty){
                    startActivity(Intent(this,RegistrationActivity::class.java))
                }
                else{
                    startActivity(Intent(this,MainActivity::class.java))
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
    }

}