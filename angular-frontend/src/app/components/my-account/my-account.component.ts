import {Component, Input, OnInit} from '@angular/core';
import {Router} from '@angular/router';

@Component({
    selector: 'app-my-account',
    templateUrl: './my-account.component.html',
    styleUrls: ['./my-account.component.css'],
})
export class MyAccountComponent  implements OnInit {

    @Input() accountDetails: AccountDetailsModel;

    constructor(private router: Router) {
    }

    ngOnInit() {
        if (!localStorage.auth) {
            this.router.navigate(['/login']);
        }

    }

}
