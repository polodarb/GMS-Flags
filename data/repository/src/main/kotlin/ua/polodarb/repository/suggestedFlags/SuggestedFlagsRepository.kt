package ua.polodarb.repository.suggestedFlags

import kotlinx.coroutines.flow.Flow
import ua.polodarb.repository.suggestedFlags.models.MergedAllTypesFlags
import ua.polodarb.repository.suggestedFlags.models.MergedAllTypesOverriddenFlags
import ua.polodarb.repository.suggestedFlags.models.SuggestedFlagsRepoModel
import ua.polodarb.repository.uiStates.UiStates

interface SuggestedFlagsRepository {

    suspend fun loadSuggestedFlags(): List<SuggestedFlagsRepoModel>?

    suspend fun getMergedOverriddenFlagsByPackage(pkg: String): Flow<MergedAllTypesOverriddenFlags>

    suspend fun getMergedAllFlags(): Flow<UiStates<MergedAllTypesFlags>>

}