package com.example.firebasecurd

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class InsertActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var editText_name: EditText
    private lateinit var editText_email: EditText
    private lateinit var btnInsert: Button
    private lateinit var imageViewShow: ImageView
    private lateinit var imageViewAdd: ImageView
    lateinit var imagepath:String
    private var fileUri: Uri? = null

//    private var imageChildName = "${UUID.randomUUID()}"

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_insert)
        imageViewShow = findViewById(R.id.insert_image_Show)
        imageViewAdd = findViewById(R.id.gallery_open)
        editText_name = findViewById(R.id.name_insert)
        editText_email = findViewById(R.id.email_insert)
        btnInsert = findViewById(R.id.insert_data)

        imageViewAdd.setOnClickListener {
            openGallery()
        }

        btnInsert.setOnClickListener {
            setUserData()
        }
    }

    private fun setUserData() {

        fileUri?.let { uri ->
            val progressBar = ProgressDialog(this)
            progressBar.setTitle("User Added please wait!")
            progressBar.setMessage("Processing...")
            progressBar.show()
            val id = UUID.randomUUID().toString()

            val imageShare = FirebaseStorage.getInstance().reference
                .child("Image")
                .child(id)

            imageShare.putFile(uri).addOnSuccessListener {

                imageShare.downloadUrl.addOnSuccessListener {
                    val get_date_name = editText_name.text.toString()
                    val get_date_email = editText_email.text.toString()


                    val map = hashMapOf(
                        "id" to id,
                        "name" to get_date_name,
                        "email" to get_date_email,
                        "image" to it.toString()
                    )

                    db.collection("student_data").document(id).set(map)
                        .addOnSuccessListener {
                            progressBar.dismiss()
                            startActivity(Intent(this, MainActivity::class.java))
                            Toast.makeText(this, "add data", Toast.LENGTH_SHORT).show()
                            finish()

                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
                        }
                }


            }

        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_PICK
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, ""), 10)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10 && resultCode == RESULT_OK && data != null && data.data != null) {

            fileUri = data.data
            try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, fileUri);
                imageViewShow.setImageBitmap(bitmap)
                UplodeImage()
            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }

    private fun UplodeImage() {
        if (fileUri == null) {
            Toast.makeText(this, "No select Image", Toast.LENGTH_SHORT).show()
            return
        }
        val imageRef = FirebaseStorage.getInstance().reference
            .child(imagepath)

        val task = imageRef.putFile(fileUri!!)
        task.addOnSuccessListener {
            task.addOnSuccessListener {
                imagepath = it.storage.downloadUrl.toString()
                Toast.makeText(this, "imageUpload success $imagepath ", Toast.LENGTH_SHORT).show()
            }

            return@addOnSuccessListener
        }.addOnFailureListener {
            Toast.makeText(this, "imageUpload failed", Toast.LENGTH_SHORT).show()
            return@addOnFailureListener
        }
    }
}


