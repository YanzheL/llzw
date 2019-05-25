import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'embryo-CtaGroup',
  templateUrl: './CTA-Group.component.html',
  styleUrls: ['./CTA-Group.component.scss']
})
export class CTAGroupComponent implements OnInit {

   callToActionArray : any = [
      {
         img_path:"assets/images/mobile.jpg",
         route :"/products/gadgets/11"
      },
      {
         img_path:"assets/images/sports.jpg",
         route :"/products/men/3"
      },
      {
         img_path:"assets/images/headphone.jpg",
         route :"/products/gadgets/10"
      },
      {
         img_path:"assets/images/t-shirts.jpg",
         route :"/products/men/5"
      },
      {
         img_path:"assets/images/watch.jpg",
         route :"/products/gadgets/12"
      },
      {
         img_path:"assets/images/shoes.jpg",
         route :"/products/men/3"
      }
   ]

   constructor() { }

   ngOnInit() {
   }

}
