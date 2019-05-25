import { Injectable } from '@angular/core';


import { HttpClient } from '@angular/common/http';

// RxJS
import { Observable, Subject, of } from 'rxjs';
//import { Observable, forkJoin, BehaviorSubject, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ApiResponse } from '../_models/api.response';
import { apiUrl, environment } from '../../../../../llzw-environments/environment';

import { Payment } from '../_models/payment.model';

const BASE_URL = apiUrl + '/payments/';  // 中文

// 暂不注入root
// @Injectable({
//   providedIn: 'root'
// })
@Injectable()
export class PaymentService {

	constructor(private http: HttpClient) {
	}


  getAll(): Observable<any> {

    let myapiUrl = apiUrl; //加个变量 是因为 TypeScript 会对常量比较 报错

    if (myapiUrl === 'api' || (myapiUrl === "http://localhost:3000"))

      return this.http.get(BASE_URL); //假数据服务时 直接返回

    else {
      return this.http.get(BASE_URL).pipe(
        mergeMap(res => {
          const result = res['data']; //真实数据服务时 返回数据需要去壳data[]
          return of(result);
        })
      );
    }

  }



	getById(id: number): Observable<ApiResponse> {
		return this.http.get<ApiResponse>(BASE_URL + id);
	}

	create(payment: Payment): Observable<ApiResponse> {
		return this.http.post<ApiResponse>(BASE_URL, payment);
	}

	update(payment: Payment): Observable<ApiResponse> {
		return this.http.put<ApiResponse>(BASE_URL + payment.id, payment);
	}


	deleteById(id: number): Observable<ApiResponse> {
		return this.http.delete<ApiResponse>(BASE_URL + id);
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
	// CREATE =>  POST: add a new product to the server
	createProduct(product): Observable<ProductModel> {
		const httpHeaders = this.httpUtils.getHTTPHeaders();
		return this.http.post<ProductModel>(API_PRODUCTS_URL, product, { headers: httpHeaders });
	}

	// READ
	getAllProducts(): Observable<ProductModel[]> {
		return this.http.get<ProductModel[]>(API_PRODUCTS_URL);
	}

	getProductById(productId: number): Observable<ProductModel> {
		return this.http.get<ProductModel>(API_PRODUCTS_URL + `/${productId}`);
	}
*/

