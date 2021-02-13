import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';

import {AccountService} from '../../services/account.service';
import {RegistrationService} from '../../services/registration.service';
import {validationHandler} from '../../utils/validationHandler';
import {formAppearAnimation} from '../../animations';
import {AccountRegistrationDataModel} from '../../models/accountRegistrationData.model';

@Component({
               selector   : 'app-registration',
               templateUrl: './registration.component.html',
               styleUrls  : ['./registration.component.css'],
               animations : [
                   formAppearAnimation,
               ],
           })
export class RegistrationComponent implements OnInit {

    state = 'invisible';

    form: FormGroup;
    submitted = false;

    constructor(private formBuilder: FormBuilder,
                private router: Router,
                private accountService: AccountService,
                private registrationService: RegistrationService) {
        this.form = this.formBuilder.group({
                                               email   : ['', [Validators.required, Validators.email]],
                                               username: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(20)]],
                                               password: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(100)]],
                                               confirm : ['', [Validators.required, Validators.minLength(5), Validators.maxLength(100)]],
                                           }, {
                                               validator: mustMatch('password', 'confirm'),
                                           });
    }

    ngOnInit() {
        if (this.state == 'invisible') {
            setTimeout(() => this.state = 'visible');
        }
        this.accountService.isLoggedIn().subscribe(
            data => {
                if (data) {
                    this.router.navigate(['/my-account']);
                }
            },
        );
    }

    get f() {
        return this.form.controls;
    }

    register() {
        this.submitted = true;
        if (this.form.invalid) {
            return;
        }
        const data: AccountRegistrationDataModel = {
            email   : this.form.value.email,
            username: this.form.value.username,
            password: this.form.value.password,
        };
        this.accountService.registerNewAccount(data).subscribe(
            () => {
                this.registrationService.userRegistered.next();
                this.router.navigate(['/login']);
            },
            error => validationHandler(error, this.form),
        );
    }
}

export function mustMatch(password: string, confirmPassword: string) {
    return (formGroup: FormGroup) => {
        const control = formGroup.controls[password];
        const matchingControl = formGroup.controls[confirmPassword];

        if (matchingControl.errors && !matchingControl.errors.mustMatch) {
            return;
        }
        if (control.value !== matchingControl.value) {
            matchingControl.setErrors({mustMatch: true});
        } else {
            matchingControl.setErrors(null);
        }
    };
}
