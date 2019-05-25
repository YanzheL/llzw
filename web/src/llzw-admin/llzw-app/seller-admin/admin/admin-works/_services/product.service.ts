import {Injectable} from '@angular/core';


import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
// RxJS
import {Observable, of} from 'rxjs';
//import { Observable, forkJoin, BehaviorSubject, of } from 'rxjs';
import {mergeMap} from 'rxjs/operators';

import {ApiResponse} from '../_models/api.response';
import {apiUrl} from '../../../../../llzw-environments/environment';

import {Product} from '../_models/product.model';

const BASE_URL = apiUrl + '/products/';  // 中文

// 暂不注入root
// @Injectable({
//   providedIn: 'root'
// })
@Injectable()
export class ProductService {

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

        let myapiUrl = apiUrl; //加个变量 是因为 TypeScript 会对常量比较 报错

        if (myapiUrl === 'api' || (myapiUrl === "http://localhost:3000"))

            return this.http.get(BASE_URL); //假数据服务时 直接返回

        else {
            return this.http.get(BASE_URL, {params}).pipe(
                mergeMap(res => {
                    const result = res['data']; //真实数据服务时 返回数据需要去壳data[]
                    return of(result);
                })
            );
        }

    }


    getById(id: number): Observable<ApiResponse> {
        let myapiUrl = apiUrl; //加个变量 是因为 TypeScript 会对常量比较 报错

        if (myapiUrl === 'api' || (myapiUrl === "http://localhost:3000"))
            return this.http.get<ApiResponse>(BASE_URL + id);
        else {
            return this.http.get<ApiResponse>(BASE_URL + id).pipe(
                mergeMap(res => {
                    const result = res['data']; //真实数据服务时 返回数据需要去壳data[]
                    return of(result);
                })
            );
        }
    }


    // getById(id: number): Observable<ApiResponse> {
    //   return this.http.get<ApiResponse>(BASE_URL + id);
    // }

    create(product: Product): Observable<ApiResponse> {

        let headers = {
            headers: new HttpHeaders({
                //'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'  //这个是不以 json 发送
                'Content-Type': 'application/json' //以 json 发送 与上面的互斥
            }),
            withCredentials: true // 重要 跨域需求
        }
        return this.http.post<ApiResponse>(BASE_URL, product, headers);
        //return this.http.post<ApiResponse>(BASE_URL, this.paramFormat(product), headers);
    }


    update(product: Product): Observable<ApiResponse> {
        let headers = {
            headers: new HttpHeaders({
                //'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'  //这个是不以 json 发送
                'Content-Type': 'application/json' //以 json 发送 与上面的互斥 不能设此选项
            }),
            withCredentials: true // 重要 跨域需求
        }
        //return this.http.put<ApiResponse>(BASE_URL + product.id, product, headers); //put 全部更新
        return this.http.patch<ApiResponse>(BASE_URL + product.id, product, headers); //patch 补充更新
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

    // 序列化参数, 将对象转换为字符串
    private paramFormat(data: any): string {
        let paramStr = '', name, value, subName, innerObj;
        for (name in data) {
            value = data[name];
            if (value instanceof Array) {
                for (let i of value) {
                    subName = name;
                    innerObj = {};
                    innerObj[subName] = i;
                    paramStr += this.paramFormat(innerObj) + '&';
                }
            } else if (value instanceof Object) {
                Object.keys(value).forEach(function (key) {
                    subName = name + '[' + key + ']';
                    innerObj = {};
                    innerObj[subName] = value[key];
                    paramStr += this.paramFormat(innerObj) + '&';
                })
            } else if (value !== undefined && value !== null) {
                paramStr += encodeURIComponent(name) + '='
                    + encodeURIComponent(value) + '&';
            }
        }
        return paramStr.length ? paramStr.substr(0, paramStr.length - 1) : paramStr;
    };

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

