import { Component, OnInit } from '@angular/core';
import { EmbryoService } from '../../../Services/Embryo.service';

@Component({
  selector: 'app-HomeTwo',
  templateUrl: './HomeTwo.component.html',
  styleUrls: ['./HomeTwo.component.scss']
})
export class HomeTwoComponent implements OnInit {

   topProducts             : any;
   lighteningDealsProducts : any;
   
   constructor(public embryoService : EmbryoService) { }

   ngOnInit() {
      this.lighteningDeals();
      this.getProducts();
   }

   public lighteningDeals() {
      this.embryoService.getProducts().valueChanges()
         .subscribe(res => this.getLighteningDealsResponse(res));
   }

   public getLighteningDealsResponse(res) {
      let productsArray : any = [];
      this.lighteningDealsProducts = null;
      productsArray.push(this.last(res.men));
      productsArray.push(this.last(res.women));
      productsArray.push(this.last(res.gadgets));
      productsArray.push(this.last(res.accessories));

      this.lighteningDealsProducts = productsArray;
   }

   last(array) {
      return array[array.length - 1];
   }

   public getProducts() {
      this.embryoService.getProducts().valueChanges()
         .subscribe(res => this.getProductsResponse(res));
   }

   public getProductsResponse(res) {
      this.topProducts = null;
      let products = ((res.men.concat(res.women)).concat(res.gadgets)).concat(res.accessories);
      this.topProducts = products;
   }

   public addToCart(value) {
      this.embryoService.addToCart(value);
   }

}
