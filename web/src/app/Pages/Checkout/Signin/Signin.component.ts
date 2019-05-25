import { Component, OnInit } from '@angular/core';
import { EmbryoService } from '../../../Services/Embryo.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-Signin',
  templateUrl: './Signin.component.html',
  styleUrls: ['./Signin.component.scss']
})
export class SigninComponent implements OnInit {


   constructor(public embryoService: EmbryoService, private router: Router) { }

   ngOnInit() {
     if (this.embryoService.isLogin()) {
       this.router.navigate(['checkout/payment']);
     }
   }

   public toggleRightSidenav() {
      this.embryoService.paymentSidenavOpen = !this.embryoService.paymentSidenavOpen;
   }

   public getCartProducts() {
      let total = 0;
      if(this.embryoService.localStorageCartProducts && this.embryoService.localStorageCartProducts.length>0) {
         for(let product of this.embryoService.localStorageCartProducts) {
            if(!product.quantity){
               product.quantity = 1;
            }
            total += (product.price*product.quantity);
         }
         total += (this.embryoService.shipping+this.embryoService.tax);
         return total;
      }
      return total;
   }

}
