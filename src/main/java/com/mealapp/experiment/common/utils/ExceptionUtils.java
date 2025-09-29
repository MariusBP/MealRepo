package com.mealapp.experiment.common.utils;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.function.Supplier;

@UtilityClass
public class ExceptionUtils {

    public static Supplier<ResponseStatusException> exception(HttpStatus httpStatus, String message) {
        return () -> new ResponseStatusException(httpStatus, message);
    }
}
