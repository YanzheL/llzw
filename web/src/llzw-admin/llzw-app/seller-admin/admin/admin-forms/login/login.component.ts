import { Component, OnInit } from '@angular/core';

import { Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { usernamelValidator, passwordlValidator, emailValidator, matchingPasswords } from '../../../../appsettings/utils/app-validators';
import { HttpClient, HttpHeaders } from '@angular/common/http';
// RxJS
import { Observable, Subject, of } from 'rxjs';

import { AppSettings } from '../../../../appsettings/app.settings';
import { Settings } from '../../../../appsettings/app.settings.model';

import { apiUrl, environment } from '../../../../../llzw-environments/environment';

const BASE_URL = apiUrl + '/../../login';  // 中文 为真 8081/api/login 否则 apiUrl + '/../../login'
export class ApiResponse {
    status: number
    success: boolean = false;
    data: any;
    error: string;
}


@Component({
    selector: 'kt-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
    public form: FormGroup;
    public settings: Settings;

    constructor(
        public appSettings: AppSettings,
        public fb: FormBuilder,
        public router: Router,
        private http: HttpClient
    ) {
        // this.settings = this.appSettings.settings;
        this.form = this.fb.group({
            //'username': [null, Validators.compose([Validators.required, usernamelValidator])],
            'username': ["test_user_seller_username_1", Validators.compose([Validators.required])],
            // 'email': [null, Validators.compose([Validators.required, emailValidator])],
            //'password': [null, Validators.compose([Validators.required, passwordlValidator])],
            'password': ["test_user_seller_PASSWORD_1", Validators.compose([Validators.required])],
            'rememberMe': false
        });
    }

    ngOnInit() {
    }


    onSubmit() {


        this.login().subscribe(data => {
            if (data.status === 200) {
                if (data.success) {
                    console.log("登录成功");
                }
            }
            // this.router.navigate(['..'], { relativeTo: this.activatedRoute });
        });

        this.getMe().subscribe(data => {
            if (data.status === 200) {
                if (data.success) {
                    console.log("取 me 成功");
                }
            }
            // this.router.navigate(['..'], { relativeTo: this.activatedRoute });
        });
    }


    login(): Observable<ApiResponse> {

        // Setup headers
        //const headers = { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) };

        let headers = {
            headers: new HttpHeaders({
                'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'  //这个是不以 json 发送
                // 'Content-Type': 'application/json' //以 json 发送 与上面的互斥
            }),
            withCredentials: true // 重要 跨域需求 也可以 设置全局这个的拦截器
        };

        let request = 'username=' + this.form.controls.username.value + '&password=' + this.form.controls.password.value + '&rememberMe=' + this.form.controls.rememberMe.value;
       
        // const request = JSON.stringify(
        //     { email: this.form.controls.username.value, password: this.form.controls.password.value }
        // );


        return this.http.post<ApiResponse>(BASE_URL, request, headers);
    }

    getMe(): Observable<ApiResponse> {
        
        let BASE_URL = apiUrl + '/users/me';

        let headers = {
            headers: new HttpHeaders({                
                //'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'  //这个是不以 json 发送
                'Content-Type': 'application/json' //以 json 发送 与上面的互斥
            }),
           withCredentials: true // 重要 跨域需求
        };
        return this.http.get<ApiResponse>(BASE_URL,  headers);
    }



    // public onSubmit(values: Object): void {
    //   if (this.form.valid) {
    //     this.router.navigate(['/']);
    //   }
    // }

    // ngAfterViewInit() {
    //   this.settings.loadingSpinner = false;
    // }
}
