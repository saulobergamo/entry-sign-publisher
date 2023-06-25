package br.com.utfpr.entry.sign.publisher.model

import org.jblas.DoubleMatrix

data class EntrySignMessage(
    val documentNumber: String? = null,
    val entrySignDoubleMatrix: DoubleMatrix? = null
)
