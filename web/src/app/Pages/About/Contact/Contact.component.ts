import { Component, OnInit } from '@angular/core';
import { EmbryoService } from '../../../Services/Embryo.service';

@Component({
  selector: 'app-contact',
  templateUrl: './Contact.component.html',
  styleUrls: ['./Contact.component.scss']
})
export class ContactComponent implements OnInit {

   contactInfo  : any;
   emailPattern : any = /\S+@\S+\.\S+/;

   constructor(public embryoService : EmbryoService) {
      this.embryoService.getContactInfo().valueChanges().subscribe(res => {this.contactInfo = res});
   }

   ngOnInit() {
      
   }
}

