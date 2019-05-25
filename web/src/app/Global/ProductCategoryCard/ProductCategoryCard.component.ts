import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'embryo-ProductCategoryCard',
  templateUrl: './ProductCategoryCard.component.html',
  styleUrls: ['./ProductCategoryCard.component.scss']
})
export class ProductCategoryCardComponent implements OnInit {

   @Input() productCategory : any;

   @Input() totalProducts : any;

   constructor() { }

   ngOnInit() {
   }

}
