import { NgModule }   from '@angular/core'; 
import { RouterModule } from '@angular/router';
import { CommonModule }   from '@angular/common';
import { MatButtonModule, 
         MatBadgeModule,
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
import { BarRatingModule } from "ngx-bar-rating";
import { AgmCoreModule } from '@agm/core';
import { FormsModule, ReactiveFormsModule }    from '@angular/forms';
import { SlickCarouselModule } from 'ngx-slick-carousel';

import { BrandslogoComponent } from './BrandsLogo/BrandsLogo.component';
import { SalesComponent } from './Sales/Sales.component';
import { FeaturesComponent } from './Features/Features.component';
import { SubscribeOneComponent } from './Subscribe/SubscribeOne/SubscribeOne.component';
import { DealOfTheDayComponent } from './DealOfTheDay/DealOfTheDay.component';
import { SocialShareComponent } from './SocialShare/SocialShare.component';
import { RatingComponent } from './Rating/Rating.component';
import { AddToCardButtonComponent } from './AddToCardButton/AddToCardButton.component';
import { ReviewPopupComponent } from './ReviewPopup/ReviewPopup.component';
import { HeaderCartComponent } from './HeaderCart/HeaderCart.component';
import { WishListComponent } from './WishList/WishList.component';
import { ConfirmationPopupComponent } from './ConfirmationPopup/ConfirmationPopup.component';
import { PageTitleComponent } from './PageTitle/PageTitle.component';
import { HomePageOneSliderComponent } from './Slider/HomePageOneSlider/HomePageOneSlider.component';
import { TimerCountDownComponent } from './TimerCountDown/TimerCountDown.component';
import { MapComponent } from './Map/Map.component';
import { CurrencyDropDownComponent } from './CurrencyDropDown/CurrencyDropDown.component';
import { LanguageDropDownComponent } from './LanguageDropDown/LanguageDropDown.component';
import { TestimonialComponent } from './Testimonial/Testimonial.component';
import { TeamComponent } from './Team/Team.component';
import { ContactFormComponent } from './ContactForm/ContactForm.component';
import { MissionVisionComponent } from './MissionVision/MissionVision.component';
import { AboutInfoComponent } from './AboutInfo/AboutInfo.component';
import { ImgZoomComponent } from './ImgZoom/ImgZoom.component';
import { CommonSignInComponent } from './CommonSignIn/CommonSignIn.component';
import { ProductCardComponent } from './ProductCard/ProductCard.component';
import { HeaderUserProfileDropdownComponent } from './HeaderUserProfileDropdown/HeaderUserProfileDropdown.component';
import { AppLogoComponent } from './AppLogo/AppLogo.component';
import { LighteningDealsComponent } from './LighteningDeals/LighteningDeals.component';
import { TopProductsComponent } from './TopProducts/TopProducts.component';
import { SubscribeTwoComponent } from './Subscribe/SubscribeTwo/SubscribeTwo.component';
import { HomePageTwoSliderComponent } from './Slider/HomePageTwoSlider/HomePageTwoSlider.component';
import { CTAGroupComponent } from './CallToAction/CTA-Group/CTA-Group.component';
import { CTATwoComponent } from './CallToAction/CTA-Two/CTA-Two.component';
import { CollectionGalleryComponent } from './CollectionGallery/CollectionGallery.component';
import { ProductCategoryCardComponent } from './ProductCategoryCard/ProductCategoryCard.component';
import { CTASingleBannerComponent } from './CallToAction/CTA-SingleBanner/CTA-SingleBanner.component';
import { DownloadAppSectionComponent } from './DownloadAppSection/DownloadAppSection.component';
import { HomePageThreeSliderComponent } from './Slider/HomePageThreeSlider/HomePageThreeSlider.component';
import { NewProductsCardComponent } from './NewProductsCard/NewProductsCard.component';


@NgModule({
   imports: [
      CommonModule,
      RouterModule,
      MatBadgeModule,
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
      BarRatingModule,
      AgmCoreModule.forRoot({
         apiKey: 'AIzaSyC9PnuRk42kbCPMOvsfHpn40r5SoyN38zI',
         libraries: ['places']
      }),
      FormsModule,
      ReactiveFormsModule,
      SlickCarouselModule
   ],
   declarations: [
      BrandslogoComponent,
      SalesComponent,
      FeaturesComponent,
      SubscribeOneComponent,
      DealOfTheDayComponent,
      SocialShareComponent,
      RatingComponent,
      AddToCardButtonComponent,
      ReviewPopupComponent,
      HeaderCartComponent,
      WishListComponent,
      ConfirmationPopupComponent,
      PageTitleComponent,
      HomePageOneSliderComponent,
      TimerCountDownComponent,
      MapComponent,
      CurrencyDropDownComponent,
      LanguageDropDownComponent,
      TestimonialComponent,
      TeamComponent,
      ContactFormComponent,
      MissionVisionComponent,
      AboutInfoComponent,
      ImgZoomComponent,
      CommonSignInComponent,
      ProductCardComponent,
      HeaderUserProfileDropdownComponent,
      AppLogoComponent,
      LighteningDealsComponent,
      TopProductsComponent,
      SubscribeTwoComponent,
      HomePageTwoSliderComponent,
      CTAGroupComponent,
      CTATwoComponent,
      CollectionGalleryComponent,
      ProductCategoryCardComponent,
      CTASingleBannerComponent,
      DownloadAppSectionComponent,
      HomePageThreeSliderComponent,
      NewProductsCardComponent
   ],
   exports: [
      BrandslogoComponent,
      SalesComponent,
      FeaturesComponent,
      SubscribeOneComponent,
      DealOfTheDayComponent,
      SocialShareComponent,
      RatingComponent,
      AddToCardButtonComponent,
      HeaderCartComponent,
      WishListComponent,
      PageTitleComponent,
      HomePageOneSliderComponent,
      TimerCountDownComponent,
      MapComponent,
      CurrencyDropDownComponent,
      LanguageDropDownComponent,
      TestimonialComponent,
      TeamComponent,
      ContactFormComponent,
      MissionVisionComponent,
      AboutInfoComponent,
      ImgZoomComponent,
      CommonSignInComponent,
      ProductCardComponent,
      HeaderUserProfileDropdownComponent,
      AppLogoComponent,
      LighteningDealsComponent,
      TopProductsComponent,
      SubscribeTwoComponent,
      HomePageTwoSliderComponent,
      CTAGroupComponent,
      CTATwoComponent,
      CollectionGalleryComponent,
      ProductCategoryCardComponent,
      CTASingleBannerComponent,
      DownloadAppSectionComponent,
      HomePageThreeSliderComponent,
      NewProductsCardComponent
   ],
   entryComponents : [
      ReviewPopupComponent,
      ConfirmationPopupComponent
   ]
})
export class GlobalModule {}
