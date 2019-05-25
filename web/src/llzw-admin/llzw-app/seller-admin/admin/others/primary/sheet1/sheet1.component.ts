import { Component, OnInit } from '@angular/core';


import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'kt-sheet1',
  templateUrl: './sheet1.component.html',
  styleUrls: ['./sheet1.component.scss']
})
export class Sheet1Component implements OnInit {


  user: FormGroup;
 
  ngOnInit() {
    // 初始化表单
    this.user = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.pattern(/([a-zA-Z0-9]+[_|_|.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|_|.]?)*[a-zA-Z0-9]+.[a-zA-Z]{2,4}/)]),
      password: new FormControl('', [Validators.required]),
      repeat: new FormControl('', [Validators.required]),
      address: new FormGroup({
        province: new FormControl(''),
        city: new FormControl(''),
        area: new FormControl(''),
        addr: new FormControl('')
      })
    });
  }
 
  onSubmit({value, valid}){
    if(!valid) return;
    console.log(JSON.stringify(value));
  }
}