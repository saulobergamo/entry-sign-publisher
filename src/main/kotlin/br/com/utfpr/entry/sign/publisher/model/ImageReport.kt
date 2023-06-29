package br.com.utfpr.entry.sign.publisher.model

data class ImageReport(
    val userName: String? = null,
    val imageId: String? = null,
    val iterations: Int? = null,
    val runTime: Double? = null,
    val error: Double? = null,
    val memory: Double? = null,
    val signType: String? = null,
    val image: ByteArray? = null
)
