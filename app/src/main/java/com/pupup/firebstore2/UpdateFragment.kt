package com.pupup.firebstore2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.pupup.firebstore2.R.id.getEmail
import com.pupup.firebstore2.R.id.getName

class UpdateFragment : Fragment() {
    val auth = FirebaseAuth.getInstance()
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_update, container, false)

        val getName : EditText =view.findViewById(R.id.getName)
        val getEmail : EditText =view.findViewById(R.id.getEmail)
        val getAge : EditText =view.findViewById(R.id.getAge)
        val update_btn : Button =view.findViewById(R.id.update_btn)

        val userId = auth.currentUser?.uid.toString()
        val db = Firebase.firestore
        db.collection("students").document(userId).get()
            .addOnSuccessListener {
               if(it !=null){
                   val name = it?.data?.get("name").toString()
                   getName.setText(name)
                   val email = it?.data?.get("email").toString()
                   getEmail.setText(email)
                   val age = it?.data?.get("age").toString()
                   getAge.setText(age)
               }
            }
            .addOnFailureListener {

            }

        update_btn.setOnClickListener {
            db.collection("students").document(userId).update(
                "name" , getName.text.toString(),
                "email",getEmail.text.toString(),
                "age",getAge.text.toString()
            )
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Update Successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(requireContext(),MainActivity::class.java))
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                }
        }
        return view
    }


}