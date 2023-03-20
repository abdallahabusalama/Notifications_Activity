@file:Suppress("DEPRECATION")

package com.example.myapplication.adapter
import android.app.ProgressDialog
import android.content.Context
import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.UserItemBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


@Suppress("DEPRECATION")
class UserAdapter(
    var context: Context,
    var data: ArrayList<String>,
    var pdfs: ArrayList<ByteArray>
) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(var binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return UserViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return data.size
    }
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.itemView.setBackground(
            ContextCompat.getDrawable(
                holder.itemView.getContext(),
                R.drawable.underline
            )
        );
        holder.binding.txtName.text =data[position]
        holder.binding.btnDownload.setOnClickListener {
            showProgressDialog(true)

            val outputStream: FileOutputStream
            try {
                outputStream = FileOutputStream(File(Environment.getExternalStorageDirectory().toString() + File.separator + data[position]))
                outputStream.write(pdfs[position])
                outputStream.close()
               showProgressDialog(false)
            } catch (e: IOException) {
                e.printStackTrace()
            }


        }
    }

    fun showProgressDialog(isShow: Boolean){
        val progressDialog = ProgressDialog(context)
        progressDialog.setTitle("SHOW PDFS")
        progressDialog.setMessage("Please wait...")
        progressDialog.setCancelable(false)
        if (isShow){
            progressDialog.show()
        }else{
            progressDialog.dismiss()
        }

    }







}