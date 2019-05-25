import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, Params }   from '@angular/router';
import { EmbryoService } from '../../../Services/Embryo.service';

@Component({
  selector: 'app-DetailPage',
  templateUrl: './DetailPage.component.html',
  styleUrls: ['./DetailPage.component.scss']
})
export class DetailPageComponent implements OnInit {

   id                : any;
   type              : any;
   apiResponse       : any;
   singleProductData : any;
   productsList      : any;
  singleProduct = {};
  labels = ['新鲜水果','chip2','chip3','chip4']
  category = ['海鲜','水果']
   constructor(private route: ActivatedRoute,
              private router: Router,
              public embryoService: EmbryoService) {

   }

   ngOnInit() {
      this.route.params.subscribe(res => {
         this.id = res.id;
         this.getData();
      });
     this.getSingleProduct();
   }

   public getData() {
      this.embryoService.getProducts().valueChanges().subscribe(res => this.checkResponse(res));
   }

   public getSingleProduct() {
    this.embryoService.getSingleProduct(this.id).subscribe(res => {
      // const data = {'feature': '新鲜水果|chip3|chip4', 'category': '海鲜.水果.素材'};
      this.singleProduct = res.data;
      // this.singleProduct = data;
      this.dealFeaAndCate(this.singleProduct['feature'], this.singleProduct['category']);
    });
   }

   dealFeaAndCate(featurestr, categorystr) {
    if (featurestr && categorystr) {
      let featurearr = featurestr.split('|');
      let categoryarr = categorystr.split('.');
      this.labels = featurearr;
      this.category = categoryarr;
    }

   }

   public checkResponse(response) {

      // this.productsList = null;
      // this.productsList = response[this.type];
      // for(let data of this.productsList)
      // {
      //    if(data.id == this.id) {
      //       this.singleProductData = data;
      //       break;
      //    }
      // }
   }

   public addToCart(value) {
      this.embryoService.addToCart(value);
   }

   public addToWishList(value) {
      this.embryoService.addToWishlist(value);
   }
}
