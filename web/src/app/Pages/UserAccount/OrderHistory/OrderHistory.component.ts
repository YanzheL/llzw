import { Component, OnInit } from '@angular/core';
import {EmbryoService} from '../../../Services/Embryo.service';



@Component({
  selector: 'app-OrderHistory',
  templateUrl: './OrderHistory.component.html',
  styleUrls: ['./OrderHistory.component.scss']
})
export class OrderHistoryComponent implements OnInit {

   displayedColumns: string[] = ['id', 'quantity', 'carrierName', 'paid', 'action'];
   dataSource = [];
value=0
   constructor(public embryoService: EmbryoService) { }

   ngOnInit() {
     this.verify();
     this.changeIndex(0);
   }
   changeIndex($event) {
     let data = { };
      if ($event === 0) {
        data = {'page': '0', 'size': '10', 'customerId': JSON.parse(localStorage.getItem('rootUser')).username, 'deliveryConfirmed': 'false', 'paid': 'false'};
      } else if ($event === 1) {
        data = {'page': '0', 'size': '10', 'customerId': JSON.parse(localStorage.getItem('rootUser')).username, 'deliveryConfirmed': 'false', 'paid': 'true'};
      } else {
        data = {'page': '0', 'size': '10', 'customerId': JSON.parse(localStorage.getItem('rootUser')).username, 'deliveryConfirmed': 'true', 'paid': 'true'};
      }
     this.getOrderHistory(data);
   }
  getOrderHistory(data) {
    this.embryoService.getOrderHistory(data).subscribe(res => {
      this.dataSource = res.data;
    });
  }
  verify() {
    if (localStorage.getItem('paymentId')) {
      let idstr = localStorage.getItem('paymentId');
      let ids = idstr.split('_');
      for (let i = 0; i < ids.length; i++) {
        if ( (i + 1) !== ids.length){
          this.embryoService.verify(ids[i]).subscribe();
        } else {
          this.embryoService.verify(ids[i]).subscribe(res => {
            // this.getOrderHistory();
          });
        }
      }
      localStorage.setItem('paymentId', [].join('_') );
    } else {
      // this.getOrderHistory();
    }
  }
}
