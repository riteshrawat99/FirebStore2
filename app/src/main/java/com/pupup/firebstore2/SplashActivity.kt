package com.pupup.firebstore2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            if(auth.currentUser?.uid.toString()!=null){
            startActivity(Intent(this,MainActivity::class.java))
            }
            else{
            startActivity(Intent(this,OtpActivity::class.java))
            }
            finish()
        },1500)

    }
}

