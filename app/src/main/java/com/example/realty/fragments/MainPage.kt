package com.example.realty.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.realty.R
import com.example.realty.adapters.RecyclerViewAdapter
import com.example.realty.interfaces.MainFunctions
import com.example.realty.interfaces.MainPageView
import com.example.realty.interfaces.WeatherAPI
import com.example.realty.models.Apartment
import com.example.realty.models.Main
import com.example.realty.models.Weather
import com.example.realty.presenters.MainPagePresenter
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_main_page.*
import moxy.MvpFragment
import moxy.presenter.InjectPresenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.roundToInt

class MainPage : Fragment(), MainFunctions, MainPageView {
    @InjectPresenter
    lateinit var mPresenter : MainPagePresenter
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_page, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getWeather()

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
    override fun showAllApartments(){
        progressBar_main.visibility = ProgressBar.VISIBLE
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
        progressBar_main.visibility = ProgressBar.INVISIBLE
    }

    override fun getWeather(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val weatherAPI = retrofit.create(WeatherAPI::class.java)
        val weather : Call<Weather> = weatherAPI.getWeather(city, API_KEY)
        weather.enqueue(object : Callback<Weather>{
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                if(response.code() == 404){
                    Toast.makeText(context, "Something goes wrong :(", Toast.LENGTH_SHORT).show()
                }
                else if(!response.isSuccessful){
                    Toast.makeText(context, "Something goes wrong :(", Toast.LENGTH_SHORT).show()
                }
                val data : Weather? = response.body()
                val main : Main = data!!.main
                val temp = (main.temp?.minus(273.15))?.roundToInt()
                Toast.makeText(context, "Погода в Тюмени сейчас: " + temp.toString() + "°C", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<Weather>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}