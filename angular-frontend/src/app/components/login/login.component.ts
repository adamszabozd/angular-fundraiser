import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {Router} from '@angular/router';

import {AccountService} from '../../services/account.service';
import {RegistrationService} from '../../services/registration.service';
import {validationHandler} from '../../utils/validationHandler';
import {formAppearAnimation} from '../../animations';

@Component({
               selector   : 'app-registration',
               templateUrl: './login.component.html',
               styleUrls  : ['./login.component.css'],
               animations: [
                   formAppearAnimation
               ]
           })
export class LoginComponent implements OnInit{

    state = "invisible";

    form = this.formBuilder.group({
                                      email   : ['', [Validators.required]],
                                      password: ['', [Validators.required]],
                                  });

    constructor(private formBuilder: FormBuilder,
                private router: Router,
                private accountService: AccountService,
                private registrationService: RegistrationService) {
    }

    ngOnInit() {
        if (this.state == "invisible") {
            setTimeout(() => this.state = "visible")
        }
        this.accountService.isLoggedIn().subscribe(
            data => {
                if (data) {
                    this.router.navigate(['/my-account']);
                }
            }
        );
    }

    login() {
        this.accountService.login(this.form.value).subscribe(
            () => {
                this.registrationService.userRegistered.next();
                this.router.navigate(['/my-account']);
                this.accountService.loggedInStatusUpdate.next(true);
            },
            error => {
                error.error = {
                    fieldErrors: [
                        {
                            field: 'email',
                            message: 'Invalid email or password',
                        },
                    ],
                };
                validationHandler(error, this.form);
            }
        );
    }

}
