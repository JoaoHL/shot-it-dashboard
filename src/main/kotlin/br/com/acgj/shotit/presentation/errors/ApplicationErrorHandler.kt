package br.com.acgj.shotit.presentation.errors

import br.com.acgj.shotit.domain.RequestError
import br.com.acgj.shotit.domain.RequestErrorResponse
import br.com.acgj.shotit.domain.RequestFieldError
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApplicationErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    fun handleMethodArgumentNotValidException(exception: MethodArgumentNotValidException): ResponseEntity<RequestErrorResponse> {
        val fields = exception.fieldErrors.map { RequestFieldError(it.field, it.defaultMessage ?: "") }
        val globals = exception.globalErrors.map { RequestFieldError(it.code ?: "", it.defaultMessage?: "" ) }
        val errors = fields + globals

        return ResponseEntity
            .badRequest()
            .body(RequestErrorResponse("Validation Error", errors))
    }

    @ExceptionHandler
    fun handleRequestError(exception: RequestError): ResponseEntity<RequestErrorResponse> = ResponseEntity.status(exception.statusCode()).body(exception.error())
}