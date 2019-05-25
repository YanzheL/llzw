import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SelectionModel } from '@angular/cdk/collections';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';

import { PaymentService } from '../../_services/payment.service';
import { Payment } from '../../_models/payment.model';


@Component({
  selector: 'kt-payment-edit',
  templateUrl: './payment-edit.component.html',
  styleUrls: ['./payment-edit.component.scss']
})
export class PaymentEditComponent implements OnInit {

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
    let paymentId = window.localStorage.getItem('editPaymentId');
    if (!paymentId) {
      alert('Invalid action.');
      this.router.navigate(['..'], { relativeTo: this.activatedRoute });
      return;
    }

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
    this.paymentService.getById(+paymentId)
      // this.paymentService.getById(2)
      .subscribe(data => {
        // 元气少女 因为此处需要编辑的控制器 少于 从 API get 到的 fields, 所以需要单独赋值
        // this.paymentForm.setValue(data.result);
        // this.paymentForm.setValue(data);
        this.paymentForm.controls['id'].setValue(data['id']);
        this.paymentForm.controls['orderId'].setValue(data['orderId']);
        this.paymentForm.controls['subject'].setValue(data['subject']);
        this.paymentForm.controls['totalAmount'].setValue(data['totalAmount']);
        this.paymentForm.controls['description'].setValue(data['description']);
      });
  }

  /*

            .subscribe(
            successCode => {
       this.statusCode = successCode;
       this.getAllArticles();
       this.backToCreateArticle();
  },
  errorCode => this.statusCode = errorCode);
  */

  onSubmit() {
    //验证不通过 不上传
    if (this.paymentForm.invalid) {
      return;
    }

    this.paymentService.update(this.paymentForm.value)
      .pipe(first())
      .subscribe(
        success => {
          this.statusCode = success.status;
          alert('Payment updated successfully.');
          console.log('successCode:', this.statusCode);
          this.router.navigate(['..'], { relativeTo: this.activatedRoute });

        },
        error => {
          this.statusCode = error.status;
          alert('errorCode' + this.statusCode);
        });
  }

}
