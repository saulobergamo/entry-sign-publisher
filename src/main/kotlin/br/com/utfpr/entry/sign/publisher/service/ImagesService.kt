package br.com.utfpr.entry.sign.publisher.service

import br.com.utfpr.entry.sign.publisher.publisher.EntrySignPublisher
import br.com.utfpr.entry.sign.publisher.util.FILE_PATH
import br.com.utfpr.entry.sign.publisher.util.readCsvToDoubleMatrix
import mu.KotlinLogging
import org.jblas.DoubleMatrix
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ImagesService(
    private val publisher: EntrySignPublisher
) {
    private val logger = KotlinLogging.logger {}

    fun getImages(): String {
        return FILE_PATH
    }

    fun processSign(
        userName: String,
        csv: MultipartFile?
    ) {
        logger.info {
            "processSign: try to read entrySign received as .csv file"
        }
        var entrySign: DoubleMatrix? = null
        try {
            entrySign = readCsvToDoubleMatrix(csv)
        } catch (e: Exception) {
            logger.error(e) { "processSign: unable to read .csv file" }
        }

        publisher.sendEntrySign(entrySign, userName)
    }
}
