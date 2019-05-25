import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {PerfectScrollbarConfigInterface} from 'ngx-perfect-scrollbar';
import {AppSettings} from '../../../../appsettings/app.settings';
import {Settings} from '../../../../appsettings/app.settings.model';
import {MenuService} from '../menu/menu.service';

import {MeService} from '../../../../services/services/me.service'; // 元气少女
import {Me} from '../../../../services/models/Me'; // 元气少女

@Component({
    selector: 'app-sidenav',
    templateUrl: './sidenav.component.html',
    styleUrls: ['./sidenav.component.scss'],
    encapsulation: ViewEncapsulation.None,
    providers: [MenuService]
})
export class SidenavComponent implements OnInit {
    public psConfig: PerfectScrollbarConfigInterface = {
        wheelPropagation: true
    };
    me = new Me; // 属性 声明为对象

    public menuItems: Array<any>;
    public settings: Settings;

    constructor(
        public appSettings: AppSettings,
        public menuService: MenuService,
        public meService: MeService, // 元气少女
    ) {
        this.settings = this.appSettings.settings;

        // 元气少女
        this.meService.getMe()
            .subscribe(data => {
                this.me = data;
                // setTimeout(() => { this.loadingIndicator = false; }, 1500);
            });
    }

    ngOnInit() {
        this.menuItems = this.menuService.getVerticalMenuItems();
    }

    ngDoCheck() {
        if (this.settings.fixedSidenav) {
            if (this.psConfig.wheelPropagation == true) {
                this.psConfig.wheelPropagation = false;
            }
        } else {
            if (this.psConfig.wheelPropagation == false) {
                this.psConfig.wheelPropagation = true;
            }
        }
    }

    public closeSubMenus() {
        let menu = document.getElementById("vertical-menu");
        if (menu) {
            for (let i = 0; i < menu.children[0].children.length; i++) {
                let child = menu.children[0].children[i];
                if (child) {
                    if (child.children[0].classList.contains('expanded')) {
                        child.children[0].classList.remove('expanded');
                        child.children[1].classList.remove('show');
                    }
                }
            }
        }
    }

}
