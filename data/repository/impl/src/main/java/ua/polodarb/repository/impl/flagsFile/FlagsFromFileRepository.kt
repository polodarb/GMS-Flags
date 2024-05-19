package ua.polodarb.repository.impl.flagsFile

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.annotation.Keep
import androidx.documentfile.provider.DocumentFile
import com.ctc.wstx.api.WstxInputProperties
import com.ctc.wstx.api.WstxOutputProperties
import com.ctc.wstx.stax.WstxInputFactory
import com.ctc.wstx.stax.WstxOutputFactory
import com.fasterxml.jackson.dataformat.xml.XmlFactory
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator
import kotlinx.coroutines.flow.flow
import org.codehaus.stax2.XMLOutputFactory2
import ua.polodarb.repository.flagsFile.FlagsFromFileRepository
import ua.polodarb.repository.flagsFile.model.LoadedFlagData
import ua.polodarb.repository.flagsFile.model.LoadedFlags
import ua.polodarb.repository.flagsFile.model.PackageFlags
import ua.polodarb.repository.uiStates.UiStates
import java.io.File
import java.io.StringWriter
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLOutputFactory

@Keep
class FlagsFromFileRepositoryImpl(
    private val context: Context
) : FlagsFromFileRepository {

    override suspend fun read(uri: Uri) = flow<UiStates<LoadedFlags>> {
        runCatching {
            val inputStream = context.contentResolver.openInputStream(uri)

            val inputFactory: XMLInputFactory = WstxInputFactory()
            inputFactory.setProperty(WstxInputProperties.P_MAX_ATTRIBUTE_SIZE, 32000)

            val outputFactory: XMLOutputFactory = WstxOutputFactory()
            outputFactory.setProperty(WstxOutputProperties.P_OUTPUT_CDATA_AS_TEXT, true)

            val factory: XmlFactory = XmlFactory.builder()
                .xmlInputFactory(inputFactory)
                .xmlOutputFactory(outputFactory)
                .build()

            val xmlMapper = XmlMapper(factory)

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
            Log.e("FILE", "message: ${it.message}")
            Log.e("FILE", "stackTrace: ${it.stackTrace}")
            emit(UiStates.Error(it.cause))
        }
    }

    override suspend fun write(flags: LoadedFlags, fileName: String): Uri {
        return try {
            val xmlMapper = XmlMapper()
            xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true)

            val outputFactory = XMLOutputFactory2.newInstance()
            val stringWriter = StringWriter()
            val xmlStreamWriter = outputFactory.createXMLStreamWriter(stringWriter)

            with(xmlStreamWriter) {
                writeStartDocument()
                writeComment("GMS Flags")
                writeStartElement("package")
                writeAttribute("name", flags.packageName)
                writeStartElement("flags")

                flags.flags.getFlagsList().forEach { flag ->
                    writeStartElement("flag")
                    writeAttribute("name", flag.name)
                    writeAttribute("type", flag.type)
                    writeAttribute("value", flag.value.toString())
                    writeEndElement()
                }

                writeEndElement()
                writeEndElement()
                writeEndDocument()

                flush()
                close()
            }

            val file = File(context.filesDir, "${fileName}123.xml")
            file.writeText(stringWriter.toString())
            DocumentFile.fromFile(file).createFile("application/xml", fileName)

            Uri.fromFile(file)
        } catch (e: Exception) {
            Log.e("write", e.stackTraceToString())
            Uri.EMPTY
        }
    }
}
