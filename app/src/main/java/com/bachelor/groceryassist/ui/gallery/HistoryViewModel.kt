package com.bachelor.groceryassist.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HistoryViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Здесь будет список Ваших продуктов"
    }
    val text: LiveData<String> = _text
}