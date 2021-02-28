import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { AccountPageComponent } from './components/account-page/account-page.component';
import { TransferFundsComponent } from './components/transfer-funds/transfer-funds.component';
import { FundraiserListComponent } from './components/fundraiser-list/fundraiser-list.component';
import { TransferConfirmationComponent } from './components/transfer-confirmation/transfer-confirmation.component';
import {HttpRequestInterceptor} from './utils/httpRequestInterceptor';
import {LoginComponent} from './components/login/login.component';
import { NewFundFormComponent } from './components/new-fund-form/new-fund-form.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import {HttpResponseInterceptor} from "./utils/httpResponseInterceptor";
import { FundraiserDetailsComponent } from './components/fundraiser-details/fundraiser-details.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { MyFundsComponent } from './components/my-funds/my-funds.component';
import { FundraiserModifyComponent } from './components/fundraiser-modify/fundraiser-modify.component';
import {TranslateLoader, TranslateModule} from "@ngx-translate/core";
import {TranslateHttpLoader} from "@ngx-translate/http-loader";
import {NgxPaginationModule} from "ngx-pagination";
import { HomePageComponent } from './components/home-page/home-page.component';
import { ChangeCurrencyComponent } from './components/change-currency/change-currency.component';
import {PasswordStrengthMeterModule} from 'angular-password-strength-meter';
import { ConvertedBalanceComponent } from './components/converted-balance/converted-balance.component';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { ConvertedTransferAmountComponent } from './components/converted-transfer-amount/converted-transfer-amount.component';
import {RecaptchaFormsModule, RecaptchaModule} from "ng-recaptcha";
import { MyFundDetailsComponent } from './components/my-fund-details/my-fund-details.component';

export function HttpLoaderFactory(httpClient: HttpClient) {
    return new TranslateHttpLoader(httpClient);
}

@NgModule({
    declarations: [
        AppComponent,
        RegistrationComponent,
        NavbarComponent,
        AccountPageComponent,
        TransferFundsComponent,
        FundraiserListComponent,
        TransferConfirmationComponent,
        LoginComponent,
        PageNotFoundComponent,
        NewFundFormComponent,
        FundraiserDetailsComponent,
        MyFundsComponent,
        FundraiserModifyComponent,
        HomePageComponent,
        ChangeCurrencyComponent,
        ConvertedBalanceComponent,
        ConvertedTransferAmountComponent,
        MyFundDetailsComponent,
    ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        AppRoutingModule,
        ReactiveFormsModule,
        HttpClientModule,
        NgxPaginationModule,
        PasswordStrengthMeterModule,
        TranslateModule.forRoot({
            loader: {
                provide: TranslateLoader,
                useFactory: HttpLoaderFactory,
                deps: [HttpClient]
            }
        }),
        NgxChartsModule,
        RecaptchaModule,
        RecaptchaFormsModule
    ],
    providers: [
        // Http Interceptor(s) -  adds with Client Credentials
        [
            {provide: HTTP_INTERCEPTORS, useClass: HttpRequestInterceptor, multi: true},
            {provide: HTTP_INTERCEPTORS, useClass: HttpResponseInterceptor, multi: true},
        ],
    ],
    bootstrap: [AppComponent],
})
export class AppModule {
}
