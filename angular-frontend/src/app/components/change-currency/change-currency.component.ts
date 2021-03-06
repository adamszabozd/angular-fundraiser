import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {CurrencyOptionModel} from '../../models/currencyOption.model';
import {FormBuilder, Validators} from '@angular/forms';
import {AccountService} from '../../services/account.service';
import {AccountDetailsModel} from '../../models/accountDetails.model';
import {validationHandler} from '../../utils/validationHandler';
import {Router} from '@angular/router';
import {numberToString} from "../../utils/numberFormatter";

@Component({
               selector   : 'app-change-currency',
               templateUrl: './change-currency.component.html',
               styleUrls  : ['./change-currency.component.css'],
           })
export class ChangeCurrencyComponent implements OnInit {

    @Input() myAccount: AccountDetailsModel;
    @Output() currencyChanged = new EventEmitter();

    currencies: CurrencyOptionModel[] | undefined;
    otherCurrencies: CurrencyOptionModel[] | undefined;
    oldCurrency: number;
    form = this.formBuilder.group({
                                      newCurrency: ['', [Validators.required]],
                                      newBalance : ['', [Validators.required]],
                                  },
    );
    newBalance: number;
    numberToString = numberToString;

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
                    } else {
                        this.oldCurrency = currency.exchangeRate;
                    }
                }
            },
            (error) => console.log(error),
        );

    }

    calculateNewBalance(event) {
        for (let otherCurrency of this.otherCurrencies) {
            if (event.target.value === otherCurrency.currencyName) {
                this.newBalance = otherCurrency.exchangeRate / this.oldCurrency * this.myAccount.balance;
            }
        }
    }

    onSubmit() {
        this.accountService.changeCurrency(this.form.value).subscribe(
            (data) => {
                this.currencyChanged.emit(data);
                this.router.navigate(['/my-account'],
                );
            },
            (error) => validationHandler(error, this.form),
        );
    }
}
