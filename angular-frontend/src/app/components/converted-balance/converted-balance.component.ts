import {Component, Input} from '@angular/core';

@Component({
               selector   : 'app-converted-balance',
               templateUrl: './converted-balance.component.html',
               styleUrls  : ['./converted-balance.component.css'],
           })
export class ConvertedBalanceComponent{

    @Input() myNewBalance: number;

}
