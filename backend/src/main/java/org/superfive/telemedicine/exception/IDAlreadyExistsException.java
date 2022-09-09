package org.superfive.telemedicine.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
public class IDAlreadyExistsException extends RuntimeException {
    private final int id;

    public IDAlreadyExistsException(int id) {
        super(String.format("A record with the given id '%s' already exists", id));
        this.id = id;
    }
}
