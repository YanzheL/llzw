import {Component, OnInit} from '@angular/core';

import {Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';


@Component({
    selector: 'kt-orders',
    templateUrl: './orders.component.html',
    styleUrls: ['./orders.component.scss']
})
export class OrdersComponent implements OnInit {

    public form: FormGroup;

    constructor(public fb: FormBuilder, public router: Router) {

        this.form = this.fb.group({
            'product_id': [null, Validators.compose([Validators.required])],
            'quantity': [null, Validators.compose([Validators.required])],
            'customer_id': [null, Validators.compose([Validators.required])],
            'address_id': [null, Validators.compose([Validators.required])],

        });
    }

    ngOnInit() {
    }

    public onSubmit(values: Object): void {
        if (this.form.valid) {
            this.router.navigate(['/login']);
        }
        console.log(JSON.stringify(values));
    }
}
