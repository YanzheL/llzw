import { Component, OnInit, Input, OnChanges } from '@angular/core';

import { EmbryoService } from '../../Services/Embryo.service';

@Component({
  selector: 'embryo-BrandsLogo',
  templateUrl: './BrandsLogo.component.html',
  styleUrls: ['./BrandsLogo.component.scss']
})
export class BrandslogoComponent implements OnInit, OnChanges {

   @Input() isRTL : any;

   slideConfig : any;

   brandsLogoArray : any = [
      {
         id:1,
         image:"assets/images/client-logo-1.png"
      },
      {
         id:2,
         image:"assets/images/client-logo-2.png"
      },
      {
         id:3,
         image:"assets/images/client-logo-3.png"
      },
      {
         id:4,
         image:"assets/images/client-logo-4.png"
      },
      {
         id:5,
         image:"assets/images/client-logo-2.png"
      },
      {
         id:6,
         image:"assets/images/client-logo-1.png"
      },
      {
         id:7,
         image:"assets/images/client-logo-3.png"
      },

   ]

   constructor(public embryoService : EmbryoService) { }

   ngOnInit() {
   }

   ngOnChanges() {
      this.slideConfig = {
         infinite: true,
         centerMode: true,
         slidesToShow: 5,
         slidesToScroll: 2,
         autoplay: true,
         autoplaySpeed: 2000,
         rtl: this.isRTL,
         responsive: [
            {
               breakpoint: 768,
               settings: {
                  centerMode: true,
                  slidesToShow: 4,
                  slidesToScroll: 2,
                  autoplay: true,
                  autoplaySpeed: 2000
               }
            },
            {
               breakpoint: 480,
               settings: {
                  centerMode: true,
                  slidesToShow: 1,
                  autoplay: true,
                  autoplaySpeed: 2000
               }
            }
         ]
      };
   }

   

}
