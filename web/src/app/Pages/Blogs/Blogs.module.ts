import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
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
import { FlexLayoutModule } from '@angular/flex-layout';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { BlogsRoutes } from './Blogs.routing';
import { DetailsComponent } from './Details/Details.component';
import { GlobalModule } from '../../Global/Global.module';

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
      RouterModule.forChild(BlogsRoutes),
      GlobalModule,
      FormsModule,
      ReactiveFormsModule
  ],
  declarations: [
     DetailsComponent
  ]
})
export class BlogsModule { }
