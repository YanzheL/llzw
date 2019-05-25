import { Component, OnInit } from '@angular/core';
import { EmbryoService } from '../../../Services/Embryo.service';

@Component({
  selector: 'app-TermAndCondition',
  templateUrl: './TermAndCondition.component.html',
  styleUrls: ['./TermAndCondition.component.scss']
})
export class TermAndConditionComponent implements OnInit {

   termContions : any ;

   constructor(public embryoService: EmbryoService) { }

   ngOnInit() {
      this.embryoService.getTermCondition().valueChanges().subscribe(res => {this.termContions = res});
   }

}
