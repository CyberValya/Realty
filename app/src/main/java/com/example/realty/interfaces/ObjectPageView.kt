package com.example.realty.interfaces

import com.example.realty.models.Apartment
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface ObjectPageView : MvpView {
    fun setInformation(item: Apartment)
}