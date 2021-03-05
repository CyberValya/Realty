package com.example.realty

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_object_page.*

class ObjectPage : Fragment(), MainFunctions {
    var id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_object_page, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        id = arguments?.getString(argumentName)
        val bundle = Bundle()
        bundle.putString(argumentName, id)

        val storage = FirebaseDatabase.getInstance().getReference(APARTMENT_KEY)
        storage.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(data in snapshot.children){
                    val item: Apartment = data.getValue(Apartment::class.java) as Apartment
                    if(item.id == id){
                        if(image_view != null && price_text != null && price_per_sm_text != null && address_text != null &&
                            rooms_text != null && square_text != null && floor_text != null){
                            Picasso.get().load(item.photo).into(image_view)
                            price_text.text = getNicePrice(item.price.toString(), 3) + " руб."
                            val pricePerSqM = item.price / item.square
                            price_per_sm_text.text = String.format("%.2f", pricePerSqM) +  " руб./м²"
                            address_text.text = item.address

                            rooms_text.text = item.rooms.toString()
                            square_text.text = String.format("%.2f", item.square) + " м²"
                            floor_text.text = item.floor.toString()

                            if(FirebaseAuth.getInstance().currentUser?.email == item.owner)
                                edit_btn.visibility = View.VISIBLE
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
            }
        })

        edit_btn.setOnClickListener{
            val bundle = Bundle()
            bundle.putString(argumentName, id)
            it.findNavController().navigate(R.id.action_objectPage_to_editPage, bundle)
        }
    }
}