import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'embryo-AddToCardButton',
  templateUrl: './AddToCardButton.component.html',
  styleUrls: ['./AddToCardButton.component.scss']
})
export class AddToCardButtonComponent implements OnInit {

   @Input() product : any ;

   @Output() addToCart: EventEmitter<any> = new EventEmitter();

   constructor() { }

   ngOnInit() {
   }

   public addToCartProduct(product:any) {
      this.addToCart.emit(product);
   }
}
