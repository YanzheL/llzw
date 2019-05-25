import { Injectable } from '@angular/core';


import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';



// RxJS
import { Observable, Subject, of } from 'rxjs';
//import { Observable, forkJoin, BehaviorSubject, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ApiResponse } from '../_models/api.response';
import { apiUrl, environment } from '../../../../../llzw-environments/environment';

import { Stock } from '../_models/stock.model';

const BASE_URL = apiUrl + '/stocks/';  // 中文

// 暂不注入root
// @Injectable({
//   providedIn: 'root'
// })
@Injectable()
export class StockService {

  constructor(private http: HttpClient) {
  }

  /*
	getAll(): Observable<any> {
		return this.http.get(BASE_URL);
  }
*/

  getAll(): Observable<any> {
    const params = new HttpParams().set("page", "0").set("size", "1000");//方式一：链式语法，set不能分开写
    //const params = new HttpParams({ fromString: "_page=1&_limit=10" });//方式二：使用 fromString
    //const params = new HttpParams({ fromObject: { _page: "1", _limit: "10" } });//方式三：使用 fromObject
    // http.get(url,{params}).subscribe(...)
    let headers = {
      headers: new HttpHeaders({
        //'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'  //这个是不以 json 发送
        'Content-Type': 'application/json' //以 json 发送 与上面的互斥 不能设此选项
      }),
      withCredentials: true, // 重要 跨域需求
      params
    }

    let myapiUrl = apiUrl; //加个变量 是因为 TypeScript 会对常量比较 报错

    if (myapiUrl === 'api' || (myapiUrl === "http://localhost:3000"))

      return this.http.get(BASE_URL); //假数据服务时 直接返回

    else {
      return this.http.get(BASE_URL, headers).pipe(
        mergeMap(res => {
          const result = res['data']; //真实数据服务时 返回数据需要去壳data[]
          return of(result);
        })
      );
    }

  }



  /*

  	findStocks(queryParams: QueryParamsModel): Observable<QueryResultsModel> {
		return this.getAllStocks().pipe(
			mergeMap(res => {
				const result = this.httpUtils.baseFilter(res, queryParams, ['status', 'condition']);
				return of(result);
			})
		);
	}


  */

  getById(id: number): Observable<ApiResponse> {

   // const params = new HttpParams().set("page", "0").set("size", "1000");//方式一：链式语法，set不能分开写
    //const params = new HttpParams({ fromString: "_page=1&_limit=10" });//方式二：使用 fromString
    //const params = new HttpParams({ fromObject: { _page: "1", _limit: "10" } });//方式三：使用 fromObject
    // http.get(url,{params}).subscribe(...)
    let headers = {
      headers: new HttpHeaders({
        //'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'  //这个是不以 json 发送
        'Content-Type': 'application/json' //以 json 发送 与上面的互斥 不能设此选项
      }),
      withCredentials: true, // 重要 跨域需求
     // params
    }

    let myapiUrl = apiUrl; //加个变量 是因为 TypeScript 会对常量比较 报错

    if (myapiUrl === 'api' || (myapiUrl === "http://localhost:3000"))
      return this.http.get<ApiResponse>(BASE_URL + id);
    else {
      return this.http.get<ApiResponse>(BASE_URL + id, headers).pipe(
        mergeMap(res => {
          const result = res['data']; //真实数据服务时 返回数据需要去壳data[]
          return of(result);
        })
      );
    }
  }


  // create
  create(stock: Stock): Observable<ApiResponse> {
    let headers = {
      headers: new HttpHeaders({
        //'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'  //这个是不以 json 发送
        'Content-Type': 'application/json' //以 json 发送 与上面的互斥 不能设此选项
      }),
      withCredentials: true // 重要 跨域需求
    }

    return this.http.post<ApiResponse>(BASE_URL, stock, headers);
  }

  update(stock: Stock): Observable<ApiResponse> {
    let headers = {
      headers: new HttpHeaders({
        //'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'  //这个是不以 json 发送
        'Content-Type': 'application/json' //以 json 发送 与上面的互斥 不能设此选项
      }),
      withCredentials: true // 重要 跨域需求
    }
    return this.http.patch<ApiResponse>(BASE_URL + stock.id, stock, headers);
  }


  deleteById(id: number): Observable<ApiResponse> {
    let headers = {
      headers: new HttpHeaders({
        //'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'  //这个是不以 json 发送
        'Content-Type': 'application/json' //以 json 发送 与上面的互斥 不能设此选项
      }),
      withCredentials: true // 重要 跨域需求
    }
    return this.http.delete<ApiResponse>(BASE_URL + id, headers);
  }

}

/*
 <div *ngIf="statusCode; else processing">
   <div *ngIf="statusCode === 201" [ngClass] = "'success'">
	    Article added successfully.
   </div>
   <div *ngIf="statusCode === 409" [ngClass] = "'success'">
        Article already exists.
   </div>
   <div *ngIf="statusCode === 200" [ngClass] = "'success'">
        Article updated successfully.
   </div>
   <div *ngIf="statusCode === 204" [ngClass] = "'success'">
        Article deleted successfully.
   </div>
   <div *ngIf="statusCode === 500" [ngClass] = "'error'">
        Internal Server Error.
   </div>
*/


/*

	getAll(): Observable<any> {
	  return this.http.get('//localhost:8080/cool-cars');
	}


  get(id: string) {
    return this.http.get(this.CAR_API + '/' + id);
  }

  save(car: any): Observable<any> {
    let result: Observable<Object>;
    if (car['href']) {
      result = this.http.put(car.href, car);
    } else {
      result = this.http.post(this.CAR_API, car);
    }
    return result;
  }

  remove(href: string) {
    return this.http.delete(href);
  }
}

*/





/*
	// CREATE =>  POST: add a new stock to the server
	createStock(stock): Observable<StockModel> {
		const httpHeaders = this.httpUtils.getHTTPHeaders();
		return this.http.post<StockModel>(API_PRODUCTS_URL, stock, { headers: httpHeaders });
	}

	// READ
	getAllStocks(): Observable<StockModel[]> {
		return this.http.get<StockModel[]>(API_PRODUCTS_URL);
	}

	getStockById(stockId: number): Observable<StockModel> {
		return this.http.get<StockModel>(API_PRODUCTS_URL + `/${stockId}`);
	}
*/

