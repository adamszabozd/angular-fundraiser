import {Component, OnInit} from '@angular/core';
import {AccountService} from "../../services/account.service";
import {Router} from "@angular/router";

@Component({
               selector   : 'app-navbar',
               templateUrl: './navbar.component.html',
               styleUrls  : ['./navbar.component.css'],
           })
export class NavbarComponent implements OnInit {
    loggedIn = false;

    constructor(private accountService: AccountService, private router: Router) {
        this.accountService.loggedInStatusUpdate.subscribe(
            status => this.loggedIn = status
        );
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
