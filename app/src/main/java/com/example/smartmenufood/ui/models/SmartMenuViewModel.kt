package com.example.smartmenufood.ui.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SmartMenuViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is SmartMenu Fragment"
    }
    val text: LiveData<String> = _text
}