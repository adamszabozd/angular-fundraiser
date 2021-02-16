import { Component, OnInit } from '@angular/core';
import {FundsService} from "../../services/funds.service";
import {FundListItemModel} from "../../models/FundListItem.model";
import {Router} from "@angular/router";

@Component({
  selector: 'app-my-funds',
  templateUrl: './my-funds.component.html',
  styleUrls: ['./my-funds.component.css']
})
export class MyFundsComponent implements OnInit {

    myFundList: Array<FundListItemModel>;

  constructor(private fundService: FundsService, private router: Router) { }

  ngOnInit() {
      this.fundService.fetchMyFunds().subscribe(
          (data)=>this.myFundList=data,
          error => console.log(error)
      )
  }

    goToModify(id: number) {
        this.router.navigate(['modify',id]);
    }
}
