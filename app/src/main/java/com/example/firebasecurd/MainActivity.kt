package com.example.firebasecurd

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class MainActivity : AppCompatActivity(),StudentAdapter.SetOnStudentClickListener {
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var listView: ListView
    private lateinit var studentDetailAdapter: StudentAdapter
    private val db = FirebaseFirestore.getInstance()
    private val userArray = ArrayList<StudentModel>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        floatingActionButton = findViewById(R.id.floatingActionButton)
        listView = findViewById(R.id.list_view)

        studentDetailAdapter = StudentAdapter(userArray, this, this)
        listView.adapter = studentDetailAdapter
        showStudents()

        floatingActionButton.setOnClickListener {
            val intent = Intent(this, InsertActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onUpdateClick(student: StudentModel) {
        val intent = Intent(this, UpdateDetailsData::class.java)
        intent.putExtra("key_id", student.id.toString())
        intent.putExtra("key_name", student.name.toString())
        intent.putExtra("key_email", student.email.toString())
        startActivity(intent)
    }

    override fun onDeleteClick(student: StudentModel) {
        deleteStudent(student.id.toString())
    }

    private fun deleteStudent(id: String) {
        db.collection("student_data").document(id).delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show()
                showStudents()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Deletion failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showStudents() {
        db.collection("student_data").get()
            .addOnSuccessListener {
                val arrayList = ArrayList<StudentModel>()
                val data = it.toObjects(StudentModel::class.java)

                studentDetailAdapter = StudentAdapter(arrayList, this, this)
                listView.adapter = studentDetailAdapter
                arrayList.addAll(data)
            }
            .addOnFailureListener {
                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
            }
    }

}