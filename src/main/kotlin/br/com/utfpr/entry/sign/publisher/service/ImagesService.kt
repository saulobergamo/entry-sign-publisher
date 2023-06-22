package br.com.utfpr.entry.sign.publisher.service

import br.com.utfpr.entry.sign.publisher.EntrySignPublisher
import br.com.utfpr.entry.sign.publisher.util.FILE_PATH
import br.com.utfpr.entry.sign.publisher.util.readCsvToDoubleMatrix
import br.com.utfpr.images.rebuild.by.sign.enums.ImageSize
import mu.KotlinLogging
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
        documentNumber: String,
        csv: MultipartFile,
        imageSize: ImageSize
    ) {
        val entrySign = readCsvToDoubleMatrix(csv)
        val entrySignString = entrySign.toString()
        publisher.sendEntrySign(entrySignString, documentNumber)
    }
}
