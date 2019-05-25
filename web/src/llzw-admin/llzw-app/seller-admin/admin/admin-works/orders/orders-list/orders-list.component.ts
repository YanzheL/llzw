import { Component, OnInit } from '@angular/core';

import { ActivatedRoute, Router } from '@angular/router';
import { SelectionModel } from '@angular/cdk/collections';

import { OrderService } from '../../_services/order.service';
import { Order } from '../../_models/order.model';

@Component({
  selector: 'kt-orders-list',
  templateUrl: './orders-list.component.html',
  styleUrls: ['./orders-list.component.scss']
})
export class OrdersListComponent implements OnInit {

  editing = {};
  orders_rows = [];
  orders_temp = [];
  selected = [];

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private orderService: OrderService
  ) {
    this.orderService.getAll()
      .subscribe(data => {
        this.orders_temp = [...data];
        this.orders_rows = data;
        // setTimeout(() => { this.loadingIndicator = false; }, 1500);
      });
  }

  ngOnInit() {
  }

  editOrder(order: Order): void {
    window.localStorage.removeItem('editOrderId');
    window.localStorage.setItem('editOrderId', order.id.toString());
    this.router.navigate(['./edit'], { relativeTo: this.activatedRoute });
  }


  deleteOrder(order: Order): void {
    this.orderService.deleteById(order.id)
      .subscribe(data => {
        this.orders_temp = this.orders_temp.filter(u => u !== Order);
        this.orders_rows = this.orders_rows.filter(u => u !== Order);
      });
  }

}
