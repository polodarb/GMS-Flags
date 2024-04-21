package ua.polodarb.repository.impl.flagsFile

import android.content.Context
import android.net.Uri
import android.util.Log
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import kotlinx.coroutines.flow.flow
import ua.polodarb.repository.flagsFile.FlagsFromFileRepository
import ua.polodarb.repository.flagsFile.model.LoadedFlagData
import ua.polodarb.repository.flagsFile.model.LoadedFlags
import ua.polodarb.repository.flagsFile.model.PackageFlags
import ua.polodarb.repository.uiStates.UiStates

class FlagsFromFileRepositoryImpl(
    private val context: Context
) : FlagsFromFileRepository {

    override suspend fun read(uri: Uri) = flow<UiStates<LoadedFlags>> {
        runCatching {
            val inputStream = context.contentResolver.openInputStream(uri)

            val xmlMapper = XmlMapper()
            val flags: PackageFlags = inputStream.use {
                xmlMapper.readValue(it, PackageFlags::class.java)
            }

            val boolMap = mutableMapOf<String, Boolean>()
            val intMap = mutableMapOf<String, Int>()
            val floatMap = mutableMapOf<String, Float>()
            val stringMap = mutableMapOf<String, String>()
            val extensionMap = mutableMapOf<String, String>()

            flags.flags.forEach { flag ->
                when (flag.type) {
                    "boolean" -> boolMap[flag.name] = flag.value.toBoolean()
                    "integer" -> intMap[flag.name] = flag.value.toInt()
                    "float" -> floatMap[flag.name] = flag.value.toFloat()
                    "string" -> stringMap[flag.name] = flag.value
                    "extVal" -> extensionMap[flag.name] = flag.value
                }
            }

            emit(
                UiStates.Success(
                    LoadedFlags(
                        packageName = flags.packageName,
                        flags = LoadedFlagData(boolMap, intMap, floatMap, stringMap, extensionMap)
                    )
                )
            )

        }.onFailure {
            emit(UiStates.Error(it.cause))
        }
    }


    override suspend fun write(flags: LoadedFlags, fileName: String) {
        TODO("Not yet implemented")
    }
}
