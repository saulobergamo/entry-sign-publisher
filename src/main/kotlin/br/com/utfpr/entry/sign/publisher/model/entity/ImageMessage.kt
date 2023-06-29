package br.com.utfpr.entry.sign.publisher.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "image_rebuild")
data class ImageMessage(
    @Id
    var id: String? = null,
    @Indexed
    val clientId: String? = null,
    val iterations: Int? = null,
    val executionTime: Double? = null,
    val error: Double? = null,
    val memory: Double? = null,
    val image: List<List<Double>>? = null
)
