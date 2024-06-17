package ua.polodarb.preferences.datastore

import kotlinx.coroutines.flow.Flow

interface DatastoreManager {

   val isOpenGmsSettingsBtnClicked: Flow<Boolean>

   val phixitWorkerExecutionCount: Flow<Int>

   fun setOpenGmsSettingsBtnClicked(value: Boolean)

   suspend fun phixitWorkerIncrementExecutionCount()

}