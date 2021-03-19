package com.example.realty.models

import com.example.realty.models.Main
import com.google.gson.annotations.SerializedName

class Weather {
    @SerializedName("main")
    lateinit var main : Main


}