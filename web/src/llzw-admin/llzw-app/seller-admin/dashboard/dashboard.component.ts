import { Component, OnInit } from '@angular/core';

import { AppSettings } from '../../appsettings/app.settings';
import { Settings } from '../../appsettings/app.settings.model';

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

    public icons = ['home', 'person', 'alarm', 'work', 'mail', 'favorite'];
    public colors = ['accent', 'primary', 'warn'];
    public settings: Settings;
    constructor(public appSettings: AppSettings) {
        this.settings = this.appSettings.settings;
    }
    ngOnInit() {
    }
}
