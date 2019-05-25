import { NgModule } from '@angular/core';
import { ServerModule } from '@angular/platform-server';
import { FlexLayoutServerModule } from '@angular/flex-layout/server';
import { ModuleMapLoaderModule } from '@nguniversal/module-map-ngfactory-loader';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';

import { AppModule } from './app.module';
import { AppComponent } from './app.component';
import { TranslateUniversalLoader } from './Services/translate-universal-loader.service'


export function translateFactory() {
   return new TranslateUniversalLoader('./dist/assets/i18n', '.json');
}


@NgModule({
  imports: [
    AppModule,
    ServerModule,
    ModuleMapLoaderModule,
    FlexLayoutServerModule,
    TranslateModule.forRoot({
         loader: {
            provide: TranslateLoader,
            useFactory: translateFactory
         }
    })
  ],
  bootstrap: [ AppComponent ],
})
export class AppServerModule {}
