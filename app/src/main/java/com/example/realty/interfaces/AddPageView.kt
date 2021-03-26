package com.example.realty.interfaces

import com.google.firebase.database.DatabaseReference
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface AddPageView : MvpView {
    fun putInStorage()
    fun getApartment(database : DatabaseReference)
}