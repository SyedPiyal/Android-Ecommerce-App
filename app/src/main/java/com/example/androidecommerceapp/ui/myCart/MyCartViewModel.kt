package com.example.androidecommerceapp.ui.myCart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyCartViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "$550"
    }
    val text: LiveData<String> = _text
}