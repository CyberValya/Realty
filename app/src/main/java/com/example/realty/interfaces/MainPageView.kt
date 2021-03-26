package com.example.realty.interfaces

import android.view.View
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType (value = AddToEndSingleStrategy::class)
interface MainPageView : MvpView{
    fun getWeather()
    fun showAllApartments()
}