import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { RegistrationComponent } from './components/registration/registration.component';
import { AccountPageComponent } from './components/account-page/account-page.component';
import { SummaryPageComponent } from './components/summary-page/summary-page.component';
import { TransferFundsComponent } from './components/transfer-funds/transfer-funds.component';

const routes: Routes = [
    {path: '', component: RegistrationComponent},
    {path: 'registration', component: RegistrationComponent},
    {path: 'my-account', component: AccountPageComponent},
    {path: 'transfer-funds', component: TransferFundsComponent},
    {path: 'summary', component: SummaryPageComponent},
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule],
})
export class AppRoutingModule {
}
