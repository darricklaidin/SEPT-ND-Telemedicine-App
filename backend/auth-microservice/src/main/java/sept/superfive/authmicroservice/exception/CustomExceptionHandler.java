package sept.superfive.authmicroservice.exception;

import sept.superfive.authmicroservice.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ApiResponse> handleAllExceptions(Exception ex, WebRequest request) {
        ex.printStackTrace();
        return handleException(ex, "Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<ApiResponse> handleIllegalArgumentException(IllegalArgumentException ex,
            WebRequest request) {
        return handleException(ex, "Illegal Argument Exception", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ApiResponse> handleRecordNotFoundException(ResourceNotFoundException ex,
            WebRequest request) {
        return handleException(ex, "Resource Not Found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public final ResponseEntity<ApiResponse> handleIDAlreadyExistsException(ResourceAlreadyExistsException ex,
            WebRequest request) {
        return handleException(ex, "Resource Already Exists", HttpStatus.CONFLICT);
    }

    private final ResponseEntity<ApiResponse> handleException(Exception ex, String msg, HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus).body(new ApiResponse(false, msg, ex.getLocalizedMessage()));
    }
}
