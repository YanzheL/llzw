import {Component, OnInit} from '@angular/core';

import {ActivatedRoute, Router} from '@angular/router';

import {PaymentService} from '../../_services/payment.service';
import {Payment} from '../../_models/payment.model';

@Component({
    selector: 'kt-payments-list',
    templateUrl: './payments-list.component.html',
    styleUrls: ['./payments-list.component.scss']
})
export class PaymentsListComponent implements OnInit {

    editing = {};
    payments_rows = [];
    payments_temp = [];
    selected = [];

    constructor(
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private paymentService: PaymentService
    ) {
        this.paymentService.getAll()
            .subscribe(data => {
                this.payments_temp = [...data];
                this.payments_rows = data;
                // setTimeout(() => { this.loadingIndicator = false; }, 1500);
            });
    }

    ngOnInit() {
    }

    editPayment(payment: Payment): void {
        window.localStorage.removeItem('editPaymentId');
        window.localStorage.setItem('editPaymentId', payment.id.toString());
        this.router.navigate(['./edit'], {relativeTo: this.activatedRoute});
    }


    deletePayment(payment: Payment): void {
        this.paymentService.deleteById(payment.id)
            .subscribe(data => {
                this.payments_temp = this.payments_temp.filter(u => u !== payment);
                this.payments_rows = this.payments_rows.filter(u => u !== payment);
            });
    }

}
