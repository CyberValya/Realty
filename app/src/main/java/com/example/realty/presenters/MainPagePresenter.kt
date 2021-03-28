package com.example.realty.presenters

import com.example.realty.fragments.MainPage
import com.example.realty.interfaces.MainPageView
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class MainPagePresenter : MvpPresenter<MainPageView>() {
    init {
        viewState.getWeather()
    }
}