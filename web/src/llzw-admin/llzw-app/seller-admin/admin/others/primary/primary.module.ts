import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {RouterModule, Routes} from '@angular/router'; // 元气少女
import {FormsModule, ReactiveFormsModule} from '@angular/forms'; // 元气少女
import {SharedModule} from '../../../../appsettings/shared/shared.module';

import {PrimaryComponent} from './primary.component';

import {Sheet2Component} from './sheet2/sheet2.component';


const routes: Routes = [
    {
        path: '',
        component: PrimaryComponent, // 默认路由
        children: [
            /*{
              path: '',
              redirectTo: 'sheet1', // 默认路由
              pathMatch: 'full'
            }, */

            {
                path: 'sheet2',
                component: Sheet2Component
            },
        ]
    }
];


@NgModule({

    declarations: [PrimaryComponent, Sheet2Component],
    imports: [
        CommonModule,

        RouterModule.forChild(routes), // 元气少女
        FormsModule,
        ReactiveFormsModule,

        SharedModule,

    ],
    exports: [RouterModule],  // 元气少女
})
export class PrimaryModule {
}
