package com.example.realty

class Apartment {
    var id: String = ""
    var address: String = ""
    var rooms: Int = 0
    var square: Double = 0.00
    var floor: Int = 0
    var price: Double = 0.00
    var photo: String = ""
    var owner: String = ""

    constructor(_id: String, _addres: String, _rooms: Int, _square: Double, _floor: Int, _price: Double, _photo: String, _owner: String){
        id = _id
        address = _addres
        rooms = _rooms
        square = _square
        floor = _floor
        price = _price
        photo = _photo
        owner = _owner
    }
}