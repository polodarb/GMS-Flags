package ua.polodarb.gmsflags.ui.screens.suggestionsScreen

data class SuggestedFlag(
    var flagName: String,
    var flagSender: String,
    var phenotypeFlagName: String,
    var phenotypePackageName: String,
    var flagValue: Boolean
)

object SuggestedFlagsList {
    val suggestedFlagsList = mutableListOf<SuggestedFlag>(
        SuggestedFlag(
            "Enable transparent statusBar in dialer",
            "Nail Sadykov",
            "45372787",
            "com.google.android.dialer.directboot",
            false
        ),
        SuggestedFlag(
            "Enable docs scanner in Drive",
            "Nail Sadykov",
            "MlkitScanningUiFeature__enable_mlkit_scanning_ui",
            "com.google.apps.drive.android#com.google.android.apps.docs",
            false
        ),
        SuggestedFlag(
            "Enable MD3 style in Android Auto",
            "Nail Sadykov",
            "SystemUi__material_you_settings_enabled",
            "com.google.android.projection.gearhead",
            false
        )
    )
}