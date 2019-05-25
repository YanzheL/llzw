import { Component, OnInit, Input, Output, EventEmitter} from '@angular/core';

@Component({
  selector: 'embryo-NewProductsCard',
  templateUrl: './NewProductsCard.component.html',
  styleUrls: ['./NewProductsCard.component.scss']
})
export class NewProductsCardComponent implements OnInit {

   @Input() product : any;

   @Input() currency : string;

   @Output() addToCart: EventEmitter<any> = new EventEmitter();

   @Output() addToWishlist: EventEmitter<any> = new EventEmitter();

   constructor() { }

   ngOnInit() {
   }

   public addToCartProduct(value:any) {
      this.addToCart.emit(value);
   }

   public productAddToWishlist(value:any) {
      this.addToWishlist.emit(value);
   }

}
