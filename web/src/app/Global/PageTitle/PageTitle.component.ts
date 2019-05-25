import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'embryo-PageTitle',
  templateUrl: './PageTitle.component.html',
  styleUrls: ['./PageTitle.component.scss']
})
export class PageTitleComponent implements OnInit {

   @Input() heading    : string;
   @Input() subHeading : string;

   constructor() { }

   ngOnInit() {
   }

}
