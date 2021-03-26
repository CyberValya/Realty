package com.example.realty.adapters

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.realty.R
import com.example.realty.interfaces.MainFunctions
import com.example.realty.models.Apartment
import com.squareup.picasso.Picasso


class RecyclerViewAdapter(
    private val list: ArrayList<Apartment>
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(),
    MainFunctions {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var image: ImageView? = null
        var price: TextView? = null
        var address: TextView? = null
        var square: TextView? = null

        init {
            image = itemView.findViewById(R.id.image)
            price = itemView.findViewById(R.id.price)
            address = itemView.findViewById(R.id.address)
            square = itemView.findViewById(R.id.square)
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
        Picasso.get().load(list[position].photo).into(holder.image)
        holder.price?.text = getNicePrice(list[position].price.toString(), 3) + " руб."
        holder.address?.text = list[position].address
        holder.square?.text = String.format("%.2f", list[position].square) + " м²"
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(argumentName, list[position].id)
            it.findNavController().navigate(R.id.action_mainPage_to_objectPage, bundle)
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }
}