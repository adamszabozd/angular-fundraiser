import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import {RegistrationComponent} from './components/registration/registration.component';
import {AccountPageComponent} from './components/account-page/account-page.component';
import {FundraiserListComponent} from './components/fundraiser-list/fundraiser-list.component';
import {TransferFundsComponent} from './components/transfer-funds/transfer-funds.component';
import {TransferConfirmationComponent} from "./components/transfer-confirmation/transfer-confirmation.component";
import {LoginComponent} from './components/login/login.component';
import {PageNotFoundComponent} from "./components/page-not-found/page-not-found.component";
import {NewFundFormComponent} from "./components/new-fund-form/new-fund-form.component";
import {FundraiserDetailsComponent} from "./components/fundraiser-details/fundraiser-details.component";
import {MyFundsComponent} from "./components/my-funds/my-funds.component";

const routes: Routes = [
    {path: '', pathMatch: 'full', redirectTo: 'fund-list'},
    {path: 'fund-list', component: FundraiserListComponent},
    {path: 'registration', component: RegistrationComponent},
    {path: 'my-account', component: AccountPageComponent},
    {path: 'transfer-funds', component: TransferFundsComponent},
    {path: 'transfer-confirmation', component: TransferConfirmationComponent},
    {path: 'transfer-confirmation/:code', component: TransferConfirmationComponent},
    {path: 'login', component: LoginComponent},
    {path: 'new-fund', component: NewFundFormComponent},
    {path: 'fund-details/:id', component: FundraiserDetailsComponent},
    {path: 'my-funds', component: MyFundsComponent},
    {path: '**', component: PageNotFoundComponent}
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule],
})
export class AppRoutingModule {
}
