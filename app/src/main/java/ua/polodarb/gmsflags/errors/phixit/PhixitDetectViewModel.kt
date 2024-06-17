package ua.polodarb.gmsflags.errors.phixit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ua.polodarb.preferences.datastore.DatastoreManager

class PhixitDetectViewModel(
    private val datastore: DatastoreManager
): ViewModel() {

    fun setOpenGmsSettingsBtnClicked(value: Boolean) {
        viewModelScope.launch {
            datastore.setOpenGmsSettingsBtnClicked(value)
        }
    }

}