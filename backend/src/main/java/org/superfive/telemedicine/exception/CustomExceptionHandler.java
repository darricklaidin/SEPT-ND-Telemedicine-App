package org.superfive.telemedicine.exception;

import org.superfive.telemedicine.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        return handleException(ex, "Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<Object> handleRecordNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return handleException(ex, "Invalid Time Range", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidTimeException.class)
    public final ResponseEntity<Object> handleInvalidTimeException(InvalidTimeException ex, WebRequest request) {
        return handleException(ex, "Invalid Time Range", HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public final ResponseEntity<Object> handleIDAlreadyExistsException(ResourceAlreadyExistsException ex,
                                                                       WebRequest request) {
        return handleException(ex, "Record Already Exists", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityTimeClashException.class)
    public final ResponseEntity<Object> handleEntityTimeClashException(EntityTimeClashException ex, WebRequest request) {
        return handleException(ex, "Time Clash", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DoctorUnavailableException.class)
    public final ResponseEntity<Object> handleDoctorUnavailableException(DoctorUnavailableException ex,
                                                                         WebRequest request) {
        return handleException(ex, "Doctor Unavailable", HttpStatus.BAD_REQUEST);
    }

    private final ResponseEntity<Object> handleException(Exception ex, String msg, HttpStatus httpStatus) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse(msg, details);
        return new ResponseEntity<>(error, httpStatus);
    }
}
