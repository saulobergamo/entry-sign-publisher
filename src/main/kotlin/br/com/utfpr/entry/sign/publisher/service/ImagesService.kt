package br.com.utfpr.entry.sign.publisher.service

import br.com.utfpr.entry.sign.publisher.model.EntrySignMessage
import br.com.utfpr.entry.sign.publisher.model.ImageReport
import br.com.utfpr.entry.sign.publisher.model.entity.EntrySign
import br.com.utfpr.entry.sign.publisher.publisher.EntrySignPublisher
import br.com.utfpr.entry.sign.publisher.repository.EntrySignRepository
import br.com.utfpr.entry.sign.publisher.repository.ImageRepository
import br.com.utfpr.entry.sign.publisher.util.SIXTY
import br.com.utfpr.entry.sign.publisher.util.THIRTY
import br.com.utfpr.entry.sign.publisher.util.readCsvToDoubleMatrix
import mu.KotlinLogging
import org.jblas.DoubleMatrix
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.util.*
import javax.imageio.ImageIO
import kotlin.math.roundToInt

@Service
class ImagesService(
    private val publisher: EntrySignPublisher,
    private val entrySignRepository: EntrySignRepository,
    private val imageRepository: ImageRepository
) {
    private val logger = KotlinLogging.logger {}

    fun getImages(imageId: String): ResponseEntity<Any>? {
        val mongoResponse = imageRepository.findByimageId(imageId)
        val size = if (mongoResponse?.signType == "true") SIXTY else THIRTY
        // Criar uma imagem BufferedImage
        val image = BufferedImage(size, size, BufferedImage.TYPE_BYTE_GRAY)

        // Preencher a imagem com os valores da matriz
        for (i in 0 until size) {
            for (j in 0 until size) {
                // Converter para escala de 0-255
                val pixelValue = ((mongoResponse?.image?.get(i)?.get(j) ?: 0).toFloat().roundToInt())
                val color = Color(pixelValue, pixelValue, pixelValue) // Criar uma cor grayscale
                image.setRGB(j, i, color.rgb) // Definir o valor do pixel na imagem
            }
        }
        // Converter a imagem em um array de bytes
        val baos = ByteArrayOutputStream()
        ImageIO.write(image, "png", baos)
        val imageBytes = baos.toByteArray()

        // Converter a imagem em base64
        val base64Image = Base64.getEncoder().encodeToString(imageBytes)

        val response = ImageReport(
            userName = mongoResponse?.userName,
            iterations = mongoResponse?.iterations,
            runTime = mongoResponse?.runTime,
            error = mongoResponse?.error,
            memory = mongoResponse?.memory,
            image = imageBytes
        )
        return ResponseEntity.ok(response)
//
//        val headers = org.springframework.http.HttpHeaders()
//        headers.contentType = MediaType.IMAGE_PNG
//        return ResponseEntity<Any>(mapOf("image" to image, "data" to response), headers, HttpStatus.OK)
    }

    fun processEntrySign(
        userName: String,
        csv: MultipartFile?
    ): String? {
        logger.info {
            "processSign: try to read entrySign received as .csv file"
        }

//        val id = UUID.randomUUID() // Gera um UUID aleatório
        val imageId = UUID.randomUUID().toString() // Converte o UUID para uma string

        val entrySign: DoubleMatrix?

        try {
            entrySign = readCsvToDoubleMatrix(csv)
            val signType = entrySign.getRows() > 30000
            prepareMessage(imageId, userName, entrySign.data, signType).also {
                entrySignRepository.save(it)
                publisher.sendEntrySign(EntrySignMessage(imageId, userName, signType))
            }
        } catch (e: Exception) {
            logger.error(e) { "processSign: unable to read .csv file" }
        }
        return imageId
    }
    private fun prepareMessage(
        idString: String,
        documentNumber: String,
        entrySignString: DoubleArray,
        signType: Boolean
    ) = EntrySign(
        imageId = idString,
        userName = documentNumber,
        signType = signType,
        entrySignDouble = entrySignString.asList()
    )
}
