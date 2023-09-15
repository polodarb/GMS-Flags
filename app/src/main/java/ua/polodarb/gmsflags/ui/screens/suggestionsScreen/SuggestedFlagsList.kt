package ua.polodarb.gmsflags.ui.screens.suggestionsScreen

data class SuggestedFlag(
    val flagName: String,
    val flagSender: String,
    val phenotypeFlagName: List<String>,
    val phenotypePackageName: String,
    val flagValue: Boolean
)

object SuggestedFlagsList {
    val suggestedFlagsList = mutableListOf<SuggestedFlag>(
        SuggestedFlag(
            "Proofreading mode in GBoard",
            "AssembleDebug",
            listOf("writing_helper", "writing_helper_enable_free_chat", "writing_helper_chip_shown_as_candidate"),
            "com.google.android.inputmethod.latin#com.google.android.inputmethod.latin",
            false
        ),
        SuggestedFlag(
            "\"Undo\" button in GBoard",
            "AssembleDebug",
            listOf("undo_access_point"),
            "com.google.android.inputmethod.latin#com.google.android.inputmethod.latin",
            false
        ),
        SuggestedFlag(
            "Enable transparent statusBar in dialer",
            "Nail Sadykov",
            listOf("45372787"),
            "com.google.android.dialer.directboot",
            false
        ),
        SuggestedFlag(
            "Enable docs scanner in Drive",
            "Nail Sadykov",
            listOf("MlkitScanningUiFeature__enable_mlkit_scanning_ui"),
            "com.google.apps.drive.android#com.google.android.apps.docs",
            false
        ),
        SuggestedFlag(
            "Enable MD3 style in Android Auto",
            "Nail Sadykov",
            listOf("SystemUi__material_you_settings_enabled"),
            "com.google.android.projection.gearhead",
            false
        )
    )
}