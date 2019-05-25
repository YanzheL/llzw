import { Component, OnInit} from '@angular/core';
import { Router, ActivatedRoute, Params }   from '@angular/router';
import {Observable, of, Subscription} from 'rxjs';

import { EmbryoService } from '../../../Services/Embryo.service';
import {PageEvent} from '@angular/material';

@Component({
  selector: 'app-ProductsList',
  templateUrl: './ProductsList.component.html',
  styleUrls: ['./ProductsList.component.scss']
})
export class ProductsListComponent implements OnInit {

   type          : any;
   pips          : boolean = true;
   tooltips      : boolean = true;
   category      : any;
   pageTitle     : string;
   subPageTitle  : string;
  products = [];
  length = 100;
  pageSize = 10;
  pageIndex = 0;
  pageSizeOptions = [5, 10, 25, 100];
  data = {'page': this.pageIndex, 'size': this.pageSize, 'valid': true};

   public subscribers: any = {};
   constructor(private route: ActivatedRoute,
               private router: Router,
               public embryoService: EmbryoService,
               ) {
   }

   ngOnInit() {
      this.route.params.subscribe(params => {
         this.route.queryParams.forEach(queryParams => {
            // this.category = queryParams['category'];
            // this.type   = null;
            // this.type = params['type'];

            this.getPageTitle();
         });
      });
     this.embryoService.getAllProduct(this.data).subscribe(res => {
       this.products = res.data.slice(this.data.page * this.data.size, this.data.page * this.data.size + this.data.size);
     });
     // this.getAllProducts();
   }
  change(event) {
     // this.length = 1000;
     this.data.size = event.pageSize;
     this.data.page = event.pageIndex;
     this.embryoService.getAllProduct(this.data).subscribe(res => {
       // this.products = res.data.slice(this.data.page * this.data.size, this.data.page * this.data.size + this.data.size);
       this.products = res.data;
     });
  }
   public getPageTitle() {
      this.pageTitle = null;
      this.subPageTitle = null;

      switch (this.type || this.category) {
         case undefined:
            this.pageTitle = "Fashion";
            this.subPageTitle="Explore your favourite fashion style.";
            break;

         case "gadgets":
            this.pageTitle = "Gadgets";
            this.subPageTitle="Check out our new gadgets.";
            break;

         case "accessories":
            this.pageTitle = "Accessories";
            this.subPageTitle="Choose the wide range of best accessories.";
            break;

         default:
            this.pageTitle = "Products";
            this.subPageTitle = null;
            break;
      }
   }

   public addToCart(value) {
      this.embryoService.addToCart(value);
   }

   public addToWishList(value) {
      this.embryoService.addToWishlist(value);
   }
   public transformHits(hits) {
      hits.forEach(hit => {
         hit.stars = [];
         for (let i = 1; i <= 5; i) {
           hit.stars.push(i <= hit.rating);
           i += 1;
         }
      });
      return hits;
   }

}
