import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
    selector: 'app-summary-page',
    templateUrl: './fundraiser-list.component.html',
    styleUrls: ['./fundraiser-list.component.css'],
})
export class FundraiserListComponent implements OnInit{

    constructor(private router: Router) { }

    ngOnInit() {
    }

}
