import { Component, OnInit } from '@angular/core';


import { Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { namelValidator, emailValidator, matchingPasswords } from '../../../../appsettings/utils/app-validators';


@Component({
  selector: 'kt-stocks',
  templateUrl: './stocks.component.html',
  styleUrls: ['./stocks.component.scss']
})
export class StocksComponent implements OnInit {


  public form: FormGroup;

  constructor(public fb: FormBuilder, public router: Router) {

    this.form = this.fb.group({

      'productId': [null, Validators.compose([Validators.required, Validators.minLength(4), namelValidator])],

      'producedAt': [null, Validators.compose([Validators.required, Validators.minLength(1), Validators.maxLength(1024)])], // 验证时间日期

      'shelfLife': [null, Validators.compose([Validators.required, Validators.pattern(/^\+?[1-9][0-9]*$/)])], // 验证非零的正整数：^\+?[1-9][0-9]*$

      'totalQuantity': [null, Validators.compose([Validators.required, Validators.pattern(/^\+?[1-9][0-9]*$/)])], // 验证非零的正整数：^\+?[1-9][0-9]*$

      'trackingId': [null, Validators.compose([namelValidator])],

      'carrierName': [null, Validators.compose([namelValidator])], // 验证非负整数（正整数 + 0） ^\d+$

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

  onSubmit111({ value, valid }) {
    if (!valid) { return; }
    console.log(JSON.stringify(value));
  }
}
