import {Injectable} from '@angular/core';
import {Settings} from './app.settings.model';

@Injectable()
export class AppSettings {
    public settings = new Settings(
        'Annular',   // theme name
        true,       // loadingSpinner
        false,      // fixedHeader 默认 true
        false,      // fixedSidenav 默认 true
        false,      // fixedSidenavUserContent
        false,      // fixedFooter
        true,       // sidenavIsOpened
        true,       // sidenavIsPinned
        'vertical', // horizontal , vertical
        'default',  // default, compact, mini
        'teal-light',   // indigo-light, teal-light, red-light, gray-light, blue-dark, green-dark, pink-dark, gray-dark
        false       // true = rtl, false = ltr
    )
}

