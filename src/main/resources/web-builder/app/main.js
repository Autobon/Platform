import 'core-js/shim'; // polyfill for older browsers
import angular from 'angular';
import 'angular-route';
import 'angular-cookies';
import 'angular-resource';
import 'angular-sanitize';
import 'angular-ui-router';

import './directives';
import controllers from './controllers';
import services from './services';

import routeConfig from './config/route';
import {exception, compileProvider} from 'angular-es6';

export const App = 'app';

export default angular
    .module(App, ['ngRoute', 'ngResource', 'ngCookies', 'ngSanitize', 'ui.router',
        services, controllers])
    .config(exception)
    .config(compileProvider)
    .config(routeConfig);

window.name = 'NG_DEFER_BOOTSTRAP!';
angular.element().ready(() => {
    angular.resumeBootstrap([App]);
});
