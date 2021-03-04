package com.example.realty

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.SuccessContinuation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_add_page.*
import kotlinx.android.synthetic.main.fragment_edit_text_place.*
import java.io.IOException
import java.util.*
import androidx.annotation.NonNull

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_add_page.add_btn
import kotlinx.android.synthetic.main.fragment_main_page.*


class AddPage : Fragment() {
    private val APARTMENT_KEY = "Apartment"
    private val PICK_IMAGE_CODE = 1001
    private var photo: String = ""
    private lateinit var filePath: Uri
    lateinit var apartment: Apartment

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val storage = FirebaseStorage.getInstance().reference
        if(requestCode == PICK_IMAGE_CODE && resultCode == RESULT_OK &&
                data != null && data.data != null){
            filePath = data.data!!
            try{
                val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, filePath)
                image_btn.setImageBitmap(bitmap)

                val storageReference = storage.child("images/" + UUID.randomUUID().toString())
                storageReference.putFile(filePath).addOnSuccessListener {
                    val result = it.metadata!!.reference!!.downloadUrl
                    result.addOnSuccessListener { res ->
                        photo = res.toString()
                    }
                }
            }
            catch (e: IOException){
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        image_btn.setOnClickListener {
            progressBar_add.visibility = View.VISIBLE
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_CODE)
            progressBar_add.visibility = View.INVISIBLE
        }

        val database = FirebaseDatabase.getInstance().getReference(APARTMENT_KEY)
        add_btn.setOnClickListener{
            if(checkingEdits()){
                if(!photo.isNullOrEmpty()){
                    val id = database.push().key
                    val address = address_edit.text.toString()
                    val rooms = rooms_edit.text.toString().toInt()
                    val square = square_edit.text.toString().toDouble()
                    val floor = floor_edit.text.toString().toInt()
                    val price = price_edit.text.toString().toDouble()
                    val owner = FirebaseAuth.getInstance().currentUser!!.email
                    apartment = Apartment(
                        id!!,
                        address,
                        rooms,
                        square,
                        floor,
                        price,
                        photo,
                        owner!!
                    )

                    database.child(id).setValue(apartment)
                    Toast.makeText(context, "Объект загружен", Toast.LENGTH_SHORT).show()
                    it.findNavController().popBackStack()
                    it.findNavController().navigate(R.id.objectPage)
                }
                else{
                    Toast.makeText(context, "Загрузите фотографию", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(
                    context,
                    "Проверьте корректность введенных данных",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }
    private fun checkingEdits(): Boolean{
        val roomsCheck = rooms_edit.text.toString().toIntOrNull()
        val squareCheck = square_edit.text.toString().toDoubleOrNull()
        val floorCheck = floor_edit.text.toString().toIntOrNull()
        val priceCheck = price_edit.text.toString().toDoubleOrNull()
        return !address_edit.text.toString().isNullOrEmpty() && roomsCheck != null && roomsCheck > 0 &&
                squareCheck != null && squareCheck > 0 && floorCheck != null && floorCheck > 0 &&
                priceCheck != null && priceCheck > 0
    }
}
