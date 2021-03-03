package com.example.realty

class Apartment (var id: String, var address: String, var rooms: Int, var square: Double,
                 var floor: Int, var price: Double, var photo: String, var owner: String) {
    constructor() : this("","",1,1.0,1,1.0,"","")
}