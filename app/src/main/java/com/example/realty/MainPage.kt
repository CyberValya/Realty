package com.example.realty

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_main_page.*

class MainPage : Fragment() {
    private val SIGN_IN_CODE: Int = 1
    private val APARTMENT_KEY = "Apartment"
    var listOfApartment = ArrayList<Apartment>()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == SIGN_IN_CODE){
            if(resultCode == AppCompatActivity.RESULT_OK){
                Toast.makeText(context, "Вы авторизованы!", Toast.LENGTH_SHORT).show()
                showAllApartments()
            }
            else{
                Toast.makeText(context, "Вы не авторизованы!", Toast.LENGTH_SHORT).show()
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
        return inflater.inflate(R.layout.fragment_main_page, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(FirebaseAuth.getInstance().currentUser == null){
            startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().setIsSmartLockEnabled(false).build(), SIGN_IN_CODE
            )
        }
        else{
            showAllApartments()
        }

        logout_btn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().setIsSmartLockEnabled(false).build(), SIGN_IN_CODE
            )
        }

        add_btn.setOnClickListener {
            it.findNavController().navigate(R.id.action_mainPage_to_addPage)
        }
    }
    private fun showAllApartments(){
        progressBar_main.visibility = View.VISIBLE
        recycler_view.layoutManager = LinearLayoutManager(context)
        val storage = FirebaseDatabase.getInstance().getReference(APARTMENT_KEY)
        listOfApartment = ArrayList<Apartment>()

        storage.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(data in snapshot.children){
                    val item: Apartment = data.getValue(Apartment::class.java) as Apartment
                    listOfApartment.add(item)
                }
                if(recycler_view != null)
                    recycler_view.adapter = RecyclerViewAdapter(listOfApartment)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
            }
        })
        progressBar_main.visibility = View.INVISIBLE
    }
}