import {Component, Input } from '@angular/core';

@Component({
  selector: 'app-converted-transfer-amount',
  templateUrl: './converted-transfer-amount.component.html',
  styleUrls: ['./converted-transfer-amount.component.css']
})
export class ConvertedTransferAmountComponent {

    @Input() targetCurrency: string;
    @Input() targetAmount: string;

}
