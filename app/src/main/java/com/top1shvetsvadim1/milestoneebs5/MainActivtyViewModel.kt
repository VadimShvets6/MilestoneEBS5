package com.top1shvetsvadim1.milestoneebs5

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.top1shvetsvadim1.coreui.RemoteManager
import com.top1shvetsvadim1.coreui.RemoteManager.KEY_BUTTON_COLOR
import com.top1shvetsvadim1.coreui.RemoteManager.KEY_TEXT
import com.top1shvetsvadim1.coreutils.launchIO
import com.top1shvetsvadim1.domain.models.Parameters
import com.top1shvetsvadim1.domain.useCase.SaveToDatastoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val saveToDatastoreUseCase: SaveToDatastoreUseCase,
) : ViewModel() {

    fun save(param: Parameters) {
        viewModelScope.launchIO {
            val data = RemoteManager.fetch()
            val text = data[KEY_TEXT]?.asString()
            val color = data[KEY_BUTTON_COLOR]?.asString()
            Log.d("Remote", "RemoteColor: $color")
            text?.let {
                saveToDatastoreUseCase(param, it) //KEY/VALUE, //ENUM/VALUE, //REPO instead of UseCase
            }
            color?.let {
                saveToDatastoreUseCase(param, it)
            }
        }
    }
}