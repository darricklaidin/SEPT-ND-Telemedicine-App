package org.superfive.telemedicine.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalTime;


@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
public class InvalidTimeException extends RuntimeException {

    private final LocalTime startTime;
    private final LocalTime endTime;

    public InvalidTimeException(LocalTime startTime, LocalTime endTime) {
        super(String.format("Invalid start time and end time: %s - %s", startTime, endTime));
        this.startTime = startTime;
        this.endTime = endTime;
    }
}

