package br.com.utfpr.entry.sign.publisher.model

data class EntrySignMessage(
    val clientId: String? = null,
    val documentNumber: String? = null,
    val entrySignDouble: List<Double>? = null
)
