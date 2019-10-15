import { Component, Input } from '@angular/core';

@Component({
    selector: 'app-my-transfers',
    templateUrl: './my-transfers.component.html',
    styleUrls: ['./my-transfers.component.css'],
})
export class MyTransfersComponent {

    @Input() incomingTransfers: Array<TransferDataModel> = [];
    @Input() outgoingTransfers: Array<TransferDataModel> = [];

}
