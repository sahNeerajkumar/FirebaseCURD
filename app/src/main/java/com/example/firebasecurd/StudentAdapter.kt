package com.example.firebasecurd

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.FirebaseStorage

class StudentAdapter(
    private val studentModelList: ArrayList<StudentModel>, private val context: Context,
    private val onStudentClickListener: SetOnStudentClickListener
) : BaseAdapter() {
    override fun getCount(): Int {
        return studentModelList.size
    }

    override fun getItem(position: Int): Any {
        return studentModelList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder", "ResourceType", "SuspiciousIndentation")

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.item_data, parent, false)
        val nameItemData = view.findViewById<TextView>(R.id.name_text)
        val emailItemData = view.findViewById<TextView>(R.id.email_text)
        val delete = view.findViewById<ImageView>(R.id.delete_icon)
        val update = view.findViewById<ImageView>(R.id.edit_icon)
        val image_Catch = view.findViewById<ImageView>(R.id.profile_image)

        nameItemData.text = studentModelList[position].name
        emailItemData.text = studentModelList[position].email

//        val image = FirebaseStorage.getInstance().reference
//            image.downloadUrl
        Glide.with(context)
            .load(studentModelList[position].image)
            .apply(RequestOptions().placeholder(R.drawable.img))
            .into(image_Catch)

        delete.setOnClickListener {

             val dialog = AlertDialog.Builder(context)
            dialog.setTitle("Delete")
            dialog.setMessage("Are you sure Delete")
            dialog.setNegativeButton("cancel"){dialog,which ->
                dialog.dismiss()
            }
            dialog.setPositiveButton("Yes"){dialog,which->
                onStudentClickListener.onDeleteClick(studentModelList[position])
            }
            val alert = dialog.create()
            dialog.show()
        }

        update.setOnClickListener {
            onStudentClickListener.onUpdateClick(studentModelList[position])
        }
        return view
    }


    interface SetOnStudentClickListener {
        fun onDeleteClick(student: StudentModel)
        fun onUpdateClick(student: StudentModel)
    }
}

