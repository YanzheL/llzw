import { Component, OnInit } from '@angular/core';

import { Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { usernamelValidator, passwordlValidator, emailValidator, matchingPasswords, phoneNumberlValidator } from '../../../../appsettings/utils/app-validators';


@Component({
  selector: 'kt-update-password',
  templateUrl: './update-password.component.html',
  styleUrls: ['./update-password.component.scss']
})
export class UpdatePasswordComponent implements OnInit {

  public form: FormGroup;

  constructor(public fb: FormBuilder, public router: Router) {

    this.form = this.fb.group({
      'oldPassword': ['', Validators.required, passwordlValidator],
      'newPassword': ['', Validators.required, passwordlValidator],
      'confirmPassword': ['', Validators.required],

    },


     { validator: matchingPasswords('newPassword', 'confirmPassword')  }
     );
  }

  // 需验证 新密码 和 旧密码不相同 此处写法需要研究
  ngOnInit() {
  }

  public onSubmit(values: Object): void {
    if (this.form.valid) {
      this.router.navigate(['/login']);
    }
    console.log(JSON.stringify(values));
  }

}
