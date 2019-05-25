import { Component, OnInit, AfterViewChecked} from '@angular/core';
import { MatTabChangeEvent } from '@angular/material';
import { ChangeDetectorRef } from '@angular/core';

import { EmbryoService } from '../../../Services/Embryo.service';

@Component({
  selector: 'app-homeone',
  templateUrl: './HomeOne.component.html',
  styleUrls: ['./HomeOne.component.scss']
})
export class HomeoneComponent implements OnInit, AfterViewChecked{

   blogList              : any;
   productReviews        : any;
   productsArray         : any;
   productsSliderData    : any;
   newProductsSliderData : any;
   slideConfig = {
      slidesToShow: 4,
      slidesToScroll:4,
      autoplay: true,
      autoplaySpeed: 2000,
      dots: true,
      responsive: [
         {
            breakpoint: 992,
            settings: {
               arrows: false,
               slidesToShow: 2,
               slidesToScroll:1
            }
         },
         {
            breakpoint: 768,
            settings: {
               arrows: false,
               slidesToShow: 2,
               slidesToScroll:2
            }
            },
         {
            breakpoint: 480,
            settings: {
               arrows: false,
               slidesToShow: 1,
               slidesToScroll:1
            }
         }
      ]
   };

   rtlSlideConfig = {
      slidesToShow: 4,
      slidesToScroll:4,
      autoplay: true,
      autoplaySpeed: 2000,
      dots: true,
      rtl: true,
      responsive: [
         {
            breakpoint: 992,
            settings: {
               arrows: false,
               slidesToShow: 2,
               slidesToScroll:1
            }
         },
         {
            breakpoint: 768,
            settings: {
               arrows: false,
               slidesToShow: 2,
               slidesToScroll:1
            }
         },
         {
            breakpoint: 480,
            settings: {
               arrows: false,
               slidesToShow: 1,
               slidesToScroll:1
            }
         }
      ]
   };
   constructor(public embryoService: EmbryoService,
               private cdRef : ChangeDetectorRef) {

      this.getBlogList();
      this.getProductRevies();

      this.embryoService.featuredProductsSelectedTab = 0;
      this.embryoService.newArrivalSelectedTab = 0;
   }

   ngOnInit() {
     this.getFeaturedProducts();
     this.getNewProductsSliderData();
   }

   ngAfterViewChecked() : void {
      this.cdRef.detectChanges();
   }

   public getFeaturedProducts() {
     const data = {'page': 0, 'size': 8, 'valid': true};
     this.embryoService.getFeaturedProducts(data).subscribe(res => {
       this.productsSliderData = res.data.slice(0, 8);
     });

     // console.log(this.productsSliderData);
     // this.embryoService.getProducts().valueChanges().subscribe(res => {this.productsArray = res});
   }

   public getNewProductsSliderData() {
     const data = {'page': 0, 'size': 8, 'valid': true};
     this.embryoService.getNewFeaturedProducts(data).subscribe(res => {
       this.newProductsSliderData = res.data.slice(0, 8);
     });

   }

   public getBlogList() {
      this.embryoService.getBlogList().valueChanges().subscribe(res => {this.blogList = res});
   }

   public addToCart(value) {
      this.embryoService.addToCart(value);
   }

   public getProductRevies() {
      this.embryoService.getProductReviews().valueChanges().subscribe(res => {this.productReviews = res });
   }

   public addToWishlist(value) {
      this.embryoService.addToWishlist(value);
   }

   public onFeaturedSelectedTab(tabIndex) {
      this.productsSliderData = null;
      switch (tabIndex) {
         case 0:
            this.productsSliderData = this.productsArray.men;
         break;

         case 1:
            this.productsSliderData = this.productsArray.women;
         break;

         case 2:
            this.productsSliderData = this.productsArray.gadgets;
         break;

         case 3:
            this.productsSliderData = this.productsArray.accessories;
         break;

         default:
            // code...
            break;
      }

      return true;
   }

   public onNewArrivalsSelectedTab(tabIndex) {
      this.newProductsSliderData = null;
      switch (tabIndex) {
         case 0:
            this.newProductsSliderData = this.productsArray.men;
         break;

         case 1:
            this.newProductsSliderData = this.productsArray.women;
         break;

         case 2:
            this.newProductsSliderData = this.productsArray.gadgets;
         break;

         case 3:
            this.newProductsSliderData = this.productsArray.accessories;
         break;

         default:
            // code...
            break;
      }

      return true;
   }
}
