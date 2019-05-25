const api_host = process.env.API_HOST || 'http://localhost:8981';
const api_base_path = process.env.API_BASE_PATH || '/api/v1';
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
    apiHost: api_host,
    apiBasePath: api_base_path,
    // url1: api_host + api_host,  // 后端API
    url1: 'https://llzw.hitnslab.com/api/v1',  // 后端API
};


export const seller_apiUrl = environment.url1;
