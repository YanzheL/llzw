import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
// Angular in memory
import {HttpClientInMemoryWebApiModule} from 'angular-in-memory-web-api';

import {RouterModule, Routes} from '@angular/router';

import {FakeApiService} from './fake-api/fake-api.service';
import {environment} from '../llzw-environments/environment';
import {LlzwAppComponent} from './llzw-app.component';

import {MenuService} from './bodypage/theme/components/menu/menu.service';

import {PERFECT_SCROLLBAR_CONFIG, PerfectScrollbarConfigInterface, PerfectScrollbarModule} from 'ngx-perfect-scrollbar';
import {AppSettings} from './appsettings/app.settings';

import {SharedModule} from './appsettings/shared/shared.module';
import {HorizontalMenuComponent} from './bodypage/theme/components/menu/horizontal-menu/horizontal-menu.component';
import {VerticalMenuComponent} from './bodypage/theme/components/menu/vertical-menu/vertical-menu.component';
import {SidenavComponent} from './bodypage/theme/components/sidenav/sidenav.component';
import {DashboardModule} from './seller-admin/dashboard/dashboard.module';
import {AdminModule} from './seller-admin/admin/admin.module';
// import { HttpConfigInterceptor } from './services/interceptor/httpconfig.interceptor'; // 元气少女 拦截器
// import {ErrorDialogService} from './services/error-dialog/errordialog.service'; // 元气少女 拦截器
import {ErrorDialogComponent} from './services/error-dialog/errordialog.component'; // 元气少女 拦截器
import {MeService} from './services/services/me.service'; // 元气少女 Me 服务
import {FileService} from './services/services/file.service'; // 元气少女 文件上传服务
import {FileDropModule} from 'ngx-file-drop'; // 元气少女 FileDropModule 从桌面拖拽文件

const DEFAULT_PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
    wheelPropagation: true,
    suppressScrollX: true
};

// import { UserMenuComponent } from './bodypage/theme/components/user-menu/user-menu.component';


// import { TopInfoContentComponent } from './bodypage/theme/components/top-info-content/top-info-content.component';
// import { FavoritesComponent } from './bodypage/theme/components/favorites/favorites.component';
// import { FullScreenComponent } from './bodypage/theme/components/fullscreen/fullscreen.component';
// import { FlagsMenuComponent } from './bodypage/theme/components/flags-menu/flags-menu.component';

// import { ApplicationsComponent } from './bodypage/theme/components/applications/applications.component';
// import { MessagesComponent } from './bodypage/theme/components/messages/messages.component';
// import { MessagesService  } from './bodypage/theme/components/messages/messages.service';
// import { DashboardComponent } from './dashboard/dashboard.component';

const routes: Routes = [
    {
        // 重定向 好像未生效
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full',

    },
    {
        path: '',
        component: LlzwAppComponent,
        children: [
            //     {
            //         path: 'dashboard',
            //         component: DashboardComponent
            //     },
            {
                path: 'dashboard',
                loadChildren: './seller-admin/dashboard/dashboard.module#DashboardModule'
            },

            {
                path: 'admin',
                loadChildren: './seller-admin/admin/admin.module#AdminModule'
            },
        ],
    }];

@NgModule({
    declarations: [
        LlzwAppComponent,
        ErrorDialogComponent,
        HorizontalMenuComponent, // 元气少女
        VerticalMenuComponent, // 元气少女
        SidenavComponent, // 元气少女

        // UserMenuComponent,
        // TopInfoContentComponent,  // 元气少女
        // FavoritesComponent,
        // FullScreenComponent,
        // FlagsMenuComponent,

        // ApplicationsComponent,
        // MessagesComponent,


        // DashboardComponent, // 元气少女

    ],
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        PerfectScrollbarModule,

        HttpClientModule,
        environment.isMockEnabled ? HttpClientInMemoryWebApiModule.forRoot(FakeApiService, {
            passThruUnknownUrl: true,
            dataEncapsulation: false
        }) : [],

        RouterModule.forChild(routes),

        SharedModule,
        DashboardModule, // 元气少女
        AdminModule, // TODO 此处有问题
        FileDropModule, // 元气少女 FileDropModule 从桌面拖拽文件

        // ErrorDialogComponent, // 元气少女 拦截器

    ],
    exports: [RouterModule],
    providers: [
        // MessagesService, // 头部

        MeService, // 元气少女 Me服务
        FileService, // 元气少女 文件上传服务

        MenuService,
        AppSettings,
        {provide: PERFECT_SCROLLBAR_CONFIG, useValue: DEFAULT_PERFECT_SCROLLBAR_CONFIG},

        // ErrorDialogService, // 元气少女拦截器
        // { provide: HTTP_INTERCEPTORS, useClass: HttpConfigInterceptor, multi: true } // 元气少女拦截器
    ],
    //  entryComponents: [ErrorDialogComponent], // 元气少女拦截器

    bootstrap: [
        // LlzwAppComponent
    ]
})
export class LlzwAppModule {
}
