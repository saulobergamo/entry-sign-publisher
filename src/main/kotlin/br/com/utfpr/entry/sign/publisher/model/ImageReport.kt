package br.com.utfpr.entry.sign.publisher.model

data class ImageReport(
    val clientId: String? = null,
    val iterations: Int? = null,
    val executionTime: Double? = null,
    val error: Double? = null,
    val memory: Double? = null,
    val image: ByteArray? = null
)
