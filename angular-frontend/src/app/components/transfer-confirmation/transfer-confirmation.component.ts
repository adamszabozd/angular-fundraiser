import {Component, OnInit} from '@angular/core';
import {TransferService} from '../../services/transfer.service';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
               selector   : 'app-transfer-confirmation',
               templateUrl: './transfer-confirmation.component.html',
               styleUrls  : ['./transfer-confirmation.component.css'],
           })
export class TransferConfirmationComponent implements OnInit {

    validCode = false;

    constructor(private transferService: TransferService,
                private router: Router,
                private route: ActivatedRoute) {
    }

    ngOnInit() {
        this.route.paramMap.subscribe(
            paramMap => {
                const confirmationCode = paramMap.get('code');
                this.transferService.confirmTransfer({'confirmationCode': confirmationCode}).subscribe(
                    () => this.validCode = true,
                    error => console.warn(error),
                );
            },
            error => console.warn(error),
        );
    }

}
