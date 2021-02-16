import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';

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
        FundraiserModifyComponent
    ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        AppRoutingModule,
        ReactiveFormsModule,
        HttpClientModule,
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
