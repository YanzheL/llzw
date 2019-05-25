import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'embryo-Review',
  templateUrl: './Review.component.html',
  styleUrls: ['./Review.component.scss']
})
export class ReviewComponent implements OnInit {

   @Input() reviews : any;

   userReviews : any = [];

   constructor() { }

   ngOnInit() {
      this.getReviews();
   }

   public getReviews() {
      this.userReviews = [];
      if(this.reviews && this.reviews.length > 0) {
         for(let review of this.reviews) {
            for(let userReview of review.user_rating) {
               this.userReviews.push(userReview)
            }
         }
      }
   }

}
