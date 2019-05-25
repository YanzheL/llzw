import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'embryo-Grid3',
  templateUrl: './Grid3.component.html',
  styleUrls: ['./Grid3.component.scss']
})
export class Grid3Component implements OnInit {

   @Input() blogList : any;

   constructor() { }

   ngOnInit() {
   }

}
