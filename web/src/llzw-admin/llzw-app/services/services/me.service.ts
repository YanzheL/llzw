import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
// RxJS
import {Observable, of} from 'rxjs'; //pipe 可以单独使用
//import { Observable, forkJoin, BehaviorSubject, of } from 'rxjs';
import {catchError, mergeMap, retry} from 'rxjs/operators';


import {apiUrl} from '../../../llzw-environments/environment';

import {Me} from '../models/Me';

// import 'rxjs/operator/map';

export class ApiResponse {
    status: number
    success: boolean = false;
    data: any;
    error: string;
}

const BASE_URL = apiUrl + '/users/me';  // 中文

@Injectable()
export class MeService {

    constructor(private http: HttpClient,
                public router: Router,
                private activatedRoute: ActivatedRoute,
    ) {
    }

    // login(data) {
    //     data = { email: 'admin@example.com', password: 'Test@123' };
    //     return this.http.post('http://localhost:3070/api/login', data);
    // }

    // getCustomerDetails() {
    //     return this.http.get('http://localhost:3070/customers/details');
    // }


    getMe(): Observable<Me> {

        let headers = {
            headers: new HttpHeaders({
                //'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'  //这个是不以 json 发送
                'Content-Type': 'application/json' //以 json 发送 与上面的互斥
            }),
            withCredentials: true // 重要 跨域需求
        };
        return this.http.get<Me>(BASE_URL, headers).pipe(
            retry(3), // 如发送错误，再尝试，至多尝试3次 出错时可以观察到 页面延迟了一小会儿跳转
            catchError(error => {
                console.error("元气少女 Error catched", error);
                //  logout();  先清除过去的登录记录
                this.router.navigate(['/session/signin']); // 不管那个，直接跳回登录页面算了
                //return of({ description: "Error Value Emitted" });
                return of([]);
            }),

            // TODO:  这里如用 map 就会出错
            mergeMap(res => {
                const result = res['data']; //真实数据服务时 返回数据需要去壳data[]
                result.logined = true; // 重要 已登录
                // 如未上传头像, 显示本地未登录头像
                result.avatar = result.avatar ? apiUrl + '/' + result.avatar : '../../../../../assets/llzw_annular/img/users/default-user.jpg'
                return of(result);
            })
        );
    }

    // 修改个人信息
    updateMe(me: Me): Observable<ApiResponse> {
        return this.http.put<ApiResponse>(BASE_URL, me);
    }

}
