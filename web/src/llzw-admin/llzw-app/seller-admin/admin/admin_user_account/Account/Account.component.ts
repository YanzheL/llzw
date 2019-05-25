import {Component, OnInit} from '@angular/core';

import {HttpClient} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
// RxJS

import {MeService} from '../../../../services/services/me.service'
import {Me} from '../../../../services/models/Me'; // 元气少女

export class ApiResponse {
    status: number
    success: boolean = false;
    data: any;
    error: string;
}


@Component({
    selector: 'kt-Account',
    templateUrl: './Account.component.html',
    styleUrls: ['./Account.component.scss']
})
export class AccountComponent implements OnInit {

    me = new Me; // 属性 声明为对象
    // username = '';
    // userImgUrl = 'assets/images/user-4.jpg'

    constructor(
        public router: Router,
        private activatedRoute: ActivatedRoute,
        private http: HttpClient,
        public meService: MeService,  // 元气少女
    ) {
        // 元气少女
        this.meService.getMe()
            .subscribe(data => {
                this.me = data;
                // setTimeout(() => { this.loadingIndicator = false; }, 1500);
            });
    }

    ngOnInit() {
    }

    /*
  ngOnInit() {
  
    this.getMe().subscribe(data => {
  
     // this.userImgUrl ='user-4.jpg' 
     this.userImgUrl ='assets/llzw_annular/img/profile/julia.jpg'  
  
      if(!data.success)
      console.log("取 me 失败");
  
      console.log("取 me 成功");
      //this.router.navigate(['/']);
      // this.router.navigate(['..'], { relativeTo: this.activatedRoute });
  });
  
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
    return this.http.get<ApiResponse>(BASE_URL, headers);
  }
  */
}
