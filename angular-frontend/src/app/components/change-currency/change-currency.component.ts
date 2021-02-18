import {Component, Input, OnInit} from '@angular/core';
import {CurrencyOptionModel} from '../../models/currencyOption.model';
import {FormBuilder, Validators} from '@angular/forms';
import {AccountService} from '../../services/account.service';
import {AccountDetailsModel} from '../../models/accountDetails.model';
import {validationHandler} from '../../utils/validationHandler';
import {FundsService} from '../../services/funds.service';
import {Router} from '@angular/router';

@Component({
               selector   : 'app-change-currency',
               templateUrl: './change-currency.component.html',
               styleUrls  : ['./change-currency.component.css'],
           })
export class ChangeCurrencyComponent implements OnInit {

    @Input() myAccount: AccountDetailsModel;

    currencies: CurrencyOptionModel[] | undefined;
    otherCurrencies: CurrencyOptionModel[] | undefined;
    form = this.formBuilder.group({
                                      newCurrency: ['', [Validators.required]],
                                      newBalance: ['', [Validators.required]],
                                  },
    );
    myNewBalance: number;

    constructor(private accountService: AccountService, private formBuilder: FormBuilder, private router: Router) {
    }

    ngOnInit() {
        this.accountService.getCurrencies().subscribe(
            (data) => {
                this.currencies = data;
                this.otherCurrencies = [];
                for (let currency of this.currencies) {
                    if (currency.currencyName !== this.myAccount.currency) {
                        this.otherCurrencies.push(currency);
                    }
                }
            },
            (error) => console.log(error),
        );

    }



    onSubmit() {
        this.accountService.changeCurrency(this.form.value).subscribe(
            () => this.router.navigate(['/my-account']),
            (error) => validationHandler(error, this.form),
        );
    }

}
