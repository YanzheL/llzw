import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable, BehaviorSubject, of} from 'rxjs';
import { MatDialogRef, MatDialog, MatDialogConfig, MatSidenav } from '@angular/material';
import { AngularFireDatabase, AngularFireList, AngularFireObject } from '@angular/fire/database';
import { ToastaService, ToastaConfig, ToastOptions, ToastData } from 'ngx-toasta';
import 'rxjs/Rx';

import { ReviewPopupComponent } from '../Global/ReviewPopup/ReviewPopup.component';
import { ConfirmationPopupComponent } from '../Global/ConfirmationPopup/ConfirmationPopup.component';
import {environment} from '../../environments/environment';
// import {environment} from '../../environments/environment.prod';

interface Response {
  data: any;
}

@Injectable()
export class EmbryoService {
    // baseUrl1 = 'http://localhost:8981/api/v1';
    baseUrl1 = '';
   sidenavOpen = false;
   paymentSidenavOpen = false;
   isDirectionRtl = false;
   featuredProductsSelectedTab: any = 0;
   newArrivalSelectedTab: any = 0;

   /**** Get currency code:- https://en.wikipedia.org/wiki/ISO_4217 *****/
   currency = 'USD';
   language = 'english';

   shipping = 12.95;
   tax = 27.95;

   products: AngularFireObject<any>;

   localStorageCartProducts: any;
   localStorageWishlist: any;
   navbarCartCount = 0;
   navbarWishlistProdCount = 0;
   buyUserCartProducts: any;
    headers = new HttpHeaders({
        'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'  //这个是不以 json 发送
        // 'Content-Type': 'application/json' //以 json 发送 与上面的互斥 bootspring此处不可以
      });
   constructor(private http: HttpClient,
               private dialog: MatDialog,
               private db: AngularFireDatabase,
               private toastyService: ToastaService,
               private toastyConfig: ToastaConfig) {

     this.baseUrl1 = environment.url1;

      this.toastyConfig.position = 'top-right';
      this.toastyConfig.theme = 'material';
      this.calculateLocalWishlistProdCounts();
      localStorage.removeItem('user');
      localStorage.removeItem('byProductDetails');

      this.db.object('products').valueChanges().subscribe(res => {this.setCartItemDefaultValue(res['gadgets'][1]); });
   }
   public setCartItemDefaultValue(setCartItemDefaultValue) {
      let products: any;
      products = JSON.parse(localStorage.getItem('cart_item')) || [];
      const found = products.some(function (el, index) {
         if (el.name == setCartItemDefaultValue.name) {
            return  true;
         }
      });
      // if (!found) { products.push(setCartItemDefaultValue); }

      localStorage.setItem('cart_item', JSON.stringify(products));
      this.calculateLocalCartProdCounts();
   }

