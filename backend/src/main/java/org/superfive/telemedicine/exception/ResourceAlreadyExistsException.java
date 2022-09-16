package org.superfive.telemedicine.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
public class ResourceAlreadyExistsException extends RuntimeException {
    private final Object fieldValue;

    public ResourceAlreadyExistsException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s with %s = '%s' already exists", resourceName, fieldName, fieldValue));
        this.fieldValue = fieldValue;
    }
}
