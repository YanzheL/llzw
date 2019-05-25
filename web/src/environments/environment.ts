// This file can be replaced during build by using the `fileReplacements` array.
// `ng build ---prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  firebase: {
    apiKey: "AIzaSyC5kr77VEU-FnklR5DBn3bPwmtmpEjYde4",
    authDomain: "embryo-version-2.firebaseapp.com",
    databaseURL: "https://embryo-version-2.firebaseio.com",
    projectId: "embryo-version-2",
    storageBucket: "embryo-version-2.appspot.com",
    messagingSenderId: "73552048992",
  },
  // url1: 'http://localhost:8981/api/v1',
  url1: 'https://llzw.hitnslab.com/api/v1',  // 后端API
};

export const seller_apiUrl = environment.url1;

/*
 * In development mode, to ignore zone related error stack frames such as
 * `zone.run`, `zoneDelegate.invokeTask` for easier debugging, you can
 * import the following file, but please comment it out in production mode
 * because it will have performance impact when throw error
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
