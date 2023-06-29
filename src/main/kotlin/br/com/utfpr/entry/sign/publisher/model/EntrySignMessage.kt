package br.com.utfpr.entry.sign.publisher.model

data class EntrySignMessage(
    val imageId: String? = null,
    val userName: String? = null,
    val signType: Boolean? = false
)
