package br.com.utfpr.entry.sign.publisher.model

data class EntrySignMessage(
    val documentNumber: String? = null,
    val entrySignAsByteArray: String? = null
)
