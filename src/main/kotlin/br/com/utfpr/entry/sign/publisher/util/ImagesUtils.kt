package br.com.utfpr.entry.sign.publisher.util

import org.jblas.DoubleMatrix
import org.springframework.web.multipart.MultipartFile
import java.io.InputStreamReader

fun readCsvToDoubleMatrix(csv: MultipartFile?): DoubleMatrix {
    val reader = csv?.inputStream?.let { InputStreamReader(it) }
    val lines = reader?.readLines()
    val realVector = lines?.map { it.toDouble() }?.toDoubleArray()
    return DoubleMatrix(realVector)
}

const val FILE_PATH = "src/main/resources/"
