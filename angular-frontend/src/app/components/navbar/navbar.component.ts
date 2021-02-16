import {Component, OnInit} from '@angular/core';
import {AccountService} from "../../services/account.service";
import {Router} from "@angular/router";
import {slideRight} from '../../animations';
import {FundsService} from "../../services/funds.service";
import {CategoryOptionModel} from "../../models/categoryOption.model";

@Component({
    selector: 'app-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['./navbar.component.css'],
    animations: [
        slideRight
    ]
})
export class NavbarComponent implements OnInit {
    slideInState = 'in';
    loggedIn = false;
    categories: CategoryOptionModel[];

    constructor(private accountService: AccountService, private router: Router, private fundService: FundsService) {
        this.accountService.loggedInStatusUpdate.subscribe(
            status => this.loggedIn = status
        );
    }

    ngOnInit() {
        this.accountService.isLoggedIn().subscribe(
            data => this.loggedIn = data
        );
        this.fundService.getInitialFormData().subscribe(
            (data) => this.categories = data,
            (error) => console.log(error)
        );
    }

    logout() {
        this.accountService.logout().subscribe(() => {
            this.accountService.loggedInStatusUpdate.next(false);
            this.router.navigate(['']);
        });
    }

}
