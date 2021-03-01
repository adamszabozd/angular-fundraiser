/*
 * Copyright © Progmasters (QTC Kft.), 2018.
 * All rights reserved. No part or the whole of this Teaching Material (TM) may be reproduced, copied, distributed,
 * publicly performed, disseminated to the public, adapted or transmitted in any form or by any means, including
 * photocopying, recording, or other electronic or mechanical methods, without the prior written permission of QTC Kft.
 * This TM may only be used for the purposes of teaching exclusively by QTC Kft. and studying exclusively by QTC Kft.’s
 * students and for no other purposes by any parties other than QTC Kft.
 * This TM shall be kept confidential and shall not be made public or made available or disclosed to any unauthorized person.
 * Any dispute or claim arising out of the breach of these provisions shall be governed by and construed in accordance with the laws of Hungary.
 */

package hu.progmasters.fundraiser.exception;

import com.fasterxml.jackson.core.JsonParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    @Autowired
    public GlobalExceptionHandler(MessageSource messageSource, LocaleResolver localeResolver) {
        this.messageSource = messageSource;
        this.localeResolver = localeResolver;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ValidationError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        logger.error("A validation error occurred: ", ex);
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return new ResponseEntity<>(processFieldErrors(fieldErrors, localeResolver.resolveLocale(request)), HttpStatus.BAD_REQUEST);
    }

    private ValidationError processFieldErrors(List<FieldError> fieldErrors, Locale locale) {
        ValidationError validationError = new ValidationError();

        for (FieldError fieldError: fieldErrors) {
            validationError.addFieldError(fieldError.getField(), messageSource.getMessage(fieldError, locale));
        }

        return validationError;
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<ApiError> handleJsonParseException(JsonParseException ex, HttpServletRequest request) {
        logger.error("Request JSON could no be parsed: ", ex);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Locale locale = localeResolver.resolveLocale(request);
        ApiError body = new ApiError("JSON_PARSE_ERROR",messageSource.getMessage("json.parse.error", null, locale), ex.getLocalizedMessage());
        return new ResponseEntity<>(body, status);

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        logger.error("Illegal argument error: ", ex);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Locale locale = localeResolver.resolveLocale(request);
        ApiError body = new ApiError("ILLEGAL_ARGUMENT_ERROR", messageSource.getMessage("illegal.argument.error", null, locale), ex.getLocalizedMessage());
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiError> defaultErrorHandler(Throwable t, HttpServletRequest request) {
        logger.error("An unexpected error occurred: ", t);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Locale locale = localeResolver.resolveLocale(request);
        ApiError body = new ApiError("UNCLASSIFIED_ERROR", messageSource.getMessage("unclassified.error", null, locale), t.getLocalizedMessage());
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(CustomRuntimeException.class)
    public ResponseEntity<ApiError> customRuntimeExceptionHandler(CustomRuntimeException e, HttpServletRequest request) {
        logger.error(e.getMessage() + " --- User: " + e.getAccountEmail());
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Locale locale = localeResolver.resolveLocale(request);
        ApiError body = new ApiError(e.getCode().toUpperCase().replace('.', '_'), messageSource.getMessage(e.getCode(), null, locale), e.getLocalizedMessage());
        return new ResponseEntity<>(body, status);
    }

}
