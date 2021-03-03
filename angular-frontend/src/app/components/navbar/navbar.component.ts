import {Component, OnInit} from '@angular/core';
import {AccountService} from '../../services/account.service';
import {Router} from '@angular/router';
import {slideRight} from '../../animations';
import {TranslateService} from '@ngx-translate/core';
import {FundsService} from '../../services/funds.service';
import {CategoryOptionModel} from '../../models/categoryOption.model';
import {ExchangeRateInfoCommandModel} from '../../models/exchangeRateInfoCommand.model';
import {CurrencyOptionModel} from '../../models/currencyOption.model';
import {rateNumberToString} from '../../utils/numberFormatter';

@Component({
               selector   : 'app-navbar',
               templateUrl: './navbar.component.html',
               styleUrls  : ['./navbar.component.css'],
               animations : [
                   slideRight,
               ],
           })
export class NavbarComponent implements OnInit {
    slideInState = 'in';
    loggedIn = false;
    categories: CategoryOptionModel[];
    exchangeRates: ExchangeRateInfoCommandModel;
    myCurrency: string;
    myRate: number;
    ratesShown: boolean = false;
    rates: Array<CurrencyOptionModel> = [];
    numberToString = rateNumberToString;

    constructor(private accountService: AccountService,
                private router: Router,
                private fundService: FundsService,
                public translate: TranslateService) {
        this.accountService.loggedInStatusUpdate.subscribe(
            status => this.loggedIn = status,
        );
        this.accountService.getExchangeRate().subscribe(
            (rates) => {
                this.exchangeRates = rates;
                this.myCurrency = this.exchangeRates.accountCurrency;
                this.myCurrencyRate(this.exchangeRates);
                this.calculateRates(this.exchangeRates);
            },
            (error) => console.log(error),
        )
        translate.addLangs(['EN', 'HU']);
        translate.setDefaultLang('EN');
        const storageLang = localStorage.getItem('lang');
        if (storageLang == 'HU' || storageLang == 'EN') {
            translate.use(storageLang);
        } else {
            const browserLang = translate.getBrowserLang();
            translate.use(browserLang.match(/hu|hu-HU/) ? 'HU' : 'EN');
        }
    }

    ngOnInit() {
        this.accountService.isLoggedIn().subscribe(
            data => this.loggedIn = data,
        );
        this.fundService.getEnums().subscribe(
            (data) => this.categories = data.categoryOptions,
            (error) => console.log(error),
        );
        this.accountService.accountExchangeRates.subscribe(
            (rates) => {
                this.exchangeRates = rates;
                this.myCurrency = this.exchangeRates.accountCurrency;
                this.myCurrencyRate(this.exchangeRates);
                this.calculateRates(this.exchangeRates);
            },
            (error) => console.log(error),
        );
    }

    logout() {
        this.accountService.logout().subscribe(() => {
            this.accountService.loggedInStatusUpdate.next(false);
            this.router.navigate(['']);
        });
    }

    updateLanguage(lang: string) {
        this.translate.use(lang).subscribe(
            () => {
                localStorage.setItem('lang', lang);
                this.fundService.getEnums().subscribe(
                    (data) => {
                        this.categories = data.categoryOptions;
                        this.fundService.languageStatusUpdate.next(data);
                    },
                    (error) => console.log(error));
            },
            (error) => console.log(error),
        );
    }

    showRates() {
        this.ratesShown = (this.ratesShown !== true);
    }

    myCurrencyRate(rates: ExchangeRateInfoCommandModel) {
        for (let currencyOption of rates.currencyOptions) {
            if (currencyOption.currencyName === rates.accountCurrency) {
                this.myRate = currencyOption.exchangeRate;
            }
        }
    }

    calculateRates(rates: ExchangeRateInfoCommandModel) {
        this.rates = [];
        for (let currencyOption of rates.currencyOptions) {
            if (currencyOption.currencyName !== rates.accountCurrency) {
                const rate: CurrencyOptionModel = {
                    currencyName: currencyOption.currencyName,
                    exchangeRate: currencyOption.exchangeRate / this.myRate
                };
                this.rates.push(rate);
            }
        }
    }
}
