import { Component, OnInit } from '@angular/core';

import { Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { namelValidator, emailValidator, matchingPasswords } from '../../../../appsettings/utils/app-validators';


@Component({
  selector: 'kt-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss']
})
export class ProductsComponent implements OnInit {


  public form: FormGroup;

  constructor(public fb: FormBuilder, public router: Router) {

/*

name            String      True        Name of this product
introduction	String	    True        Introduction of this product
price	        Float	    True        Price of this product
ca	            String	    True	    Certificate authority name
certId	        String	    True	    Qualification certificate id

*/
    this.form = this.fb.group({
      'name': [null, Validators.compose([Validators.required, Validators.minLength(4), namelValidator])],
      'introduction': [null, Validators.compose([Validators.required, Validators.minLength(1), Validators.maxLength(1024)])],

      'price': [null, Validators.compose([Validators.required, Validators.pattern(/^(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$/)])],
      // 正浮点数 ^(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$

      'ca': [null, Validators.compose([Validators.required, namelValidator])],
      'certId': [null, Validators.compose([Validators.required, namelValidator])],
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
    if (!valid) return;
    console.log(JSON.stringify(value));
  }
}
