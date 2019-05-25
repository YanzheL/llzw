import { Component, OnInit, Input, OnChanges} from '@angular/core';

@Component({
  selector: 'embryo-HomePageOneSlider',
  templateUrl: './HomePageOneSlider.component.html',
  styleUrls: ['./HomePageOneSlider.component.scss']
})
export class HomePageOneSliderComponent implements OnInit, OnChanges {

   @Input() isRTL : boolean = false;

   slideConfig : any;

   homePageOneSlideData = [
     {
       theClass: 'slider-img-1',
       theTitle: 'Big Sale',
       des: 'Women\'s Summer Collection',
       to: '/products/women'
     },
     {
       theClass: 'slider-img-2',
       theTitle: '50% Off',
       des: 'Men\'s Winter Collection',
       to: '/products/men'
     },
     {
       theClass: 'slider-img-3',
       theTitle: 'Today\'s Special',
       des: 'Sale On Jackets',
       to: '/products/men'
     }
   ];

   constructor() { }

   ngOnInit() {
   }

   ngOnChanges() {
      this.slideConfig = {
         slidesToShow: 1,
         slidesToScroll:1,
         autoplay: true,
         autoplaySpeed: 2000,
         dots: false,
         rtl: this.isRTL,
         responsive: [
          {
             breakpoint: 768,
             settings: {
                arrows: false,
                slidesToShow: 1
             }
             },
          {
             breakpoint: 480,
             settings: {
                arrows: false,
                slidesToShow: 1
             }
          }
         ]
      };
   }

}
