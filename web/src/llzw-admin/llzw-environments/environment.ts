// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `.angular-cli.json`.
import {seller_apiUrl} from '../../environments/environment'

export const environment = {
    production: false,
    // apiUrl: 'http://localhost:8081/api/v1_0_0',

    isMockEnabled: false, // You have to switch this, when your real back-end is done

    authTokenKey: 'authce9d77b308c149d5992a80073637e4d5',
    siteName: 'LLZW 商务'
};


//export const apiUrl = environment.isMockEnabled ? 'api' : 'http://localhost:3000'; // josn-server
//export const apiUrl = environment.isMockEnabled ? 'api' : 'http://localhost:8081/api/v1';
//export const apiUrl = environment.isMockEnabled ? 'api' : 'http://localhost:8981/api/v1';
//export const apiUrl = environment.isMockEnabled ? 'api' : 'http://192.168.233.131:8981/api/v1';
export const apiUrl = seller_apiUrl;
