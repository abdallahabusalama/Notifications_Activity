@file:Suppress("CAST_NEVER_SUCCEEDS")
package com.example.myapplication

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.UserAdapter
import com.example.myapplication.databinding.ActivityAdapterUserBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

@Suppress("DEPRECATION")
class MainActivity2 : AppCompatActivity() {

    lateinit var binding: ActivityAdapterUserBinding
    private lateinit var recycleView: RecyclerView
    val storage = Firebase.storage
    val storageRef = storage.reference
    val pdfFiles = java.util.ArrayList<ByteArray>()
    val pdfFileNames = java.util.ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdapterUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getPdfFilesFromFirebaseStorage()
        recycleView = findViewById(R.id.listView)
    }
    private fun getPdfFilesFromFirebaseStorage() {
        showProgressDialog(true)
        val pdfRef = storageRef.child("pdfs")
        pdfRef.listAll().addOnSuccessListener { listResult ->

            for (pdf in listResult.items) {
                pdf.getBytes(Long.MAX_VALUE).addOnSuccessListener { pdfBytes ->
                    pdfFiles.add(pdfBytes)
                    pdfFileNames.add(pdf.name)
                    Log.e("TAG", pdfBytes.toString()+pdf.name)

                    if (pdfFiles.size == listResult.items.size) {
                        recycleView.layoutManager = LinearLayoutManager(this)
                        recycleView.adapter = UserAdapter(this,pdfFileNames,pdfFiles)
                        showProgressDialog(false)
                    }
                }.addOnFailureListener { exception ->
                    Log.e("TAG", "Error downloading PDF file", exception)
                }
            }
        }.addOnFailureListener { exception ->
            Log.e("TAG", "Error listing PDF files", exception)
        }
    }
    fun showProgressDialog(isShow: Boolean){
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("SHOW PDFS")
        progressDialog.setMessage("Please wait...")
        progressDialog.setCancelable(false)
        if (isShow){
            progressDialog.show()
        }
        if(isShow == false){
            progressDialog.dismiss()
        }

    }

}
