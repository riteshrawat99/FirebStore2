package com.pupup.firebstore2

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    val auth  = FirebaseAuth.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val menu_icon :ImageView = findViewById(R.id.menu_icon)
        val navigationView : NavigationView = findViewById(R.id.navigationView)
        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val frameLayout : FrameLayout = findViewById(R.id.frameLayout)

        menu_icon.setOnClickListener {
            drawerLayout.open()
        }
        val userId = auth.currentUser?.uid.toString()
        val db = Firebase.firestore
        var data : StudentModel?

        db.collection("students").document(userId).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // Data exists in the document
                    val data = documentSnapshot.data // This gives you a Map of the data
                    // You can convert the data to your custom model if needed

                    val menu = navigationView.menu
                    val menuName = menu.findItem(R.id.reg_name)
                    menuName.title = data?.get("name") as String
                    val menuEmail = menu.findItem(R.id.reg_email)
                    menuEmail.title = data?.get("email") as String
                    val menuAge = menu.findItem(R.id.reg_age)
                } else {
                    // Document does not exist
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors here
            }


        navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.reg_name-> Toast.makeText(this, "name", Toast.LENGTH_SHORT).show()
                R.id.logout->{
                    auth.signOut()
                    startActivity(Intent(this,OtpActivity::class.java))
                    Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()
                }
                R.id.updateFra->
                {
                    changeFragment(UpdateFragment())
                    drawerLayout.close()
                }
                R.id.delete->{
                    db.collection("students").document(userId).delete()
                    Toast.makeText(this, "Delete Successfully!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,OtpActivity::class.java))
                }
            }
            true
        }
    }

    fun changeFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout,fragment).commit()
    }

}