package com.example.realty.interfaces


interface MainFunctions {
    val APARTMENT_KEY : String get() = "Apartment"
    val PICK_IMAGE_CODE: Int get() = 1001
    val SIGN_IN_CODE: Int get() = 1
    val API_KEY : String get() = "61f81a240d16168f528341490297018f"
    val argumentName : String get() = "argumentId"
//    api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}
    val city : String get() = "Tyumen"

    fun checkingEdits(rooms: String, square: String, floor: String, price: String): Boolean{
        val roomsCheck = rooms.toIntOrNull()
        val squareCheck = square.toDoubleOrNull()
        val floorCheck = floor.toIntOrNull()
        val priceCheck = price.toIntOrNull()
        return roomsCheck != null && roomsCheck > 0 && squareCheck != null && squareCheck > 0 &&
                floorCheck != null && floorCheck > 0 && priceCheck != null && priceCheck > 0
    }
    fun getNicePrice(_price: String, spacing: Int): String{
        val reversedPrice = _price.reversed()
        var result = ""
        for(counter in reversedPrice.indices){
            result += reversedPrice[counter]
            if((counter + 1) % spacing == 0 && counter != reversedPrice.length - 1)
                result += " "
        }
        return result.reversed() //output example:2 340 653 (a space is inserted after every 'spacing' letter)
    }
}