package com.example.realty

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_edit_page.*
import kotlinx.android.synthetic.main.fragment_edit_text_place.*

class EditPage : Fragment() {
    private val APARTMENT_KEY = "Apartment"
    private val argumentName = "argumentId"
    lateinit var storage: DatabaseReference
    var id: String? = null
    lateinit var apartment: Apartment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_page, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        id = arguments?.getString(argumentName)

        storage = FirebaseDatabase.getInstance().getReference(APARTMENT_KEY)
        storage.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    val item: Apartment = data.getValue(Apartment::class.java) as Apartment
                    if (item.id == id) {
                        apartment = item
                        price_edit.setText("" + item.price)
                        rooms_edit.setText("" + item.rooms)
                        square_edit.setText("" + item.square)
                        floor_edit.setText("" + item.floor)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
            }
        })

        save_btn.setOnClickListener {
            progressBar_edit.visibility = View.VISIBLE
            if(checkingEdits()){
                apartment.price = price_edit.text.toString().toDouble()
                apartment.rooms = rooms_edit.text.toString().toInt()
                apartment.square = square_edit.text.toString().toDouble()
                apartment.floor = floor_edit.text.toString().toInt()
                storage.child(id!!).setValue(apartment)
                Toast.makeText(
                    context,
                    "Изменения внесены",
                    Toast.LENGTH_SHORT
                ).show()
                it.findNavController().popBackStack()
            }
            else{
                Toast.makeText(context, "Проверьте корректность введенных данных", Toast.LENGTH_SHORT).show()
            }
            progressBar_edit.visibility = View.INVISIBLE
        }
    }
    private fun checkingEdits(): Boolean{
        val roomsCheck = rooms_edit.text.toString().toIntOrNull()
        val squareCheck = square_edit.text.toString().toDoubleOrNull()
        val floorCheck = floor_edit.text.toString().toIntOrNull()
        val priceCheck = price_edit.text.toString().toDoubleOrNull()
        return roomsCheck != null && roomsCheck > 0 && squareCheck != null && squareCheck > 0 &&
                floorCheck != null && floorCheck > 0 && priceCheck != null && priceCheck > 0
    }
}