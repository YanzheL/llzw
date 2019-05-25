import {Component, OnInit} from '@angular/core';

import {ActivatedRoute, Router} from '@angular/router';
// Material

import {ProductService} from '../../_services/product.service';
import {Product} from '../../_models/product.model';

import {MeService} from '../../../../../services/services/me.service';
import {Me} from '../../../../../services/models/Me'; // 元气少女
@Component({
    selector: 'kt-products-list',
    templateUrl: './products-list.component.html',
    styleUrls: ['./products-list.component.scss'],
    // changeDetection: ChangeDetectionStrategy.OnPush
})
export class ProductsListComponent implements OnInit {

    //products: Array<any>;
    editing = {};
    products_rows = [];
    products_temp = [];
    selected = [];
    me = new Me; // 属性 声明为对象
    constructor(
        //public dialog: MatDialog,

        private router: Router,
        private activatedRoute: ActivatedRoute,
        private productService: ProductService,
        public meService: MeService, // 元气少女
    ) {
        this.productService.getAll()
            .subscribe(data => {
                this.products_temp = [...data];
                this.products_rows = data;
                // setTimeout(() => { this.loadingIndicator = false; }, 1500);
            });

        // 元气少女
        this.meService.getMe()
            .subscribe(data => {
                this.me = data;
                console.log('this.me.username', this.me.username)
                // setTimeout(() => { this.loadingIndicator = false; }, 1500);
            });

    }

    ngOnInit() {
    }

    // 编辑 现有的
    editProduct(product: Product): void {
        window.localStorage.removeItem('operateType');
        window.localStorage.setItem('operateType', '1');

        window.localStorage.removeItem('targetProductId');
        window.localStorage.setItem('targetProductId', product.id.toString());
        this.router.navigate(['./edit'], {relativeTo: this.activatedRoute});
    }

    // 以全新方式新建
    newAddProduct(): void {
        window.localStorage.removeItem('operateType');
        window.localStorage.setItem('operateType', '2');
        this.router.navigate(['./add'], {relativeTo: this.activatedRoute});
    }

    // 以克隆方式新建
    duplicateAddProduct(product: Product): void {
        window.localStorage.removeItem('operateType');
        window.localStorage.setItem('operateType', '3');

        window.localStorage.removeItem('targetProductId');
        window.localStorage.setItem('targetProductId', product.id.toString());
        this.router.navigate(['./add'], {relativeTo: this.activatedRoute});
    }


    deleteProduct(product: Product): void {
        this.productService.deleteById(product.id)
            .subscribe(data => {
                this.products_temp = this.products_temp.filter(u => u !== product);
                this.products_rows = this.products_rows.filter(u => u !== product);

            });
    }

    // 编辑 现有的
    addStock(product: Product): void {
        // window.localStorage.removeItem('operateType');
        // window.localStorage.setItem('operateType', '1');

        window.localStorage.removeItem('targetProductId');
        window.localStorage.setItem('targetProductId', product.id.toString());
        this.router.navigate(['../stocks/add'], {relativeTo: this.activatedRoute});
    }


}
