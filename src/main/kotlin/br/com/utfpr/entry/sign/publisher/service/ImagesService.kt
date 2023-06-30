package br.com.utfpr.entry.sign.publisher.service

import br.com.utfpr.entry.sign.publisher.exception.NotFoundException
import br.com.utfpr.entry.sign.publisher.model.EntrySignMessage
import br.com.utfpr.entry.sign.publisher.model.ImageReport
import br.com.utfpr.entry.sign.publisher.model.UploadResponse
import br.com.utfpr.entry.sign.publisher.model.entity.EntrySign
import br.com.utfpr.entry.sign.publisher.model.entity.ImageMessage
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
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
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

    fun getImages(imageId: String): ImageReport? {
        val mongoResponse: ImageMessage?
        try {
            mongoResponse = imageRepository.findByimageId(imageId)
            if (mongoResponse != null) {
                return buildImage(mongoResponse)
            } else throw NotFoundException("getImages: image not found for imageId=$imageId")
        } catch (e: NotFoundException) {
            logger.error(e) {
                "getImages: image not found for imageId=$imageId"
            }
            throw e
        } catch (e: Exception) {
            logger.error {
                "getImages: error getting image from database for imageId=$imageId"
            }
            throw e
        }
    }

    private fun buildImage(mongoResponse: ImageMessage): ImageReport {
        val size = if (mongoResponse.signType == "true") SIXTY else THIRTY
        // Criar uma imagem BufferedImage
        val image = BufferedImage(size, size, BufferedImage.TYPE_BYTE_GRAY)

        // Preencher a imagem com os valores da matriz
        for (i in 0 until size) {
            for (j in 0 until size) {
                // Converter para escala de 0-255
                val pixelValue = ((mongoResponse.image?.get(i)?.get(j) ?: 0).toFloat().roundToInt())
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

        return ImageReport(
            imageId = mongoResponse.imageId,
            userName = mongoResponse.userName,
            iterations = mongoResponse.iterations,
            runTime = mongoResponse.runTime,
            error = mongoResponse.error,
            memory = mongoResponse.memory,
            signType = mongoResponse.signType,
            algorithm = mongoResponse.algorithm,
            cpuUsage = mongoResponse.cpu,
            imagePath = byteArrayToJpg(imageBytes, "src/main/resources/${mongoResponse.imageId}.jpg"),
            image = imageBytes
        )
    }

    private fun byteArrayToJpg(byteArray: ByteArray, imagePath: String): String? {
        try {
            // Criar um arquivo temporário para armazenar os bytes do array
            val tempFilePath = Files.createTempFile("temp_image", ".jpg")

            // Gravar o byteArray no arquivo temporário
            FileOutputStream(tempFilePath.toFile()).use { stream ->
                stream.write(byteArray)
            }

            // Renomear o arquivo temporário com o caminho especificado
            val targetPath = Paths.get(imagePath)
            Files.move(tempFilePath, targetPath)

            // Retornar o caminho da imagem
            return targetPath.toString()
        } catch (e: IOException) {
            logger.error(e) {
                "byteArrayToJpg: error converting byteArray to image JPG for image+$imagePath"
            }
            // Lidar com exceções ou erros ao processar o arquivo
        }

        // Retornar null em caso de erro
        return null
    }

    fun processEntrySign(
        userName: String,
        csv: MultipartFile?
    ): UploadResponse {
        logger.info {
            "processSign: try to read entrySign received as .csv file"
        }
        val response = UploadResponse()
//        val id = UUID.randomUUID() // Gera um UUID aleatório
        var imageId = UUID.randomUUID().toString() // Converte o UUID para uma string
        while (entrySignRepository.findByImageId(imageId) != null) imageId = UUID.randomUUID().toString()

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
        response.imageId = imageId
        response.userName = userName
        return response
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
