package br.com.utfpr.entry.sign.publisher.config

import br.com.utfpr.entry.sign.publisher.exception.NotFoundException
import br.com.utfpr.entry.sign.publisher.model.BaseExceptionBody
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException::class)
    fun notFoundExceptionHandler(
        ex: NotFoundException,
        request: WebRequest
    ): ResponseEntity<BaseExceptionBody> {
        val baseExceptionBody = BaseExceptionBody(
            error = HttpStatus.NOT_FOUND.reasonPhrase,
            message = ex.localizedMessage,
            status = HttpStatus.NOT_FOUND.value(),
            path = request.contextPath
        )
        return ResponseEntity(baseExceptionBody, HttpHeaders(), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(value = [Exception::class])
    fun handleException(ex: Exception, request: WebRequest): ResponseEntity<BaseExceptionBody> {
        val baseExceptionBody = BaseExceptionBody(
            error = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
            message = ex.localizedMessage,
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            path = request.contextPath
        )
        return ResponseEntity(baseExceptionBody, HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
