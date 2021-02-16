import { Component, OnInit } from '@angular/core';
import {FundsService} from "../../services/funds.service";
import {FundListItemModel} from "../../models/FundListItem.model";

@Component({
  selector: 'app-my-funds',
  templateUrl: './my-funds.component.html',
  styleUrls: ['./my-funds.component.css']
})
export class MyFundsComponent implements OnInit {

    myFundList: Array<FundListItemModel>;

  constructor(private fundService: FundsService) { }

  ngOnInit() {
      this.fundService.fetchMyFunds().subscribe(
          (data)=>this.myFundList=data,
          error => console.log(error)
      )
  }

}
