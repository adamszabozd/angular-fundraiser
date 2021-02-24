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

import hu.progmasters.fundraiser.dto.account.AccountRegistrationCommand;
import hu.progmasters.fundraiser.service.SharedValidationService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AccountRegistrationCommandValidator implements Validator {

    private final SharedValidationService validationService;

    public AccountRegistrationCommandValidator(SharedValidationService validationService) {
        this.validationService = validationService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return AccountRegistrationCommand.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AccountRegistrationCommand registration = (AccountRegistrationCommand) target;
        //TODO - Review: A validátorokban rengeteg a kódismétlés, illetve a SonarLint is nagyon nyafog érte
        // Ezeket a feltétel kezeléseket gyönyörűen ki lehet emelni metódusba
        // ÉS be lehet húzni őket egy közös absztrakt osztályba ( ha gondoljátok megmutatom mire gondolok )
        if (registration.getEmail() == null || registration.getEmail().isEmpty()) {
            errors.rejectValue("email", "email.notEmpty");
        }
        if(registration.getEmail() != null && validationService.emailAlreadyExists(registration.getEmail())){
            errors.rejectValue("email", "email.already.exists");
        }
        if (registration.getUsername() == null || registration.getUsername().isEmpty()) {
            errors.rejectValue("username","username.notEmpty");
        }
        if (registration.getUsername() != null && registration.getUsername().length() < 4) {
            errors.rejectValue("username","username.TooShort");
        }
        if (registration.getUsername() != null && registration.getUsername().length() > 20) {
            errors.rejectValue("username","username.TooLong");
        }
        if(validationService.usernameAlreadyExists(registration.getUsername())){
            errors.rejectValue("username", "username.already.exists");
        }
        if (registration.getPassword() == null || registration.getPassword().isEmpty()) {
            errors.rejectValue("password", "password.notEmpty");
        }
        if (registration.getPassword() != null && registration.getPassword().length() < 5) {
            errors.rejectValue("password","password.TooShort");
        }
        if (registration.getPassword() != null && registration.getPassword().length() > 100) {
            errors.rejectValue("password","password.TooLong");
        }
        if (!validationService.validateCaptcha(registration.getCaptcha())) {
            errors.rejectValue("captcha", "captcha.missing");
        }
    }
}
