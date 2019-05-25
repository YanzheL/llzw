import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { EmbryoService } from '../../../Services/Embryo.service';
declare var $: any;

@Component({
  selector: 'HeaderThree',
  templateUrl: './HeaderThree.component.html',
  styleUrls: ['./HeaderThree.component.scss']
})
export class HeaderThreeComponent implements OnInit {

   cartProducts     : any;
   popupResponse    : any;
   wishlistProducts : any;

   constructor(public embryoService: EmbryoService) { }

   ngOnInit() {
   }

   public toggleSidebar()
   {
      this.embryoService.sidenavOpen = !this.embryoService.sidenavOpen;
   }

   public toggleSearch() {
      $('app-main').toggleClass('form-open');
   }

   public openConfirmationPopup(value:any) {
      let message = "Are you sure you want to delete this product?";
      this.embryoService.confirmationPopup(message).
         subscribe(res => {this.popupResponse = res},
                   err => console.log(err),
                   ()  => this.getPopupResponse(this.popupResponse, value, 'cart')
                  );
   }

   public getPopupResponse(response:any, value:any, type) {
      if(response) {
         if(type == 'cart'){
            this.embryoService.removeLocalCartProduct(value);
         } else {
            this.embryoService.removeLocalWishlistProduct(value);
         }
      }
   }

   public addAllWishlistToCart(values:any) {
      this.embryoService.addAllWishListToCart(values);
   }

   public openWishlistConfirmationPopup(value:any) {
      let message = "Are you sure you want to add all products?";
      this.embryoService.confirmationPopup(message).
         subscribe(res => {this.popupResponse = res},
                   err => console.log(err),
                   ()  => this.getPopupResponse(this.popupResponse, value, 'wishlist')
                  );
   }

   public selectedCurrency(value) {
      this.embryoService.currency = value;
   }

   public selectedLanguage(value) {
      this.embryoService.language = value;
   }

   public addToCart(value) {
      this.embryoService.addToCart(value, 'wishlist');
   }

}
