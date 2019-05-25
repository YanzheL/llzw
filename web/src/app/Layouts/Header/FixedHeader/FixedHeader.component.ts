import { Component, OnInit } from '@angular/core';
import { EmbryoService } from '../../../Services/Embryo.service';

@Component({
  selector: 'embryo-FixedHeader',
  templateUrl: './FixedHeader.component.html',
  styleUrls: ['./FixedHeader.component.scss']
})
export class FixedHeaderComponent implements OnInit {

  constructor(private embryoService : EmbryoService) { }

  ngOnInit() {
  }

  public toggleSidebar()
   {
      this.embryoService.sidenavOpen = !this.embryoService.sidenavOpen;
   }

}
