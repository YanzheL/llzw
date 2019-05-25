import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule, Routes} from '@angular/router'; // 元气少女
// import { BrowserModule } from '@angular/platform-browser'; // 元气少女
import {FormsModule, ReactiveFormsModule} from '@angular/forms'; // 元气少女
import {NgxDatatableModule} from '@swimlane/ngx-datatable'; // ngx-datatable表格 元气少女
import {SharedModule} from '../../../appsettings/shared/shared.module';
// API 数据服务
import {ProductService} from './_services/product.service';
import {StockService} from './_services/stock.service';
import {PaymentService} from './_services/payment.service';
import {OrderService} from './_services/order.service';

import {AdminWorksComponent} from './admin-works.component';

import {ProductsListComponent} from './products/products-list/products-list.component';
// import { ProductEditComponent } from './products/product-edit/product-edit.component';
import {ProductAddComponent} from './products/product-add/product-add.component';

import {StocksListComponent} from './stocks/stocks-list/stocks-list.component';
// import { StockEditComponent } from './stocks/stock-edit/stock-edit.component';
import {StockAddComponent} from './stocks/stock-add/stock-add.component';

import {NgxTableComponent} from './ngx-table/ngx-table.component';

import {PaymentsListComponent} from './payments/payments-list/payments-list.component';
import {PaymentEditComponent} from './payments/payment-edit/payment-edit.component';
import {PaymentAddComponent} from './payments/payment-add/payment-add.component';

import {OrdersListComponent} from './orders/orders-list/orders-list.component';
import {OrderEditComponent} from './orders/order-edit/order-edit.component';
import {OrderAddComponent} from './orders/order-add/order-add.component';

import {FileDropModule} from 'ngx-file-drop'; //文件拖拽
import {FlexModule} from '@angular/flex-layout' // 排版

// import { HttpClientModule } from '@angular/common/http';  //此处不能导入 否则内部 假数据用不了


const routes: Routes = [
    {
        path: '',
        component: AdminWorksComponent, // 默认路由
        children: [
            // /*{
            // 				path: '',
            // 				redirectTo: 'payments', // 默认路由
            // 				pathMatch: 'full'
            // 			}, */
            {
                path: 'products',
                component: ProductsListComponent
            },
            {
                path: 'products/add',
                component: ProductAddComponent
            },
            {
                path: 'products/edit',
                //component: ProductEditComponent
                component: ProductAddComponent
            },
            // {
            //     path: 'products/edit/:id',
            //     component: ProductEditComponent
            // },

            {
                path: 'stocks',
                component: StocksListComponent
            },
            {
                path: 'stocks/edit',
                component: StockAddComponent
            },
            {
                path: 'stocks/add',
                component: StockAddComponent
            },

            {
                path: 'orders',
                component: OrdersListComponent
            },
            {
                path: 'orders/edit',
                component: OrderEditComponent
            },
            {
                path: 'orders/add',
                component: OrderAddComponent
            },

            {
                path: 'payments',
                component: PaymentsListComponent
            },
            {
                path: 'payments/edit',
                component: PaymentEditComponent
            },
            {
                path: 'payments/add',
                component: PaymentAddComponent
            },

            {
                path: 'ngx-table',
                component: NgxTableComponent
            },
        ]
    }
];


@NgModule({
    declarations: [
        AdminWorksComponent,

        ProductsListComponent,
        //ProductEditComponent,
        ProductAddComponent,

        StocksListComponent,
        //StockEditComponent,
        StockAddComponent,

        NgxTableComponent,

        PaymentsListComponent,
        PaymentEditComponent,
        PaymentAddComponent,

        OrdersListComponent,
        OrderEditComponent,
        OrderAddComponent,

    ],
    imports: [
        CommonModule,
        // BrowserModule,  // 元气少女
        NgxDatatableModule,  // 元气少女

        RouterModule.forChild(routes), // 元气少女
        FormsModule,
        ReactiveFormsModule,
        FileDropModule, //文件拖拽
        FlexModule, // 排版

        SharedModule,
        //  HttpClientModule, //此处不能导入 否则内部 假数据用不了


    ],
    exports: [
        RouterModule,
    ],
    providers: [
        ProductService, // 元气少女
        StockService, // 元气少女
        PaymentService, // 元气少女
        OrderService, // 元气少女
        // { provide: MAT_DATE_LOCALE, useValue: 'zh-TW' },

    ],
})
export class AdminWorksModule {
}
