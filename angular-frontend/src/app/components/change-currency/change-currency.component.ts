import {Component, Input, OnInit} from '@angular/core';
import {CurrencyOptionModel} from '../../models/currencyOption.model';
import {FormBuilder, Validators} from '@angular/forms';
import {AccountService} from '../../services/account.service';
import {AccountDetailsModel} from '../../models/accountDetails.model';

@Component({
               selector   : 'app-change-currency',
               templateUrl: './change-currency.component.html',
               styleUrls  : ['./change-currency.component.css'],
           })
export class ChangeCurrencyComponent implements OnInit {

    @Input() myAccount: AccountDetailsModel;

    currencies: CurrencyOptionModel[] | undefined;
    form = this.formBuilder.group({
                                              new_currency: ['', [Validators.required]],
                                          },
    );

    constructor(private accountService: AccountService, private formBuilder: FormBuilder) {
    }

    ngOnInit() {
        this.accountService.getCurrencies().subscribe(
            (data) => {
                this.currencies = data;
            },
            (error) => console.log(error),
        );
    }

    onSubmit() {

    }

}
