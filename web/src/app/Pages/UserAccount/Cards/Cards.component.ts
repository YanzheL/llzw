import { Component, OnInit } from '@angular/core';
import {EmbryoService} from '../../../Services/Embryo.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-Cards',
  templateUrl: './Cards.component.html',
  styleUrls: ['./Cards.component.scss']
})
export class CardsComponent implements OnInit {
  id = '';
  order = null;
  product = [];
  constructor(public embryoService: EmbryoService, private route: ActivatedRoute) {
    this.route.params.subscribe(params => {
      this.route.queryParams.forEach(queryParams => {
        this.id = queryParams['id'];
      });
    });
  }

  ngOnInit() {
    this.getSpecificeOrder(this.id);
  }
  getSpecificeOrder(id) {
    this.embryoService.getSpecificOrder(id).subscribe(res => {
      this.order = res.data;
      this.embryoService.getSingleProduct(res.data.product).subscribe( res1 => {
        this.product = res1.data;
        console.log(this.product);
      });
     });
  }
  ToPay(id) {
    this.embryoService.getRelatedPaymentsforOrder(id).subscribe(res1 => {
      if (!localStorage.getItem('paymentId')) {
        localStorage.setItem('paymentId', [res1.data.id].join('_') );
      } else {
        let idstr = localStorage.getItem('paymentId');
        let ids = idstr.split('_');
        ids.push(res1.data.id);
        localStorage.setItem('paymentId', ids.join('_'));
      }
      this.embryoService.paymentsRetry(res1.data.id).subscribe(res2 => {
        open(res2.data);
      });
    });

  }
  deliveryConfirm(id) {
    this.embryoService.confirmationPopup('您确认收货吗？').subscribe(res => {
      if (res === 'yes') {
        this.embryoService.deliveryConfirm(id).subscribe(res => {
          this.order.deliveryConfirmed = true;
        });
      }
    } );

  }
}
