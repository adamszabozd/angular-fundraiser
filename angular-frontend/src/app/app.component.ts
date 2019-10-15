import { Component, OnInit } from '@angular/core';
import { RegistrationService } from './services/registration.service';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {

    shouldDisplayNavbar = false;

    constructor(private registrationService: RegistrationService) {}

    ngOnInit() {
        if (localStorage.getItem('auth')) {
            this.shouldDisplayNavbar = true;
        }

        this.registrationService.userRegistered.subscribe(
            () => this.shouldDisplayNavbar = true
        )
    }
}
