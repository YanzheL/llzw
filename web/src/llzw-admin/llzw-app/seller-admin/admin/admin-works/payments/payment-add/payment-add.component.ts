import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SelectionModel } from '@angular/cdk/collections';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { PaymentService } from '../../_services/payment.service';
import { Payment } from '../../_models/payment.model';

@Component({
  selector: 'kt-payment-add',
  templateUrl: './payment-add.component.html',
  styleUrls: ['./payment-add.component.scss']
})
export class PaymentAddComponent implements OnInit {

  statusCode: number; // http 返回状态码
  payment: Payment;
  paymentForm: FormGroup;

  constructor(
      private formBuilder: FormBuilder,
      private router: Router,
      private activatedRoute: ActivatedRoute,
      private paymentService: PaymentService
  ) { }

  ngOnInit() {
            /*
        orderId	    Integer	    True    Parent Order ID
        subject	    String	    True    ubject of this payment
        totalAmount	Float	    True    Total Amount
        description	String	    False	Description
        */

       this.paymentForm = this.formBuilder.group({
        id: [null, Validators.required],
        orderId: ['', Validators.required],
        subject: ['', Validators.required],
        totalAmount: ['', Validators.required],
        description: [''],
    });
  }

  onSubmit() {

    //验证不通过 不上传
    if (this.paymentForm.invalid) {
      return;
    }


    this.paymentService.create(this.paymentForm.value)
      .subscribe( data => {
        this.router.navigate(['..'], { relativeTo: this.activatedRoute });
      });
  }

}
