import { Component, OnInit } from '@angular/core';
import { EmbryoService } from '../../../Services/Embryo.service';

@Component({
  selector: 'app-Faq',
  templateUrl: './Faq.component.html',
  styleUrls: ['./Faq.component.scss']
})
export class FaqComponent implements OnInit {

   faqData : any 

   constructor(public embryoService : EmbryoService) { }

   ngOnInit() {
      this.embryoService.getFaq().valueChanges().subscribe(res => {this.faqData = res});
   }

}
