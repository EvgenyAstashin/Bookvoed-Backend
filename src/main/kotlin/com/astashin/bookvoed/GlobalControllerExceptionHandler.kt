package com.astashin.bookvoed

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.lang.Exception
import javax.servlet.http.HttpServletResponse

@ControllerAdvice
class GlobalControllerExceptionHandler {

    @ExceptionHandler(Exception::class)
    fun handleConflict(response: HttpServletResponse, exception: Exception) {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error")
    }
}