package br.com.utfpr.entry.sign.publisher.model

data class ImageReport(
    val imageId: String? = null,
    val userName: String? = null,
    val iterations: Int? = null,
    val runTime: Double? = null,
    val error: Double? = null,
    val memory: Double? = null,
    val signType: String? = null,
    val algorithm: String? = null,
    val cpuUsage: Double? = null,
    val imagePath: String? = null,
    val image: ByteArray? = null
)
