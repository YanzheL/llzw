import { Component, OnInit, Input } from '@angular/core';
declare var $: any;

@Component({
  selector: 'embryo-DealOfTheDay',
  templateUrl: './DealOfTheDay.component.html',
  styleUrls: ['./DealOfTheDay.component.scss']
})
export class DealOfTheDayComponent implements OnInit {

   @Input() singleProduct : any; 

   @Input() currency : string;

   counterDateTime = new Date(new Date().setHours(20,0,0,0));

   constructor() {}

   ngOnInit() {
   }

   /**
    * getOfferImagePath is used to change the image path on click event. 
    */
   public getOfferImagePath(imgPath: any, index:number) {
      $('.selected_img').removeClass('border-active');
      this.singleProduct.image = imgPath;
      $("#"+index+"_img").addClass('border-active');
   }

}
