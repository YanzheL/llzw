import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'embryo-CurrencyDropDown',
  templateUrl: './CurrencyDropDown.component.html',
  styleUrls: ['./CurrencyDropDown.component.scss']
})
export class CurrencyDropDownComponent implements OnInit {

   @Output() selectedCurrency : EventEmitter<any> = new EventEmitter();

   @Input() selectedValue : string = "USD";

   currencyArray : any = [
      {
         code:"INR",
         name:"Indian Rupee",
         image:"assets/images/india.png"
      },
      {
         code:"USD",
         name:"United States Doller",
         image:"assets/images/united-states.png"
      },
      {
         code:"AUD",
         name:"Australian Doller",
         image:"assets/images/australia.png"
      },
      {
         code:"GBP",
         name:"United Kingdom",
         image:"assets/images/united-kingdom.png"
      },
      {
         code:"ILR",
         name:"Israeli Shekel",
         image:"assets/images/israel.png"
      },
      {
         code:"EUR",
         name:"France",
         image:"assets/images/france.png"
      }
   ]

   constructor() { }

   ngOnInit() {
   }

   selectionChange(data) {
      if(data && data.value){
         this.selectedCurrency.emit(data.value);
      }
   }

}
