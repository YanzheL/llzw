import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
// import { DragulaModule } from 'ng2-dragula';
import { DragulaService, DragulaModule } from 'ng2-dragula';
import { SharedModule } from '../../appsettings/shared/shared.module';
import { DashboardComponent } from './dashboard.component';

export const routes = [
  {
    path: '',
    component: DashboardComponent,
    pathMatch: 'full'
  }
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    // DragulaModule.forRoot(),

    DragulaModule,
    SharedModule
  ],
  declarations: [
    DashboardComponent
  ],
providers: [ DragulaService ]



})
export class DashboardModule { }
