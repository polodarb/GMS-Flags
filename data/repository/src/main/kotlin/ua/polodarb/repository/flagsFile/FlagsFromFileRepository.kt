package ua.polodarb.repository.flagsFile

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import ua.polodarb.repository.flagsFile.model.LoadedFlags
import ua.polodarb.repository.uiStates.UiStates

interface FlagsFromFileRepository {

    suspend fun read(uri: Uri): Flow<UiStates<LoadedFlags>>

    suspend fun write(flags: LoadedFlags, fileName: String)

}