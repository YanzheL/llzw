import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';
// import 'rxjs/operator/map';

// RxJS
import { Observable, pipe, Subject, of } from 'rxjs'; //pipe 可以单独使用
//import { Observable, forkJoin, BehaviorSubject, of } from 'rxjs';
import { map, mergeMap, tap, catchError, retry } from 'rxjs/operators';


import { apiUrl, environment } from '../../../llzw-environments/environment';

export class ApiResponse {
  responseId: string;
  timestamp: string;
  status: number;
  success: boolean = false;
  data: any;
  error: string;
}


const BASE_URL = apiUrl + '/files';  // 中文

@Injectable()
export class FileService {

  constructor(private http: HttpClient,
    public router: Router,
    private activatedRoute: ActivatedRoute,
  ) { }

  // login(data) {
  //     data = { email: 'admin@example.com', password: 'Test@123' };
  //     return this.http.post('http://localhost:3070/api/login', data);
  // }

  // getCustomerDetails() {
  //     return this.http.get('http://localhost:3070/customers/details');
  // }


  createFile(data: any): Observable<string[]> {

    let headers = {
      headers: new HttpHeaders({
        //'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'  //这个是不以 json 发送
        //'Content-Type': 'application/json' //以 json 发送 与上面的互斥
      }),
      withCredentials: true // 重要 跨域需求
    };
    return this.http.post<ApiResponse>(BASE_URL, data, headers).pipe(
      // retry(3), // 如发送错误，再尝试，至多尝试3次 出错时可以观察到 页面延迟了一小会儿跳转
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

        return of(result);
      })
    );
  }


}
