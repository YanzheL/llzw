import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { RouterModule, Routes } from '@angular/router'; // 元气少女
import { FormsModule, ReactiveFormsModule } from '@angular/forms'; // 元气少女

import { SharedModule } from '../../../appsettings/shared/shared.module'; // 元气少女

import { AdminFormsComponent } from './admin-forms.component';

import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { RealnameVerificationComponent } from './realname-verification/realname-verification.component';
import { UpdatePasswordComponent } from './update-password/update-password.component';
import { ProductsComponent } from './products/products.component';
import { StocksComponent } from './stocks/stocks.component';
import { OrdersComponent } from './orders/orders.component';
import { AddressesComponent } from './addresses/addresses.component';

const routes: Routes = [
  {
    path: '',
    component: AdminFormsComponent, // 默认路由
    children: [
			/*{
				path: '',
				redirectTo: 'sheet1', // 默认路由
				pathMatch: 'full'
			}, */
      {
        path: 'login',
        component: LoginComponent
      },
      {
        path: 'register',
        component: RegisterComponent
      },
      {
        path: 'realname-verification',
        component: RealnameVerificationComponent
      },
      {
        path: 'update-password',
        component: UpdatePasswordComponent
      },
      {
        path: 'products',
        component: ProductsComponent
      },
      {
        path: 'stocks',
        component: StocksComponent
      },
      {
        path: 'orders',
        component: OrdersComponent
      },
      {
        path: 'addresses',
        component: AddressesComponent
      },
    ]
  }
];


@NgModule({
  declarations: [
    AdminFormsComponent,
    LoginComponent,
    RegisterComponent,
    RealnameVerificationComponent,
    UpdatePasswordComponent,
    ProductsComponent,
    StocksComponent,
    OrdersComponent,
    AddressesComponent],
  imports: [
    CommonModule,

    RouterModule.forChild(routes), // 元气少女
    FormsModule,
    ReactiveFormsModule,

    SharedModule,

  ],
  exports: [RouterModule],  // 元气少女

})
export class AdminFormsModule { }
