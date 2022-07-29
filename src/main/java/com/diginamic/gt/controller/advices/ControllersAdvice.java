package com.diginamic.gt.controller.advices;

import com.diginamic.gt.dto.ErrorDTO;
import com.diginamic.gt.exceptions.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ControllersAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BadRequestException.class)
    public @ResponseBody ErrorDTO handler(BadRequestException e) {
        //e.printStackTrace(); // for the log
        log.error("une erreur est survenue", e);
        return new ErrorDTO(e.getCode(), e.getMessage());
    }
}
