import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {RouterModule, Routes} from '@angular/router'; // 元气少女
import {FormsModule, ReactiveFormsModule} from '@angular/forms'; // 元气少女
import {UserFormsComponent} from './user-forms.component';

import {SigninComponent} from './signin/signin.component';
import {SignoutComponent} from './signout/signout.component';
import {SignupComponent} from './signup/signup.component';
import {DemitComponent} from './demit/demit.component';


const routes: Routes = [
    {
        path: '',
        component: UserFormsComponent, // 默认路由
        children: [
            /*
          {
            path: '',
            redirectTo: 'signin', // 默认路由
            pathMatch: 'full'
          },
          */
            /*
               {
                 path: 'signin',
                 component: SigninComponent
               },
               */


            {
                path: 'signin',  // 将 signin 与子路由 loadChildren 挂钩
                loadChildren: './login/login.module#LoginModule'
            },


            {
                path: 'signout',
                component: SignoutComponent
            },
            {
                path: 'signup',
                component: SignupComponent
            },
            {
                path: 'demit',
                component: DemitComponent
            },
        ]
    }
];

@NgModule({
    declarations: [
        UserFormsComponent, // 声明 组件

        SigninComponent,
        SignoutComponent,
        SignupComponent,
        DemitComponent
    ],
    imports: [
        CommonModule,

        RouterModule.forChild(routes), // 元气少女
        FormsModule,
        ReactiveFormsModule,

        // SigninComponent, // 导入模块 增加此行
        // SignoutComponent, // 导入模块 增加此行
        // SignupComponent, // 导入模块 增加此行
        // DemitComponent // 导入模块 增加此行
    ],


    entryComponents: [

        // SigninComponent, // 导入模块 增加此行
        // SignoutComponent, // 导入模块 增加此行
        // SignupComponent, // 导入模块 增加此行
        // DemitComponent // 导入模块 增加此行
    ],
    exports: [RouterModule], // 元气少女
})
export class UserFormsModule {
}
