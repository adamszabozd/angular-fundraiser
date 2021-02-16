import {Component, OnInit} from '@angular/core';
import {AccountService} from "../../services/account.service";
import {Router} from "@angular/router";
import {slideRight} from '../../animations';
import {TranslateService} from "@ngx-translate/core";

@Component({
               selector   : 'app-navbar',
               templateUrl: './navbar.component.html',
               styleUrls  : ['./navbar.component.css'],
               animations: [
                   slideRight
               ]
           })
export class NavbarComponent implements OnInit {
    slideInState = 'in';
    loggedIn = false;

    constructor(private accountService: AccountService, private router: Router, public translate: TranslateService) {
        this.accountService.loggedInStatusUpdate.subscribe(
            status => this.loggedIn = status
        );
        translate.addLangs(['EN', 'HU']);
        translate.setDefaultLang('EN');
        const browserLang = translate.getBrowserLang();
        translate.use(browserLang.match(/hu|hu-HU/) ? 'HU' : 'EN');
    }

    ngOnInit() {
        this.accountService.isLoggedIn().subscribe(
            data => this.loggedIn = data
        );
    }

    logout() {
        this.accountService.logout().subscribe(() => {
            this.accountService.loggedInStatusUpdate.next(false);
            this.router.navigate(['']);
        });
    }

}
