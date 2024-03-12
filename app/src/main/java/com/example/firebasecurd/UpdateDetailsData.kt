package com.example.firebasecurd

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class UpdateDetailsData : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var editText_name: EditText
    private lateinit var editText_email: EditText
    private lateinit var btnUpdate: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_details_data)
        val id = intent.extras?.getString("key_id")
        val name = intent.extras?.getString("key_name")
        val email = intent.extras?.getString("key_email")

        btnUpdate = findViewById(R.id.Update_data_Btn)

        editText_name = findViewById(R.id.name_update)
        editText_email = findViewById(R.id.email_update)

        editText_name.setText(name)
        editText_email.setText(email)

        btnUpdate.setOnClickListener {
            val getDateName = editText_name.text.toString()
            val getDateEmail = editText_email.text.toString()

            val map= mapOf(
                "id" to id,
                "name" to  getDateName,
                "email" to  getDateEmail)
            db.collection("student_data").document("$id").update(map)
                .addOnSuccessListener {
                    Toast.makeText(this, "user updated", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                }
                .addOnFailureListener {
                    Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
                }
        }


    }
}