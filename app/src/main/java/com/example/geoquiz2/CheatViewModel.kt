package com.example.geoquiz2

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

const val ANSWER_SHOWN = "com.example.geoquiz2.answer_shown"

class CheatViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    var isAnswerShown : Boolean
        get() = savedStateHandle[ANSWER_SHOWN] ?: false
        set(value) = savedStateHandle.set(ANSWER_SHOWN, value)
}