import { Component, OnInit, HostListener } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { Observable, of as observableOf, interval ,  Subscription } from 'rxjs';
import { map, take, delay, withLatestFrom, finalize, tap } from 'rxjs/operators';
import { LoadingBarService } from '@ngx-loading-bar/core';
import { EmbryoService } from '../Services/Embryo.service';
import { MenuItems } from '../Core/menu/menu-items/menu-items';
import { Directionality } from '@angular/cdk/bidi';
import { MediaObserver, MediaChange } from '@angular/flex-layout';
import { TranslateService } from '@ngx-translate/core';
import { Meta, Title } from "@angular/platform-browser";

declare var $: any;

@Component({
  selector: 'app-main',
  templateUrl: './Main.component.html',
  styleUrls: ['./Main.component.scss']
})
export class MainComponent implements OnInit {

   timer = 0;
   isRtl: any;
   private _dirChangeSubscription = Subscription.EMPTY;
   currentUrl : any;

   constructor(private loader : LoadingBarService,
               public embryoService : EmbryoService,
               public menuItems: MenuItems,
               dir: Directionality,
               public translate: TranslateService,
               private router: Router,
               meta: Meta, title: Title) {

      title.setTitle('Embryo - Angular Material Design eCommerce Template');

      meta.addTags([
         { name: 'author',   content: 'The IRON Network'},
         { name: 'keywords', content: ' angular, angular 2, angular 6, angular 7, angular material, clean, creative, ecommerce, frontend, online store, shop, shopping, store, typescript, ui framework '},
         { name: 'description', content: 'Embryo is an E-Commerce angular 7 based template with material design. It also comes with Angular cli. Now you have all the power to maintain your ecommerce site. Responsive design gives your user to use in any modern devices. Clean Code gives you the power to customize the code as per as your requirments. Embryo has all the basics functionality which is required in ecommerce site. Rtl design makes the multi-language support with more easy way.' }
      ]);

      if(this.embryoService.isDirectionRtl) {
         this.isRtl = 'rtl';
      } else {
         this.isRtl = 'ltr';
      }

      this.router.events
        .subscribe((event) => {
          if (event instanceof NavigationEnd) {
            this.currentUrl = event.url;
          }
        });
   }

   ngOnInit() {
      this.startTimer();
   }

   public startTimer() {
      this.timer = 0;
      interval(1000).pipe(
      take(3),
      tap(value => { this.timer = value + 1; }),
       finalize(() => this.loader.complete()),
      ).subscribe();

      // We're sure that subscription has been made, we can start loading bar service
      this.loader.start();
   }

   public hideSideNav() {
      this.embryoService.sidenavOpen = false;
   }

   public changeDirection() {
      if(this.isRtl == "rtl") {
         this.isRtl = "ltr";
         this.embryoService.isDirectionRtl = false;
      } else {
         this.isRtl = "rtl"
         this.embryoService.isDirectionRtl = true;
      }

      this.embryoService.featuredProductsSelectedTab = 0;
      this.embryoService.newArrivalSelectedTab = 0;
   }

   /**
    * On window scroll add class header-fixed.
    */
   @HostListener('window:scroll', ['$event'])
   onScrollEvent($event){
      let scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop || 0;
      if (scrollTop >= 200) {
           $("app-main").addClass("header-fixed");
       } else {
           $("app-main").removeClass("header-fixed");
       }
   }
}
