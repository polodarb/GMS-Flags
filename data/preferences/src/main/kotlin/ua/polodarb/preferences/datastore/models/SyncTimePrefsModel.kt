package ua.polodarb.preferences.datastore.models

import java.util.concurrent.TimeUnit

data class SyncTimePrefsModel(val value: Long, val unit: TimeUnit)
