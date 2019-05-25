import { Component, OnInit } from '@angular/core';
import { EmbryoService } from '../../../Services/Embryo.service';

@Component({
  selector: 'app-PrivacyPolicy',
  templateUrl: './PrivacyPolicy.component.html',
  styleUrls: ['./PrivacyPolicy.component.scss']
})
export class PrivacyPolicyComponent implements OnInit {

   privacyPolicyData : any;

   constructor(public embryoService : EmbryoService) { }

   ngOnInit() {
      this.embryoService.getPrivacyPolicy().valueChanges().subscribe(res => {this.privacyPolicyData = res});
   }

}
