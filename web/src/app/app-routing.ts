import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes} from '@angular/router';

import { MainComponent } from './Main/Main.component';
import { HomeoneComponent } from './Pages/Home/HomeOne/HomeOne.component';
import { HomeTwoComponent } from './Pages/Home/HomeTwo/HomeTwo.component';
import { HomeThreeComponent } from './Pages/Home/HomeThree/HomeThree.component';
import { CartComponent } from './Pages/Cart/Cart.component';
import { NotFoundComponent } from './Pages/NotFound/NotFound.component';

export const AppRoutes: Routes = [
   {
      path : '',
      redirectTo: 'home',
      pathMatch: 'full',
   }, {
      path : '',
      component : MainComponent,
      children: [
         {
            path : 'home',
            component : HomeoneComponent
         },
        {
            path : 'home-two',
            component : HomeTwoComponent
         },
         {
            path : 'home-three',
            component : HomeThreeComponent
         },
         {
            path: 'products',
            loadChildren: './Pages/Products/Products.module#ProductsModule'
         },
         {
            path: 'cart',
            component: CartComponent
         },
         {
          path: 'not-found',
          component: NotFoundComponent
         },
         {
            path: 'session',
            loadChildren: './Pages/Session/Session.module#SessionModule'
         },
         {
            path: 'checkout',
            loadChildren: './Pages/Checkout/Checkout.module#CheckoutModule'
         },
         {
            path: '',
            loadChildren: './Pages/About/About.module#AboutModule'
         },
         {
            path: 'blogs',
            loadChildren: './Pages/Blogs/Blogs.module#BlogsModule'
         },
         {
            path: 'account',
            loadChildren: './Pages/UserAccount/UserAccount.module#UserAccountModule'
         },
         {
            // 元气少女
            path: 'seller',
            loadChildren: '../llzw-admin/llzw-app/llzw-app.module#LlzwAppModule'
         },
      ]
   },
   {
      path: '**',
      redirectTo: 'not-found'
   }
]
