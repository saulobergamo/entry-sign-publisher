package br.com.utfpr.entry.sign.publisher.controller

import br.com.utfpr.entry.sign.publisher.model.ImageReport
import br.com.utfpr.entry.sign.publisher.model.UploadResponse
import br.com.utfpr.entry.sign.publisher.service.ImagesService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import mu.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@Tag(name = "EntrySignPyblisher")
@Validated
@RestController
@RequestMapping("/images")
class ImagesController(private val imagesService: ImagesService) {

    private val logger = KotlinLogging.logger { }

    @GetMapping("/download")
    @Operation(summary = "get images by user documentNumber")
    fun getImages(
        @RequestParam @Valid @NotBlank
        imageId: String
    ): ResponseEntity<ImageReport?> {
        logger.info { "getImages: getting images - imageId=$imageId" }
        val responseHeader = HttpHeaders()
        responseHeader.set(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*")

        val response = imagesService.getImages(imageId)
        return ResponseEntity.ok().headers(responseHeader).body(response).also {
            logger.info { "getImages: success getting images for user=${it.body?.userName}" }
        }
    }

    @PostMapping("/upload")
    @Operation(summary = "post csv document")
    fun postSignCSV(
        @RequestParam(name = "userName", required = false) userName: String,
        @RequestParam @Valid
        csv: MultipartFile? = null
    ): ResponseEntity<UploadResponse> {
        logger.info {
            "postSignCSV: processing file uploaded by user=$userName\""
        }
        val responseHeader = HttpHeaders()
        responseHeader.set(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*")
        val response = imagesService.processEntrySign(userName, csv).also {
            logger.info {
                "processSign: file uploaded from user=$userName"
            }
        }
        return ResponseEntity.ok().headers(responseHeader).body(response)
    }
}
