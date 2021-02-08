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

package hu.progmasters.fundraiser.validation;

import hu.progmasters.fundraiser.dto.AccountRegistrationCommand;
import hu.progmasters.fundraiser.service.SharedValidationService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.servlet.http.HttpServletRequest;

@Component
public class AccountRegistrationCommandValidator implements Validator {

    private SharedValidationService validationService;

    private HttpServletRequest request;

    public AccountRegistrationCommandValidator(SharedValidationService validationService, HttpServletRequest request) {
        this.validationService = validationService;
        this.request = request;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return AccountRegistrationCommand.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AccountRegistrationCommand registration = (AccountRegistrationCommand) target;
        if(validationService.emailAlreadyExists(registration.getEmail())){
            errors.rejectValue("email", "email.already.exists");
        }
        if(validationService.usernameAlreadyExists(registration.getUsername())){
            errors.rejectValue("username", "username.already.exists");
        }
    }
}
