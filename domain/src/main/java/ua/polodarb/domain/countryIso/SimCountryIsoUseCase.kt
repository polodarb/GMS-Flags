package ua.polodarb.domain.countryIso

import android.content.Context
import android.telephony.TelephonyManager

class SimCountryIsoUseCase(private val context: Context) {

    operator fun invoke(): Result<String> {
        return try {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
            val simCountryIso = telephonyManager?.simCountryIso
            if (!simCountryIso.isNullOrBlank()) {
                Result.success(simCountryIso)
            } else {
                Result.failure(NullPointerException("SIM country ISO is null or blank"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
