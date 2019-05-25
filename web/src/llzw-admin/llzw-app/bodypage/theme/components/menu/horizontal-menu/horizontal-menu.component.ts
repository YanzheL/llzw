import {Component, Input, OnInit, ViewEncapsulation} from '@angular/core';
import {AppSettings} from '../../../../../appsettings/app.settings';
import {Settings} from '../../../../../appsettings/app.settings.model';
import {MenuService} from '../menu.service';

@Component({
    selector: 'app-horizontal-menu',
    templateUrl: './horizontal-menu.component.html',
    styleUrls: ['./horizontal-menu.component.scss'],
    encapsulation: ViewEncapsulation.None,
    providers: [MenuService]
})
export class HorizontalMenuComponent implements OnInit {
    @Input('menuParentId') menuParentId;
    public menuItems: Array<any>;
    public settings: Settings;

    constructor(public appSettings: AppSettings, public menuService: MenuService) {
        this.settings = this.appSettings.settings;
    }

    ngOnInit() {
        this.menuItems = this.menuService.getHorizontalMenuItems();
        this.menuItems = this.menuItems.filter(item => item.parentId == this.menuParentId);
    }

}