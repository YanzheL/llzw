
import {interval as observableInterval} from 'rxjs';

import {map} from 'rxjs/operators';

import { Component, ElementRef, OnInit, OnDestroy, ViewEncapsulation, Input, Output, EventEmitter } from '@angular/core';
import { Observable, Subscription } from 'rxjs/Rx';

@Component({
	selector: 'embryo-TimerCountDown',
	templateUrl: './TimerCountDown.component.html',
	styleUrls: ['./TimerCountDown.component.scss'],
})

export class TimerCountDownComponent implements OnInit, OnDestroy {

   @Input() dateTime : any;

   private future: Date;
   private futureString: string;
   private diff: number;
   private $counter: Observable<number>;
   private subscription: Subscription;
   private message: string;

   hours : any;
   minutes : any;
   seconds : any;

   constructor() {}

   dhms(t) 
   {
      if(t && t > 0)
      {
         let days, hours, minutes, seconds;
         days = Math.floor(t / 86400);
         t -= days * 86400;
         hours = Math.floor(t / 3600) % 24;
         t -= hours * 3600;
         minutes = Math.floor(t / 60) % 60;
         t -= minutes * 60;
         seconds = t % 60;

         if(hours < 10)
         {
            this.hours = '0'+ hours;
         }
         else
         {
            this.hours = hours;
         }
         
         if(minutes < 10)
         {
            this.minutes = '0'+ minutes;
         }
         else
         {
            this.minutes = minutes;
         }

         if(seconds < 10)
         {
            this.seconds = '0'+ seconds;
         }
         else
         {
            this.seconds = seconds;
         }
      }
      else
      {
         this.hours = '00';
         this.minutes = '00';
         this.seconds = '00';
         this.subscription.unsubscribe();
      }
   }


   ngOnInit() 
   {
      if(this.dateTime)
      {
         this.future = this.dateTime;
         this.$counter = observableInterval(1000).pipe(map((x) => {
            this.diff = Math.floor((this.future.getTime() - new Date().getTime()) / 1000);
            return x;
         }));

         this.subscription = this.$counter.subscribe((x) => this.dhms(this.diff));
      }
   }

   ngOnDestroy(): void 
   {
      this.subscription.unsubscribe();
   }
}