  getFeaturedProducts(data): Observable<any> {
    return this.http.get(this.baseUrl1 + '/products', data);
    // return this.http.get('http://localhost:8081' + '/products', data);
  }
  getAllProducts1(): Observable<any> {
    return this.http.get('http://localhost:8081' + '/products/2');
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
  getNewFeaturedProducts(data): Observable<any> {
    // return this.http.get(this.baseUrl1 + '/products', {withCredentials: true, params: data, headers: this.headers });
    return this.http.get(this.baseUrl1 + '/products', {withCredentials: true, params: data});
  }
  getSingleProduct(id): Observable<any> {
    // return this.http.get(this.baseUrl1 + '/products/' + id, {withCredentials: true, headers: this.headers });
    return this.http.get(this.baseUrl1 + '/products/' + id, {withCredentials: true });
  }
  public getAllProduct(data): Observable<any> {
     // return this.http.get(this.baseUrl1 + '/products',  {withCredentials: true, params: data, headers: this.headers });
     return this.http.get(this.baseUrl1 + '/products',  {withCredentials: true, params: data });
  }
  public getStocksById(id): Observable<any> {
    // return this.http.get(this.baseUrl1 + '/stocks/' + id, {withCredentials: true, headers: this.headers });
    return this.http.get(this.baseUrl1 + '/stocks/' + id, {withCredentials: true });
  }
/**
 *  UserAccount
 */
  getOrderHistory(data): Observable<any> {
    // return this.http.get(this.baseUrl1 + '/orders',  {withCredentials: true, params: data, headers: this.headers });
    return this.http.get(this.baseUrl1 + '/orders',  {withCredentials: true, params: data });
  }
  getUserInfo(): Observable<any> {
    // return this.http.get(this.baseUrl1 + '/users/me', {withCredentials: true, headers: this.headers });
    return this.http.get(this.baseUrl1 + '/users/me', {withCredentials: true });
  }
  getUserAddress(data): Observable<any> {
    // return this.http.get(this.baseUrl1 + '/addresses', {withCredentials: true, params: data, headers: this.headers });
    return this.http.get(this.baseUrl1 + '/addresses', {withCredentials: true, params: data });
  }
  creatAddress(data): Observable<any> {
    // return this.http.post(this.baseUrl1 + '/addresses', data , {withCredentials: true, headers: this.headers });
    return this.http.post(this.baseUrl1 + '/addresses', data , {withCredentials: true });
  }
  updatePassword(data): Observable<any> {
    // return this.http.put(this.baseUrl1 + '/users/updatePassword', data , {withCredentials: true, headers: this.headers });
    return this.http.put(this.baseUrl1 + '/users/updatePassword', data , {withCredentials: true });
  }


  /**
   *  Checkout
   */
  isLogin() {
    const rootUser = JSON.parse(localStorage.getItem('rootUser'));
    if (rootUser) {
      return true;
    } else {
      return false;
    }
  }

  CreateMyOrder(data): Observable<any> {
    // return this.http.post(this.baseUrl1 + '/orders', data, {withCredentials: true, headers: this.headers });
    return this.http.post(this.baseUrl1 + '/orders', data, {withCredentials: true });
  }

  CreatMyPayment(data): Observable<any> {
    // return this.http.post(this.baseUrl1 + '/payments', data, {withCredentials: true, headers: this.headers });
    return this.http.post(this.baseUrl1 + '/payments', data, {withCredentials: true });
  }
  getSpecificOrder(id): Observable<any> {
    // return this.http.get(this.baseUrl1 + '/orders/' + id, {withCredentials: true, headers: this.headers });
    return this.http.get(this.baseUrl1 + '/orders/' + id, {withCredentials: true });
  }
  getRelatedPaymentsforOrder(id): Observable<any> {
    // return this.http.get(this.baseUrl1 + '/payments?orderId=' + id, {withCredentials: true, headers: this.headers });
    return this.http.get(this.baseUrl1 + '/payments?orderId=' + id, {withCredentials: true });
  }
  paymentsRetry(id): Observable<any> {
    // return this.http.get(this.baseUrl1 + '/payments/retry/' + id, {withCredentials: true, headers: this.headers });
    return this.http.get(this.baseUrl1 + '/payments/retry/' + id, {withCredentials: true });
  }
  deliveryConfirm(id): Observable<any> {
    // return this.http.patch(this.baseUrl1 + '/orders/' + id, {'action': 'DELIVERY_CONFIRM'}, {withCredentials: true, headers: this.headers });
    return this.http.patch(this.baseUrl1 + '/orders/' + id + '/DELIVERY_CONFIRM', '' ,{withCredentials: true });
  }
   verify (id): Observable<any> {
    // return this.http.patch(this.baseUrl1 + '/orders/' + id, {'action': 'DELIVERY_CONFIRM'}, {withCredentials: true, headers: this.headers });
    return this.http.get(this.baseUrl1 + '/payments/verify/' + id , {withCredentials: true });
  }
  /**
   *  Session
   */
  login(user): Observable<any> {
    // return this.http.post(this.baseUrl1 + '/login', this.paramFormat(user), {withCredentials: true, headers: this.headers });
    return this.http.post( this.baseUrl1 + '/login', this.paramFormat(user), {withCredentials: true, headers: this.headers });
  }
  register (data): Observable<any> {
    console.log(this.paramFormat(data));
    // return this.http.post(this.baseUrl1 + '/users/register', data , {withCredentials: true, headers: this.headers });
    return this.http.post(this.baseUrl1 + '/users/register', data , {withCredentials: true });
  }
   public reviewPopup(singleProductDetails, reviews) {
      let review: MatDialogRef<ReviewPopupComponent>;
      const dialogConfig = new MatDialogConfig();
      if (this.isDirectionRtl) {
         dialogConfig.direction = 'rtl';
      } else {
         dialogConfig.direction = 'ltr';
      }

      review = this.dialog.open(ReviewPopupComponent, dialogConfig);
      review.componentInstance.singleProductDetails = singleProductDetails;
      review.componentInstance.reviews = reviews;

      return review.afterClosed();
   }

   public confirmationPopup(message: string) {
      let confirmationPopup: MatDialogRef<ConfirmationPopupComponent>;
      confirmationPopup = this.dialog.open(ConfirmationPopupComponent);
      confirmationPopup.componentInstance.message = message;

      return confirmationPopup.afterClosed();
   }

   public getProducts() {
      this.products = this.db.object('products');
      return this.products;
   }

   /*
      ----------  Cart Product Function  ----------
   */

   // Adding new Product to cart in localStorage
   public addToCart(data: any, type: any= '') {
      let products: any;
      products = JSON.parse(localStorage.getItem('cart_item')) || [];
      const productsLength = products.length;

      const toastOption: ToastOptions = {
         title: 'Adding Product To Cart',
         msg: 'Product adding to the cart',
         showClose: true,
         timeout: 1000,
         theme: 'material'
      };

      const found = products.some(function (el, index) {
         if (el.name == data.name) {
            if (!data.quantity) { data.quantity = 1; }
            products[index]['quantity'] = data.quantity;
            return  true;
         }
      });
      if (!found) { products.push(data); }

      if (productsLength == products.length) {
         toastOption.title = 'Product Already Added';
         toastOption.msg = 'You have already added this product to cart list';
      }

      if (type == 'wishlist') {
         this.removeLocalWishlistProduct(data);
      }

      this.toastyService.wait(toastOption);
      setTimeout(() => {
         localStorage.setItem('cart_item', JSON.stringify(products));
         this.calculateLocalCartProdCounts();
      }, 500);
   }

   public buyNow(data: any) {
      let products: any;
      products = JSON.parse(localStorage.getItem('cart_item')) || [];

      const found = products.some(function (el, index) {
         if (el.name == data.name) {
            if (!data.quantity) { data.quantity = 1; }
            products[index]['quantity'] = data.quantity;
            return  true;
         }
      });
      if (!found) { products.push(data); }

      localStorage.setItem('cart_item', JSON.stringify(products));
      this.calculateLocalCartProdCounts();
   }

   public updateAllLocalCartProduct(products: any) {
      localStorage.removeItem('cart_item');

      localStorage.setItem('cart_item', JSON.stringify(products));
   }

   // returning LocalCarts Product Count
   public calculateLocalCartProdCounts() {
      this.localStorageCartProducts = null;
      this.localStorageCartProducts = JSON.parse(localStorage.getItem('cart_item')) || [];
      this.navbarCartCount = +((this.localStorageCartProducts).length);
   }

   // Removing cart from local
   public removeLocalCartProduct(product: any) {
      const products: any = JSON.parse(localStorage.getItem('cart_item'));

      for (let i = 0; i < products.length; i++) {
         if (products[i].productId === product.productId) {
           products.splice(i, 1);
           break;
         }
      }

      const toastOption: ToastOptions = {
         title: 'Remove Product From Cart',
         msg: 'Product removing from cart',
         showClose: true,
         timeout: 1000,
         theme: 'material'
      };

      this.toastyService.wait(toastOption);
      setTimeout(() => {
         // ReAdding the products after remove
         localStorage.setItem('cart_item', JSON.stringify(products));
         this.calculateLocalCartProdCounts();
      }, 500);
   }

   /*
      ----------  Wishlist Product Function  ----------
   */

   // Adding new Product to Wishlist in localStorage
   public addToWishlist(data: any) {
      const toastOption: ToastOptions = {
         title: 'Adding Product To Wishlist',
         msg: 'Product adding to the wishlist',
         showClose: true,
         timeout: 1000,
         theme: 'material'
      };

      let products: any;
      products = JSON.parse(localStorage.getItem('wishlist_item')) || [];
      const productsLength = products.length;

      const found = products.some(function (el, index) {
         if (el.name == data.name) {
            if (!data.quantity) { data.quantity = 1; }
            products[index]['quantity'] = data.quantity;
            return  true;
         }
      });
      if (!found) { products.push(data); }

      if (productsLength == products.length) {
         toastOption.title = 'Product Already Added';
         toastOption.msg = 'You have already added this product to wishlist';
      }

      this.toastyService.wait(toastOption);
      setTimeout(() => {
         localStorage.setItem('wishlist_item', JSON.stringify(products));
         this.calculateLocalWishlistProdCounts();
      }, 500);

   }

   // returning LocalWishlist Product Count
   public calculateLocalWishlistProdCounts() {

      this.localStorageWishlist = null;
      this.localStorageWishlist = JSON.parse(localStorage.getItem('wishlist_item')) || [];
      this.navbarWishlistProdCount = +((this.localStorageWishlist).length);
   }

   // Removing Wishlist from local
   public removeLocalWishlistProduct(product: any) {
      const products: any = JSON.parse(localStorage.getItem('wishlist_item'));

      for (let i = 0; i < products.length; i++) {
         if (products[i].productId === product.productId) {
           products.splice(i, 1);
           break;
         }
      }

      const toastOption: ToastOptions = {
         title: 'Remove Product From Wishlist',
         msg: 'Product removing from wishlist',
         showClose: true,
         timeout: 1000,
         theme: 'material'
      };


      this.toastyService.wait(toastOption);
      setTimeout(() => {
         // ReAdding the products after remove
         localStorage.setItem('wishlist_item', JSON.stringify(products));
         this.calculateLocalWishlistProdCounts();
      }, 500);

   }

   public addAllWishListToCart(dataArray: any) {
      let a: any;
      a = JSON.parse(localStorage.getItem('cart_item')) || [];

      for (const singleData of dataArray) {
         a.push(singleData);
      }

      const toastOption: ToastOptions = {
         title: 'Adding All Product To Cart',
         msg: 'Products adding to the cart',
         showClose: true,
         timeout: 1000,
         theme: 'material'
      };

      this.toastyService.wait(toastOption);
      setTimeout(() => {
         localStorage.removeItem('wishlist_item');
         localStorage.setItem('cart_item', JSON.stringify(a));
         this.calculateLocalCartProdCounts();
         this.calculateLocalWishlistProdCounts();
      }, 500);

   }

   /**
    * getBlogList used to get the blog list.
    */
   public getBlogList() {
      let blogs: any;
      blogs = this.db.list('blogs');
      return blogs;
   }

   /**
    * getContactInfo used to get the contact infomation.
    */
   public getContactInfo() {
      let contact: any;
      contact = this.db.object('contact');
      return contact;
   }

   /**
    * getTermCondition used to get the term and condition.
    */
   public getTermCondition() {
      let termCondition: any;
      termCondition = this.db.list('term_condition');
      return termCondition;
   }

   /**
    * getPrivacyPolicy used to get the privacy policy.
    */
   public getPrivacyPolicy() {
      let privacyPolicy: any;
      privacyPolicy = this.db.list('privacy_policy');
      return privacyPolicy;
   }

   /**
    * getFaq used to get the faq.
    */
   public getFaq() {
      let faq: any;
      faq = this.db.object('faq');
      return faq;
   }

   /**
    * getProductReviews used to get the product review.
    */
   public getProductReviews() {
      let review: any;
      review = this.db.list('product_reviews');
      return review;
   }

   /**
    * Buy Product functions
    */
   public addBuyUserDetails(formdata) {
     localStorage.setItem('user', JSON.stringify(formdata));

      const product = JSON.parse(localStorage.getItem('cart_item'));
      localStorage.setItem('byProductDetails', JSON.stringify(product));
      this.buyUserCartProducts = JSON.parse(localStorage.getItem('byProductDetails'));

      localStorage.removeItem('cart_item');
      this.calculateLocalCartProdCounts();
   }

   public removeBuyProducts() {
      localStorage.removeItem('byProductDetails');
      this.buyUserCartProducts = JSON.parse(localStorage.getItem('byProductDetails'));
   }

   /**
    * getTeam used to get the team data.
    */
   public getTeam() {
      let team: any;
      team = this.db.list('team');
      return team;
   }

   /**
    * getTestimonial used to get the testimonial data.
    */
   public getTestimonial() {
      let testimonial: any;
      testimonial = this.db.object('testimonial');
      return testimonial;
   }

   /**
    * getMissionVision used to get the Mission and Vision data.
    */
   public getMissionVision() {
      let mission_vision: any;
      mission_vision = this.db.list('mission_vision');
      return mission_vision;
   }

   /**
    * getAboutInfo used to get the about info data.
    */
   public getAboutInfo() {
      let about_info: any;
      about_info = this.db.object('about_info');
      return about_info;
   }

}
