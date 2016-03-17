import angular from 'angular';
import 'angular-route';
import 'angular-cookies';
import 'angular-resource';
import 'angular-sanitize';
import 'angular-ui-router';

import directives from './directives';
import controllers, {templateCache} from './controllers';
import services from './services';
import config from './config';

export const App = 'app';

const app = angular.module(App, ['ngRoute', 'ngResource', 'ngCookies',
                'ngSanitize', 'ui.router', directives, services, controllers])
            .provider('template', function() {
                this.$get = () => {return templateCache;};
            });

config.forEach(c => {
    app.config(c);
});


import 'bootstrap/dist/css/bootstrap.css';
window.name = 'NG_DEFER_BOOTSTRAP!';
angular.element().ready(() => {
    angular.resumeBootstrap([App]);
});

export default app;
