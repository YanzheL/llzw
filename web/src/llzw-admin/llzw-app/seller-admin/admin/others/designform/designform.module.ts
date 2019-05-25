import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { RouterModule, Routes } from '@angular/router'; // 元气少女
import { FormsModule, ReactiveFormsModule } from '@angular/forms'; // 元气少女

import { DesignformComponent } from './designform.component'; // 元气少女
 import { UserFormsModule } from './user-forms/user-forms.module'; // 导入模块 增加此行

const routes: Routes = [
  {
    path: '',
    component: DesignformComponent, // 默认路由
    children: [
      {
        path: 'user-forms',  // 将 user-forms 与子路由 loadChildren 挂钩
        loadChildren: './user-forms/user-forms.module#UserFormsModule'
      },
    ]
  }
];

@NgModule({
  declarations: [
    DesignformComponent,
     // UserFormsModule
    ],
  imports: [
    CommonModule,

    RouterModule.forChild(routes), // 元气少女
    FormsModule,
    ReactiveFormsModule,

    UserFormsModule,  // 导入模块 增加此行
  ],

  exports: [RouterModule], // 元气少女
})
export class DesignformModule { }
