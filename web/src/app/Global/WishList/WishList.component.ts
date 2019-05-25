import { Component, OnInit, OnChanges, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'embryo-WishList',
  templateUrl: './WishList.component.html',
  styleUrls: ['./WishList.component.scss']
})
export class WishListComponent implements OnInit, OnChanges {

   @Input() wishListProducts : any;

   @Input() count        : number;

   @Input() currency      : string;

   @Output() removeWishListData : EventEmitter<any> = new EventEmitter();

   @Output() addAllWishlistToCart : EventEmitter<any> = new EventEmitter();

   @Output() addToCart: EventEmitter<any> = new EventEmitter();

   hiddenBadge = true;

   constructor() { }

   ngOnInit() {
   }

   ngOnChanges() {
      if(this.count && this.count != 0) {
         this.hiddenBadge = false;
      } else {
         this.hiddenBadge = true;
      }
   }

   public confirmationPopup(product:any) {
      this.removeWishListData.emit(product);
   }

   public addAllToCart() {
      this.addAllWishlistToCart.emit(this.wishListProducts);
   }

   public calculatePrice(product) {
      let total = null;
      total = product.price*product.quantity;
      return total;
   }

   public addToCartProduct(product) {
      this.addToCart.emit(product);
   }

}
