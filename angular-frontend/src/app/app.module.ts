import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { MyAccountComponent } from './components/my-account/my-account.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { MyTransfersComponent } from './components/my-transfers/my-transfers.component';
import { AccountPageComponent } from './components/account-page/account-page.component';
import { TransferFundsComponent } from './components/transfer-funds/transfer-funds.component';
import { SummaryPageComponent } from './components/summary-page/summary-page.component';
import { TransferConfirmationComponent } from './components/transfer-confirmation/transfer-confirmation.component';

@NgModule({
    declarations: [
        AppComponent,
        RegistrationComponent,
        MyAccountComponent,
        NavbarComponent,
        MyTransfersComponent,
        AccountPageComponent,
        TransferFundsComponent,
        SummaryPageComponent,
        TransferConfirmationComponent,
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        ReactiveFormsModule,
        HttpClientModule,
    ],
    providers: [],
    bootstrap: [AppComponent],
})
export class AppModule {
}
