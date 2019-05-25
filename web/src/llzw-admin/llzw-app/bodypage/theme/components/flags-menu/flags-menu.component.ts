import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {AppSettings} from '../../../../appsettings/app.settings';
import {Settings} from '../../../../appsettings/app.settings.model';

@Component({
    selector: 'app-flags-menu',
    templateUrl: './flags-menu.component.html',
    styleUrls: ['./flags-menu.component.scss'],
    encapsulation: ViewEncapsulation.None
})
export class FlagsMenuComponent implements OnInit {

    public settings: Settings;

    constructor(public appSettings: AppSettings) {
        this.settings = this.appSettings.settings;
    }

    ngOnInit() {
    }

}
