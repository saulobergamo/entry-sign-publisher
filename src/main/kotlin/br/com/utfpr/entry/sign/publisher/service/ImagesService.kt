package br.com.utfpr.entry.sign.publisher.service

import br.com.utfpr.entry.sign.publisher.model.EntrySignMessage
import br.com.utfpr.entry.sign.publisher.publisher.EntrySignPublisher
import br.com.utfpr.entry.sign.publisher.util.FILE_PATH
import br.com.utfpr.entry.sign.publisher.util.readCsvToDoubleMatrix
import mu.KotlinLogging
import org.jblas.DoubleMatrix
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class ImagesService(
    private val publisher: EntrySignPublisher
) {
    private val logger = KotlinLogging.logger {}

    fun getImages(): String {
        return FILE_PATH
    }

    fun processSign(
        documentNumber: String,
        csv: MultipartFile?
    ) {
        logger.info {
            "processSign: try to read entrySign received as .csv file"
        }

        val id = UUID.randomUUID() // Gera um UUID aleatório
        val idString = id.toString() // Converte o UUID para uma string

        val entrySign: DoubleMatrix?
        try {
            entrySign = readCsvToDoubleMatrix(csv)
            prepareMessage(idString, documentNumber, entrySign.data).also {
                publisher.sendEntrySign(it)
            }
        } catch (e: Exception) {
            logger.error(e) { "processSign: unable to read .csv file" }
        }
    }
    private fun prepareMessage(
        idString: String,
        documentNumber: String,
        entrySignString: DoubleArray
    ) = EntrySignMessage(
        clientId = idString,
        documentNumber = documentNumber,
        entrySignDouble = entrySignString.asList()
    )
}
