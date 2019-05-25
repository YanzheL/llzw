import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, ValidationErrors, ValidatorFn, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {EmbryoService} from '../../../Services/Embryo.service';
import {ToastaService} from 'ngx-toasta';

@Component({
  selector: 'embryo-Register',
  templateUrl: './Register.component.html',
  styleUrls: ['./Register.component.scss']
})
export class RegisterComponent implements OnInit {

  constructor(private embryoService: EmbryoService,
              private router: Router,) { }
  username = new FormControl('', [
    Validators.required,
  ]);
  email = new FormControl('', [
    Validators.required,
    Validators.email
  ]);
  phoneNumber = new FormControl('', [
    Validators.minLength(11),
    Validators.maxLength(11)
  ]);
  password = new FormControl('');
  confirmPassword = new FormControl('');
  equal = true;

  ngOnInit() {
  }
  onFocus() {
    this.equal=true;
  }
  onBlur() {
    if (this.confirmPassword.value === this.password.value){
      this.equal = true;
    } else {
      this.equal  = false;
    }
  }
  onSubmit() {
    if (!this.equal) {
      return;
    }
    const data = {
      username: this.username.value,
      password: this.password.value,
      email: this.email.value,
      phoneNumber: this.phoneNumber.value,
      role: 'ROLE_CUSTOMER'
    };
    this.embryoService.register(data).subscribe(res => {
      console.log(res);
      if (res.success) {
        this.embryoService.confirmationPopup('register success, go to login now').subscribe(res1 => {
          if (res1) {
            this.router.navigate(['/session/signin']);
          }
          });
      }
    }, error1  => {
      if (error1.error.success) {
        this.sendMessage(error1.error.error.message);
      }
    });
  }
  public sendMessage(value:any) {
    const message = value;
    this.embryoService.confirmationPopup(message).
    subscribe(res => {
      },
      err => console.log(err)
      // ()  => this.getPopupResponse(this.popupResponse, value, 'cart')
    );
  }
}
