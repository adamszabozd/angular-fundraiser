import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {Router} from '@angular/router';

import {AccountService} from '../../services/account.service';
import {RegistrationService} from '../../services/registration.service';
import {validationHandler} from '../../utils/validationHandler';

@Component({
               selector   : 'app-registration',
               templateUrl: './login.component.html',
               styleUrls  : ['./login.component.css'],
           })
export class LoginComponent implements OnInit {

    form = this.formBuilder.group({
                                      email   : ['', [Validators.required, Validators.email]],
                                      password: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(100)]],
                                  });

    constructor(private formBuilder: FormBuilder,
                private router: Router,
                private accountService: AccountService,
                private registrationService: RegistrationService) {
    }

    ngOnInit() {
        if (localStorage.getItem('auth')) {
            this.router.navigate(['/my-account']);
        }
    }

    register() {
        console.log('registering', this.form.value);
        this.accountService.login(this.form.value).subscribe(
            () => {
                localStorage.setItem('auth', 'true');
                this.registrationService.userRegistered.next();
                this.router.navigate(['/my-account']);
            },
            error => validationHandler(error, this.form),
        );
    }

}
