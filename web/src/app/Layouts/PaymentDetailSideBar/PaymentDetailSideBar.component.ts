import { Component, OnInit,ViewChild } from '@angular/core';
import { LoadingBarService } from '@ngx-loading-bar/core';

import { EmbryoService } from '../../Services/Embryo.service';

@Component({
  selector: 'embryo-PaymentDetailSideBar',
  templateUrl: './PaymentDetailSideBar.component.html',
  styleUrls: ['./PaymentDetailSideBar.component.scss']
})
export class PaymentDetailSideBarComponent implements OnInit {

   cartProducts  : any;
   popupResponse : any;

   constructor(public embryoService: EmbryoService,
               private loadingBar: LoadingBarService) { }

   ngOnInit() {
   }

   public calculateTotalPrice() {
      let subtotal = 0;
      if(this.embryoService.localStorageCartProducts && this.embryoService.localStorageCartProducts.length>0) {
         for(let product of this.embryoService.localStorageCartProducts) {
            subtotal += (product.price *product.quantity) ;
         }
      }
      return subtotal;
   }

   public removeProduct(value) {
      let message = "Are you sure you want to delete this product?";
      this.embryoService.confirmationPopup(message).
         subscribe(res => {this.popupResponse = res},
                   err => console.log(err),
                   ()  => this.getPopupResponse(this.popupResponse, value)
                  );
   }

   public getPopupResponse(response, value) {
      if(response){
         this.embryoService.removeLocalCartProduct(value);
         this.embryoService.paymentSidenavOpen = false;
      }
   }

   public calculateProductSinglePrice(product:any, value: any) {
      let price = 0;
      product.quantity = value;
      price = product.price*value;
      return price;
   }

   public getTotalPrice() {
      let total = 0;
      if(this.embryoService.localStorageCartProducts && this.embryoService.localStorageCartProducts.length>0) {
         for(let product of this.embryoService.localStorageCartProducts) {
            total += (product.price*product.quantity);
         }
         total += (this.embryoService.shipping+this.embryoService.tax);
      }
      return total;
   }

}
