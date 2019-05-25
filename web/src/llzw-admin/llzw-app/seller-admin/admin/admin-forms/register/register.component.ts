import { Component, OnInit } from '@angular/core';

import { Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { usernamelValidator, passwordlValidator, emailValidator, matchingPasswords, phoneNumberlValidator } from '../../../../appsettings/utils/app-validators';

@Component({
  selector: 'kt-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  public form: FormGroup;

  constructor(public fb: FormBuilder, public router: Router) {

    this.form = this.fb.group({
      'username': [null, Validators.compose([Validators.required, usernamelValidator])],
      'email': [null, Validators.compose([Validators.required, emailValidator])],
      'password': ['', Validators.required, passwordlValidator],
      'confirmPassword': ['', Validators.required],

      'phoneNumber': [null, Validators.compose([Validators.required, phoneNumberlValidator])],

      'role': ['User'],
    }, { validator: matchingPasswords('password', 'confirmPassword') });
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
