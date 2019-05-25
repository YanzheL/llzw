import { Component, OnInit, Input, OnChanges, Renderer2, ElementRef, ViewChild, AfterViewInit} from '@angular/core';
import { Router, ActivatedRoute, Params }   from '@angular/router';
declare var $: any;

import {EmbryoService } from '../../Services/Embryo.service';

@Component({
  selector: 'embryo-ShopDetails',
  templateUrl: './ShopDetails.component.html',
  styleUrls: ['./ShopDetails.component.scss']
})
export class ShopDetailsComponent implements OnInit, OnChanges {

   @Input() detailData : any;
   @Input() currency   : string;

   mainImgPath   : string;
   totalPrice    : any;
   type          : any;
   colorsArray   : string[] = ["Red", "Blue", "Yellow", "Green"];
   sizeArray     : number[] = [36,38,40,42,44,46,48];
   quantityArray : number[] = [1,2,3,4,5,6,7,8,9,10];
   productReviews : any;
   images = [
     'assets/images/men/3-item-a.jpg',
     'assets/images/men/3-item-b.jpg',
     'assets/images/men/3-item-c.jpg',
     'assets/images/men/3-item-d.jpg',
     'assets/images/men/3-item-e.jpg'
   ];
  chipclass= ['chipclass1','chipclass2','chipclass3','chipclass4']
  @Input() labels ;
  @Input() category ;
   constructor(private route: ActivatedRoute,
               private router: Router,
               public embryoService : EmbryoService
               ) {
      this.embryoService.getProductReviews().valueChanges().subscribe(res => {this.productReviews = res});
   }

   ngOnInit() {
      this.mainImgPath = this.detailData.mainImageFiles ? this.detailData.mainImageFiles[0] : '';
      this.totalPrice  = this.detailData.price;

      this.route.params.subscribe(res => {
         this.type = null;
         this.type = res.type;
      });
      // this.setQuantityArray();
   }
  // setQuantityArray () {
  //    let num = 10;
  //    if ( this.stocks! = null && this.stocks.currentQuantity < num) {
  //      num = this.stocks.currentQuantity;
  //    }
  //    for (let i = 1; i <= num; i++) {
  //      this.quantityArray[i] = i;
  //    }
  // }
   ngOnChanges() {
      this.mainImgPath = null;
      this.totalPrice  = null;
      this.mainImgPath = this.detailData.mainImageFiles ? this.detailData.mainImageFiles[0] : '';
      this.totalPrice  = this.detailData.price;
   }

   /**
    * getImagePath is used to change the image path on click event.
    */
   public getImagePath(imgPath: string, index:number) {
      $('.p-link').removeClass('border-active');
      this.mainImgPath = imgPath;
      $("#"+index+"_img").addClass('border-active');
   }

   public calculatePrice(detailData:any, value: any) {
      detailData['quantity'] = value;
      this.totalPrice = detailData.price*value;
   }

   public reviewPopup(detailData) {
      let reviews : any = null;
      for(let review of this.productReviews) {
         // if((review.id == detailData.id) && (review.type == detailData.type) && (review.category == detailData.category)){
         //    singleProduct = review;
         //    break;
         // }

        reviews = review.user_rating;
      }

      this.embryoService.reviewPopup(detailData, reviews);
   }

   public addToWishlist(value:any) {
      this.embryoService.addToWishlist(value);
   }

   public addToCart(value:any) {
      this.embryoService.addToCart(value);
   }

   public buyNow(value:any) {
      this.embryoService.buyNow(value);
      this.router.navigate(['/checkout']);
   }

}
