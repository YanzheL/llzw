import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';
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
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {CardModule} from 'ngx-card/ngx-card';

import {FlexLayoutModule} from '@angular/flex-layout';
import {CheckoutRoutes} from './Checkout.routing';
import {PaymentComponent} from './Payment/Payment.component';
import {SigninComponent} from './Signin/Signin.component';
import {FinalReceiptComponent} from './FinalReceipt/FinalReceipt.component';

import {GlobalModule} from '../../Global/Global.module';
import {LoginGuard} from '../../Services/login.guard';


@NgModule({
    imports: [
        CommonModule,
        MatButtonModule,
        FlexLayoutModule,
        MatCardModule,
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
        RouterModule.forChild(CheckoutRoutes),
        GlobalModule,
        FormsModule,
        ReactiveFormsModule,
        CardModule
    ],
    declarations: [
        PaymentComponent,
        SigninComponent,
        FinalReceiptComponent
    ],
    providers: [LoginGuard]
})
export class CheckoutModule {
}
