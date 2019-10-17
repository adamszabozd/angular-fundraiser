import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AccountService } from '../../services/account.service';

@Component({
  selector: 'app-account-page',
  templateUrl: './account-page.component.html',
  styleUrls: ['./account-page.component.css'],
})
export class AccountPageComponent implements OnInit {

  accountDetails: AccountDetailsModel = {
    username: '',
    goal: '',
    balance: null,
    funds: null,
  };
  outgoingTransfers: Array<TransferDataModel>;
  incomingTransfers: Array<TransferDataModel>;

  constructor(private accountService: AccountService, private router: Router) {}

  ngOnInit(): void {
    if (!localStorage.getItem('auth')) {
      this.router.navigate(['/registration']);
    } else {
      this.accountService.getMyAccountDetails().subscribe(
        data => {
          this.accountDetails = {
            username: data.username,
            goal: data.goal,
            balance: data.balance,
            funds: data.funds,
          };
          this.outgoingTransfers = data.outgoingTransfers;
          this.incomingTransfers = data.incomingTransfers;
        },
      );
    }
  }

}
