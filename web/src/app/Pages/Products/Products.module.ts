import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';
import {NgAisModule} from 'angular-instantsearch';
import {FormsModule} from '@angular/forms';

import {ProductsRoutes} from './Products.routing';
import {ProductsListComponent} from './ProductsList/ProductsList.component';
import {DetailPageComponent} from './DetailPage/DetailPage.component';
import {GlobalModule} from '../../Global/Global.module';
import {TemplatesModule} from '../../Templates/Templates.module';

import {FlexLayoutModule} from '@angular/flex-layout';
import {
    MatButtonModule,
    MatCardModule,
    MatCheckboxModule,
    MatChipsModule,
    MatDatepickerModule,
    MatDialogModule,
    MatExpansionModule,
    MatGridListModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatMenuModule,
    MatNativeDateModule,
    MatPaginatorModule,
    MatProgressBarModule,
    MatProgressSpinnerModule,
    MatRadioModule,
    MatSelectModule,
    MatSidenavModule,
    MatSliderModule,
    MatSnackBarModule,
    MatTableModule,
    MatTabsModule,
    MatToolbarModule,
    MatTooltipModule
} from '@angular/material';


@NgModule({
    imports: [
        CommonModule,
        RouterModule.forChild(ProductsRoutes),
        FlexLayoutModule,
        MatCardModule,
        MatButtonModule,
        MatMenuModule,
        MatToolbarModule,
        MatIconModule,
        MatInputModule,
        MatDatepickerModule,
        MatNativeDateModule,
        MatProgressSpinnerModule,
        MatTableModule,
        MatExpansionModule,
        MatSelectModule,
        MatSnackBarModule,
        MatTooltipModule,
        MatChipsModule,
        MatListModule,
        MatSidenavModule,
        MatTabsModule,
        MatProgressBarModule,
        MatCheckboxModule,
        MatSliderModule,
        MatRadioModule,
        MatDialogModule,
        MatGridListModule,
        GlobalModule,
        TemplatesModule,
        NgAisModule,
        FormsModule,
        MatPaginatorModule,
    ],
    declarations: [
        ProductsListComponent,
        DetailPageComponent
    ]
})
export class ProductsModule {
}
