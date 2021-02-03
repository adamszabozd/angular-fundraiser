import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { RegistrationComponent } from './components/registration/registration.component';
import { AccountPageComponent } from './components/account-page/account-page.component';
import { FundraiserListComponent } from './components/fundraiser-list/fundraiser-list.component';
import { TransferFundsComponent } from './components/transfer-funds/transfer-funds.component';
import {TransferConfirmationComponent} from "./components/transfer-confirmation/transfer-confirmation.component";
import {LoginComponent} from './components/login/login.component';

const routes: Routes = [
    {path: '', component: FundraiserListComponent},
    {path: 'registration', component: RegistrationComponent},
    {path: 'my-account', component: AccountPageComponent},
    {path: 'transfer-funds', component: TransferFundsComponent},
    {path: 'transfer-confirmation/:code', component: TransferConfirmationComponent},
    {path: 'login', component: LoginComponent},
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule],
})
export class AppRoutingModule {
}
