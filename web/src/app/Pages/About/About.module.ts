import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

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

import { AboutRoutes } from './About.routing';

import { AboutUsComponent } from './AboutUs/AboutUs.component';
import { ContactComponent } from './Contact/Contact.component';
import { FaqComponent } from './Faq/Faq.component';
import { TermAndConditionComponent } from './TermAndCondition/TermAndCondition.component';
import { PrivacyPolicyComponent } from './PrivacyPolicy/PrivacyPolicy.component';

import { GlobalModule } from '../../Global/Global.module';

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(AboutRoutes),
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
      FormsModule,
      GlobalModule,
      ReactiveFormsModule
  ],
  declarations: [
     AboutUsComponent,
     ContactComponent,
     FaqComponent,
     TermAndConditionComponent,
     PrivacyPolicyComponent
  ]
})
export class AboutModule { }
