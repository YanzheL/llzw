import {Component} from '@angular/core';
import {ActivatedRoute, ActivatedRouteSnapshot, NavigationEnd, Router, UrlSegment} from "@angular/router";
import {Title} from '@angular/platform-browser';
import {AppSettings} from '../../app.settings';
import {Settings} from '../../app.settings.model';
import {MenuService} from '../../../bodypage/theme/components/menu/menu.service'; // 元气少女


@Component({
    selector: 'app-breadcrumb',
    templateUrl: './breadcrumb.component.html',
    styleUrls: ['./breadcrumb.component.scss'],
    providers: [MenuService]
})
export class BreadcrumbComponent {

    public pageTitle: string;
    public breadcrumbs: {
        name: string;
        url: string
    }[] = [];

    public settings: Settings;

    constructor(public appSettings: AppSettings,
                public router: Router,
                public activatedRoute: ActivatedRoute,
                public title: Title,
                private menuService: MenuService // 元气少女
    ) {
        this.settings = this.appSettings.settings;
        this.router.events.subscribe(event => {
            if (event instanceof NavigationEnd) {
                this.breadcrumbs = [];
                this.parseRoute(this.router.routerState.snapshot.root);
                this.pageTitle = "";
                this.breadcrumbs.forEach(breadcrumb => {
                    this.pageTitle += ' > ' + breadcrumb.name;
                })
                this.title.setTitle(this.settings.name + this.pageTitle);
            }
        })
    }

    public closeSubMenus() {
        if (this.settings.menu === "vertical") {
            this.menuService.closeAllSubMenus(); // 元气少女
        }
    }

    private parseRoute(node: ActivatedRouteSnapshot) {
        if (node.data['breadcrumb']) {
            if (node.url.length) {
                let urlSegments: UrlSegment[] = [];
                node.pathFromRoot.forEach(routerState => {
                    urlSegments = urlSegments.concat(routerState.url);
                });
                let url = urlSegments.map(urlSegment => {
                    return urlSegment.path;
                }).join('/');
                this.breadcrumbs.push({
                    name: node.data['breadcrumb'],
                    url: '/' + url
                })
            }
        }
        if (node.firstChild) {
            this.parseRoute(node.firstChild);
        }
    }


}
