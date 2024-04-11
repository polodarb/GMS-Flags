package ua.polodarb.repository.suggestedFlags

import kotlinx.coroutines.flow.Flow
import ua.polodarb.repository.suggestedFlags.model.MergedAllTypesFlags
import ua.polodarb.repository.suggestedFlags.model.MergedAllTypesOverriddenFlags
import ua.polodarb.repository.uiStates.UiStates

interface MergedSuggestedFlagsRepository {

    suspend fun getMergedOverriddenFlagsByPackage(pkg: String): Flow<MergedAllTypesOverriddenFlags>

    suspend fun getMergedAllFlags(): Flow<UiStates<MergedAllTypesFlags>>


}