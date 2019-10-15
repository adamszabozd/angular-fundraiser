import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { AccountService } from '../../services/account.service';
import { validationHandler } from '../../utils/validationHandler';
import { RegistrationService } from '../../services/registration.service';

@Component({
    selector: 'app-registration',
    templateUrl: './registration.component.html',
    styleUrls: ['./registration.component.css'],
})
export class RegistrationComponent implements OnInit {

    form = this.formBuilder.group({
        username: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(30)]],
        goal: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(100)]],
    });

    constructor(private formBuilder: FormBuilder,
                private router: Router,
                private accountService: AccountService,
                private registrationService: RegistrationService) {}

    ngOnInit() {
        if (localStorage.getItem('auth')) {
            this.router.navigate(['/my-account']);
        }
    }

    register() {
        console.log('registering', this.form.value);
        this.accountService.registerNewAccount(this.form.value).subscribe(
            () => {
                localStorage.setItem('auth', 'true');
                this.registrationService.userRegistered.next();
                this.router.navigate(['/my-account']);
            },
            error => validationHandler(error, this.form),
        );
    }

}
