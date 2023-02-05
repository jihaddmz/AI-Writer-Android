package com.jihad.aiwriter.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jihad.aiwriter.helpers.HelperSharedPreference
import com.jihad.aiwriter.helpers.Helpers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ViewModelSettings : ViewModel() {

    val nbOfGenerationsLeft: MutableStateFlow<Int> = MutableStateFlow(
        HelperSharedPreference.getInt(HelperSharedPreference.SP_SETTINGS, HelperSharedPreference.SP_SETTINGS_NB_OF_GENERATIONS_LEFT, 3)
    )
    var showDialogNbOfGenerationsLeftExceeded: MutableState<Boolean> = mutableStateOf(false)

    fun decrementNbOfGenerationsLeft(){
        viewModelScope.launch {
            nbOfGenerationsLeft.emit(nbOfGenerationsLeft.value - 1)
        }
    }

    fun changeNbOfGenerationsLeft(value: Int){
        viewModelScope.launch {
            nbOfGenerationsLeft.emit(value)
        }
    }

    fun changeShowDialogNbOfGenerationsLeftExceeded(value: Boolean){
        viewModelScope.launch {
            Helpers.logD("${value} output")
            showDialogNbOfGenerationsLeftExceeded = mutableStateOf(value)
        }
    }
}