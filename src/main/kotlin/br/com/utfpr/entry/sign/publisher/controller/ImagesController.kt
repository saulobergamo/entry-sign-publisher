package br.com.utfpr.entry.sign.publisher.controller

import br.com.utfpr.entry.sign.publisher.service.ImagesService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import mu.KotlinLogging
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
        @RequestParam(name = "userName", required = false) userName: String,
        @RequestParam @Valid @NotBlank
        imageId: String
    ): ResponseEntity<Any>? {
        logger.info { "getImages: getting images for user=$userName" }
        return imagesService.getImages(imageId).also {
            logger.info { "getImages: success getting images for user=$userName" }
        }
    }

    @PostMapping("/upload")
    @Operation(summary = "post csv document")
    fun postSignCSV(
        @RequestParam(name = "userName", required = false) userName: String,
        @RequestParam @Valid
        csv: MultipartFile? = null
    ): String? {
        logger.info {
            "postSignCSV: processing file uploaded by user=$userName\""
        }
        return imagesService.processEntrySign(userName, csv).also {
            logger.info {
                "processSign: file uploaded from user=$userName"
            }
        }
    }
}
