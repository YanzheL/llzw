import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { NgAisModule } from 'angular-instantsearch';
import { SessionRoutes } from './Session.routing';
import { GlobalModule } from '../../Global/Global.module';
import { TemplatesModule } from '../../Templates/Templates.module';

import { FlexLayoutModule } from '@angular/flex-layout';
import { MatButtonModule,
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
   MatGridListModule
} from '@angular/material';

import { RegisterComponent } from './Register/Register.component';
import { SignInComponent } from './SignIn/SignIn.component';
import { ForgotPasswordComponent } from './ForgotPassword/ForgotPassword.component';
import { ThankYouComponent } from './ThankYou/ThankYou.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

@NgModule({
   imports: [
      CommonModule,
      RouterModule.forChild(SessionRoutes),
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
      ReactiveFormsModule,
   ],
   declarations: [
      RegisterComponent,
      SignInComponent,
      ThankYouComponent,
      ForgotPasswordComponent
   ]
})
export class SessionModule { }
