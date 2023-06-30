package br.com.utfpr.entry.sign.publisher.model

import java.time.LocalDateTime

class BaseExceptionBody(
    val timeStamp: LocalDateTime = LocalDateTime.now(),
    val status: Int? = null,
    val error: String? = null,
    val message: String? = null,
    val path: String? = null
)
