package org.superfive.telemedicine.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
public class InvalidScheduleException extends RuntimeException {

    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;

    public InvalidScheduleException(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        super(String.format("Invalid start date and end date: %s - %s", startDateTime, endDateTime));
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }
}

