import { BrowserModule} from '@angular/platform-browser';
import { NgModule} from '@angular/core';
import { HttpClientModule, HttpClient} from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule }    from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
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
import { LoadingBarRouterModule } from '@ngx-loading-bar/router';
import { LoadingBarModule } from '@ngx-loading-bar/core';
import { AngularFireModule } from '@angular/fire';
import { AngularFirestoreModule } from '@angular/fire/firestore';
import { AngularFireDatabaseModule } from '@angular/fire/database';
import { ToastaModule } from 'ngx-toasta';
import { BidiModule } from '@angular/cdk/bidi';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { SlickCarouselModule } from 'ngx-slick-carousel';

import { environment } from '../environments/environment';

import { AppRoutes } from './app-routing';
import { GlobalModule } from './Global/Global.module';
import { TemplatesModule } from './Templates/Templates.module';
import { MenuItems } from './Core/menu/menu-items/menu-items';

import { EmbryoService } from './Services/Embryo.service';

import { AppComponent } from './app.component';
import { MainComponent } from './Main/Main.component';
import { HeaderOneComponent } from './Layouts/Header/HeaderOne/HeaderOne.component';
import { HeaderTwoComponent } from './Layouts/Header/HeaderTwo/HeaderTwo.component';
import { HeaderThreeComponent } from './Layouts/Header/HeaderThree/HeaderThree.component';
import { FooterOneComponent } from './Layouts/Footer/FooterOne/FooterOne.component';
import { FooterTwoComponent } from './Layouts/Footer/FooterTwo/FooterTwo.component';
import { MenuComponent } from './Layouts/Menu/Menu/Menu.component';
import { HomeoneComponent } from './Pages/Home/HomeOne/HomeOne.component';
import { HomeTwoComponent } from './Pages/Home/HomeTwo/HomeTwo.component';
import { HomeThreeComponent } from './Pages/Home/HomeThree/HomeThree.component';
import { CartComponent } from './Pages/Cart/Cart.component';
import { NotFoundComponent } from './Pages/NotFound/NotFound.component';
import { SideBarMenuComponent } from './Layouts/Menu/SidebarMenu/SidebarMenu.component';
import { PaymentDetailSideBarComponent } from './Layouts/PaymentDetailSideBar/PaymentDetailSideBar.component';
import { FixedHeaderComponent } from './Layouts/Header/FixedHeader/FixedHeader.component';


/********** Custom option for ngx-translate ******/
export function createTranslateLoader(http: HttpClient) {
   return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    AppComponent,
    MainComponent,
    HomeoneComponent,
    HeaderOneComponent,
    FooterOneComponent,
    MenuComponent,
    SideBarMenuComponent,
    CartComponent,
    NotFoundComponent,
    PaymentDetailSideBarComponent,
    HomeTwoComponent,
    HeaderTwoComponent,
    FooterTwoComponent,
    HomeThreeComponent,
    HeaderThreeComponent,
    FixedHeaderComponent
  ],
  imports: [
    BrowserModule.withServerTransition({appId: 'embryo-seo-pre'}),
    BrowserAnimationsModule,
    RouterModule.forRoot(AppRoutes, {onSameUrlNavigation: 'reload'}),
    GlobalModule,
    TemplatesModule,
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
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    LoadingBarRouterModule,
    LoadingBarModule.forRoot(),
    AngularFireModule.initializeApp(environment.firebase, 'embryo'),
    AngularFirestoreModule,
    AngularFireDatabaseModule,
    ToastaModule.forRoot(),
    BidiModule,
    TranslateModule.forRoot({
       loader: {
          provide: TranslateLoader,
          useFactory: createTranslateLoader,
          deps: [HttpClient]
       }
    }),
    SlickCarouselModule
  ],
   providers: [
      MenuItems,
      EmbryoService
   ],
   bootstrap: [AppComponent]
})
export class AppModule { }
