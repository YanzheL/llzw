import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CommonModule } from '@angular/common';

import { AdminComponent } from './admin.component';

// 环境
import { environment } from '../../../llzw-environments/environment';
// Fake API Angular-in-memory
import { HttpClientInMemoryWebApiModule } from 'angular-in-memory-web-api';
// Core
// 假数据服务
import { FakeApiService } from '../../fake-api/fake-api.service';

import { AdminWorksModule } from './admin-works/admin-works.module'; // 元气少女
import { AdminFormsModule } from './admin-forms/admin-forms.module'; // 元气少女
import { PrimaryModule } from './others/primary/primary.module'; // 元气少女
import { DesignformModule } from './others/designform/designform.module'; // 元气少女
import { AdminUserAccountModule } from './admin_user_account/admin_user_account.module'; // 元气少女
// import { AppSettings } from '../../appsettings/app.settings'; // 元气少女 view头部用



const routes: Routes = [
  {
    path: '',
    component: AdminComponent,
    // canActivate: [AuthGuard],
    children: [
      {
        // 元气少女
        path: 'admin-forms',
        loadChildren: './admin-forms/admin-forms.module#AdminFormsModule'
      },

      {
        // 元气少女
        path: 'admin-works',
        loadChildren: './admin-works/admin-works.module#AdminWorksModule'
      },

      {
        // 元气少女
        path: 'admin-works',
        loadChildren: './admin_user_account/admin_user_account.module#AdminUserAccountModule'
      },

      {
        // 元气少女
        path: 'primary',
        loadChildren: './others/primary/primary.module#PrimaryModule'
      },

      {
        // 元气少女
        path: 'designform',
        loadChildren: './others/designform/designform.module#DesignformModule'
      },
    ]
  },
];


@NgModule({
  declarations: [AdminComponent],
  imports: [
     CommonModule,
    RouterModule.forChild(routes),

    environment.isMockEnabled ? HttpClientInMemoryWebApiModule.forFeature(FakeApiService, {
      passThruUnknownUrl: true,
      dataEncapsulation: false
    }) : [],

    // 模块在 imports 导入
    AdminWorksModule, // 元气少女
    AdminUserAccountModule, // 元气少女
    AdminFormsModule, // 元气少女
    PrimaryModule, // 元气少女
    DesignformModule, // 元气少女
  ],
  exports: [RouterModule],
  providers: [
    //  AppSettings,  // 元气少女 view头部用
  ],
})
export class AdminModule { }
