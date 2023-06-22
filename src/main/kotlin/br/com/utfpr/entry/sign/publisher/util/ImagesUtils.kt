package br.com.utfpr.entry.sign.publisher.util

import org.apache.commons.math3.linear.ArrayRealVector
import org.apache.commons.math3.linear.RealVector
import org.jblas.DoubleMatrix
import org.springframework.web.multipart.MultipartFile
import java.io.InputStreamReader
fun readCsvToDoubleMatrix(csv: MultipartFile): DoubleMatrix {
    val reader = InputStreamReader(csv.inputStream)
    val lines = reader.readLines()
    val realVector = lines.map { it.toDouble() }.toDoubleArray()
    return DoubleMatrix(realVector)
}
fun readCsvToRealVector(csv: MultipartFile): RealVector {
    val reader = InputStreamReader(csv.inputStream)
    val lines = reader.readLines()
    val realVector = lines.map { it.toDouble() }.toDoubleArray()
    return ArrayRealVector(realVector)
}

const val FILE_PATH = "src/main/resources/"
