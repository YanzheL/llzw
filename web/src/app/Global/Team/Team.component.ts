import {Component, Input, OnInit} from '@angular/core';

@Component({
    selector: 'embryo-Team',
    templateUrl: './Team.component.html',
    styleUrls: ['./Team.component.scss']
})
export class TeamComponent implements OnInit {

    @Input() data: any;

    constructor() {
    }

    ngOnInit() {
    }

}
