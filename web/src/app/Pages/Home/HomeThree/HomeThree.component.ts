import { Component, OnInit } from '@angular/core';
import { EmbryoService } from '../../../Services/Embryo.service';

@Component({
  selector: 'app-HomeThree',
  templateUrl: './HomeThree.component.html',
  styleUrls: ['./HomeThree.component.scss']
})
export class HomeThreeComponent implements OnInit {

   categories : any = {
      clothing     : [],
      shoes        : [],
      accessories  : [],
      gadgets      : []
   }
   products : any;
   allProducts : any;
   newProductsSliderData : any;
   slideConfig = {
      slidesToShow: 4,
      slidesToScroll:2,
      autoplay: true,
      autoplaySpeed: 2000,
      dots: false,
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

   rtlSlideConfig = {
      slidesToShow: 4,
      slidesToScroll:4,
      autoplay: true,
      autoplaySpeed: 2000,
      dots: false,
      rtl: true,
      responsive: [
         {
            breakpoint: 992,
            settings: {
               arrows: false,
               slidesToShow: 2,
               slidesToScroll:2
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

   constructor(public embryoService : EmbryoService) { }

   ngOnInit() {
      this.getProducts();
   }

   public getProducts() {
      this.embryoService.getProducts().valueChanges()
         .subscribe(res => this.getProductsResponse(res));
   }

   public getProductsResponse(res) {
      this.products = res;
      this.allProducts = ((res.men.concat(res.women)).concat(res.gadgets)).concat(res.accessories);
     
      for(let product of this.allProducts) {
         switch (product.category_type) {
            case "clothing":
               this.categories.clothing.push(product);
               break;

            case "shoes":
               this.categories.shoes.push(product);
               break;

            case "accessories":
               this.categories.accessories.push(product);
               break;

            case "gadgets":
               this.categories.gadgets.push(product);
               break;
            
            default:
               // code...
               break;
         }
      }
   }

   public onNewArrivalsSelectedTab(tabIndex) {
      this.newProductsSliderData = null;
      switch (tabIndex) {
         case 0:
            this.newProductsSliderData = this.allProducts;
         break;

         case 1:
            this.newProductsSliderData = this.products.men;
         break;

         case 2:
            this.newProductsSliderData = this.products.women;
         break;

         case 3:
            this.newProductsSliderData = this.products.gadgets;
         break;
         
         default:
            // code...
            break;
      }

      return true;
   }

   public addToCart(value) {
      this.embryoService.addToCart(value);
   }

   public addToWishlist(value) {
      this.embryoService.addToWishlist(value);
   }

}
