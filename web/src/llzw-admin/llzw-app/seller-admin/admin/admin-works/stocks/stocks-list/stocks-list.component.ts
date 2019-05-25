import { Component, OnInit } from '@angular/core';

import { ActivatedRoute, Router } from '@angular/router';
import { SelectionModel } from '@angular/cdk/collections';

import { StockService } from '../../_services/stock.service';
import { Stock } from '../../_models/stock.model';

@Component({
  selector: 'kt-stocks-list',
  templateUrl: './stocks-list.component.html',
  styleUrls: ['./stocks-list.component.scss']
})
export class StocksListComponent implements OnInit {

  editing = {};
  stocks_rows = [];
  stocks_temp = [];
  selected = [];

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private stockService: StockService
  ) {
    this.stockService.getAll()
      .subscribe(data => {
        this.stocks_temp = [...data];
        this.stocks_rows = data;
        // setTimeout(() => { this.loadingIndicator = false; }, 1500);
      });
  }

  ngOnInit() {
  }

  addStock(): void {
    window.localStorage.removeItem('addStock');
    window.localStorage.setItem('addStock', '1');
    this.router.navigate(['./edit'], { relativeTo: this.activatedRoute });
  }

  editStock(stock: Stock): void {
    window.localStorage.removeItem('editStockId');
    window.localStorage.setItem('editStockId', stock.id.toString());
    this.router.navigate(['./edit'], { relativeTo: this.activatedRoute });
  }


  deleteStock(stock: Stock): void {
    this.stockService.deleteById(stock.id)
      .subscribe(data => {
        this.stocks_temp = this.stocks_temp.filter(u => u !== stock);
        this.stocks_rows = this.stocks_rows.filter(u => u !== stock);
      });
  }

}

