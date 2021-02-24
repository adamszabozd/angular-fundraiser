import {Component, Input} from '@angular/core';
import {numberToString} from "../../utils/numberFormatter";

@Component({
               selector   : 'app-converted-balance',
               templateUrl: './converted-balance.component.html',
               styleUrls  : ['./converted-balance.component.css'],
           })
export class ConvertedBalanceComponent{

    @Input() myNewBalance: number;
    numberToString = numberToString;
}
