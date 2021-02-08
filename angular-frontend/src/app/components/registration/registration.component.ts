import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {Router} from '@angular/router';

import {AccountService} from '../../services/account.service';
import {RegistrationService} from '../../services/registration.service';
import {validationHandler} from '../../utils/validationHandler';

@Component({
    selector: 'app-registration',
    templateUrl: './registration.component.html',
    styleUrls: ['./registration.component.css'],
})
export class RegistrationComponent implements OnInit {

    form = this.formBuilder.group({
        email: ['', [Validators.required, Validators.email]],
        username: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(20)]],
        password: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(100)]],
    });

    constructor(private formBuilder: FormBuilder,
                private router: Router,
                private accountService: AccountService,
                private registrationService: RegistrationService) {
    }

    ngOnInit() {
        this.accountService.isLoggedIn().subscribe(
            data => {
                if (data) {
                    this.router.navigate(['/my-account']);
                }
            }
        );
    }

    register() {
        console.log('registering', this.form.value);
        this.accountService.registerNewAccount(this.form.value).subscribe(
            () => {
                this.registrationService.userRegistered.next();
                this.router.navigate(['/login']);
            },
            error => validationHandler(error, this.form),
        );
    }

}
