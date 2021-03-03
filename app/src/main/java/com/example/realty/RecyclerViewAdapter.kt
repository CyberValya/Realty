package com.example.realty

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class RecyclerViewAdapter (private val list: ArrayList<Apartment>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var image: ImageView? = null
        var price: TextView? = null
        var address: TextView? = null

        init {
            image = itemView.findViewById(R.id.image)
            price = itemView.findViewById(R.id.price)
            address = itemView.findViewById(R.id.address)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.apartment_card,
            null
        )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(list[position].photo + ".jpg").into(holder.image)
        holder.price?.text = list[position].price.toString()
        holder.address?.text = list[position].address
    }

    override fun getItemCount(): Int {
        return list.size
    }
}