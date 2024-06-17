package ua.polodarb.gmsflags.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.first
import ua.polodarb.repository.databases.gms.GmsDBRepository

class MainActivityViewModel(
    private val gmsDBRepository: GmsDBRepository
) : ViewModel() {

    suspend fun isPhixitSchemaUsed(): Boolean =
        gmsDBRepository.isPhixitSchemaUsed().first()

}
