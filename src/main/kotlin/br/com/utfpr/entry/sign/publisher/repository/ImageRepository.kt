package br.com.utfpr.entry.sign.publisher.repository

import br.com.utfpr.entry.sign.publisher.model.entity.ImageMessage
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
@Document
interface ImageRepository : MongoRepository<ImageMessage, String> {
    fun findByimageId(imageId: String): ImageMessage?
}
