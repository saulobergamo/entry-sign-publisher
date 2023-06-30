package br.com.utfpr.entry.sign.publisher.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "image_rebuild")
data class ImageMessage(
    @Id
    var id: String? = null,
    @Indexed
    val imageId: String? = null,
    val userName: String? = null,
    val iterations: Int? = null,
    val runTime: Double? = null,
    val error: Double? = null,
    val memory: Double? = null,
    val signType: String? = null,
    val algorithm: String? = null,
    val cpu: Double? = null,
    val image: List<List<Double>>? = null
)
