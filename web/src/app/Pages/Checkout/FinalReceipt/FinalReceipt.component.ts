import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { EmbryoService } from '../../../Services/Embryo.service';

declare var $: any;

@Component({
  selector: 'app-FinalReceipt',
  templateUrl: './FinalReceipt.component.html',
  styleUrls: ['./FinalReceipt.component.scss']
})
export class FinalReceiptComponent implements OnInit {

   deliveryDate : any;
   products     : any;
   userDetails  : any;
   todayDate    : any = new Date();
   a = {}
   constructor(public embryoService: EmbryoService, public router: Router) {
      this.getDeliveryDate();
      this.userDetails = JSON.parse(localStorage.getItem("user"));
   }

   ngOnInit() {
   }

   public getDeliveryDate() {
      this.deliveryDate = new Date();
      this.deliveryDate.setDate(this.deliveryDate.getDate() + 5);
   }

   public calculateProductSinglePrice(product:any, value: any) {
      let price = 0;
      if(!value) {
         value = 1;
      }
      price = product.price*value;
      return price;
   }

   public calculateTotalPrice() {
      let subtotal = 0;
      if(this.embryoService.buyUserCartProducts && this.embryoService.buyUserCartProducts.length>0) {
         for(let product of this.embryoService.buyUserCartProducts) {
            if(!product.quantity){
               product.quantity = 1;
            }
            subtotal += (product.price *product.quantity) ;
         }
         return subtotal;
      }
      return subtotal;
   }

   public getTotalPrice() {
      let total = 0;
      if(this.embryoService.buyUserCartProducts && this.embryoService.buyUserCartProducts.length>0) {
         for(let product of this.embryoService.buyUserCartProducts) {
            if(!product.quantity){
               product.quantity = 1;
            }
            total += (product.price*product.quantity);
         }
         // total += (this.embryoService.shipping+this.embryoService.tax);
         return total;
      }
      return total;
   }

   public goToHome() {
     // 提交订单
     let createdOrder = null;
     const paymentInfo = {};
     const addressId = this.userDetails;
     const products = this.embryoService.buyUserCartProducts;
     const username = JSON.parse(localStorage.getItem('rootUser')).username;
     let order = {};
     order['addressId'] = addressId;
     order['customerId'] = username;
     for (const pro of products) {
        order['productId'] = pro.id;
        order['quantity'] = pro.quantity;
        this.embryoService.CreateMyOrder(order).subscribe(res => {
            createdOrder = res.data;
            paymentInfo['orderId'] = createdOrder.id;
            paymentInfo['subject'] = 'subject';
            paymentInfo['description'] = 'description';
            this.embryoService.CreatMyPayment(paymentInfo).subscribe( res => {
              open(res.data.orderString);
              if (!localStorage.getItem('paymentId')) {
                  localStorage.setItem('paymentId', [res.data.id].join('_') );
                } else {
                  let idstr = localStorage.getItem('paymentId');
                  let ids = idstr.split('_');
                  ids.push(res.data.id);
                  localStorage.setItem('paymentId', ids.join('_'));
              }
            });
        });
     }
      this.embryoService.removeBuyProducts();
      this.router.navigate(['/']);
   }
   // public printDiv()
   // {
   //    var printContents = $( $('#payment-receipt').html() );
   //    var originalContents = $('body > *').hide();
   //    $('body').append( printContents );
   //    window.print();
   //    printContents.remove();
   //    originalContents.show();
   // }

}
